/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/KeyPacket.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: KeyPacket.java,v $
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

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class KeyPacket extends ServerBasePacket
{
	private static final String _S__01_KEYPACKET = "[S] 01 KeyPacket";
	public void setKey(byte[] key)
	{
		writeC(0x00);
		writeC(0x01);
		writeC(key[0]);
		writeC(key[1]);
		writeC(key[2]);
		writeC(key[3]);
		writeC(key[4]);
		writeC(key[5]);
		writeC(key[6]);
		writeC(key[7]);
	}

	public byte[] getContent()
	{
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__01_KEYPACKET;
	}

}
