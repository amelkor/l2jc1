/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AquireSkillInfo.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: AquireSkillInfo.java,v $
 * Revision 1.3  2004/07/04 11:14:53  l2chef
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

/**
 * <code>
 * sample 
 * 
 * a4
 * 4d000000 01000000 98030000 			Attack Aura, level 1, sp cost
 * 01000000 							number of requirements
 * 05000000 47040000 0100000 000000000	   1 x spellbook advanced attack                                                 .
 * </code>
 * 
 * format   ddd d (dddd)
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class AquireSkillInfo extends ServerBasePacket
{
	private static final String _S__A4_AQUIRESKILLINFO = "[S] A4 AquireSkillInfo";
	private Vector _reqs;
	private int _id;
	private int _level;
	private int _spCost;
	
	class Req
	{
		public int id;
		public int count;
		public int type;
		public int unk;
		
		Req(int type, int id, int count, int unk)
		{
			this.id = id;
			this.type = type;
			this.count = count;
			this.unk = unk;
		}
	}

	public AquireSkillInfo(int id, int level, int spCost)
	{
		_reqs = new Vector();
		_id = id;
		_level = level;
		_spCost = spCost;
	}	
	
	public void addRequirement(int type, int id, int count, int unk)
	{
		_reqs.add(new Req(type, id, count, unk));
	}
	
	public byte[] getContent()
	{
		writeC(0xa4);
		writeD(_id);
		writeD(_level);
		writeD(_spCost);
		
		writeD(_reqs.size());

		for (int i = 0; i < _reqs.size(); i++)
		{
			Req temp = (Req) _reqs.get(i);
			writeD(temp.type);
			writeD(temp.id);
			writeD(temp.count);
			writeD(temp.unk);
		}

		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__A4_AQUIRESKILLINFO;
	}
	
}
