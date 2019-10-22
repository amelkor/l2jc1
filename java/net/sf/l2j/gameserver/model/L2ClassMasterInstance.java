/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2ClassMasterInstance.java,v 1.4 2004/10/23 22:32:19 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 22:32:19 $
 * $Revision: 1.4 $
 * $Log: L2ClassMasterInstance.java,v $
 * Revision 1.4  2004/10/23 22:32:19  l2chef
 * fixed classchange problem if were already level 40 when trying to change to job level 2
 * (inspired by Trigunflame fix)
 *
 * Revision 1.3  2004/09/28 02:25:48  nuocnam
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

import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.serverpackets.SetToLocation;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/23 22:32:19 $
 */
public class L2ClassMasterInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2ClassMasterInstance.class.getName());
	
	/**
	 * @param template
	 */
	public L2ClassMasterInstance(L2Npc template)
	{
		super(template);
	}
	
	public void onAction(L2PcInstance player)
	{
		
		if (getObjectId() != player.getTargetId())
		{
			player.setCurrentState(L2Character.STATE_IDLE);
			_log.fine("ClassMaster selected:"+getObjectId());
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
			player.sendPacket(my);
			// correct location
			player.sendPacket(new SetToLocation(this));
		}
		else
		{
			_log.fine("ClassMaster activated");
			int classId = player.getClassId();

			int jobLevel = 0;
			int level = player.getLevel();

			switch (classId)
			{
				case ClassId.fighter:
				case ClassId.mage:
				case ClassId.elvenFighter:
				case ClassId.elvenMage:
				case ClassId.darkFighter:
				case ClassId.darkMage:
				case ClassId.orcFighter:
				case ClassId.orcMage:
				case ClassId.dwarvenFighter:
					jobLevel = 1;
					break;
				case ClassId.elvenScout:
				case ClassId.elvenKnight:
				case ClassId.elvenWizard:
				case ClassId.oracle:
				case ClassId.knight:
				case ClassId.warrior:
				case ClassId.rogue:
				case ClassId.wizard:
				case ClassId.cleric:
				case ClassId.palusKnight:
				case ClassId.assassin:
				case ClassId.darkWizard:
				case ClassId.shillienOracle:
				case ClassId.orcRaider:
				case ClassId.orcMonk:
				case ClassId.orcShaman:
				case ClassId.scavenger: 
				case ClassId.artisan:
					jobLevel = 2;
					break;
				default:
					jobLevel = 3;
			}
			
			
			if( (level >= 20 && jobLevel == 1) ||
				(level >= 40 && jobLevel == 2) )
			{				
				showChatWindow(player, classId);
			}
			else 
			{
				NpcHtmlMessage html = new NpcHtmlMessage(1);
				switch (jobLevel)
				{
					case 1:
						html.setHtml("<html><head><body>Come back here when you reached level 20.</body></html>");
						break;
					case 2:
						html.setHtml("<html><head><body>Come back here when you reached level 40.</body></html>");
						break;
					case 3:
						html.setHtml("<html><head><body>There is nothing more you can learn.</body></html>");
						break;
				}
				player.sendPacket(html);
			}
				
			player.sendPacket(new ActionFailed());
		}
	}
	
	public String getHtmlPath(int npcId, int val)
	{
		return "data/html/classmaster/" + val + ".htm";
	}

	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if(command.startsWith("change_class"))
		{
			int val = Integer.parseInt(command.substring(13));
			changeClass(player, val);
		}
		else 
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			
			super.onBypassFeedback(player, command);
		}
	}
	
	private void changeClass(L2PcInstance player, int val)
	{
		_log.fine("Changing class to ClassId:"+val);
		player.setClassId(val);
		_log.fine("name:"+player.getName());
		_log.fine("level:"+player.getLevel());
		_log.fine("classId:"+player.getClassId());
		UserInfo ui = new UserInfo(player);
		player.sendPacket(ui);
		CharInfo info = new CharInfo(player);
		player.broadcastPacket(info);
	}
}