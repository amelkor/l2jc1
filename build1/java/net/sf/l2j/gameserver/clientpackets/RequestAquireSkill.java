/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestAquireSkill.java,v 1.7 2004/10/23 22:11:09 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 22:11:09 $
 * $Revision: 1.7 $
 * $Log: RequestAquireSkill.java,v $
 * Revision 1.7  2004/10/23 22:11:09  l2chef
 * use new shortcut constant
 *
 * Revision 1.6  2004/10/23 21:54:14  dethx
 * shortcuts will be updated after upgrading a skill
 *
 * Revision 1.5  2004/07/17 11:31:15  l2chef
 * added break if skill is found (NuocNam)
 *
 * Revision 1.4  2004/07/16 22:58:31  l2chef
 * learn skills requires SP (NuocNam)
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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.serverpackets.AquireSkillList;
import net.sf.l2j.gameserver.serverpackets.ShortCutRegister;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/10/23 22:11:09 $
 */
public class RequestAquireSkill extends ClientBasePacket
{
	private static final String _C__6C_REQUESTAQUIRESKILL = "[C] 6C RequestAquireSkill";
	private static Logger _log = Logger.getLogger(RequestAquireSkill.class.getName());

	/**
	 * packet type id 0x6c
	 * format:		cdd
	 * @param rawPacket
	 */
	public RequestAquireSkill(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);

		L2PcInstance player = client.getActiveChar();
		Connection con = client.getConnection();

		int id = readD();
		int level = readD();

		L2Skill skill = SkillTable.getInstance().getInfo(id, level);
		L2SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(player);
		int _requiredSp=0;
		for (int i = 0; i < skills.length; i++)
		{
			if (skills[i].getId() == id) 
			{
				_requiredSp = skills[i].getSpCost();
				break;
			}
		}
		
		if (player.getSp() >= _requiredSp) 
		{
			player.addSkill(skill);
			_log.fine("Learned skill "+id+" for "+_requiredSp+" SP.");
			player.setSp(player.getSp() - _requiredSp);
			
			StatusUpdate su = new StatusUpdate(player.getObjectId());
			su.addAttribute(StatusUpdate.SP, player.getSp());
			player.sendPacket(su);

			SystemMessage sm = new SystemMessage(SystemMessage.LEARNED_SKILL_S1);
			sm.addSkillName(id);
			player.sendPacket(sm);
			
			//update all the shortcuts to this skill
			if (level > 1)
			{
				for (int i = 0; i <= 100; i++) // 10 shortcuts x 10 menus = 100
				{
					if (player.getShortCut(i) != null) 
					{
						if (player.getShortCut(i).getId() == id && player.getShortCut(i).getType() == L2ShortCut.TYPE_SKILL)
						{
							player.sendPacket(new ShortCutRegister(i, player.getShortCut(i).getType(), id, level, player.getShortCut(i).getUnk()));
							player.registerShortCut(new L2ShortCut(i, player.getShortCut(i).getType(), id, level, player.getShortCut(i).getUnk()));
						}
					}
				}
			}
		} 
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.NOT_ENOUGH_SP_TO_LEARN_SKILL);
			player.sendPacket(sm);
			_log.fine("Not enough SP!");
		}

		skills = SkillTreeTable.getInstance().getAvailableSkills(player);
				
		AquireSkillList asl = new AquireSkillList();		
		for (int i = 0; i < skills.length; i++)
		{
			asl.addSkill(skills[i].getId(), skills[i].getLevel(),skills[i].getLevel(),skills[i].getSpCost(), 0);
		}
		
		player.getNetConnection().sendPacket(asl);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__6C_REQUESTAQUIRESKILL;
	}
}
