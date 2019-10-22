/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/TargetUnselected.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: TargetUnselected.java,v $
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
 * format  dddd
 * 
 * sample
 * 0000: 3a  69 08 10 48  02 c1 00 00  f7 56 00 00  89 ea ff    :i..H.....V.....
 * 0010: ff  0c b2 d8 61                                     ....a
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class TargetUnselected extends ServerBasePacket
{
	private static final String _S__3A_TARGETUNSELECTED = "[S] 3A TargetUnselected";
	private L2Character _target;
	private int _x;
	private int _y;
	private int _z;

	/**
	 * @param _characters
	 */
	public TargetUnselected(L2Character cha)
	{
		_target = cha;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
	}


	public byte[] getContent()
	{
		writeC(0x3a);
		writeD(_target.getObjectId());
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_target.getTargetId()); //??  probably not used in client

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__3A_TARGETUNSELECTED;
	}
}
