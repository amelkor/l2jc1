/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2CharTemplate.java,v 1.2 2004/06/27 08:51:43 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:43 $
 * $Revision: 1.2 $
 * $Log: L2CharTemplate.java,v $
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

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:51:43 $
 */
public class L2CharTemplate
{
	private int _classId;
	private String _className;
	private int _str;
	private int _con;
	private int _dex;
	private int _int;
	private int _wit;
	private int _men;
	private int _hp;
	private int _mp;
	private int _patk;
	private int _pdef;
	private int _pspd;
	private int _matk;
	private int _mdef;
	private int _mspd;
	private int _acc;
	private int _crit;
	private int _evas;
	private int _moveSpd;
	private int _load;
	
	private int _x;
	private int _y;
	private int _z;
	
	private double _mUnk1;
	private double _mUnk2;
	private double _mColR;
	private double _mColH;

	private double _fUnk1;
	private double _fUnk2;
	private double _fColR;
	private double _fColH;
	private int _raceId;

	private ArrayList _items = new ArrayList();
	private int _canCraft;
	
	/**
	 * @return Returns the acc.
	 */
	public int getAcc()
	{
		return _acc;
	}
	/**
	 * @param acc The acc to set.
	 */
	public void setAcc(int acc)
	{
		_acc = acc;
	}
	/**
	 * @return Returns the classId.
	 */
	public int getClassId()
	{
		return _classId;
	}
	/**
	 * @param classId The classId to set.
	 */
	public void setClassId(int classId)
	{
		_classId = classId;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return _className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		_className = className;
	}
	/**
	 * @return Returns the con.
	 */
	public int getCon()
	{
		return _con;
	}
	/**
	 * @param con The con to set.
	 */
	public void setCon(int con)
	{
		_con = con;
	}
	/**
	 * @return Returns the crit.
	 */
	public int getCrit()
	{
		return _crit;
	}
	/**
	 * @param crit The crit to set.
	 */
	public void setCrit(int crit)
	{
		_crit = crit;
	}
	/**
	 * @return Returns the dex.
	 */
	public int getDex()
	{
		return _dex;
	}
	/**
	 * @param dex The dex to set.
	 */
	public void setDex(int dex)
	{
		_dex = dex;
	}
	/**
	 * @return Returns the evas.
	 */
	public int getEvas()
	{
		return _evas;
	}
	/**
	 * @param evas The evas to set.
	 */
	public void setEvas(int evas)
	{
		_evas = evas;
	}
	/**
	 * @return Returns the hp.
	 */
	public int getHp()
	{
		return _hp;
	}
	/**
	 * @param hp The hp to set.
	 */
	public void setHp(int hp)
	{
		_hp = hp;
	}
	/**
	 * @return Returns the int.
	 */
	public int getInt()
	{
		return _int;
	}
	/**
	 * @param int1 The int to set.
	 */
	public void setInt(int int1)
	{
		_int = int1;
	}
	/**
	 * @return Returns the load.
	 */
	public int getLoad()
	{
		return _load;
	}
	/**
	 * @param load The load to set.
	 */
	public void setLoad(int load)
	{
		_load = load;
	}
	/**
	 * @return Returns the matk.
	 */
	public int getMatk()
	{
		return _matk;
	}
	/**
	 * @param matk The matk to set.
	 */
	public void setMatk(int matk)
	{
		_matk = matk;
	}
	/**
	 * @return Returns the mdef.
	 */
	public int getMdef()
	{
		return _mdef;
	}
	/**
	 * @param mdef The mdef to set.
	 */
	public void setMdef(int mdef)
	{
		_mdef = mdef;
	}
	/**
	 * @return Returns the men.
	 */
	public int getMen()
	{
		return _men;
	}
	/**
	 * @param men The men to set.
	 */
	public void setMen(int men)
	{
		_men = men;
	}
	/**
	 * @return Returns the moveSpd.
	 */
	public int getMoveSpd()
	{
		return _moveSpd;
	}
	/**
	 * @param moveSpd The moveSpd to set.
	 */
	public void setMoveSpd(int moveSpd)
	{
		_moveSpd = moveSpd;
	}
	/**
	 * @return Returns the mp.
	 */
	public int getMp()
	{
		return _mp;
	}
	/**
	 * @param mp The mp to set.
	 */
	public void setMp(int mp)
	{
		_mp = mp;
	}
	/**
	 * @return Returns the mspd.
	 */
	public int getMspd()
	{
		return _mspd;
	}
	/**
	 * @param mspd The mspd to set.
	 */
	public void setMspd(int mspd)
	{
		_mspd = mspd;
	}
	/**
	 * @return Returns the patk.
	 */
	public int getPatk()
	{
		return _patk;
	}
	/**
	 * @param patk The patk to set.
	 */
	public void setPatk(int patk)
	{
		_patk = patk;
	}
	/**
	 * @return Returns the pdef.
	 */
	public int getPdef()
	{
		return _pdef;
	}
	/**
	 * @param pdef The pdef to set.
	 */
	public void setPdef(int pdef)
	{
		_pdef = pdef;
	}
	/**
	 * @return Returns the pspd.
	 */
	public int getPspd()
	{
		return _pspd;
	}
	/**
	 * @param pspd The pspd to set.
	 */
	public void setPspd(int pspd)
	{
		_pspd = pspd;
	}
	/**
	 * @return Returns the str.
	 */
	public int getStr()
	{
		return _str;
	}
	/**
	 * @param str The str to set.
	 */
	public void setStr(int str)
	{
		_str = str;
	}
	/**
	 * @return Returns the wit.
	 */
	public int getWit()
	{
		return _wit;
	}
	/**
	 * @param wit The wit to set.
	 */
	public void setWit(int wit)
	{
		_wit = wit;
	}
	/**
	 * @return Returns the fColH.
	 */
	public double getFColH()
	{
		return _fColH;
	}
	/**
	 * @param colH The fColH to set.
	 */
	public void setFColH(double colH)
	{
		_fColH = colH;
	}
	/**
	 * @return Returns the fColR.
	 */
	public double getFColR()
	{
		return _fColR;
	}
	/**
	 * @param colR The fColR to set.
	 */
	public void setFColR(double colR)
	{
		_fColR = colR;
	}
	/**
	 * @return Returns the fUnk1.
	 */
	public double getFUnk1()
	{
		return _fUnk1;
	}
	/**
	 * @param unk1 The fUnk1 to set.
	 */
	public void setFUnk1(double unk1)
	{
		_fUnk1 = unk1;
	}
	/**
	 * @return Returns the fUnk2.
	 */
	public double getFUnk2()
	{
		return _fUnk2;
	}
	/**
	 * @param unk2 The fUnk2 to set.
	 */
	public void setFUnk2(double unk2)
	{
		_fUnk2 = unk2;
	}
	/**
	 * @return Returns the mColH.
	 */
	public double getMColH()
	{
		return _mColH;
	}
	/**
	 * @param colH The mColH to set.
	 */
	public void setMColH(double colH)
	{
		_mColH = colH;
	}
	/**
	 * @return Returns the mColR.
	 */
	public double getMColR()
	{
		return _mColR;
	}
	/**
	 * @param colR The mColR to set.
	 */
	public void setMColR(double colR)
	{
		_mColR = colR;
	}
	/**
	 * @return Returns the mUnk1.
	 */
	public double getMUnk1()
	{
		return _mUnk1;
	}
	/**
	 * @param unk1 The mUnk1 to set.
	 */
	public void setMUnk1(double unk1)
	{
		_mUnk1 = unk1;
	}
	/**
	 * @return Returns the mUnk2.
	 */
	public double getMUnk2()
	{
		return _mUnk2;
	}
	/**
	 * @param unk2 The mUnk2 to set.
	 */
	public void setMUnk2(double unk2)
	{
		_mUnk2 = unk2;
	}
	/**
	 * @return Returns the x.
	 */
	public int getX()
	{
		return _x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(int x)
	{
		_x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public int getY()
	{
		return _y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(int y)
	{
		_y = y;
	}
	/**
	 * @return Returns the z.
	 */
	public int getZ()
	{
		return _z;
	}
	/**
	 * @param z The z to set.
	 */
	public void setZ(int z)
	{
		_z = z;
	}
	/**
	 * @param i
	 */
	public void setRaceId(int raceId)
	{
		_raceId = raceId;
	}
	/**
	 * @return Returns the raceId.
	 */
	public int getRaceId()
	{
		return _raceId;
	}
	/**
	 * add starter equipment
	 * @param i
	 */
	public void addItem(int itemId)
	{
		_items.add(new Integer(itemId));
	}
	
	/**
	 *
	 * @return itemIds of all the starter equipment
	 */
	public Integer[] getItems()
	{
		return (Integer[]) _items.toArray(new Integer[_items.size()]);
	}
	/**
	 * @param b
	 */
	public void setCanCraft(int b)
	{
		_canCraft = b;
	}
	/**
	 * @return Returns the canCraft.
	 */
	public int getCanCraft()
	{
		return _canCraft;
	}
}
