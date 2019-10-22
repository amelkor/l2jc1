/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2LvlupData.java,v 1.2 2004/09/28 02:23:46 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:23:46 $
 * $Revision: 1.2 $
 * $Log: L2LvlupData.java,v $
 * Revision 1.2  2004/09/28 02:23:46  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/07/14 22:05:10  l2chef
 * Hp/Mp increase added (NightMarez)
 *
 *
 * Revision 1.1  2004/07/10 16:09:51  NightMarez.
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
 * @author NightMarez
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:23:46 $
 */
public class L2LvlupData
{
	private int _classid;
	private double _defaulthp;
	private double _defaulthpadd;
	private double _defaulthpbonus;
	private double _defaultmp;
	private double _defaultmpadd;
	private double _defaultmpbonus;

	
	
	/**
	 * @return Returns the classid.
	 */
	public int getClassid() {
		return _classid;
	}
	/**
	 * @return Returns the defaulthp.
	 */
	public double getDefaulthp() {
		return _defaulthp;
	}
	/**
	 * @return Returns the defaulthpadd.
	 */
	public double getDefaulthpadd() {
		return _defaulthpadd;
	}
	/**
	 * @return Returns the defaulthpbonus.
	 */
	public double getDefaulthpbonus() {
		return _defaulthpbonus;
	}
	/**
	 * @return Returns the defaultmp.
	 */
	public double getDefaultmp() {
		return _defaultmp;
	}
	/**
	 * @return Returns the defaultmpadd.
	 */
	public double getDefaultmpadd() {
		return _defaultmpadd;
	}
	/**
	 * @return Returns the defaultmpbonus.
	 */
	public double getDefaultmpbonus() {
		return _defaultmpbonus;
	}
	/**
	 * @param classid The classid to set.
	 */
	public void setClassid(int classid) {
		_classid = classid;
	}
	/**
	 * @param defaulthp The defaulthp to set.
	 */
	public void setDefaulthp(double defaulthp) {
		_defaulthp = defaulthp;
	}
	/**
	 * @param defaulthpadd The defaulthpadd to set.
	 */
	public void setDefaulthpadd(double defaulthpadd) {
		_defaulthpadd = defaulthpadd;
	}
	/**
	 * @param defaulthpbonus The defaulthpbonus to set.
	 */
	public void setDefaulthpbonus(double defaulthpbonus) {
		_defaulthpbonus = defaulthpbonus;
	}
	/**
	 * @param defaultmp The defaultmp to set.
	 */
	public void setDefaultmp(double defaultmp) {
		_defaultmp = defaultmp;
	}
	/**
	 * @param defaultmpadd The defaultmpadd to set.
	 */
	public void setDefaultmpadd(double defaultmpadd) {
		_defaultmpadd = defaultmpadd;
	}
	/**
	 * @param defaultmpbonus The defaultmpbonus to set.
	 */
	public void setDefaultmpbonus(double defaultmpbonus) {
		_defaultmpbonus = defaultmpbonus;
	}
}
