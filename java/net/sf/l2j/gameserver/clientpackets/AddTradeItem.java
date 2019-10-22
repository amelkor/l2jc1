/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/AddTradeItem.java,v 1.5 2004/09/28 01:53:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:53:56 $
 * $Revision: 1.5 $
 * $Log: AddTradeItem.java,v $
 * Revision 1.5  2004/09/28 01:53:56  nuocnam
 * Added javadoc header.
 *
 * Revision 1.4  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.3  2004/08/10 20:36:45  l2chef
 * pending transactions are cancled on disconnect (whatev)
 *
 * Revision 1.2  2004/08/08 22:58:04  l2chef
 * changing a trade will invalidate the confirm
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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.serverpackets.SendTradeDone;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.TradeOwnAdd;
import net.sf.l2j.gameserver.serverpackets.TradeOtherAdd;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/09/28 01:53:56 $
 */
public class AddTradeItem  extends ClientBasePacket
{
	private static final String _C__16_ADDTRADEITEM = "[C] 16 AddTradeItem";
	private static Logger _log = Logger.getLogger(AddTradeItem.class.getName());
	
	public AddTradeItem(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int tradeId = readD();
		int objectId = readD();
		int amount = readD();
		
		L2PcInstance player = client.getActiveChar();
		L2PcInstance requestor = player.getTransactionRequester();
		
		if (requestor.getTransactionRequester() != null)
		{
			L2TradeList playerItemList = player.getTradeList();
			
			//TODO add an option to configure server trade behavior. org.mode or improved mode... now only improved mode is active
			// both players have a accept again after a change
			player.getTradeList().setConfirmedTrade(false);
			requestor.getTradeList().setConfirmedTrade(false);
			
			if (playerItemList.getItems().size() > 0)
			{
				if (!playerItemList.contains(objectId))//is not item already in list
				{
					L2ItemInstance temp = new L2ItemInstance();
					temp.setObjectId(player.getInventory().getItem(objectId).getObjectId());
					temp.setCount(amount);
					playerItemList.addItem(temp);
					
					player.sendPacket(new TradeOwnAdd(player.getInventory().getItem(objectId),amount));
					requestor.sendPacket(new TradeOtherAdd(player.getInventory().getItem(objectId),amount));
				}
				else
				{
					int InvItemCount = player.getInventory().getItem(objectId).getCount();
					L2ItemInstance tempTradeItem = playerItemList.getItem(objectId);
					if  (!(InvItemCount == tempTradeItem.getCount()))
					{
						if ((amount + tempTradeItem.getCount()) >=  InvItemCount)
						{
							tempTradeItem.setCount(InvItemCount);
							player.sendPacket(new TradeOwnAdd(player.getInventory().getItem(objectId),amount));
							requestor.sendPacket(new TradeOtherAdd(player.getInventory().getItem(objectId),amount));
						}
						else
						{
							tempTradeItem.setCount(amount + tempTradeItem.getCount());
							player.sendPacket(new TradeOwnAdd(player.getInventory().getItem(objectId),amount));
							requestor.sendPacket(new TradeOtherAdd(player.getInventory().getItem(objectId),amount));
						}
					}
				}
			}
			else
			{
				L2ItemInstance temp = new L2ItemInstance();
				temp.setObjectId(objectId);
				temp.setCount(amount);
				playerItemList.addItem(temp);
				player.sendPacket(new TradeOwnAdd(player.getInventory().getItem(objectId),amount));
				requestor.sendPacket(new TradeOtherAdd(player.getInventory().getItem(objectId),amount));
			}
		}
		else
		{
			// trade partner logged off. trade is canceld
			player.sendPacket(new SendTradeDone(0));
			SystemMessage msg = new SystemMessage(SystemMessage.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.sendPacket(msg);
			player.setTransactionRequester(null);
			requestor.getTradeList().getItems().clear();
			player.getTradeList().getItems().clear();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__16_ADDTRADEITEM;
	}
}
