/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2TrainerInstance.java,v 1.5 2004/08/11 19:32:50 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/11 19:32:50 $
 * $Revision: 1.5 $
 * $Log: L2TrainerInstance.java,v $
 * Revision 1.5  2004/08/11 19:32:50  l2chef
 * Trainers can have Texts like other NPC (LittleVexy)
 *
 * Revision 1.4  2004/07/04 11:13:27  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/29 18:46:02  l2chef
 * fixed movement freeze bug when interacting with NPCs
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.SkillTreeTable;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.AquireSkillList;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/11 19:32:50 $
 */
public class L2TrainerInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2TrainerInstance.class.getName());

	/**
	 * @param template
	 */
	public L2TrainerInstance(L2Npc template)
	{
		super(template);
	}
	
	/**
	 * this is called when a player interacts with this NPC
	 * @param player
	 */
	public void onAction(L2PcInstance player)
	{
		_log.fine("Trainer activated");
		super.onAction(player);
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
		
		return "data/html/trainer/" + pom + ".htm";
	}

	/**
	 * this displays SkillList to the player.
	 * @param player
	 */
	public void showSkillList(L2PcInstance player)
	{
		_log.fine("SkillList activated on: "+getObjectId());

		L2SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(player);
		AquireSkillList asl = new AquireSkillList();
		for (int i = 0; i < skills.length; i++)
		{
			asl.addSkill(skills[i].getId(), skills[i].getLevel(),skills[i].getLevel(),skills[i].getSpCost(), 0);
		}
		player.sendPacket(asl);
		player.sendPacket(new ActionFailed());
	}
	
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("SkillList"))
		{
			showSkillList(player);
		}
		else 
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			
			super.onBypassFeedback(player, command);
		}
	}
}
