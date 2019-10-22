/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2Npc.java,v 1.4 2004/08/08 00:48:45 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/08 00:48:45 $
 * $Revision: 1.4 $
 * $Log: L2Npc.java,v $
 * Revision 1.4  2004/08/08 00:48:45  l2chef
 * npc datatable merged with npctable
 *
 * Revision 1.3  2004/07/13 22:51:12  l2chef
 * removed empty constructor
 *
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

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.L2DropData;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/08/08 00:48:45 $
 */
public class L2Npc
{
	private int _npcId;
	private String _type;
	private double _radius;
	private double _height;
	private String _name;
	private String _sex;
	private int _level;
	private int _attackRange;
	private int _hp;
	private int _mp;
	private int _exp;
	private int _sp;
	private int _patk;
	private int _pdef;
	private int _matk;
	private int _mdef;
	private int _atkspd;
	private boolean _agro;
	private int _matkspd;
	private int _rhand;
	private int _lhand;
	private int _armor;
	private int _walkSpeed;
	private int _runSpeed;
	private List _drops = new ArrayList();

	/**
	 * @return Returns the agro.
	 */
	public boolean getAgro()
	{
		return _agro;
	}
	/**
	 * @return Returns the armor.
	 */
	public int getArmor()
	{
		return _armor;
	}
	/**
	 * @return Returns the atkspd.
	 */
	public int getAtkspd()
	{
		return _atkspd;
	}
	/**
	 * @return Returns the attackrange.
	 */
	public int getAttackRange()
	{
		return _attackRange;
	}
	/**
	 * @return Returns the exp.
	 */
	public int getExp()
	{
		return _exp;
	}
	/**
	 * @return Returns the hp.
	 */
	public int getHp()
	{
		return _hp;
	}
	/**
	 * @return Returns the level.
	 */
	public int getLevel()
	{
		return _level;
	}
	/**
	 * @return Returns the lhand.
	 */
	public int getLhand()
	{
		return _lhand;
	}
	/**
	 * @return Returns the matk.
	 */
	public int getMatk()
	{
		return _matk;
	}
	/**
	 * @return Returns the matkspd.
	 */
	public int getMatkspd()
	{
		return _matkspd;
	}
	/**
	 * @return Returns the mdef.
	 */
	public int getMdef()
	{
		return _mdef;
	}
	/**
	 * @return Returns the mp.
	 */
	public int getMp()
	{
		return _mp;
	}
	/**
	 * @return Returns the patk.
	 */
	public int getPatk()
	{
		return _patk;
	}
	/**
	 * @return Returns the pdef.
	 */
	public int getPdef()
	{
		return _pdef;
	}
	/**
	 * @return Returns the rhand.
	 */
	public int getRhand()
	{
		return _rhand;
	}
	/**
	 * @return Returns the runspd.
	 */
	public int getRunSpeed()
	{
		return _runSpeed;
	}
	/**
	 * @return Returns the sex.
	 */
	public String getSex()
	{
		return _sex;
	}
	/**
	 * @return Returns the sp.
	 */
	public int getSp()
	{
		return _sp;
	}
	/**
	 * @return Returns the walkspd.
	 */
	public int getWalkSpeed()
	{
		return _walkSpeed;
	}
	/**
	 * @param agro
	 *            The agro to set.
	 */
	public void setAgro(boolean agro)
	{
		_agro = agro;
	}
	/**
	 * @param armor
	 *            The armor to set.
	 */
	public void setArmor(int armor)
	{
		_armor = armor;
	}
	/**
	 * @param atkspd
	 *            The atkspd to set.
	 */
	public void setAtkspd(int atkspd)
	{
		_atkspd = atkspd;
	}
	/**
	 * @param attackrange
	 *            The attackrange to set.
	 */
	public void setAttackRange(int attackrange)
	{
		_attackRange = attackrange;
	}
	/**
	 * @param exp
	 *            The exp to set.
	 */
	public void setExp(int exp)
	{
		_exp = exp;
	}
	/**
	 * @param hp
	 *            The hp to set.
	 */
	public void setHp(int hp)
	{
		_hp = hp;
	}
	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	/**
	 * @param lhand
	 *            The lhand to set.
	 */
	public void setLhand(int lhand)
	{
		_lhand = lhand;
	}
	/**
	 * @param matk
	 *            The matk to set.
	 */
	public void setMatk(int matk)
	{
		_matk = matk;
	}
	/**
	 * @param matkspd
	 *            The matkspd to set.
	 */
	public void setMatkspd(int matkspd)
	{
		_matkspd = matkspd;
	}
	/**
	 * @param mdef
	 *            The mdef to set.
	 */
	public void setMdef(int mdef)
	{
		_mdef = mdef;
	}
	/**
	 * @param mp
	 *            The mp to set.
	 */
	public void setMp(int mp)
	{
		_mp = mp;
	}
	/**
	 * @param patk
	 *            The patk to set.
	 */
	public void setPatk(int patk)
	{
		_patk = patk;
	}
	/**
	 * @param pdef
	 *            The pdef to set.
	 */
	public void setPdef(int pdef)
	{
		_pdef = pdef;
	}
	/**
	 * @param rhand
	 *            The rhand to set.
	 */
	public void setRhand(int rhand)
	{
		_rhand = rhand;
	}
	/**
	 * @param runspd
	 *            The runspd to set.
	 */
	public void setRunSpeed(int runspd)
	{
		_runSpeed = runspd;
	}
	/**
	 * @param sex
	 *            The sex to set.
	 */
	public void setSex(String sex)
	{
		_sex = sex;
	}
	/**
	 * @param sp
	 *            The sp to set.
	 */
	public void setSp(int sp)
	{
		_sp = sp;
	}
	/**
	 * @param walkspd
	 *            The walkspd to set.
	 */
	public void setWalkSpeed(int walkspd)
	{
		_walkSpeed = walkspd;
	}
	
	/**
	 * @param id
	 */
	public void setNpcId(int id)
	{
		_npcId = id;
	}
	
	/**
	 * @param string
	 */
	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @param string
	 */
	public void setType(String type)
	{
		_type = type;
	}

	/**
	 * @param radius
	 */
	public void setRadius(double radius)
	{
		_radius = radius;
	}

	/**
	 * @param height
	 */
	public void setHeight(double height)
	{
		_height = height;
	}
	/**
	 * @return
	 */
	public double getHeight()
	{
		return _height;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @return
	 */
	public int getNpcId()
	{
		return _npcId;
	}

	/**
	 * @return
	 */
	public double getRadius()
	{
		return _radius;
	}

	/**
	 * @return
	 */
	public String getType()
	{
		return _type;
	}
	
	public void addDropData(L2DropData drop)
	{
		_drops.add(drop);
	}
	
	/**
	 * 
	 * @return list of L2DropData objects
	 */
	public List getDropData()
	{
		return _drops;
	}

}
