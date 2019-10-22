/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2TeleporterInstance.java,v 1.3 2004/07/23 02:23:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/23 02:23:21 $
 * $Revision: 1.3 $
 * $Log: L2TeleporterInstance.java,v $
 * Revision 1.3  2004/07/23 02:23:21  l2chef
 * npc chat dialogs unified (NuocNam)
 *
 * Revision 1.2  2004/07/13 22:56:40  l2chef
 * refactored duplicate code
 *
 * Revision 1.1  2004/07/08 22:20:28  l2chef
 * teleporter npcs added by NightMarez and Nuocnam
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
package net.sf.l2j.gameserver.model;

/**
 * @author NightMarez
 * @version $Revision: 1.3 $ $Date: 2004/07/23 02:23:21 $
 *
 */

import java.util.logging.Logger;
import java.util.ArrayList;

import net.sf.l2j.gameserver.TeleportLocationTable;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.templates.L2Npc;

public class L2TeleporterInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2TeleporterInstance.class.getName());
	private ArrayList _tpLocs;
	private int _tpId;
	
	/**
	 * @param template
	 */
	public L2TeleporterInstance(L2Npc template)
	{
		super(template);
	}
	
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		// first do the common stuff
		// and handle the commands that all NPC classes know
		super.onBypassFeedback(player, command);
		
		if (command.startsWith("goto"))
		{
			int val = Integer.parseInt(command.substring(5));
			doTeleport(player, val);
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
		
		return "data/html/teleporter/" + pom + ".htm";
	}

	
	public void onAction(L2PcInstance player)
	{
		_log.fine("Teleporter activated");
		super.onAction(player);
	}
			
	private void doTeleport(L2PcInstance player, int val)
	{	
		
		L2TeleportLocation list = TeleportLocationTable.getInstance().getTemplate(val);
		
		if (list != null)
		{
			if(player.getAdena() >= list.getPrice())
			{
				_log.fine("Teleporting to new location: "+list.getLocX()+":"+list.getLocY()+":"+list.getLocZ());	
				// take away money it costs to teleport from player
				player.reduceAdena(list.getPrice());
				_log.fine("Took: "+list.getPrice()+" Adena from player for teleport");
				// teleport
				TeleportToLocation Tloc = new TeleportToLocation(player, list.getLocX(), list.getLocY(), list.getLocZ());				
				player.sendPacket(Tloc);
			}
			else
			{
				_log.fine("Not enough adena to teleport");
				// send "not enough adena" message
				SystemMessage sm = new SystemMessage(279);
				player.sendPacket(sm);				
			}
		}
		else
		{
			_log.warning("No teleport destination with id:" +val);
		}
		player.sendPacket( new ActionFailed() );
	}
}