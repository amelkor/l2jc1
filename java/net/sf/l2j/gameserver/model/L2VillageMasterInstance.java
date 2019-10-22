/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2VillageMasterInstance.java,v 1.4 2004/09/28 02:25:48 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:25:48 $
 * $Revision: 1.4 $
 * $Log: L2VillageMasterInstance.java,v $
 * Revision 1.4  2004/09/28 02:25:48  nuocnam
 * Added header and copyright notice.
 *
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
package net.sf.l2j.gameserver.model;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2Npc;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.ClanTable;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/09/28 02:25:48 $
 */
public class L2VillageMasterInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2VillageMasterInstance.class.getName());
	
	/**
	 * @param template
	 */
	public L2VillageMasterInstance(L2Npc template)
	{
		super(template);
	}
	
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		// first do the common stuff
		// and handle the commands that all NPC classes know
		super.onBypassFeedback(player, command);
		
		if (command.startsWith("create_clan"))
		{
			String val = command.substring(12);
			createClan(player, val);
		} 
	}
	
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		} 
		else 
		{
			pom = npcId + "-" + val;
		}
		
		return "data/html/villagemaster/" + pom + ".htm";
	}

	
	public void onAction(L2PcInstance player)
	{
		_log.fine("Village Master activated");
		super.onAction(player);
	}
	
	//Private stuff
	public void createClan(L2PcInstance player, String clanName)
	{
		_log.fine(player.getObjectId()+"("+player.getName()+") requested clan creation from "+getObjectId()+"("+getName()+")");
		if (player.getLevel() < 10)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.FAILED_TO_CREATE_CLAN);
			player.sendPacket(sm);
			return;
		}
		
		if (player.getClanId() != 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.FAILED_TO_CREATE_CLAN);
			player.sendPacket(sm);
			return;
		}

		if (clanName.length() > 16)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.CLAN_NAME_TOO_LONG);
			player.sendPacket(sm);
			return;
		}
		
		L2Clan clan = ClanTable.getInstance().createClan(player, clanName);
		if (clan == null)
		{
			// clan name is already taken
			SystemMessage sm = new SystemMessage(SystemMessage.CLAN_NAME_INCORRECT);
			player.sendPacket(sm);
			return;
		}
		
		player.setClan(clan);
		player.setClanId(clan.getClanId());
		player.setIsClanLeader(true);
		
		//should be update packet only
		PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan, player);
		player.sendPacket(pu);

		UserInfo ui = new UserInfo(player);
		player.sendPacket(ui);

		SystemMessage sm = new SystemMessage(SystemMessage.CLAN_CREATED);
		player.sendPacket(sm);
	}
}
