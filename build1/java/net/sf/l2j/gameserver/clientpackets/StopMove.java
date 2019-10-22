/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/StopMove.java,v 1.1 2004/08/06 00:24:20 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:24:20 $
 * $Revision: 1.1 $
 * $Log: StopMove.java,v $
 * Revision 1.1  2004/08/06 00:24:20  l2chef
 * cursor movement added (Deth)
 *
 * Revision 1.1  2004/07/28 23:53:07  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.serverpackets.StopMoveWithLocation;
import net.sf.l2j.gameserver.serverpackets.StopRotation;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/06 00:24:20 $
 */
public class StopMove extends ClientBasePacket
{
	private static final String _C__36_STOPMOVE = "[C] 36 StopMove";

	/**
	 * packet type id 0x36
	 * 
	 * sample
	 * 
	 * 36
	 * a8 4f 02 00 // x
	 * 17 85 01 00 // y
	 * a7 00 00 00 // z
	 * 98 90 00 00 // heading?
	 * 
	 * format:		cdddd
	 * @param decrypt
	 */
	public StopMove(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int x = readD();
		int y = readD();
		int z = readD();
		int heading = readD();
		L2Character player = client.getActiveChar();
		player.stopMove();

		_log.fine("client: x:"+x+" y:"+y+" z:"+z+" server x:"+player.getX()+" y:"+player.getZ()+" z:"+player.getZ());
		StopMoveWithLocation smwl = new StopMoveWithLocation(player);
		client.getActiveChar().sendPacket(smwl);
		client.getActiveChar().broadcastPacket(smwl);
		
		StopRotation sr = new StopRotation(client.getActiveChar(), heading);
		client.getActiveChar().sendPacket(sr);
		client.getActiveChar().broadcastPacket(sr);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__36_STOPMOVE;
	}
}