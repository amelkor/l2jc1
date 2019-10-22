/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/Appearing.java,v 1.3 2004/10/06 14:54:45 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/06 14:54:45 $
 * $Revision: 1.3 $
 * $Log: Appearing.java,v $
 * Revision 1.3  2004/10/06 14:54:45  whatev66
 * all previous known removed when appearing
 *
 * Revision 1.2  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.1  2004/07/14 22:09:26  l2chef
 * hander for appear packet added
 *
 * Revision 1.4  2004/07/04 19:21:04  l2chef
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
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2NpcInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2PetInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.serverpackets.SpawnItem;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * Appearing Packet Handler<p>
 * <p>
 * 0000: 30 <p>
 * <p>
 * 
 * @version $Revision: 1.3 $ $Date: 2004/10/06 14:54:45 $
 */
public class Appearing extends ClientBasePacket
{
	private static final String _C__30_APPEARING = "[C] 30 Appearing";
	private static Logger _log = Logger.getLogger(Appearing.class.getName());

	// c

	/**
	 * @param decrypt
	 */
	public Appearing(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		// this is just a trigger packet. it has no content
		
		L2PcInstance activeChar = client.getActiveChar();
		activeChar.removeAllKnownObjects();
		Connection con = client.getConnection();
		UserInfo ui = new UserInfo(activeChar);
		con.sendPacket(ui);

		L2Object[] visible = L2World.getInstance().getVisibleObjects(activeChar, 2000);
		_log.fine("npc in range:"+visible.length);
		for (int i = 0; i < visible.length; i++)
		{
			activeChar.addKnownObject(visible[i]);
			
			if (visible[i] instanceof L2ItemInstance)
			{
				SpawnItem si = new SpawnItem((L2ItemInstance) visible[i]);
				con.sendPacket(si);
				// client thread should also have a list with all objects known to the client
			}
			else if (visible[i] instanceof L2NpcInstance)
			{
				NpcInfo ni = new NpcInfo((L2NpcInstance) visible[i]);
				con.sendPacket(ni);
				
				L2NpcInstance npc = (L2NpcInstance) visible[i];
				npc.addKnownObject(activeChar);
			}
			else if (visible[i] instanceof L2PetInstance)
			{
				NpcInfo ni = new NpcInfo((L2PetInstance) visible[i]);
				con.sendPacket(ni);

				L2PetInstance npc = (L2PetInstance) visible[i];
				npc.addKnownObject(activeChar);
			}
			else if (visible[i] instanceof L2PcInstance)
			{
				// send player info to our client
				L2PcInstance player = (L2PcInstance) visible[i];
				con.sendPacket(new CharInfo(player));
				
				// notify other player about us
				player.addKnownObject(activeChar);
				player.getNetConnection().sendPacket(new CharInfo(activeChar));
			}
		}
		
		L2World.getInstance().addVisibleObject(activeChar);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__30_APPEARING;
	}
}
