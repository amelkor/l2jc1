/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PrivateBuyListBuy.java,v 1.7 2004/10/23 21:46:57 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 21:46:57 $
 * $Revision: 1.7 $
 * $Log: PrivateBuyListBuy.java,v $
 * Revision 1.7  2004/10/23 21:46:57  l2chef
 * now you cannot sell more items than you really have
 *
 * Revision 1.6  2004/10/23 19:40:12  l2chef
 * now you cannot sell more items than you really have
 *
 * Revision 1.5  2004/10/17 06:46:21  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.4  2004/09/28 03:01:08  nuocnam
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
package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.TradeItem;
import java.util.ArrayList;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/10/23 21:46:57 $
 */
public class PrivateBuyListBuy extends ServerBasePacket
{
	private static final String _S__D1_PRIVATEBUYLISTBUY = "[S] D1 PrivateBuyListBuy";
	private L2PcInstance _buyer;
	private L2PcInstance _seller;
	
	public PrivateBuyListBuy(L2PcInstance buyer,L2PcInstance seller)
	{
		_buyer = buyer;
		_seller = seller;
	}
	
	public  byte[] getContent()
	{
		writeC(0xD1);
		writeD(_buyer.getObjectId());
		writeD(_seller.getAdena());
		ArrayList buyerslist =_buyer.getBuyList();
		L2ItemInstance[] sellerItems =_seller.getInventory().getItems();
		ArrayList sellerslist = new ArrayList();
		
		int count = buyerslist.size();
		boolean add;
		
		
		TradeItem temp2,temp3; 
		for (int i = 0; i < count; i++)
		{
			temp2 = (TradeItem )buyerslist.get(i);
			add = false;
			for (int x = 0; x < sellerItems.length; x++)
			{
				L2ItemInstance item = sellerItems[x];
				if (temp2.getItemId() == item.getItemId())
				{
					temp3 = new TradeItem();
					temp3.setCount(item.getCount());
					temp3.setItemId(item.getItemId());
					temp3.setObjectId(item.getObjectId());
					temp2.setObjectId(item.getObjectId());
					temp3.setOwnersPrice(temp2.getOwnersPrice());
					temp3.setstorePrice(item.getPrice());
					if (!sellerslist.contains(temp3))
					{
						sellerslist.add(temp3);
						add = true;
					}
					
					break;
				}
			}
			
			if (!add)
			{
				temp3 = new TradeItem();
				temp3.setCount(0);
				temp3.setItemId(temp2.getItemId());
				temp3.setOwnersPrice(temp2.getOwnersPrice());
				sellerslist.add(temp3);
			}
		}
		
		count = sellerslist.size();
		writeD(count);
		int type;
		for (int i = 0; i < count; i++)
		{
			temp2 = (TradeItem) buyerslist.get(i);
			temp3 =	(TradeItem) sellerslist.get(i);
			int buyCount = temp2.getCount();
			int sellCount = temp3.getCount();
			
			writeD(temp3.getObjectId());
			writeD(temp3.getItemId()); 
			writeH(1);
			if (sellCount > buyCount)//give max possible sell amount
			{
				writeD(buyCount);
			}
			else
			{
				writeD(sellCount);
			}
			
			writeD(temp3.getStorePrice());
			writeH(2);

			if (sellCount > buyCount)
			{
				writeD(buyCount);
			}
			else
			{
				writeD(sellCount);
			}
			writeH(3);
			writeD(temp3.getOwnersPrice());//buyers price
			
			if (buyCount > sellCount)  // maximum possible tradecount
			{
				writeD(sellCount);
			}
			else
			{
				writeD(buyCount);
			}
		}
			
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__D1_PRIVATEBUYLISTBUY;
	}
}
