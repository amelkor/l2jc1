/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/Die.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: Die.java,v $
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
 * sample
 * 0b 
 * 952a1048		objectId 
 * 00000000 00000000 00000000 00000000 00000000 00000000
 
 * format  dddddd   rev 377
 * format  ddddddd   rev 417
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class Die extends ServerBasePacket
{
	private static final String _S__0B_DIE = "[S] 0B Die";
	private L2Character _cha;
	private int _sessionId;

	/**
	 * @param _characters
	 */
	public Die(L2Character cha)
	{
		_cha = cha;
	}


	public byte[] getContent()
	{
		_bao.write(0x0b);
		
		writeD(_cha.getObjectId()); 
		writeD(0x01);  // to nearest village  --> 6d 00 00 00 00
		writeD(0x01);  // to hide away        --> 6d 01 00 00 00
		writeD(0x01);  // to castle           --> 6d 02 00 00 00
		writeD(0x01);  // to siege HQ         --> 6d 03 00 00 00
		writeD(0x00);  // sweepable  (blue glow)
		writeD(0x01);  // to FIXED ???        --> 6d 04 00 00 00

		return _bao.toByteArray();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__0B_DIE;
	}
}
