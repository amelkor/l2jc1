/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/SendPrivateStoreBuyBuyList.java,v 1.2 2004/09/28 01:40:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:40:56 $
 * $Revision: 1.2 $
 * $Log: SendPrivateStoreBuyBuyList.java,v $
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
import net.sf.l2j.gameserver.model.TradeItem;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:40:56 $
 */
public class SendPrivateStoreBuyBuyList extends ClientBasePacket{
	private static final String _C__96_SENDPRIVATESTOREBUYBUYLIST = "[C] 96 SendPrivateStoreBuyBuyList";
	private static Logger _log = Logger.getLogger(SendPrivateStoreBuyBuyList.class.getName());
	
	
	public SendPrivateStoreBuyBuyList (byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int buyerID = readD();
		
		int count = readD();
		
		L2World world;
		world = L2World.getInstance();
		L2PcInstance seller = client.getActiveChar();
		L2PcInstance buyer = (L2PcInstance) world.findObject(buyerID);
		ArrayList buyerlist = buyer.getBuyList(),sellerlist = new ArrayList();
		int cost = 0;
		TradeItem temp;
		
		for (int i = 0; i < count; i++)
		{
			
			temp = new TradeItem();
			temp.setObjectId(readD());
			temp.setItemId(readD());
			readH();
			temp.setCount(readD());
			temp.setOwnersPrice(readD());
			cost = cost + (temp.getOwnersPrice()*temp.getCount());
			sellerlist.add(temp);
		}
		
		
		if (buyer.getAdena() >= cost && count > 0 && buyer.getPrivateStoreType() ==3)
		{
			buyer.getTradeList().BuySellItems(buyer,buyerlist,seller,sellerlist);
			buyer.getTradeList().updateBuyList(buyer,buyerlist);
			if (buyer.getBuyList().size() == 0)
			{
				
				buyer.setPrivateStoreType(0);
				buyer.sendPacket(new ChangeWaitType (buyer,1));
				buyer.broadcastPacket(new ChangeWaitType (buyer,1));
				buyer.sendPacket(new UserInfo(buyer));
				buyer.broadcastPacket(new UserInfo(buyer));
				
			}
			
		}
		else
		{
			buyer.getTradeList().updateBuyList(buyer,buyerlist);
		}
		
		
		
		
		
	}
	public String getType()
	{
		return _C__96_SENDPRIVATESTOREBUYBUYLIST;
	}
	
}
