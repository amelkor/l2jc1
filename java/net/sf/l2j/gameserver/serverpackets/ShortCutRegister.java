/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/ShortCutRegister.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: ShortCutRegister.java,v $
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

/**
 * 
 *
 * sample
 *  
 * 56 
 * 01000000 04000000 dd9fb640 01000000
 * 
 * 56 
 * 02000000 07000000 38000000 03000000 01000000
 * 
 * 56 
 * 03000000 00000000 02000000 01000000
 * 
 * format   dd d/dd/d d
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class ShortCutRegister extends ServerBasePacket
{
	private static final String _S__56_SHORTCUTREGISTER = "[S] 56 ShortCutRegister";
	public int _slot;
	public int _type;
	public int _typeId;
	public int _level;
	public int _dat2;

	/**
	 * Register new skill shortcut
	 * @param slot
	 * @param type
	 * @param typeId
	 * @param level
	 * @param dat2
	 */
	public ShortCutRegister(int slot, int type, int typeId, int level, int dat2)
	{
		_slot = slot;
		_type = type;
		_typeId = typeId;
		_level = level;
		_dat2 = dat2;
	}	
	
	/**
	 * Register new item or action shortcut
	 * @param slot
	 * @param type
	 * @param typeId
	 * @param dat2
	 */
	public ShortCutRegister(int slot, int type, int typeId, int dat2)
	{
		this(slot, type, typeId, -1, dat2);
	}	
	
	public byte[] getContent()
	{
		writeC(0x56);
		writeD(_type);			
		writeD(_slot);
		writeD(_typeId);
		if (_level > -1)
		{
			writeD(_level);
		}				
		writeD(_dat2);			

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__56_SHORTCUTREGISTER;
	}
}
