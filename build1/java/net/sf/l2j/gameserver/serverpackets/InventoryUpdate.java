/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/InventoryUpdate.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: InventoryUpdate.java,v $
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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;


/**
 * 
 *
 * sample
 *  
 * 37 
 * 01 00 		// item count
 * 
 * 03 00		// update type   01-added?? 02-modified 03-removed
 * 04 00 		// itemType1  0-weapon/ring/earring/necklace  1-armor/shield  4-item/questitem/adena
 * c6 37 50 40  // objectId 
 * cd 09 00 00  // itemId
 * 05 00 00 00  // count
 * 05 00		// itemType2  0-weapon  1-shield/armor  2-ring/earring/necklace  3-questitem  4-adena  5-item
 * 00 00 		// always 0 ??
 * 00 00 		// equipped 1-yes
 * 00 00 		// slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
 * 00 00 		// always 0 ??
 * 00 00		// always 0 ??
 *  
 
 * format   h (hh dddhhhh hh)	revision 377
 * format   h (hh dddhhhd hh)   revision 415
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class InventoryUpdate extends ServerBasePacket
{
	private static Logger _log = Logger.getLogger(InventoryUpdate.class.getName());
	private static final String _S__37_INVENTORYUPDATE = "[S] 37 InventoryUpdate";
	private ArrayList _items;
	private boolean _showWindow;

	public InventoryUpdate()
	{
		_items = new ArrayList();
	}	
	
	/**
	 * @param items
	 */
	public InventoryUpdate(ArrayList items)
	{
		_items = items;
	}

	public void addNewItem(L2ItemInstance item )
	{
		item.setLastChange(1);
		_items.add(item);
	}

	public void addModifiedItem(L2ItemInstance item )
	{
		item.setLastChange(2);
		_items.add(item);
	}

	public void addRemovedItem(L2ItemInstance item )
	{
		item.setLastChange(3);
		_items.add(item);
	}

	public byte[] getContent()
	{
		writeC(0x37);
		int count = _items.size(); 
		writeH(count);
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance temp = (L2ItemInstance) _items.get(i);
			_log.fine("oid:" + Integer.toHexString(temp.getObjectId()) +" item:" + temp.getItem().getName()+" last change:" + temp.getLastChange());
			
			writeH(temp.getLastChange());	
			writeH(temp.getItem().getType1()); // item type1
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(temp.getItem().getType2());	// item type2
			writeH(0x00);	// ?
			
			if (temp.isEquipped())
			{
				writeH(0x01);
			}
			else
			{
				writeH(0x00);
			}
//			writeH(temp.getItem().getBodyPart());	// rev 377   slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			writeD(temp.getItem().getBodyPart());	// rev 415   slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			writeH(temp.getEnchantLevel());	// enchant level
			writeH(0x00);	// ?
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__37_INVENTORYUPDATE;
	}
}
