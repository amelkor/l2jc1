/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2TeleportLocation.java,v 1.2 2004/07/13 22:57:00 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 22:57:00 $
 * $Revision: 1.2 $
 * $Log: L2TeleportLocation.java,v $
 * Revision 1.2  2004/07/13 22:57:00  l2chef
 * removed empty constructor
 *
 * Revision 1.1  2004/07/08 22:20:28  l2chef
 * teleporter npcs added by NightMarez and Nuocnam
 *
 * Revision 1.1  2004/07/07 22:09:51  nuocnam
 * Initial release
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
 * @version $Revision: 1.2 $ $Date: 2004/07/13 22:57:00 $
 */
public class L2TeleportLocation
{
	private int _teleId;
	private int _locX;
	private int _locY;
	private int _locZ;
	private int _price;


	/**
	 * @param id
	 */
	public void setTeleId(int id)
	{
		_teleId = id;
	}

	/**
	 * @param locX
	 */
	public void setLocX(int locX)
	{
		_locX = locX;
	}

	/**
	 * @param locY
	 */
	public void setLocY(int locY)
	{
		_locY = locY;
	}

	/**
	 * @param locZ
	 */
	public void setLocZ(int locZ)
	{
		_locZ = locZ;
	}

	/**
	 * @param locZ
	 */
	public void setPrice(int price)
	{
		_price = price;
	}


	/**
	 * @return
	 */
	public int getTeleId()
	{
		return _teleId;
	}

	/**
	 * @return
	 */
	public int getLocX()
	{
		return _locX;
	}

	/**
	 * @return
	 */
	public int getLocY()
	{
		return _locY;
	}

	/**
	 * @return
	 */
	public int getLocZ()
	{
		return _locZ;
	}

	/**
	 * @return
	 */
	public int getPrice()
	{
		return _price;
	}


}
