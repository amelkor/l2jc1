/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/TradeDone.java,v 1.6 2004/09/28 01:53:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:53:56 $
 * $Revision: 1.6 $
 * $Log: TradeDone.java,v $
 * Revision 1.6  2004/09/28 01:53:56  nuocnam
 * Added javadoc header.
 *
 * Revision 1.5  2004/08/10 20:36:45  l2chef
 * pending transactions are cancled on disconnect (whatev)
 *
 * Revision 1.4  2004/08/08 23:00:10  l2chef
 * added system messages and fixed transaction setting
 *
 *
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SendTradeDone;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.6 $ $Date: 2004/09/28 01:53:56 $
 */
public class TradeDone extends ClientBasePacket
{
	private static final String _C__17_TRADEDONE = "[C] 17 TradeDone";
	private static Logger _log = Logger.getLogger(TradeDone.class.getName());
	
	public TradeDone(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int response = readD();
		
		L2PcInstance player = client.getActiveChar();
		L2PcInstance requestor = player.getTransactionRequester();
		
		if (requestor.getTransactionRequester() != null)
		{
			if (response == 1 )
			{
				player.getTradeList().setConfirmedTrade(true);
				if (requestor.getTradeList().hasConfirmed())
				{
					player.getTradeList().tradeItems(player,requestor);
					requestor.getTradeList().tradeItems(requestor,player);
					requestor.sendPacket(new SendTradeDone(1));
					player.sendPacket(new SendTradeDone(1));
					requestor.getTradeList().getItems().clear();
					player.getTradeList().getItems().clear();
					SystemMessage msg = new SystemMessage(SystemMessage.TRADE_SUCCESSFUL);
					requestor.sendPacket(msg);
					player.sendPacket(msg);
					
					// clear transaction flag
					requestor.setTransactionRequester(null);
					player.setTransactionRequester(null);
				}
				else
				{
					// first party accepted the trade
					SystemMessage msg = new SystemMessage(SystemMessage.S1_CONFIRMED_TRADE);
					msg.addString(player.getName());
					requestor.sendPacket(msg);
				}
			}
			else
			{
				player.sendPacket(new SendTradeDone(0));
				requestor.sendPacket(new SendTradeDone(0));

				player.setTradeList(null);
				requestor.setTradeList(null);
				
				SystemMessage msg = new SystemMessage(SystemMessage.S1_CANCELED_TRADE);
				msg.addString(player.getName());
				requestor.sendPacket(msg);
				
				// clear transaction flag
				requestor.setTransactionRequester(null);
				player.setTransactionRequester(null);
			}
		}
		else
		{
			// trade partner logged off. trade is canceld
			player.sendPacket(new SendTradeDone(0));
			SystemMessage msg = new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.sendPacket(msg);
			player.setTransactionRequester(null);
			requestor.setTradeList(null);
			player.setTradeList(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__17_TRADEDONE;
	}
}
