/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/EnterWorld.java,v 1.14 2004/10/24 11:08:27 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/24 11:08:27 $
 * $Revision: 1.14 $
 * $Log: EnterWorld.java,v $
 * Revision 1.14  2004/10/24 11:08:27  l2chef
 * *** empty log message ***
 *
 * Revision 1.13  2004/10/23 22:12:44  l2chef
 * use new shortcut constant
 *
 * Revision 1.12  2004/10/22 23:02:03  l2chef
 * changed procedure for login of dead players. should fix the bug where dead players can move around
 *
 * Revision 1.11  2004/10/11 17:21:50  nuocnam
 * Sets GM status in L2PcInstance
 *
 * Revision 1.10  2004/08/10 00:48:12  l2chef
 * clan members get notified on login
 *
 * Revision 1.9  2004/07/30 00:16:19  l2chef
 * fix for dead respawn problem
 *
 * Revision 1.8  2004/07/29 21:20:13  l2chef
 * announcement added (Deth)
 *
 * Revision 1.7  2004/07/25 00:32:11  l2chef
 * items on shortcut bar can now be restored
 *
 * Revision 1.6  2004/07/23 01:42:49  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.5  2004/07/19 02:03:11  l2chef
 * update
 *
 * Revision 1.4  2004/07/04 19:21:04  l2chef
 * *** empty log message ***
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import net.sf.l2j.Base64;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.GmListTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.Die;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.Announcements;

/**
 * Enter World Packet Handler<p>
 * <p>
 * 0000: 03 <p>
 * <p>
 * 
 * @version $Revision: 1.14 $ $Date: 2004/10/24 11:08:27 $
 */
public class EnterWorld extends ClientBasePacket
{
	private static final String _C__03_ENTERWORLD = "[C] 03 EnterWorld";
	private static Logger _log = Logger.getLogger(EnterWorld.class.getName());

	// c

	/**
	 * @param decrypt
	 */
	public EnterWorld(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		// this is just a trigger packet. it has no content
		
		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();
		if (client.getAccessLevel() >= 100) 
		{
			activeChar.setIsGM(true);
			GmListTable.getInstance().addGm(activeChar);
		}
		
		SystemMessage sm = new SystemMessage(34);
		con.sendPacket(sm);
		sm = new SystemMessage(614);
		
		
		sm.addString(getText("VGhpcyBTZXJ2ZXIgaXMgcnVubmluZyBMMko="));
		sm.addString(getText("IHZlcnNpb24gMC40IChmaW5hbCk="));
		con.sendPacket(sm);
		sm = new SystemMessage(614);
		sm.addString(getText("Y3JlYXRlZCBieSBMMkNoZWYgYW5kIHRoZQ=="));
		sm.addString(getText("IEwySiB0ZWFtLg=="));
		con.sendPacket(sm);
		sm = new SystemMessage(614);
		sm.addString(getText("dmlzaXQgIGwyai5zb3VyY2Vmb3JnZS5uZXQ="));
		sm.addString(getText("ICBmb3Igc3VwcG9ydC4="));
		con.sendPacket(sm);

		Announcements.getInstance().showAnnouncements(activeChar);
		
		ItemList il = new ItemList(activeChar, false);
		activeChar.sendPacket(il);
		
		ShortCutInit sci = new ShortCutInit();
		L2ShortCut[] shortcuts = activeChar.getAllShortCuts();
		for (int i = 0; i < shortcuts.length; i++)
		{
			switch (shortcuts[i].getType())
			{
				case L2ShortCut.TYPE_ACTION:
				{
					sci.addActionShotCut(shortcuts[i].getSlot(),shortcuts[i].getId(),shortcuts[i].getUnk());
					break;
				}
				case L2ShortCut.TYPE_SKILL:
				{
					sci.addSkillShotCut(shortcuts[i].getSlot(),shortcuts[i].getId(),shortcuts[i].getLevel(),shortcuts[i].getUnk());
					break;
				}
				case L2ShortCut.TYPE_ITEM:
				{
					sci.addItemShotCut(shortcuts[i].getSlot(),shortcuts[i].getId(),shortcuts[i].getUnk());
					break;
				}
				default:
				{
					_log.warning("unknown shortcut type " + shortcuts[i].getType());
				}
			}
		}

		con.sendPacket(sci);

		UserInfo ui = new UserInfo(activeChar);
		con.sendPacket(ui);
		if (activeChar.isDead())
		{
			// no broadcast needed since the player will already spawn dead to others
			activeChar.sendPacket(new Die(activeChar));
		}

		L2World.getInstance().addVisibleObject(activeChar);

		notifyClanMembers(activeChar);

		
	}

	/**
	 * @param activeChar
	 */
	private void notifyClanMembers(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			clan.getClanMember(activeChar.getName()).setPlayerInstance(activeChar);
			SystemMessage msg = new SystemMessage(SystemMessage.CLAN_MEMBER_S1_LOGGED_IN);
			msg.addString(activeChar.getName());

			L2PcInstance[] clanMembers = clan.getOnlineMembers(activeChar.getName());
			for (int i = 0; i < clanMembers.length; i++)
			{
				clanMembers[i].sendPacket(msg);
			}
		}
	}

	/**
	 * @param string
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getText(String string) throws UnsupportedEncodingException
	{
		String result = new String(Base64.decode(string), "UTF-8"); 
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__03_ENTERWORLD;
	}
}
