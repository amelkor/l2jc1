/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPrivateStoreBuyManage.java,v 1.2 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.2 $
 * $Log: RequestPrivateStoreBuyManage.java,v $
 * Revision 1.2  2004/09/28 01:50:14  nuocnam
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

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.PrivateSellListBuy;
//import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestPrivateStoreBuyManage extends ClientBasePacket{
	private static final String _C__90_REQUESTPRIVATESTOREBUYMANAGE = "[C] 90 RequestPrivateStoreBuyManage";
	private static Logger _log = Logger.getLogger(RequestPrivateStoreBuyManage.class.getName());
	
	
	public RequestPrivateStoreBuyManage(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		L2PcInstance player = client.getActiveChar();
		int privatetype=player.getPrivateStoreType();
		if (privatetype == 0)
		{
			if (player.getWaitType() !=1)
			{
				player.setWaitType(1);
				player.sendPacket(new ChangeWaitType (player,1));
				player.broadcastPacket(new ChangeWaitType (player,1));
			}
			
			if (player.getTradeList() == null)
			{
				player.setTradeList(new L2TradeList(0));
			}
			if (player.getBuyList() == null)
			{
				player.setBuyList(new ArrayList());
			}
			player.getTradeList().updateBuyList(player,player.getBuyList());
			player.setPrivateStoreType(4);
			player.sendPacket(new PrivateSellListBuy(client.getActiveChar()));
			//player.sendPacket(new UserInfo(player));
			//player.broadcastPacket(new UserInfo(player));
		
		}
		
		if (privatetype == 3)
		{
			player.setPrivateStoreType(4);
			player.sendPacket(new PrivateSellListBuy(client.getActiveChar()));	
			player.sendPacket(new ChangeWaitType (player,1));
			player.broadcastPacket(new ChangeWaitType (player,1));
			
			
		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__90_REQUESTPRIVATESTOREBUYMANAGE;
	}

}
