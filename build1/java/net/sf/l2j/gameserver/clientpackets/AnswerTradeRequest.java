/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/AnswerTradeRequest.java,v 1.5 2004/09/28 01:53:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:53:56 $
 * $Revision: 1.5 $
 * $Log: AnswerTradeRequest.java,v $
 * Revision 1.5  2004/09/28 01:53:56  nuocnam
 * Added javadoc header.
 *
 * Revision 1.4  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.3  2004/08/10 20:36:45  l2chef
 * pending transactions are cancled on disconnect (whatev)
 *
 * Revision 1.2  2004/08/08 22:58:59  l2chef
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
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.serverpackets.SendTradeDone;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.TradeStart;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/09/28 01:53:56 $
 */
public class AnswerTradeRequest extends ClientBasePacket{
	private static final String _C__40_ANSWERTRADEREQUEST = "[C] 40 AnswerTradeRequest";
	private static Logger _log = Logger.getLogger(AnswerTradeRequest.class.getName());
	
	public AnswerTradeRequest(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int response = readD();
		
		L2PcInstance player = client.getActiveChar();
		L2PcInstance requestor = player.getTransactionRequester();

		if (requestor.getTransactionRequester() != null)
		{
			if (response == 1)
			{
				SystemMessage msg = new SystemMessage(SystemMessage.BEGIN_TRADE_WITH_S1);
				msg.addString(player.getName());
				requestor.sendPacket(msg);
				requestor.sendPacket(new TradeStart(requestor));
				if (requestor.getTradeList() == null)
				{
					requestor.setTradeList(new L2TradeList(0));
				}
				msg = new SystemMessage(SystemMessage.BEGIN_TRADE_WITH_S1);
				msg.addString(requestor.getName());
				player.sendPacket(msg);
				player.sendPacket(new TradeStart(player));
				if (player.getTradeList() == null)
				{
					player.setTradeList(new L2TradeList(0));
				}
			}
			else
			{
				SystemMessage msg = new SystemMessage(SystemMessage.S1_DENIED_TRADE_REQUEST);
				msg.addString(player.getName());
				requestor.sendPacket(msg);
				requestor.setTransactionRequester(null);
				player.setTransactionRequester(null);
			}
		}
		else 
		{
			if (response != 0)
			{
				// trade partner logged off. trade is canceld
				player.sendPacket(new SendTradeDone(0));
				SystemMessage msg = new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME);
				player.sendPacket(msg);
				player.setTransactionRequester(null);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__40_ANSWERTRADEREQUEST;
	}
}
