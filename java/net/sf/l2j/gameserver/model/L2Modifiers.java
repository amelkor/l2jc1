/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Modifiers.java,v 1.2 2004/09/28 02:25:48 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:25:48 $
 * $Revision: 1.2 $
 * $Log: L2Modifiers.java,v $
 * Revision 1.2  2004/09/28 02:25:48  nuocnam
 * Added header and copyright notice.
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
 * This class ...
 * 
 * @author NightMarez
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:25:48 $
 */
public class L2Modifiers
{

	private int _classid;
	private int _modstr;
	private int _modcon;
	private int _moddex;
	private int _modint;
	private int _modmen;
	private int _modwit;
	
	/**
	 * @return Returns the classid.
	 */
	public int getClassid() {
		return _classid;
	}
	/**
	 * @return Returns the modcon.
	 */
	public int getModcon() {
		return _modcon;
	}
	/**
	 * @return Returns the moddex.
	 */
	public int getModdex() {
		return _moddex;
	}
	/**
	 * @return Returns the modint.
	 */
	public int getModint() {
		return _modint;
	}
	/**
	 * @return Returns the modmen.
	 */
	public int getModmen() {
		return _modmen;
	}
	/**
	 * @return Returns the modstr.
	 */
	public int getModstr() {
		return _modstr;
	}
	/**
	 * @return Returns the modwit.
	 */
	public int getModwit() {
		return _modwit;
	}
	/**
	 * @param classid The classid to set.
	 */
	public void setClassid(int classid) {
		_classid = classid;
	}
	/**
	 * @param modcon The modcon to set.
	 */
	public void setModcon(int modcon) {
		_modcon = modcon;
	}
	/**
	 * @param moddex The moddex to set.
	 */
	public void setModdex(int moddex) {
		_moddex = moddex;
	}
	/**
	 * @param modint The modint to set.
	 */
	public void setModint(int modint) {
		_modint = modint;
	}
	/**
	 * @param modmen The modmen to set.
	 */
	public void setModmen(int modmen) {
		_modmen = modmen;
	}
	/**
	 * @param modstr The modstr to set.
	 */
	public void setModstr(int modstr) {
		_modstr = modstr;
	}
	/**
	 * @param modwit The modwit to set.
	 */
	public void setModwit(int modwit) {
		_modwit = modwit;
	}
}
