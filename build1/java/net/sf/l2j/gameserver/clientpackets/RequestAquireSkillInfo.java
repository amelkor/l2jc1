/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestAquireSkillInfo.java,v 1.5 2004/07/16 23:15:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/16 23:15:21 $
 * $Revision: 1.5 $
 * $Log: RequestAquireSkillInfo.java,v $
 * Revision 1.5  2004/07/16 23:15:21  l2chef
 * added break if skill is found
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

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.serverpackets.AquireSkillInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/07/16 23:15:21 $
 */
public class RequestAquireSkillInfo extends ClientBasePacket
{
	private static final String _C__6B_REQUESTAQUIRESKILLINFO = "[C] 6B RequestAquireSkillInfo";
	/**
	 * packet type id 0x6b
	 * format:		cdd
	 * @param rawPacket
	 */
	public RequestAquireSkillInfo(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);

		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();

		int id = readD();
		int level = readD();
		L2Skill skill = SkillTable.getInstance().getInfo(id, level);
		
		L2SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(activeChar);
		int requiredSp=0;
		for (int i = 0; i < skills.length; i++)
		{
			if (skills[i].getId() == id) 
			{
				requiredSp = skills[i].getSpCost();
				break;
			}
		}

		
		AquireSkillInfo asi = new AquireSkillInfo(skill.getId(), skill.getLevel(), requiredSp);
//		asi.addRequirement(6,5,1,2);
//		asi.addRequirement(5,1097,2,0);
		
		con.sendPacket(asi);
		
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__6B_REQUESTAQUIRESKILLINFO;
	}
}
