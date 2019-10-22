/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2SkillLearn.java,v 1.2 2004/06/27 08:51:42 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:42 $
 * $Revision: 1.2 $
 * $Log: L2SkillLearn.java,v $
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
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:51:42 $
 */
public class L2SkillLearn
{
	// these two build the primary key
	private int _id;
	private int _level;
	
	// not needed, just for easier debug
	private String _name;
	
	private int _spCost;
	private int _minLevel;
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
	 * @return Returns the minLevel.
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	/**
	 * @param minLevel The minLevel to set.
	 */
	public void setMinLevel(int minLevel)
	{
		_minLevel = minLevel;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return _name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		_name = name;
	}
	/**
	 * @return Returns the spCost.
	 */
	public int getSpCost()
	{
		return _spCost;
	}
	/**
	 * @param spCost The spCost to set.
	 */
	public void setSpCost(int spCost)
	{
		_spCost = spCost;
	}
}
