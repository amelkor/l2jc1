/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/SetPrivateStoreListSell.java,v 1.2 2004/09/28 01:40:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:40:56 $
 * $Revision: 1.2 $
 * $Log: SetPrivateStoreListSell.java,v $
 * Revision 1.2  2004/09/28 01:40:56  nuocnam
 * Added header and copyright notice.
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
import java.util.ArrayList;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.TradeItem;
import net.sf.l2j.gameserver.serverpackets.PrivateStoreMsgSell;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:40:56 $
 */
public class SetPrivateStoreListSell extends ClientBasePacket{
	private static final String _C__74_SETPRIVATESTORELISTSELL = "[C] 74 SetPrivateStoreListSell";
	private static Logger _log = Logger.getLogger(SetPrivateStoreListSell.class.getName());
	
	
	public SetPrivateStoreListSell(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		
		int count = readD();
		L2PcInstance player = client.getActiveChar();
		L2TradeList tradelist = player.getTradeList();
		TradeItem temp;
		player.setSellList(new ArrayList());
		ArrayList listsell = player.getSellList();
		
		
		for (int x = 0; x < count ; x++)
		{
			temp = new TradeItem();
			
			temp.setObjectId(readD());
			temp.setCount(readD());
			temp.setOwnersPrice(readD());
			temp.setItemId(player.getInventory().getItem(temp.getObjectId()).getItemId());
			listsell.add(temp);
			
		}
		
		if (count == 0)
		{
			listsell = null;
		}
	
		if (count!= 0)
		{
			player.setPrivateStoreType(1);
			player.sendPacket(new ChangeWaitType (player,0));
			player.broadcastPacket(new ChangeWaitType (player,0));
			player.sendPacket(new UserInfo(player));
			player.broadcastPacket(new UserInfo(player));
			player.sendPacket(new PrivateStoreMsgSell(player));
			player.broadcastPacket(new PrivateStoreMsgSell(player));
		}
		else 
		{
			player.setPrivateStoreType(0);
			player.sendPacket(new UserInfo(player));
			player.broadcastPacket(new UserInfo(player));
			//send error no items sellected to sell
			//msg_begin	id=280	group=[popup]	msg=[You do not have any items to sell.]
		}
		
		
	}
	
	
	public String getType()
	{
		return _C__74_SETPRIVATESTORELISTSELL;
	}
	
	
}
