/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestShortCutDel.java,v 1.3 2004/07/04 11:12:33 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:12:33 $
 * $Revision: 1.3 $
 * $Log: RequestShortCutDel.java,v $
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:12:33 $
 */
public class RequestShortCutDel extends ClientBasePacket
{
	private static final String _C__35_REQUESTSHORTCUTDEL = "[C] 35 RequestShortCutDel";
	/**
	 * packet type id 0x35
	 * format:		cd
	 * @param rawPacket
	 */
	public RequestShortCutDel(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);

		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();

		int slot = readD();
		
		activeChar.deleteShortCut(slot);
		// client needs no confirmation. this packet is just to inform the server
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__35_REQUESTSHORTCUTDEL;
	}
}
