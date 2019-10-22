/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CharMoveToLocation.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: CharMoveToLocation.java,v $
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

import net.sf.l2j.gameserver.model.L2Character;

/**
 * 0000: 01  7a 73 10 4c  b2 0b 00 00  a3 fc 00 00  e8 f1 ff    .zs.L...........
 * 0010: ff  bd 0b 00 00  b3 fc 00 00  e8 f1 ff ff             .............
 
 * 
 * ddddddd
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class CharMoveToLocation extends ServerBasePacket
{
	private static final String _S__01_CHARMOVETOLOCATION = "[S] 01 CharMoveToLocation";
	private L2Character _cha;
	
	public CharMoveToLocation(L2Character cha)
	{
		_cha=cha;
	}
	
	public byte[] getContent()
	{
		_bao.write(0x01);
		
		writeD(_cha.getObjectId());
		
		writeD(_cha.getXdestination());
		writeD(_cha.getYdestination());
		writeD(_cha.getZdestination());
		
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__01_CHARMOVETOLOCATION;
	}

}
