/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2Armor.java,v 1.2 2004/06/27 08:51:43 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:43 $
 * $Revision: 1.2 $
 * $Log: L2Armor.java,v $
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
public class L2Armor extends L2Item
{
	public static final int ARMORTYPE_NONE = 0x01;
	public static final int ARMORTYPE_LIGHT = 0x02;
	public static final int ARMORTYPE_HEAVY = 0x03;
	public static final int ARMORTYPE_MAGIC = 0x04;

	private int _armorType;
	private int _avoidModifier;
	private int _pDef;
	private int _mDef;
	private int _mpBonus;
	

	/**
	 * @param i
	 */
	public void setAvoidModifier(int i)
	{
		_avoidModifier = i;
	}

	/**
	 * @param i
	 */
	public void setMpBonus(int i)
	{
		_mpBonus = i;
	}
	/**
	 * @return Returns the mDef.
	 */
	public int getMDef()
	{
		return _mDef;
	}
	/**
	 * @param def The mDef to set.
	 */
	public void setMDef(int def)
	{
		_mDef = def;
	}
	/**
	 * @return Returns the pDef.
	 */
	public int getPDef()
	{
		return _pDef;
	}
	/**
	 * @param def The pDef to set.
	 */
	public void setPDef(int def)
	{
		_pDef = def;
	}
	/**
	 * @return Returns the avoidModifier.
	 */
	public int getAvoidModifier()
	{
		return _avoidModifier;
	}
	/**
	 * @return Returns the mpBonus.
	 */
	public int getMpBonus()
	{
		return _mpBonus;
	}
	/**
	 * @return Returns the armorType.
	 */
	public int getArmorType()
	{
		return _armorType;
	}
	/**
	 * @param armorType The armorType to set.
	 */
	public void setArmorType(int armorType)
	{
		_armorType = armorType;
	}
}
