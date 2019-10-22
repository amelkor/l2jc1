/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/StopMoveWithLocation.java,v 1.2 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.2 $
 * $Log: StopMoveWithLocation.java,v $
 * Revision 1.2  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/08/06 00:24:20  l2chef
 * cursor movement added (Deth)
 *
 * Revision 1.1  2004/07/28 23:56:11  l2chef
 * Selling items implemented (Deth)
 *
 * Revision 1.0  2004/07/28 15:11:47  deth
 * first release
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
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:44:16 $
 */
public class StopMoveWithLocation extends ServerBasePacket
{
	private static final String _S__5F_STOPMOVEWITHLOCATION = "[S] 5F StopMoveWithLocation";
	private L2Character _char;
	private int _x;
	private int _y;
	private int _z;
	
	public StopMoveWithLocation(L2Character player)
	{
		_char = player;
	}
	
	public byte[] getContent()
	{
		writeC(0x5f);
		writeD(_char.getObjectId());
		writeD(_char.getX());
		writeD(_char.getY());
		writeD(_char.getZ());
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__5F_STOPMOVEWITHLOCATION;
	}
}
