/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/StartRotating.java,v 1.1 2004/08/06 00:24:20 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:24:20 $
 * $Revision: 1.1 $
 * $Log: StartRotating.java,v $
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
import net.sf.l2j.gameserver.serverpackets.BeginRotation;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/06 00:24:20 $
 */
public class StartRotating extends ClientBasePacket
{
	private static final String _C__4A_STARTROTATING = "[C] 4A StartRotating";

	/**
	 * packet type id 0x4a
	 * 
	 * sample
	 * 
	 * 4a
	 * fb 0f 00 00 // degree (goes from 0 to 65535)
	 * 01 00 00 00 // side (01 00 00 00 = right, ff ff ff ff = left)
	 * 
	 * format:		cdd
	 * @param decrypt
	 */
	public StartRotating(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int degree = readD();
		int side = readD();
		BeginRotation br = new BeginRotation(client.getActiveChar(), degree, side);
		client.getActiveChar().sendPacket(br);
		client.getActiveChar().broadcastPacket(br);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__4A_STARTROTATING;
	}
}