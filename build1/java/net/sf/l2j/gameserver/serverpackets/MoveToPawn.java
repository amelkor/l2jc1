/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/MoveToPawn.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: MoveToPawn.java,v $
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
import net.sf.l2j.gameserver.model.L2Object;

/**
 * 
 * 0000: 75  7a 07 80 49  63 27 00 4a  ea 01 00 00  c1 37 fe    uz..Ic'.J.....7. <p>
 * 0010: ff 9e c3 03 00 8f f3 ff ff                         .........<p>
 * <p>
 * 
 * format   dddddd		(player id, target id, distance, startx, starty, startz)<p>
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class MoveToPawn extends ServerBasePacket
{
	private static final String _S__75_MOVETOPAWN = "[S] 75 MoveToPawn";
	private L2Character _cha;
	private L2Object _target;
	private int _distance;
	
	public MoveToPawn(L2Character cha, L2Object target, int distance)
	{
		_cha=cha;
		_target = target;
		_distance = distance;
	}
	
	public byte[] getContent()
	{
		_bao.write(0x75);
		
		writeD(_cha.getObjectId());
		writeD(_target.getObjectId());
		writeD(_distance);
		
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
		return _S__75_MOVETOPAWN;
	}
}
