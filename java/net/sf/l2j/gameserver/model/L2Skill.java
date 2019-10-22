/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Skill.java,v 1.3 2004/08/17 00:49:19 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/17 00:49:19 $
 * $Revision: 1.3 $
 * $Log: L2Skill.java,v $
 * Revision 1.3  2004/08/17 00:49:19  l2chef
 * new skill handlers contributed by Angel Kira.
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
 * @version $Revision: 1.3 $ $Date: 2004/08/17 00:49:19 $
 */
public class L2Skill
{
	public static int OP_ALWAYS = 1;
	public static int OP_ONCE = 2;
	public static int OP_DURATION = 3;
	public static int OP_TOGGLE = 4;
	
	public static int TARGET_SELF = 0;
	public static int TARGET_ONE = 1;
	public static int TARGET_PARTY = 2;
	public static int TARGET_CLAN = 3;
	public static int TARGET_PET = 4;
	public static int TARGET_ENEMY = 5;	// maybe not needed
	public static int TARGET_FRIEND = 6; // maybe not needed
	
	
	// these two build the primary key
	private int _id;
	private int _level;
	
	// not needed, just for easier debug
	private String _name;
	private int _operateType;
	private boolean _magic;
	private int _mpConsume;
	private int _hpConsume;
	private int _itemConsume;
	private int _itemConsumeId;
	private int _castRange;
	
	// all times in milliseconds 
	private int _skillTime;
	private int _hitTime;
	private int _reuseDelay;
	private int _buffDuration;
	
	private int _targetType;
	private int _power;
	
	public int getPower()
	{
		return _power;
	}
	
	public void setPower(int power)
	{
		_power = power;
	}
	
	/**
	 * @return Returns the buffDuration.
	 */
	public int getBuffDuration()
	{
		return _buffDuration;
	}
	/**
	 * @param buffDuration The buffDuration to set.
	 */
	public void setBuffDuration(int buffDuration)
	{
		_buffDuration = buffDuration;
	}
	/**
	 * @return Returns the castRange.
	 */
	public int getCastRange()
	{
		return _castRange;
	}
	/**
	 * @param castRange The castRange to set.
	 */
	public void setCastRange(int castRange)
	{
		_castRange = castRange;
	}
	/**
	 * @return Returns the hitTime.
	 */
	public int getHitTime()
	{
		return _hitTime;
	}
	/**
	 * @param hitTime The hitTime to set.
	 */
	public void setHitTime(int hitTime)
	{
		_hitTime = hitTime;
	}
	/**
	 * @return Returns the hpConsume.
	 */
	public int getHpConsume()
	{
		return _hpConsume;
	}
	/**
	 * @param hpConsume The hpConsume to set.
	 */
	public void setHpConsume(int hpConsume)
	{
		_hpConsume = hpConsume;
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
	 * @return Returns the itemConsume.
	 */
	public int getItemConsume()
	{
		return _itemConsume;
	}
	/**
	 * @param itemConsume The itemConsume to set.
	 */
	public void setItemConsume(int itemConsume)
	{
		_itemConsume = itemConsume;
	}
	/**
	 * @return Returns the itemConsumeId.
	 */
	public int getItemConsumeId()
	{
		return _itemConsumeId;
	}
	/**
	 * @param itemConsumeId The itemConsumeId to set.
	 */
	public void setItemConsumeId(int itemConsumeId)
	{
		_itemConsumeId = itemConsumeId;
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
	 * @return Returns the magic.
	 */
	public boolean isMagic()
	{
		return _magic;
	}
	/**
	 * @param magic The magic to set.
	 */
	public void setMagic(boolean magic)
	{
		_magic = magic;
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
	 * @return Returns the operateType.
	 */
	public int getOperateType()
	{
		return _operateType;
	}
	/**
	 * @param operateType The operateType to set.
	 */
	public void setOperateType(int operateType)
	{
		_operateType = operateType;
	}
	/**
	 * @return Returns the reuseDelay.
	 */
	public int getReuseDelay()
	{
		return _reuseDelay;
	}
	/**
	 * @param reuseDelay The reuseDelay to set.
	 */
	public void setReuseDelay(int reuseDelay)
	{
		_reuseDelay = reuseDelay;
	}
	/**
	 * @return Returns the skillTime.
	 */
	public int getSkillTime()
	{
		return _skillTime;
	}
	/**
	 * @param skillTime The skillTime to set.
	 */
	public void setSkillTime(int skillTime)
	{
		_skillTime = skillTime;
	}
	/**
	 * @return Returns the targetType.
	 */
	public int getTargetType()
	{
		return _targetType;
	}
	/**
	 * @param targetType The targetType to set.
	 */
	public void setTargetType(int targetType)
	{
		_targetType = targetType;
	}
	/**
	 * @return
	 */
	public boolean isPassive()
	{
		return _operateType == OP_ALWAYS;
	}
}
