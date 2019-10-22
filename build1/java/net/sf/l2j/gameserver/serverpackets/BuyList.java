/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/BuyList.java,v 1.4 2004/08/06 00:22:39 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:22:39 $
 * $Revision: 1.4 $
 * $Log: BuyList.java,v $
 * Revision 1.4  2004/08/06 00:22:39  l2chef
 * player trading added (whatev)
 *
 * Revision 1.3  2004/07/04 11:14:53  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.2  2004/06/27 08:51:42  jeichhorn
 * Added copyright notice
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

import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.L2ItemInstance;


/**
 * sample
 *
 * 1d 
 * 1e 00 00 00 			// ??
 * 5c 4a a0 7c 			// buy list id
 * 02 00				// item count
 *  
 * 04 00 				// itemType1  0-weapon/ring/earring/necklace  1-armor/shield  4-item/questitem/adena
 * 00 00 00 00 			// objectid
 * 32 04 00 00 			// itemid
 * 00 00 00 00 			// count
 * 05 00 				// itemType2  0-weapon  1-shield/armor  2-ring/earring/necklace  3-questitem  4-adena  5-item
 * 00 00 			
 * 60 09 00 00			// price
 *  
 * 00 00
 * 00 00 00 00 
 * b6 00 00 00 
 * 00 00 00 00 
 * 00 00 
 * 00 00 				 
 * 80 00 				//	body slot 	 these 4 values are only used if itemtype1 = 0 or 1
 * 00 00 				//
 * 00 00 				//
 * 00 00 				//
 * 50 c6 0c 00
 *  
 
 * format   dd h (h dddhh hhhh d)	revision 377
 * format   dd h (h dddhh dhhh d)	revision 377
 * 
 * @version $Revision: 1.4 $ $Date: 2004/08/06 00:22:39 $
 */
public class BuyList extends ServerBasePacket
{
	private static final String _S__1D_BUYLIST = "[S] 1D BuyList";
	private L2TradeList _list;
	private int _money;

	public BuyList(L2TradeList list, int currentMoney)
	{
		_list = list;
		_money = currentMoney;
	}	
	
	public byte[] getContent()
	{
		writeC(0x1d);
		writeD(_money);		// current money
		writeD(_list.getListId());
		
		int count = _list.getItems().size(); 
		writeH(count);
		ArrayList items = _list.getItems();
		
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance temp = (L2ItemInstance) items.get(i);
			int type = temp.getItem().getType1();
			writeH(type); // item type1
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(temp.getItem().getType2());	// item type2
			writeH(0x00);	// ?

			if (type < 4)
			{	
//				writeH(temp.getItem().getBodyPart());	// rev 377  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
				writeD(temp.getItem().getBodyPart());	// rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
				writeH(temp.getEnchantLevel());	// enchant level
				writeH(0x00);	// ?
				writeH(0x00);
			}
			
			writeD(temp.getPrice());
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__1D_BUYLIST;
	}
}
