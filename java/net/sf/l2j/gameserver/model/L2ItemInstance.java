/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2ItemInstance.java,v 1.4 2004/08/04 21:14:49 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/04 21:14:49 $
 * $Revision: 1.4 $
 * $Log: L2ItemInstance.java,v $
 * Revision 1.4  2004/08/04 21:14:49  l2chef
 * new flag for items added to prevent duping
 *
 * Revision 1.3  2004/07/17 23:04:07  l2chef
 * setting the target before movement
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


import net.sf.l2j.gameserver.templates.*;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/08/04 21:14:49 $
 */
public class L2ItemInstance extends L2Object
{
	private int _count = 1;
	private int _itemId;
	private boolean _equipped = false;
	private L2Item	_item;
	private int _equippedSlot = -1;
	
	
	private int _price;
	private int _enchantLevel;
	public static final int ADDED = 1;
	public static final int REMOVED = 3;
	public static final int MODIFIED = 2;
	private int _lastChange = 2;	//1 ??, 2 modified, 3 removed
	private boolean _onTheGround;
	
	
	/**
	 * @return
	 */
	public int getCount()
	{
		return _count;
	}

	/**
	 * @param count
	 */
	public void setCount(int count)
	{
		_count = count;
	}

	public boolean isEquipable()
	{
		
		return !(_item.getBodyPart() == 0 || _item instanceof L2EtcItem );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEquipped()
	{
		return _equippedSlot != -1;
	}

	/**
	 * @param equipped
	 */
	public void setEquipSlot(int slot)
	{
		_equippedSlot = slot;
	}

	/**
	 * @param equipped
	 */
	public int getEquipSlot()
	{
		return _equippedSlot;
	}
	
	/**
	 * @return
	 */
	public L2Item getItem()
	{
		return _item;
	}

	/**
	 * @param item
	 */
	public void setItem(L2Item item)
	{
		_item = item;
		_itemId = item.getItemId();
	}

	/**
	 * @return
	 */
	public int getItemId()
	{
		return _itemId;
	}

	/**
	 * @return Returns the price.
	 */
	public int getPrice()
	{
		return _price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(int price)
	{
		_price = price;
	}

	/**
	 * @return Returns the lastChange.
	 */
	public int getLastChange()
	{
		return _lastChange;
	}

	/**
	 * @param lastChange The lastChange to set.
	 */
	public void setLastChange(int lastChange)
	{
		_lastChange = lastChange;
	}

	public boolean isStackable()
	{
		return _item.isStackable();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#onAction(net.sf.l2j.gameserver.model.L2PcInstance)
	 */
	public void onAction(L2PcInstance player)
	{
		// this causes the validate position handler to do the pickup if the location is reached.
		player.setCurrentState(L2Character.STATE_PICKUP_ITEM);

		player.setTarget(this);
		player.moveTo(getX(), getY(), getZ(), 0 );
	}
	/**
	 * @return Returns the enchantLevel.
	 */
	public int getEnchantLevel()
	{
		return _enchantLevel;
	}
	/**
	 * @param enchantLevel The enchantLevel to set.
	 */
	public void setEnchantLevel(int enchantLevel)
	{
		_enchantLevel = enchantLevel;
	}

	/**
	 * @return
	 */
	public boolean isOnTheGround()
	{
		return _onTheGround;
	}

	/**
	 * @param b
	 */
	public void setOnTheGround(boolean b)
	{
		_onTheGround = b;
	}
}
