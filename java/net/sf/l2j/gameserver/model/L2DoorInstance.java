/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2DoorInstance.java,v 1.3 2004/07/04 11:13:27 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:13:27 $
 * $Revision: 1.3 $
 * $Log: L2DoorInstance.java,v $
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

import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:13:27 $
 */
public class L2DoorInstance extends L2Object
{
	private static Logger _log = Logger.getLogger(L2DoorInstance.class.getName());
	
	private	int _damage;
	private int _open;
	private int _enemy;
	private int _unknown;
	
	/**
	 */
	public L2DoorInstance()
	{
		super();
	}
	
	/**
	 * this is called when a player interacts with this NPC
	 * @param player
	 */
	public void onAction(L2PcInstance player)
	{
		_log.fine("Door activated");
	}
	/**
	 * @return Returns the damage.
	 */
	public int getDamage()
	{
		return _damage;
	}
	/**
	 * @param damage The damage to set.
	 */
	public void setDamage(int damage)
	{
		_damage = damage;
	}
	/**
	 * @return Returns the enemy.
	 */
	public int getEnemy()
	{
		return _enemy;
	}
	/**
	 * @param enemy The enemy to set.
	 */
	public void setEnemy(int enemy)
	{
		_enemy = enemy;
	}
	/**
	 * @return Returns the open.
	 */
	public int getOpen()
	{
		return _open;
	}
	/**
	 * @param open The open to set.
	 */
	public void setOpen(int open)
	{
		_open = open;
	}
	/**
	 * @return Returns the unknown.
	 */
	public int getUnknown()
	{
		return _unknown;
	}
	/**
	 * @param unknown The unknown to set.
	 */
	public void setUnknown(int unknown)
	{
		_unknown = unknown;
	}
}
