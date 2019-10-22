/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/Location.java,v 1.1 2004/08/11 22:56:37 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/11 22:56:37 $
 * $Revision: 1.1 $
 * $Log: Location.java,v $
 * Revision 1.1  2004/08/11 22:56:37  l2chef
 * init
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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
 * @version $Revision: 1.1 $ $Date: 2004/08/11 22:56:37 $
 */

public class Location
{
	private int _x;
	private int _y;
	private int _z;
	

	public Location(int x, int y, int z)
	{
		_x = x;
		_y = y;
		_z = z;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public int getZ()
	{
		return _z;
	}
}
