/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/Logout.java,v 1.9 2004/08/08 16:38:24 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/08 16:38:24 $
 * $Revision: 1.9 $
 * $Log: Logout.java,v $
 * Revision 1.9  2004/08/08 16:38:24  l2chef
 * cleanup only needed if a char is active
 *
 * Revision 1.8  2004/08/08 16:14:27  l2chef
 * deleteMe() is used
 *
 * Revision 1.7  2004/07/23 01:43:00  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.6  2004/07/19 02:01:35  l2chef
 * party code completed (whatev)
 *
 * Revision 1.5  2004/07/18 17:35:58  l2chef
 * players are now correctly removed from the world and their regeneration is stopped
 *
 * Revision 1.4  2004/07/04 11:12:33  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:27  l2chef
 * using jdk logger instead of println
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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.LeaveWorld;

/**
 * This class ...
 * 
 * @version $Revision: 1.9 $ $Date: 2004/08/08 16:38:24 $
 */
public class Logout extends ClientBasePacket
{
	private static final String _C__09_LOGOUT = "[C] 09 Logout";
	private static Logger _log = Logger.getLogger(Logout.class.getName());
	
	// c

	/**
	 * @param decrypt
	 */
	public Logout(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		// this is just a trigger packet. it has no content
		
		// normally the server would send serveral "delete object" before "leaveWorld"
		// we skip that for now

		LeaveWorld ql = new LeaveWorld();
		client.getConnection().sendPacket(ql);

		// removing player from the world
		L2PcInstance player = client.getActiveChar();
		if (player != null)
		{
			player.deleteMe();
			//save character
			client.saveCharToDisk(player);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__09_LOGOUT;
	}
}
