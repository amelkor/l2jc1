/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2ShortCut.java,v 1.3 2004/10/23 22:12:44 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 22:12:44 $
 * $Revision: 1.3 $
 * $Log: L2ShortCut.java,v $
 * Revision 1.3  2004/10/23 22:12:44  l2chef
 * use new shortcut constant
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

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/10/23 22:12:44 $
 */
public class L2ShortCut
{
	public final static int TYPE_SKILL = 2;
	public final static int TYPE_ACTION = 3;
	public final static int TYPE_ITEM = 1;
	
	private int _slot;
	private int _type;
	private int _id;
	private int _level;
	private int _unk;
	
	/**
	 * 
	 */
	public L2ShortCut(int slot, int type, int id, int level, int unk) 
	{
		super();
		_slot = slot;
		_type = type;
		_id = id;
		_level = level;
		_unk = unk;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return _id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id)
	{
		_id = id;
	}
	/**
	 * @return Returns the level.
	 */
	public int getLevel()
	{
		return _level;
	}
	/**
	 * @param level The level to set.
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	/**
	 * @return Returns the slot.
	 */
	public int getSlot()
	{
		return _slot;
	}
	/**
	 * @param slot The slot to set.
	 */
	public void setSlot(int slot)
	{
		_slot = slot;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType()
	{
		return _type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type)
	{
		_type = type;
	}
	/**
	 * @return Returns the unk.
	 */
	public int getUnk()
	{
		return _unk;
	}
	/**
	 * @param unk The unk to set.
	 */
	public void setUnk(int unk)
	{
		_unk = unk;
	}
}
