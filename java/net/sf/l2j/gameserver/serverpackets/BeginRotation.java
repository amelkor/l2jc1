/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/BeginRotation.java,v 1.1 2004/08/06 00:24:20 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:24:20 $
 * $Revision: 1.1 $
 * $Log: BeginRotation.java,v $
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

import net.sf.l2j.gameserver.model.L2PcInstance;

public class BeginRotation extends ServerBasePacket
{
	private static final String _S__77_BEGINROTATION = "[S] 77 BeginRotation";
	private L2PcInstance _char;
	private int _degree;
	private int _side;
	
	public BeginRotation(L2PcInstance player, int degree, int side)
	{
		_char = player;
		_degree = degree;
		_side = side;
	}
	
	public byte[] getContent()
	{
		writeC(0x77);
		writeD(_char.getObjectId());
		writeD(_degree);
		writeD(_side);
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__77_BEGINROTATION;
	}
}
