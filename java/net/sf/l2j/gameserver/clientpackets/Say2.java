/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/Say2.java,v 1.15 2004/10/11 17:10:00 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:10:00 $
 * $Revision: 1.15 $
 * $Log: Say2.java,v $
 * Revision 1.15  2004/10/11 17:10:00  nuocnam
 * GM commands moved to SendBypassBuildCmd
 *
 * Revision 1.14  2004/09/27 08:44:51  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.13  2004/09/19 01:37:31  nuocnam
 * added .invul for admins (sh1ny)
 *
 * Revision 1.12  2004/09/15 23:59:19  l2chef
 * announce exceptions are catched. gametime command moved to board (Deth)
 *
 * Revision 1.11  2004/08/17 21:51:16  l2chef
 * party chat added (Deth)
 *
 * Revision 1.10  2004/08/15 22:28:28  l2chef
 * announcement has no souce
 * tell-target not online is now a systemmessage
 *
 * Revision 1.9  2004/08/11 23:20:33  l2chef
 * clan chat added
 *
 * Revision 1.8  2004/07/28 23:54:43  l2chef
 * announcements added (Deth)
 *
 * Revision 1.7  2004/07/25 02:26:34  l2chef
 * ingame time can be displayed with .time
 *
 * Revision 1.6  2004/07/14 22:06:10  l2chef
 * some refactoring and mob spawning fixed
 *
 * Revision 1.5  2004/07/05 23:03:12  l2chef
 * draft version of admin interface... contributed by blurcode
 *
 * Revision 1.4  2004/07/04 11:09:02  l2chef
 * commands removed.
 * say is now only local
 * shout and trade is global
 *
 * Revision 1.3  2004/06/29 22:55:35  l2chef
 * tell and say works now. say is actually a server broadcast now
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
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.AdminCommands;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.15 $ $Date: 2004/10/11 17:10:00 $
 */
public class Say2 extends ClientBasePacket
{
	private static final String _C__38_SAY2 = "[C] 38 Say2";
	private static Logger _log = Logger.getLogger(Say2.class.getName());

	public final static int ALL = 0;
	public final static int SHOUT = 1;
	public final static int TELL = 2;
	public final static int PARTY = 3;
	public final static int CLAN = 4;
	public final static int PRIVATE_CHAT_PLAYER = 6; // used for petition
	public final static int PRIVATE_CHAT_GM = 7; // used for petition
	public final static int TRADE = 8;
	public final static int GM_MESSAGE = 9;
	public final static int ANNOUNCEMENT = 10;
	

	/**
	 * packet type id 0x38
	 * format:		cSd (S)
	 * @param decrypt
	 */
	public Say2(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		String text = readS();
		int type = readD();
		String target = null;
		if (type == TELL)
		{
			target = readS();
		}
		
		_log.fine("Say type:"+type);
		
		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();


		CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
	
		if (type == TELL)
		{
			L2PcInstance receiver = L2World.getInstance().getPlayer(target);
			if (receiver != null)
			{
				receiver.sendPacket(cs);
				activeChar.sendPacket(cs);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_NOT_ONLINE);
				sm.addString(target);
				activeChar.sendPacket(sm);
			}
		}
		else if (type == SHOUT || type == TRADE)
		{
			L2PcInstance[] players = L2World.getInstance().getAllPlayers();
			for (int i = 0; i < players.length; i++)
			{
				players[i].sendPacket(cs);
			}
		}
		else if (type == ALL)
		{
			Set players = activeChar.getKnownPlayers();
			for (Iterator iter = players.iterator(); iter.hasNext();)
			{
				L2PcInstance player = (L2PcInstance) iter.next();
				player.sendPacket(cs);
			}
			
			activeChar.sendPacket(cs);
		}
		else if (type == CLAN && activeChar.getClan() != null)
		{
			activeChar.getClan().broadcastToOnlineMembers(cs);
		}
		else if (type == PARTY && activeChar.isInParty())
		{
			activeChar.getParty().broadcastToPartyMembers(cs);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__38_SAY2;
	}
}
