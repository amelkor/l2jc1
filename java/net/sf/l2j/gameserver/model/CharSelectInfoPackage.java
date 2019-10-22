/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/CharSelectInfoPackage.java,v 1.2 2004/09/24 19:42:19 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/09/24 19:42:19 $
 * $Revision: 1.2 $
 * $Log: CharSelectInfoPackage.java,v $
 * Revision 1.2  2004/09/24 19:42:19  jeichhorn
 * fixed file comments
 *
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
 * Used to Store data sent to Client for Character
 * Selection screen.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/24 19:42:19 $
 */
public class CharSelectInfoPackage 
{
	private String _name;
	private int _charId = 0x00030b7a;
	private int _exp =0;
	private int _sp =0;
	private int _clanId=0;
	private int _race=0;
	private int _classId=0;
	private int _deleteTimer=0;
	private int _face=0;
	private int _hairStyle=0;
	private int _hairColor=0;
	private int _sex=0;
	private int _level = 1;
	private int _maxHp=0;
	private double _currentHp=0;
	private int _maxMp=0;
	private double _currentMp=0;
	private Inventory _inventory = new Inventory();
	
	
	public int getCharId()
	{
		return _charId;
	}
	public void setCharId(int charId)
	{
		this._charId = charId;
	}
	public int getClanId()
	{
		return _clanId;
	}
	public void setClanId(int clanId)
	{
		this._clanId = clanId;
	}
	public int getClassId()
	{
		return _classId;
	}
	public void setClassId(int classId)
	{
		this._classId = classId;
	}
	public double getCurrentHp()
	{
		return _currentHp;
	}
	public void setCurrentHp(double currentHp)
	{
		this._currentHp = currentHp;
	}
	public double getCurrentMp()
	{
		return _currentMp;
	}
	public void setCurrentMp(double currentMp)
	{
		this._currentMp = currentMp;
	}
	public int getDeleteTimer()
	{
		return _deleteTimer;
	}
	public void setDeleteTimer(int deleteTimer)
	{
		this._deleteTimer = deleteTimer;
	}
	public int getExp()
	{
		return _exp;
	}
	public void setExp(int exp)
	{
		this._exp = exp;
	}
	public int getFace()
	{
		return _face;
	}
	public void setFace(int face)
	{
		this._face = face;
	}
	public int getHairColor()
	{
		return _hairColor;
	}
	public void setHairColor(int hairColor)
	{
		this._hairColor = hairColor;
	}
	public int getHairStyle()
	{
		return _hairStyle;
	}
	public void setHairStyle(int hairStyle)
	{
		this._hairStyle = hairStyle;
	}
	public Inventory getInventory()
	{
		return _inventory;
	}
	public void setInventory(Inventory inventory)
	{
		this._inventory = inventory;
	}
	public int getLevel()
	{
		return _level;
	}
	public void setLevel(int level)
	{
		this._level = level;
	}
	public int getMaxHp()
	{
		return _maxHp;
	}
	public void setMaxHp(int maxHp)
	{
		this._maxHp = maxHp;
	}
	public int getMaxMp()
	{
		return _maxMp;
	}
	public void setMaxMp(int maxMp)
	{
		this._maxMp = maxMp;
	}
	public String getName()
	{
		return _name;
	}
	public void setName(String name)
	{
		this._name = name;
	}
	public int getRace()
	{
		return _race;
	}
	public void setRace(int race)
	{
		this._race = race;
	}
	public int getSex()
	{
		return _sex;
	}
	public void setSex(int sex)
	{
		this._sex = sex;
	}
	public int getSp()
	{
		return _sp;
	}
	public void setSp(int sp)
	{
		this._sp = sp;
	}
}
