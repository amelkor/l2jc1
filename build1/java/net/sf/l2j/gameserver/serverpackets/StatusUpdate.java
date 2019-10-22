/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/StatusUpdate.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: StatusUpdate.java,v $
 * Revision 1.3  2004/07/04 11:14:52  l2chef
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
 * 0000: 1a 79 90 f0 48 02 00 00 00 09 00 00 00 ff 00 00    .y..H...........
 * 0010: 00 0a 00 00 00 47 01 00 00                         .....G...
 * 
 * format   d d(dd) 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class StatusUpdate extends ServerBasePacket
{
	private static final String _S__1A_STATUSUPDATE = "[S] 1A StatusUpdate";
	public static int LEVEL = 0x01;
	public static int EXP = 0x02;
	public static int STR = 0x03;
	public static int DEX = 0x04;
	public static int CON = 0x05;
	public static int INT = 0x06;
	public static int WIT = 0x07;
	public static int MEN = 0x08;
	
	public static int CUR_HP = 0x09;
	public static int MAX_HP = 0x0a;
	public static int CUR_MP = 0x0b;
	public static int MAX_MP = 0x0c;

	public static int SP = 0x0d;
	public static int CUR_LOAD = 0x0e;
	public static int MAX_LOAD = 0x0f;

	public static int P_ATK = 0x11;
	public static int ATK_SPD = 0x12;
	public static int P_DEF = 0x13;
	public static int EVASION = 0x14;
	public static int ACCURACY = 0x15;
	public static int CRITICAL = 0x16;
	public static int M_ATK = 0x17;
	public static int CAST_SPD = 0x18;
	public static int M_DEF = 0x19;
	public static int PVP_FLAG = 0x1a;
	public static int KARMA = 0x1b;

	private int _objectId;
	private Vector _attributes;
	
	class Attribute
	{
		/** id values
		 * 09 - current health
		 * 0a - max health
		 * 0b - current mana
		 * 0c - max mana
		 * 
		 */
		public int id;
		public int value;
		
		Attribute(int id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}

	public StatusUpdate(int objectId)
	{
		_attributes = new Vector();
		_objectId = objectId;
	}	
	
	public void addAttribute(int id, int level)
	{
		_attributes.add(new Attribute(id, level));
	}
	
	public byte[] getContent()
	{
		writeC(0x1a);
		writeD(_objectId);
		writeD(_attributes.size());

		for (int i = 0; i < _attributes.size(); i++)
		{
			Attribute temp = (Attribute) _attributes.get(i);
			
			writeD(temp.id);
			writeD(temp.value);
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__1A_STATUSUPDATE;
	}
}
