/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestRestart.java,v 1.11 2004/08/08 16:17:03 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/08 16:17:03 $
 * $Revision: 1.11 $
 * $Log: RequestRestart.java,v $
 * Revision 1.11  2004/08/08 16:17:03  l2chef
 * deleteMe() is used for cleanup
 *
 * Revision 1.10  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.9  2004/07/23 01:43:12  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.8  2004/07/19 02:01:35  l2chef
 * party code completed (whatev)
 *
 * Revision 1.7  2004/07/18 17:35:58  l2chef
 * players are now correctly removed from the world and their regeneration is stopped
 *
 * Revision 1.6  2004/07/17 23:07:10  l2chef
 * player is removed from the known object list of mobs when reviving
 *
 * Revision 1.5  2004/07/11 23:37:38  l2chef
 * chars are always reloaded from disk  (whatev)
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
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;
import net.sf.l2j.gameserver.serverpackets.RestartResponse;

/**
 * This class ...
 * 
 * @version $Revision: 1.11 $ $Date: 2004/08/08 16:17:03 $
 */
public class RequestRestart extends ClientBasePacket
{
	private static final String _C__46_REQUESTRESTART = "[C] 46 RequestRestart";
	private static Logger _log = Logger.getLogger(RequestRestart.class.getName());	
			
	/**
	 * packet type id 0x46
	 * format:		c
	 * @param decrypt
	 */
	public RequestRestart(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);

		// removing player from the world
		L2PcInstance player = client.getActiveChar();
		player.deleteMe();

		RestartResponse response = new RestartResponse();
		client.getConnection().sendPacket(response);
		//save character
		client.saveCharToDisk(client.getActiveChar());
		
			
		client.setActiveChar(null);
		// send char list
		CharSelectInfo cl = new CharSelectInfo(client.getLoginName(), client.getSessionId());
		client.getConnection().sendPacket(cl);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__46_REQUESTRESTART;
	}
}
