/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetInventoryUpdate.java,v 1.3 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.3 $
 * $Log: PetInventoryUpdate.java,v $
 * Revision 1.3  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.2  2004/08/14 22:47:19  l2chef
 * commet update
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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * This class ...
 * 
 * @author Yme
 * @version $Revision: 1.3 $ $Date: 2004/09/28 02:44:16 $
 */
public class PetInventoryUpdate extends ServerBasePacket{
	private static Logger _log = Logger.getLogger(InventoryUpdate.class.getName());
	private static final String _S__37_INVENTORYUPDATE = "[S] 37 InventoryUpdate";
	private ArrayList _items;
	private boolean _showWindow;

	public PetInventoryUpdate()
	{
		_items = new ArrayList();
	}	
	
	/**
	 * @param items
	 */
	public PetInventoryUpdate(ArrayList items)
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
		writeC(0xcc);
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
