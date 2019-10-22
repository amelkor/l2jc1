/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2Weapon.java,v 1.2 2004/06/27 08:51:43 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:43 $
 * $Revision: 1.2 $
 * $Log: L2Weapon.java,v $
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
public class L2Weapon  extends L2Item
{
	public static final int WEAPON_TYPE_NONE = 0x01;
	public static final int WEAPON_TYPE_SWORD = 0x02;
	public static final int WEAPON_TYPE_BLUNT = 0x03;
	public static final int WEAPON_TYPE_DAGGER = 0x04;
	public static final int WEAPON_TYPE_BOW = 0x05;
	public static final int WEAPON_TYPE_POLE = 0x06;
	public static final int WEAPON_TYPE_ETC = 0x07;
	public static final int WEAPON_TYPE_FIST = 0x08;
	public static final int WEAPON_TYPE_DUAL = 0x09;
	public static final int WEAPON_TYPE_DUALFIST = 0x0a;
	
	private int _soulShotCount;
	private int _spiritShotCount;
	private int _pDam;
	private int _rndDam;
	private int _weaponType;
	private int _critical;
	private double _hitModifier;
	private int _avoidModifier;
	private int _shieldDef;
	private int _shieldDefRate;
	private int _atkSpeed;
	private int _mpConsume;
	private int _mDam;
	
	
	
	/**
	 * @return Returns the soulShotCount.
	 */
	public int getSoulShotCount()
	{
		return _soulShotCount;
	}
	/**
	 * @param soulShotCount The soulShotCount to set.
	 */
	public void setSoulShotCount(int soulShotCount)
	{
		_soulShotCount = soulShotCount;
	}
	/**
	 * @return Returns the spiritShotCount.
	 */
	public int getSpiritShotCount()
	{
		return _spiritShotCount;
	}
	/**
	 * @param spiritShotCount The spiritShotCount to set.
	 */
	public void setSpiritShotCount(int spiritShotCount)
	{
		_spiritShotCount = spiritShotCount;
	}
	/**
	 * @return Returns the pDam.
	 */
	public int getPDamage()
	{
		return _pDam;
	}
	/**
	 * @param dam The pDam to set.
	 */
	public void setPDamage(int dam)
	{
		_pDam = dam;
	}
	/**
	 * @return Returns the rndDam.
	 */
	public int getRandomDamage()
	{
		return _rndDam;
	}
	/**
	 * @param rndDam The rndDam to set.
	 */
	public void setRandomDamage(int rndDam)
	{
		_rndDam = rndDam;
	}
	/**
	 * @return Returns the weaponType.
	 */
	public int getWeaponType()
	{
		return _weaponType;
	}
	/**
	 * @param weaponType The weaponType to set.
	 */
	public void setWeaponType(int weaponType)
	{
		_weaponType = weaponType;
	}
	/**
	 * @return Returns the atkSpeed.
	 */
	public int getAttackSpeed()
	{
		return _atkSpeed;
	}
	/**
	 * @param atkSpeed The atkSpeed to set.
	 */
	public void setAttackSpeed(int atkSpeed)
	{
		_atkSpeed = atkSpeed;
	}
	/**
	 * @return Returns the avoidModifier.
	 */
	public int getAvoidModifier()
	{
		return _avoidModifier;
	}
	/**
	 * @param avoidModifier The avoidModifier to set.
	 */
	public void setAvoidModifier(int avoidModifier)
	{
		_avoidModifier = avoidModifier;
	}
	/**
	 * @return Returns the critical.
	 */
	public int getCritical()
	{
		return _critical;
	}
	/**
	 * @param critical The critical to set.
	 */
	public void setCritical(int critical)
	{
		_critical = critical;
	}
	/**
	 * @return Returns the hitModifier.
	 */
	public double getHitModifier()
	{
		return _hitModifier;
	}
	/**
	 * @param hitModifier The hitModifier to set.
	 */
	public void setHitModifier(double hitModifier)
	{
		_hitModifier = hitModifier;
	}
	/**
	 * @return Returns the mDam.
	 */
	public int getMDamage()
	{
		return _mDam;
	}
	/**
	 * @param dam The mDam to set.
	 */
	public void setMDamage(int dam)
	{
		_mDam = dam;
	}
	/**
	 * @return Returns the mpConsume.
	 */
	public int getMpConsume()
	{
		return _mpConsume;
	}
	/**
	 * @param mpConsume The mpConsume to set.
	 */
	public void setMpConsume(int mpConsume)
	{
		_mpConsume = mpConsume;
	}
	/**
	 * @return Returns the shieldDef.
	 */
	public int getShieldDef()
	{
		return _shieldDef;
	}
	/**
	 * @param shieldDef The shieldDef to set.
	 */
	public void setShieldDef(int shieldDef)
	{
		_shieldDef = shieldDef;
	}
	/**
	 * @return Returns the shieldDefRate.
	 */
	public int getShieldDefRate()
	{
		return _shieldDefRate;
	}
	/**
	 * @param shieldDefRate The shieldDefRate to set.
	 */
	public void setShieldDefRate(int shieldDefRate)
	{
		_shieldDefRate = shieldDefRate;
	}
}
