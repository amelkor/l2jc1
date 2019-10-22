/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/Warehouse.java,v 1.3 2004/09/24 19:42:19 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/09/24 19:42:19 $
 * $Revision: 1.3 $
 * $Log: Warehouse.java,v $
 * Revision 1.3  2004/09/24 19:42:19  jeichhorn
 * fixed file comments
 *
 * Revision 1.1  2004/06/27 08:51:42  nuocnam
 * initial release, based on Inventory.java
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

package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/24 19:42:19 $
 */
public class Warehouse
{
	private static Logger _log = Logger.getLogger(Warehouse.class.getName());
			
	private ArrayList _items;

	
	
	public Warehouse()
	{	
		_items = new ArrayList();
	}
	
	public int getSize()
	{
		return _items.size();
	}
	
	public ArrayList getItems()
	{
		return _items;
	}
	
	public L2ItemInstance addItem(L2ItemInstance newItem)
	{
		L2ItemInstance result = newItem;
		boolean stackableFound = false;
		
		if (newItem.isStackable())
		{
			int itemId = newItem.getItemId();
			L2ItemInstance old = findItemId(itemId);
			if (old != null)
			{
				old.setCount(old.getCount()+newItem.getCount()); // add new item to existing stack
				stackableFound = true;
				old.setLastChange(L2ItemInstance.MODIFIED);
				result = old;
			}
		}
		
		if (!stackableFound)
		{	
			_items.add(newItem);
			newItem.setLastChange((L2ItemInstance.ADDED));
		}
		
		return result;
	}
	

	private L2ItemInstance findItemId(int itemId)
	{
		for (int i=0; i<_items.size();i++)
		{
			L2ItemInstance temp = (L2ItemInstance)_items.get(i);
			if (temp.getItemId() == itemId)
			{
				return temp;
			}
		}

		return null;
	}
	

	/**
	 * @param objectId
	 * @return
	 */
	public L2ItemInstance getItem(int objectId)
	{
		for (int i = 0; i < _items.size(); i++)
		{
			L2ItemInstance temp = (L2ItemInstance) _items.get(i);
			if (temp.getObjectId() == objectId)
			{
				return temp;
			}
		}
		
		return null;
	}
	
	public L2ItemInstance destroyItem(int itemId, int count)
	{
		L2ItemInstance item = findItemId(itemId);
		if (item != null) 
		{
			if (item.getCount() == count)
			{	
				_items.remove(item);
				item.setCount(0);
				item.setLastChange(L2ItemInstance.REMOVED);
			}
			else
			{
				item.setCount(item.getCount()-count);
				item.setLastChange(L2ItemInstance.MODIFIED);
			}
		} 
		else 
		{
			_log.fine("can't destroy "+count+" item(s) "+itemId);
		}
		return item;
	}
}
	
