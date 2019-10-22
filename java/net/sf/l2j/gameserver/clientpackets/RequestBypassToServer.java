/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestBypassToServer.java,v 1.9 2004/09/18 22:08:11 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/18 22:08:11 $
 * $Revision: 1.9 $
 * $Log: RequestBypassToServer.java,v $
 * Revision 1.9  2004/09/18 22:08:11  nuocnam
 * Changed showboard_ to bbs_ (sh1ny)
 *
 * Revision 1.8  2004/09/17 23:59:32  nuocnam
 * added try/catch block so client won't crash on bad bypass
 *
 * Revision 1.7  2004/09/15 23:44:54  l2chef
 * boad commands added (Deth)
 *
 * Revision 1.6  2004/07/14 22:06:28  l2chef
 * some refactoring and mob spawning fixed
 *
 * Revision 1.5  2004/07/07 23:41:04  l2chef
 * new merchant conversation files (done by NightMarez)
 * some design changes
 *
 * Revision 1.4  2004/07/05 23:01:19  l2chef
 * draft version of admin interface... contributed by blurcode
 *
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

import net.sf.l2j.gameserver.AdminCommands;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2NpcInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.CommunityBoard;

/**
 * This class ...
 * 
 * @version $Revision: 1.9 $ $Date: 2004/09/18 22:08:11 $
 */
public class RequestBypassToServer extends ClientBasePacket
{
	// S

	private static final String _C__21_REQUESTBYPASSTOSERVER = "[C] 21 RequestBypassToServer";
	/**
	 * @param decrypt
	 */
	public RequestBypassToServer(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		String command = readS();
		
		try {
			if (command.startsWith("admin_") && client.getAccessLevel() >= 100)
			{
				AdminCommands.getInstance().handleCommands(client, command);
			}
			else if (command.equals("come_here"))
			{
				comeHere(client);
			}
			else if (command.startsWith("npc_"))
			{
				int endOfId = command.indexOf('_', 5);
				String id = command.substring(4, endOfId);
				L2Object object = L2World.getInstance().findObject(Integer.parseInt(id));
				if (object instanceof L2NpcInstance)
				{
					((L2NpcInstance) object).onBypassFeedback(client.getActiveChar(), command.substring(endOfId+1));
				}
			}
			else if (command.startsWith("bbs_"))
			{
				CommunityBoard.getInstance().handleCommands(client, command);
			}
		} catch (Exception e) {
			_log.warning("Bad RequestBypassToServer: "+e.toString());
		}
	}

	/**
	 * @param client
	 */
	private void comeHere(ClientThread client) 
	{
		L2Object obj = client.getActiveChar().getTarget();
		if (obj instanceof L2NpcInstance)
		{
			L2NpcInstance temp = (L2NpcInstance) obj;
			L2PcInstance player = client.getActiveChar(); 
			temp.setTarget(player);
			temp.moveTo(player.getX(),player.getY(), player.getZ(), 0 );
		}
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__21_REQUESTBYPASSTOSERVER;
	}
}
