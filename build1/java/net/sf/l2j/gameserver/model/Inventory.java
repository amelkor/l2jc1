/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/Inventory.java,v 1.12 2004/10/17 06:48:04 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:48:04 $
 * $Revision: 1.12 $
 * $Log: Inventory.java,v $
 * Revision 1.12  2004/10/17 06:48:04  l2chef
 * no more direct access to Item collection to avoid wrong usage
 * fixed double adena reduction on dropItem
 *
 * Revision 1.11  2004/09/28 20:31:30  l2chef
 * full armor slot handling fixed
 *
 * Revision 1.10  2004/09/25 22:43:11  l2chef
 * fixed adena dupe bug
 *
 * Revision 1.9  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.6  2004/07/30 22:29:41  l2chef
 * player status gets updated when destroying equiped items (NuocNam)
 *
 * Revision 1.5  2004/07/12 20:54:40  l2chef
 * warehouses added (nuocnan)
 * char data is now stored in subfolder data/accounts
 *
 * Revision 1.4  2004/07/04 19:15:13  l2chef
 * fixed bug with picking up adena
 *
 * Revision 1.3  2004/07/04 11:13:27  l2chef
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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;


/**
 * This class ...
 * 
 * @version $Revision: 1.12 $ $Date: 2004/10/17 06:48:04 $
 */
public class Inventory
{
	private static Logger _log = Logger.getLogger(Inventory.class.getName());
	
	public static final int PAPERDOLL_UNDER = 0;
	public static final int PAPERDOLL_LEAR = 1;
	public static final int PAPERDOLL_REAR = 2;
	public static final int PAPERDOLL_NECK = 3;
	public static final int PAPERDOLL_LFINGER = 4;
	public static final int PAPERDOLL_RFINGER = 5;
	public static final int PAPERDOLL_HEAD = 6;
	public static final int PAPERDOLL_RHAND = 7;
	public static final int PAPERDOLL_LHAND = 8;
	public static final int PAPERDOLL_GLOVES = 9;
	public static final int PAPERDOLL_CHEST = 10;
	public static final int PAPERDOLL_LEGS = 11;
	public static final int PAPERDOLL_FEET = 12;
	public static final int PAPERDOLL_BACK = 13;
	public static final int PAPERDOLL_LRHAND = 14;

	
	private L2ItemInstance[] _paperdoll;

	private L2ItemInstance _adena;
	
	private ArrayList _items;
	private int _totalWeight;

	
	
	public Inventory()
	{
		_paperdoll = new L2ItemInstance[0x10];
	
		_items = new ArrayList();
	}
	
	public int getSize()
	{
		return _items.size();
	}
	
	public L2ItemInstance[] getItems()
	{
		return (L2ItemInstance[]) _items.toArray(new L2ItemInstance[_items.size()]);
	}
	
	public L2ItemInstance addItem(L2ItemInstance newItem)
	{
		L2ItemInstance result = newItem;
		boolean stackableFound = false;
		
		if (newItem.getItemId() == 57)
		{
			addAdena(newItem.getCount());
			return _adena;
		}
		
		if (newItem.isStackable())
		{
			int itemId = newItem.getItemId();
			L2ItemInstance old = findItemByItemId(itemId);
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
		
		refreshWeight();
		return result;
	}
	

	public L2ItemInstance findItemByItemId(int itemId)
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
	 * @return
	 */
	public L2ItemInstance getPaperdollItem(int slot)
	{
		return _paperdoll[slot];
	}

	public int getPaperdollItemId(int slot)
	{
		if (_paperdoll[slot] != null)
		{
			return _paperdoll[slot].getItemId();
		}
		else
		{
			return 0;
		}
	}

	public int getPaperdollObjectId(int slot)
	{
		if (_paperdoll[slot] != null)
		{
			return _paperdoll[slot].getObjectId();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * equips an item in the given slot. the item has to be already in the inventory
	 * @param underwear
	 */
	public void setPaperdollItem(int slot, L2ItemInstance item)
	{
		_paperdoll[slot] = item;
		item.setEquipSlot(slot);
		refreshWeight();
	}

	/**
	 * @param slot
	 */
	public L2ItemInstance[] unEquipItemInBodySlot(int slot)
	{
		ArrayList unequipedItems = new ArrayList();
		
		_log.fine("--- unequip body slot:" + slot);
		int pdollSlot = -1;
		L2ItemInstance item = null;
		
		switch (slot)
		{
			case L2Item.SLOT_L_EAR:
			{
				pdollSlot = PAPERDOLL_LEAR;
				break;
			}
			case L2Item.SLOT_R_EAR:
			{
				pdollSlot = PAPERDOLL_REAR;
				break;
			}
			case L2Item.SLOT_NECK:
			{
				pdollSlot = PAPERDOLL_NECK;
				break;
			}
			case L2Item.SLOT_R_FINGER:
			{
				pdollSlot = PAPERDOLL_RFINGER;
				break;
			}
			case L2Item.SLOT_L_FINGER:
			{
				pdollSlot = PAPERDOLL_LFINGER;
				break;
			}
			case L2Item.SLOT_HEAD:
			{
				pdollSlot = PAPERDOLL_HEAD;
				break;
			}
			case L2Item.SLOT_R_HAND:
			{
				pdollSlot = PAPERDOLL_RHAND;
				break;
			}
			case L2Item.SLOT_L_HAND:
			{
				pdollSlot = PAPERDOLL_LHAND;
				break;
			}
			case L2Item.SLOT_GLOVES:
			{
				pdollSlot = PAPERDOLL_GLOVES;
				break;
			}
			case L2Item.SLOT_CHEST:
			case L2Item.SLOT_FULL_ARMOR:
			{
				pdollSlot = PAPERDOLL_CHEST;
				break;
			}
			case L2Item.SLOT_LEGS:
			{
				pdollSlot = PAPERDOLL_LEGS;
				break;
			}
			case L2Item.SLOT_LR_HAND:
			{
				unEquipSlot(unequipedItems, PAPERDOLL_LHAND);
				unEquipSlot(PAPERDOLL_RHAND);// this should be the same as in LRHAND
				pdollSlot = PAPERDOLL_LRHAND;
				break;
			}
			case L2Item.SLOT_BACK:
			{
				pdollSlot = PAPERDOLL_BACK;
				break;
			}
			case L2Item.SLOT_FEET:
			{
				pdollSlot = PAPERDOLL_FEET;
				break;
			}
			case L2Item.SLOT_UNDERWEAR:
			{
				pdollSlot = PAPERDOLL_UNDER;
				break;
			}			
		}

		unEquipSlot(unequipedItems, pdollSlot);
		
		return (L2ItemInstance[]) unequipedItems.toArray(new L2ItemInstance[unequipedItems.size()]);
	}

	
	/**
	 * same as unEquipItemFromBodySlot, but can be used with L2ItemInstance.getEquipSlot()
	 * @param slot
	 */
	public L2ItemInstance[] unEquipItemOnPaperdoll(int pdollSlot)
	{
		ArrayList unequipedItems = new ArrayList();
		
		_log.fine("--- unequip body slot:" + pdollSlot);
		L2ItemInstance item = null;
		
		if (pdollSlot == PAPERDOLL_LRHAND)
			{
				unEquipSlot(unequipedItems, PAPERDOLL_LHAND);
				unEquipSlot(PAPERDOLL_RHAND);// this should be the same as in LRHAND
			}

		unEquipSlot(unequipedItems, pdollSlot);
		
		return (L2ItemInstance[]) unequipedItems.toArray(new L2ItemInstance[unequipedItems.size()]);
	}

	/**
	 * 
	 * @param item
	 * @return  arraylist of all items that were updated
	 */
	public ArrayList equipItem(L2ItemInstance item)
	{
		ArrayList changedItems = new ArrayList();
		
		int targetSlot = item.getItem().getBodyPart();
		switch (targetSlot)
		{
			case L2Item.SLOT_LR_HAND:
			{
				unEquipSlot(changedItems, PAPERDOLL_LHAND);
				L2ItemInstance old1 = unEquipSlot(PAPERDOLL_LRHAND);
				if (old1 != null)
				{
					// exchange 2h for 2h
					changedItems.add(old1);
					unEquipSlot(PAPERDOLL_RHAND);
					unEquipSlot(changedItems, PAPERDOLL_LHAND);
				}
				else
				{
					unEquipSlot(changedItems, PAPERDOLL_RHAND);
				}
				
				setPaperdollItem(PAPERDOLL_RHAND, item);
				setPaperdollItem(PAPERDOLL_LRHAND, item);
				
				if (((L2Weapon)item.getItem()).getWeaponType() == L2Weapon.WEAPON_TYPE_BOW)
				{
					L2ItemInstance arrow = findArrowForBow(item.getItem());
					if (arrow != null)
					{
						setPaperdollItem(PAPERDOLL_LHAND, arrow);
						arrow.setLastChange(L2ItemInstance.MODIFIED);
						changedItems.add(arrow);
					}
				}
				
				break;
			}
			case L2Item.SLOT_L_HAND:
			{
				L2ItemInstance old1 = unEquipSlot(PAPERDOLL_LRHAND);
				if (old1 != null)
				{
					unEquipSlot(changedItems, PAPERDOLL_RHAND);
				}
				unEquipSlot(changedItems, PAPERDOLL_LHAND);
				setPaperdollItem(PAPERDOLL_LHAND, item);
				break;
			}
			case L2Item.SLOT_R_HAND:
			{
				if (unEquipSlot(changedItems, PAPERDOLL_LRHAND))
				{
					unEquipSlot(changedItems, PAPERDOLL_LHAND);
					unEquipSlot(PAPERDOLL_RHAND);
				}
				else
				{
					unEquipSlot(changedItems, PAPERDOLL_RHAND);
				}
				
				setPaperdollItem(PAPERDOLL_RHAND, item);
				break;
			}
			case L2Item.SLOT_L_EAR | L2Item.SLOT_R_EAR:
			{
				if (_paperdoll[PAPERDOLL_LEAR] == null)
				{
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
				else if (_paperdoll[PAPERDOLL_REAR] == null)
				{
					setPaperdollItem(PAPERDOLL_REAR, item);
				}
				else
				{
					unEquipSlot(changedItems, PAPERDOLL_LEAR);
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
					
				break;
			}
			case L2Item.SLOT_L_FINGER | L2Item.SLOT_R_FINGER:
			{
				if (_paperdoll[PAPERDOLL_LFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				else if (_paperdoll[PAPERDOLL_RFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_RFINGER, item);
				}
				else
				{
					unEquipSlot(changedItems, PAPERDOLL_LFINGER);
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				break;
			}
			case L2Item.SLOT_NECK:
			{
				unEquipSlot(changedItems, PAPERDOLL_NECK);
				setPaperdollItem(PAPERDOLL_NECK, item);
				break;
			}
			case L2Item.SLOT_FULL_ARMOR:
			{
				unEquipSlot(changedItems, PAPERDOLL_CHEST);
				unEquipSlot(changedItems, PAPERDOLL_LEGS);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			}
			case L2Item.SLOT_CHEST:
			{
				unEquipSlot(changedItems, PAPERDOLL_CHEST);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			}
			case L2Item.SLOT_LEGS:
			{
				// handle full armor
				L2ItemInstance chest = getPaperdollItem(PAPERDOLL_CHEST);
				if (chest != null && chest.getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR)
				{
					unEquipSlot(changedItems, PAPERDOLL_CHEST);
				}
				
				unEquipSlot(changedItems, PAPERDOLL_LEGS);
				setPaperdollItem(PAPERDOLL_LEGS, item);
				break;
			}
			case L2Item.SLOT_FEET:
			{
				unEquipSlot(changedItems, PAPERDOLL_FEET);
				setPaperdollItem(PAPERDOLL_FEET, item);
				break;
			}
			case L2Item.SLOT_GLOVES:
			{
				unEquipSlot(changedItems, PAPERDOLL_GLOVES);
				setPaperdollItem(PAPERDOLL_GLOVES, item);
				break;
			}
			case L2Item.SLOT_HEAD:
			{
				unEquipSlot(changedItems, PAPERDOLL_HEAD);
				setPaperdollItem(PAPERDOLL_HEAD, item);
				break;
			}
			case L2Item.SLOT_UNDERWEAR:
			{
				unEquipSlot(changedItems, PAPERDOLL_UNDER);
				setPaperdollItem(PAPERDOLL_UNDER, item);
				break;
			}
			case L2Item.SLOT_BACK:
			{
				unEquipSlot(changedItems, PAPERDOLL_BACK);
				setPaperdollItem(PAPERDOLL_BACK, item);
				break;
			}
			default:
			{
				_log.warning("unknown body slot:" + targetSlot);
			}
		}

		changedItems.add(item);
		item.setLastChange(L2ItemInstance.MODIFIED);
		
		return changedItems;
	}

	private L2ItemInstance unEquipSlot(int slot)
	{
		_log.fine("--- unEquipSlot: "+ slot);
		L2ItemInstance item =_paperdoll[slot]; 
		if (item != null)
		{
			item.setEquipSlot(-1);
			item.setLastChange(L2ItemInstance.MODIFIED);
			_paperdoll[slot] = null;
		}
		
		return item;
	}

	/**
	 * @returns true if an item was unequiped
	 */
	private boolean unEquipSlot(ArrayList changedItems, int slot)
	{
		if (slot == -1)
		{
			// invalid slot 
			return false;
		}
		
		L2ItemInstance item =_paperdoll[slot]; 
		if (item != null)
		{
			item.setEquipSlot(-1);
			changedItems.add(item);
			item.setLastChange(L2ItemInstance.MODIFIED);
			_paperdoll[slot] = null;
		}
		
		return item != null;
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

	/**
	 * @return
	 */
	public L2ItemInstance getAdenaInstance()
	{
		return _adena;
	}
	/**
	 * @return
	 */
	public int getAdena()
	{
		if (_adena == null)
		{
			return 0;
		}
		else
		{
			return _adena.getCount();
		}
	}

	
	public void setAdena(int adena)
	{
		if (adena == 0)
		{
			if ( _adena != null && _items.contains(_adena))
			{
				_items.remove(_adena);
				_adena = null;
			}
		}
		else 
		{
			if (_adena == null)
			{
				_adena = ItemTable.getInstance().createItem(57);
			}
			
			_adena.setCount(adena);
			if (!_items.contains(_adena))
			{
				_items.add(_adena);
			}
		}
	}
	
	/**
	 * @param adena
	 */
	public void addAdena(int adena)
	{
		setAdena(getAdena() + adena);
	}
	
	public void reduceAdena(int adena)
	{
		setAdena(getAdena() - adena);
	}

	/**
	 * @param objectId
	 */
	public L2ItemInstance destroyItem(int objectId, int count)
	{
		L2ItemInstance item = getItem(objectId);
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
		refreshWeight();
		return item;
	}

	// we need this one cuz warehouses send itemId only
	/**
	 * @param objectId
	 */
	public L2ItemInstance destroyItemByItemId(int itemId, int count)
	{
		L2ItemInstance item = findItemByItemId(itemId);
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
		refreshWeight();
		return item;
	}

	/**
	 * @param objectId
	 */
	public L2ItemInstance dropItem(int objectId, int count)
	{
		L2ItemInstance oldItem = getItem(objectId);
		return dropItem(oldItem, count);
	}

	
	
	/**
	 * @param oldItem
	 * @param count
	 * @return
	 */
	public L2ItemInstance dropItem(L2ItemInstance oldItem, int count)
	{
		if (oldItem == null)
		{
			_log.warning("DropItem: item id does not exist in inventory");
			return null;
		}
		
		if (oldItem.isEquipped())
		{
			unEquipItemInBodySlot(oldItem.getItem().getBodyPart());
		}
			
		if (oldItem.getItemId() == 57)
		{
			reduceAdena(count);
			L2ItemInstance adena = ItemTable.getInstance().createItem(oldItem.getItemId());
			adena.setCount(count);
			return adena;
		}
		else if (oldItem.getCount() == count)
		{	
			_log.fine(" count = count  --> remove");
			_items.remove(oldItem);
			oldItem.setLastChange(L2ItemInstance.REMOVED);
			refreshWeight();
			return oldItem;
		}
		else
		{
			_log.fine(" count != count  --> reduce");
			oldItem.setCount(oldItem.getCount()-count);
			oldItem.setLastChange(L2ItemInstance.MODIFIED);
			L2ItemInstance newItem = ItemTable.getInstance().createItem(oldItem.getItemId());
			newItem.setCount(count);
			refreshWeight();
			return newItem;
		}
	}

	private void refreshWeight()
	{
		int weight = 0;
		
		for (Iterator iter = _items.iterator(); iter.hasNext();)
		{
			L2ItemInstance element = (L2ItemInstance) iter.next();
			weight += element.getItem().getWeight() * element.getCount();
		}
		
		_totalWeight = weight;
	}
	
	/**
	 * @return Returns the totalWeight.
	 */
	public int getTotalWeight()
	{
		return _totalWeight;
	}
	
	public L2ItemInstance findArrowForBow(L2Item bow)
	{
		int arrowsId = 0;
		
		switch (bow.getCrystalType())
		{
			case (L2Weapon.CRYSTAL_NONE):
			{
				arrowsId = 17;
				break;
			}
			case (L2Weapon.CRYSTAL_D):
			{
				arrowsId = 1341;
				break;
			}
			case (L2Weapon.CRYSTAL_C):
			{
				arrowsId = 1342;
				break;
			}
			case (L2Weapon.CRYSTAL_B):
			{
				arrowsId = 1343;
				break;
			}
			case (L2Weapon.CRYSTAL_A):
			{
				arrowsId = 1344;
				break;
			}
			case (L2Weapon.CRYSTAL_S):
			{
				arrowsId = 1345;
				break;
			}
			default:
			{
				// broken weapon.csv ??
				arrowsId = 17;
				break;
			}
		}

		 return findItemByItemId(arrowsId);
	}
}
