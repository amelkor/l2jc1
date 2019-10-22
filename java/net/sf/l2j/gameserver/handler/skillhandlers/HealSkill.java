/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/skillhandlers/HealSkill.java,v 1.6 2004/10/19 23:49:20 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/19 23:49:20 $
 * $Revision: 1.6 $
 * $Log: HealSkill.java,v $
 * Revision 1.6  2004/10/19 23:49:20  nuocnam
 * Target is now passed to skill handlers. No skill handler should use activeChar.getTarget() ever
 *
 * Revision 1.5  2004/09/28 02:17:03  nuocnam
 * corrected header
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
package net.sf.l2j.gameserver.handler.skillhandlers;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.ArrayList;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.6 $ $Date: 2004/10/19 23:49:20 $
 */
public class HealSkill implements ISkillHandler
{
	private static final int SELF_HEAL = 1216;
	private static final int DEVINE_HEAL = 45;
	private static final int ELEMENTAL_HEAL = 58;
	private static final int HEAL = 1011;
	private static final int BATTLE_HEAL = 1015;
	private static final int GROUP_HEAL = 1027;
	private static final int SERVITOR_HEAL = 1127;
	private static final int GREATER_GROUP_HEAL = 1219;
	
	// all the items ids that this handler knowns
	private static Logger _log = Logger.getLogger(HealSkill.class.getName());
	private static int[] _skillIds = {SELF_HEAL, DEVINE_HEAL, ELEMENTAL_HEAL, HEAL, BATTLE_HEAL, GROUP_HEAL, SERVITOR_HEAL, GREATER_GROUP_HEAL};

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public void useSkill(L2PcInstance activeChar, L2Skill skill, L2Object target) throws IOException
	{
	    if (skill.getTargetType() == L2Skill.TARGET_PET)
	    {
	    	L2Character pet = activeChar.getPet();
	    	double hp = pet.getCurrentHp();
			hp += skill.getPower();
			pet.setCurrentHp(hp);
	    }
	    else if (skill.getTargetType() == L2Skill.TARGET_PARTY && activeChar.isInParty())
	    {
	    	ArrayList players = activeChar.getParty().getPartyMembers();
	    	for(int i = 0; i < players.size(); i++) {
	    		L2PcInstance player = (L2PcInstance)players.get(i);
		    	double hp = player.getCurrentHp();
		    	hp += skill.getPower();
				player.setCurrentHp(hp);
				
				StatusUpdate su = new StatusUpdate(player.getObjectId());
				su.addAttribute(StatusUpdate.CUR_HP, (int)hp);
				player.sendPacket(su);
				
				player.sendPacket(new SystemMessage(SystemMessage.REJUVENATING_HP));
			}
	    }
		else  // all other single target heals
		{
			double hp = activeChar.getCurrentHp();
			hp += skill.getPower();
			activeChar.setCurrentHp(hp);
			
			StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
			su.addAttribute(StatusUpdate.CUR_HP, (int)hp);
			activeChar.sendPacket(su);
			
			activeChar.sendPacket(new SystemMessage(SystemMessage.REJUVENATING_HP));
		}
	}
	
	public int[] getSkillIds()
	{
		return _skillIds;
	}
}
