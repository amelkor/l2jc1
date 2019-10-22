/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AquireSkillList.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: AquireSkillList.java,v $
 * Revision 1.3  2004/07/04 11:14:52  l2chef
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
package net.sf.l2j.gameserver.serverpackets;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * sample 
 * 
 * a3 
 * 05000000 
 * 03000000 03000000 06000000 3c000000 00000000 	power strike
 * 10000000 02000000 06000000 3c000000 00000000 	mortal blow
 * 38000000 04000000 06000000 36010000 00000000 	power shot
 * 4d000000 01000000 01000000 98030000 01000000 	attack aura  920sp
 * 8e000000 03000000 03000000 cc010000 00000000     Armor Mastery
 * 
 * format   d (ddddd)
 * skillid, level, maxlevel?,
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $ 
 */
public class AquireSkillList extends ServerBasePacket
{
	private static Logger _log = Logger.getLogger(AquireSkillList.class.getName());
	private static final String _S__A3_AQUIRESKILLLIST = "[S] A3 AquireSkillList";
	private Vector _skills;
	
	class Skill
	{
		public int id;
		public int nextLevel;
		public int maxLevel;
		public int spCost;
		public int requirements;
		
		Skill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
		{
			this.id = id;
			this.nextLevel = nextLevel;
			this.maxLevel = maxLevel;
			this.spCost = spCost;
			this.requirements = requirements;
		}
	}

	public AquireSkillList()
	{
		_skills = new Vector();
	}	
	
	public void addSkill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, spCost, requirements));
	}
	
	public byte[] getContent()
	{
		writeC(0xa3);
		writeD(_skills.size());

		for (int i = 0; i < _skills.size(); i++)
		{
			Skill temp = (Skill) _skills.get(i);
			writeD(temp.id);
			writeD(temp.nextLevel);
			writeD(temp.maxLevel);
			writeD(temp.spCost);
			writeD(temp.requirements);
			_log.fine("skill: " + temp.id + " "+ temp.nextLevel + " "+ temp.maxLevel + " "+ temp.spCost + " "+ temp.requirements );
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__A3_AQUIRESKILLLIST;
	}
}
