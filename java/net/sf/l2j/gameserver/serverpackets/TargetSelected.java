/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/TargetSelected.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: TargetSelected.java,v $
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
 * format   ddddd
 * 
 * sample
 * 0000: 39  0b 07 10 48  3e 31 10 48  3a f6 00 00  91 5b 00    9...H>1.H:....[.
 * 0010: 00  4c f1 ff ff                                     .L...
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class TargetSelected extends ServerBasePacket
{
	private static final String _S__39_TARGETSELECTED = "[S] 39 TargetSelected";
	private int _objectId;
	private int _targetId;
	private int _x;
	private int _y;
	private int _z;
	

	/**
	 * @param _characters
	 */
	public TargetSelected(int objectId, int targetId, int x, int y, int z)
	{
		_objectId = objectId;
		_targetId = targetId;
		_x = x;
		_y = y;
		_z = z;
	}


	public byte[] getContent()
	{
		writeC(0x39);
		writeD(_objectId);
		writeD(_targetId);
		writeD(_x);
		writeD(_y);
		writeD(_z);

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__39_TARGETSELECTED;
	}
}
