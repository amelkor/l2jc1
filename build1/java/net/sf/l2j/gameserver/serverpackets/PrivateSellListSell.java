/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PrivateSellListSell.java,v 1.3 2004/10/17 06:46:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:21 $
 * $Revision: 1.3 $
 * $Log: PrivateSellListSell.java,v $
 * Revision 1.3  2004/10/17 06:46:21  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.2  2004/09/28 03:01:08  nuocnam
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

import java.util.ArrayList;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.TradeItem;

/**
 * 3 section to this packet
 * 1)playerinfo which is always sent
 * dd
 *
 * 2)list of items which can be added to sell
 * d(hhddddhhhd)
 *
 * 3)list of items which have already been setup 
 * for sell in previous sell private store sell manageent 
 * d(hhddddhhhdd) * 
 * @version $Revision: 1.3 $ $Date: 2004/10/17 06:46:21 $
 */
public class PrivateSellListSell  extends ServerBasePacket
{
	
	private static final String _S__B3_PRIVATESELLLISTSELL = "[S] B3 PrivateSellListSell";
	private L2PcInstance _seller;
	
	public PrivateSellListSell(L2PcInstance seller)
	{
		_seller = seller;
	}
	
	public byte[] getContent()
	{
		writeC(0xB3);
		//section 1 
		writeD(_seller.getObjectId());
		writeD(_seller.getAdena());
		L2ItemInstance[] inventory = _seller.getInventory().getItems();
		L2TradeList list =new L2TradeList(0);
		ArrayList sellList =_seller.getSellList();
		int count = _seller.getInventory().getSize();
		TradeItem temp2; 
		
				
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance item = inventory[i];
			if ((!item.isEquipped()) && (item.getItem().getType2() != 3)&& !((item.getItem().getType2() == 4) && (item.getItem().getType1() == 4))&& !((item.getItem().getType2() == 1) && (item.getItem().getType1() == 1))&& !item.isEquipped())
			{
				list.addItem(item);
			}
		}
		
		if (sellList.size() !=0 )
		{//no existing items in sell list
			for (int i = 0; i < sellList.size(); i++)
			{
				temp2 = (TradeItem)sellList.get(i);
				list.removeItem(temp2.getObjectId(),temp2.getCount());
			}
		}
		
		count = list.getItems().size();
		//section2 
		writeD(count);//for potential sells
		
		L2ItemInstance temp;
		int type;
		for (int i = 0; i < count; i++)
		{
			temp = (L2ItemInstance) list.getItems().get(i);
			
			writeD(0); 
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeD(0);
			writeH(0x00);
			writeH(0x00);
			writeH(0x00);
			writeD(500);
			
		}	
		//section 3
		count = sellList.size();
		writeD(count);//count for any items already added for sell
		if (count !=0)
		{	
			
			for (int i = 0; i < count; i++)
			{
				temp2 = (TradeItem)sellList.get(i);
								
				System.out.println("item:" + temp2.getObjectId());
				writeD(0); 
				writeD(temp2.getObjectId());
				writeD(temp2.getItemId());
				writeD(temp2.getCount());
				writeD(0);
				writeH(0x00);	
				writeH(0x00);
				writeH(0x00);
				writeD(temp2.getOwnersPrice());//your price
				writeD(temp2.getStorePrice());//fixed store price
			}	
		}
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__B3_PRIVATESELLLISTSELL;
	}
}
