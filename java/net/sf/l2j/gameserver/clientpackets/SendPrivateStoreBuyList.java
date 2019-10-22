/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/SendPrivateStoreBuyList.java,v 1.2 2004/09/28 01:40:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:40:56 $
 * $Revision: 1.2 $
 * $Log: SendPrivateStoreBuyList.java,v $
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

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.TradeItem ;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:40:56 $
 */
public class SendPrivateStoreBuyList extends ClientBasePacket{
	private static final String _C__79_SENDPRIVATESTOREBUYLIST = "[C] 79 SendPrivateStoreBuyList";
	private static Logger _log = Logger.getLogger(SendPrivateStoreBuyList.class.getName());
	
	
	public SendPrivateStoreBuyList(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int sellerID = readD();
		int count = readD();
		
		L2World world;
		world = L2World.getInstance();
		L2PcInstance seller = (L2PcInstance) world.findObject(sellerID);
		L2PcInstance buyer = client.getActiveChar();
		ArrayList buyerlist = new ArrayList(),sellerlist = seller.getSellList();
		int cost = 0;
		TradeItem temp;
		
		for (int i = 0; i < count; i++)
		{
			temp = new TradeItem();
			temp.setObjectId(readD());
			temp.setCount(readD());
			temp.setOwnersPrice(readD());
			temp.setItemId(seller.getInventory().getItem(temp.getObjectId()).getItemId());
			cost = cost + (temp.getOwnersPrice()*temp.getCount());
			buyerlist.add(temp);
		}
		if (buyer.getAdena() >= cost && count > 0 && seller.getPrivateStoreType() ==1)
		{
			seller.getTradeList().BuySellItems(buyer,buyerlist,seller,sellerlist);
			
			if (seller.getSellList().size() == 0)
			{
				seller.setPrivateStoreType(0);
				seller.sendPacket(new ChangeWaitType (seller,1));
				seller.broadcastPacket(new ChangeWaitType (seller,1));
				seller.sendPacket(new UserInfo(seller));
				seller.broadcastPacket(new UserInfo(seller));
				
			}
			
		}
		else
		{
			SystemMessage msg = new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA);
			buyer.sendPacket(msg);
		}
		
		
		
		
	}
	public String getType()
	{
		return _C__79_SENDPRIVATESTOREBUYLIST;
	}
	
}
