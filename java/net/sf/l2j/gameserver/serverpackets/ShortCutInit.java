/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/ShortCutInit.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: ShortCutInit.java,v $
 * Revision 1.3  2004/07/04 11:14:53  l2chef
 * logging is used instead of system.out
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
package net.sf.l2j.gameserver.serverpackets;

import java.util.Vector;

/**
 * 
 *
 * sample
 *  
 * 57 
 * 0d 00 00 00 
 * 03 00 00 00  00 00 00 00  02 00 00 00  01 00 00 00 
 * 01 00 00 00  01 00 00 00  46 28 91 40  01 00 00 00 
 * 02 00 00 00  02 00 00 00  03 00 00 00  06 00 00 00  01 00 00 00  
 * 02 00 00 00  03 00 00 00  38 00 00 00  06 00 00 00  01 00 00 00  
 * 01 00 00 00  04 00 00 00  5f 37 32 43  01 00 00 00  
 * 03 00 00 00  05 00 00 00  05 00 00 00  01 00 00 00  
 * 01 00 00 00  06 00 00 00  3a df c3 41  01 00 00 00  
 * 01 00 00 00  07 00 00 00  5d 69 d1 41  01 00 00 00 
 * 01 00 00 00  08 00 00 00  7b 86 73 42  01 00 00 00 
 * 03 00 00 00  09 00 00 00  00 00 00 00  01 00 00 00 
 * 02 00 00 00  0a 00 00 00  4d 00 00 00  01 00 00 00  01 00 00 00 
 * 02 00 00 00  0b 00 00 00  5b 00 00 00  01 00 00 00  01 00 00 00 
 * 01 00 00 00  0c 00 00 00  5f 37 32 43  01 00 00 00
 
 * format   d *(1dddd)/(2ddddd)/(3dddd)
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class ShortCutInit extends ServerBasePacket
{
	private static final String _S__57_SHORTCUTINIT = "[S] 57 ShortCutInit";
	private Vector _shortCuts;
	
	class ShortCut
	{
		public int slot;
		public int type;
		public int typeId;
		public int level;
		public int dat2;
		
		ShortCut(int slot, int type, int typeId, int level, int dat2)
		{
			this.slot = slot;
			this.type = type;
			this.typeId = typeId;
			this.level = level;
			this.dat2 = dat2;
		}
	}

	public ShortCutInit()
	{
		_shortCuts = new Vector();
	}	
	
	/**
	 * 
	 * @param slot
	 * @param skillId
	 * @param level
	 * @param dat2  ?? usually  0x01
	 */
	public void addSkillShotCut(int slot, int skillId, int level, int dat2)
	{
		_shortCuts.add(new ShortCut(slot,2,skillId,level,dat2));
	}

	public void addItemShotCut(int slot, int inventoryId, int dat2)
	{
		_shortCuts.add(new ShortCut(slot,1,inventoryId,-1,dat2));
	}

	/**
	 * 
	 * @param int slot
	 * @param int actionId
	 * @param int dat2 ???
	 */
	public void addActionShotCut(int slot, int actionId, int dat2)
	{
		_shortCuts.add(new ShortCut(slot,3,actionId,-1,dat2));
	}
	
	public byte[] getContent()
	{
		writeC(0x57);
		writeD(_shortCuts.size());

		for (int i = 0; i < _shortCuts.size(); i++)
		{
			ShortCut temp = (ShortCut) _shortCuts.get(i);
			writeD(temp.type);			
			writeD(temp.slot);
			writeD(temp.typeId);
			if (temp.level > -1)
			{
				writeD(temp.level);
			}				
			writeD(temp.dat2);			
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__57_SHORTCUTINIT;
	}
}
