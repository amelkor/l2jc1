/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2EtcItem.java,v 1.2 2004/06/27 08:51:43 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:43 $
 * $Revision: 1.2 $
 * $Log: L2EtcItem.java,v $
 * Revision 1.2  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.gameserver.templates;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:51:43 $
 */
public class L2EtcItem  extends L2Item
{
	public static final int TYPE_ARROW = 0;
	public static final int TYPE_MATERIAL = 1;
	public static final int TYPE_PET_COLLAR = 2;
	public static final int TYPE_POTION = 3;
	public static final int TYPE_RECEIPE = 4;
	public static final int TYPE_SCROLL = 5;
	public static final int TYPE_QUEST = 6;
	public static final int TYPE_MONEY = 7;
	public static final int TYPE_OTHER = 8;
	public static final int TYPE_SPELLBOOK = 9;
		
	
	private int _type;	// specific type, only server internal

	/**
	 * @return Returns the type.
	 */
	public int getEtcItemType()
	{
		return _type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setEtcItemType(int type)
	{
		_type = type;
	}


}
