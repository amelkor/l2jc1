/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2DropData.java,v 1.1 2004/08/08 00:47:44 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/08 00:47:44 $
 * $Revision: 1.1 $
 * $Log: L2DropData.java,v $
 * Revision 1.1  2004/08/08 00:47:44  l2chef
 * droptable from LittleVexy added
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

/**
/*
 * 
 * Special thanks to nuocnam
 * Author: LittleVexy 
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/08 00:47:44 $
 */
public class L2DropData
{
	public static final int MAX_CHANCE = 1000000;

	private int _itemId;
	private int _mindrop;
	private int _maxdrop;
	private boolean _sweep;
	private int _chance;

	
	public int getItemId()
	{
		return _itemId;
	}

	public void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinDrop()
	{
		return _mindrop;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxDrop()
	{
		return _maxdrop;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSweep()
	{
		return _sweep;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getChance()
	{
		return _chance;
	}
	
	/**
	 * 
	 * @param mindrop
	 */
	public void setMinDrop(int mindrop)
	{
		_mindrop = mindrop;
	}
	
	/**
	 * 
	 * @param maxdrop
	 */
	public void setMaxDrop(int maxdrop)
	{
		_maxdrop = maxdrop;
	}
	
	/**
	 * 
	 * @param sweep
	 */
	public void setSweep(boolean sweep)
	{
		_sweep = sweep;
	}
	
	/**
	 * 
	 * @param chance
	 */
	public void setChance(int chance)
	{
		_chance = chance;
	}
}