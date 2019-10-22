/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PledgeShowMemberListAll.java,v 1.6 2004/08/10 20:38:07 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/10 20:38:07 $
 * $Revision: 1.6 $
 * $Log: PledgeShowMemberListAll.java,v $
 * Revision 1.6  2004/08/10 20:38:07  l2chef
 * unused attributes deleted
 *
 * Revision 1.5  2004/08/10 00:51:08  l2chef
 * online status is reflected
 *
 * Revision 1.4  2004/08/09 00:08:48  l2chef
 * clan related updated (NuocNam)
 *
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

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * 
 *
 * sample
 * 0000: 68 
 * b1010000 
 * 48 00 61 00 6d 00 62 00 75 00 72 00 67 00 00 00   H.a.m.b.u.r.g...
 * 43 00 61 00 6c 00 61 00 64 00 6f 00 6e 00 00 00   C.a.l.a.d.o.n...
 * 00000000  crestid | not used (nuocnam)
 * 00000000 00000000 00000000 00000000 
 * 22000000 00000000 00000000 
 * 00000000 ally id
 * 00 00	ally name
 * 00000000 ally crrest id 
 * 
 * 02000000
 *  
 * 6c 00 69 00 74 00 68 00 69 00 75 00 6d 00 31 00 00 00  l.i.t.h.i.u.m...
 * 0d000000		level 
 * 12000000 	class id
 * 00000000 	
 * 01000000 	offline 1=true
 * 00000000
 *  
 * 45 00 6c 00 61 00 6e 00 61 00 00 00   E.l.a.n.a...
 * 08000000 
 * 19000000 
 * 01000000 
 * 01000000 
 * 00000000
 * 
 *  
 * format   dSS dddddddddSd d (Sddddd)
 * 
 * @version $Revision: 1.6 $ $Date: 2004/08/10 20:38:07 $
 */
public class PledgeShowMemberListAll extends ServerBasePacket
{
	private static final String _S__68_PLEDGESHOWMEMBERLISTALL = "[S] 68 PledgeShowMemberListAll";
	private L2Clan _clan;
	private L2PcInstance _activeChar;
	
	public PledgeShowMemberListAll(L2Clan clan, L2PcInstance activeChar)
	{
		_clan = clan;
		_activeChar = activeChar;
	}	
	
	public byte[] getContent()
	{
		writeC(0x68);
		writeD(_clan.getClanId());
		writeS(_clan.getName());
		writeS(_clan.getLeaderName());
		writeD(0); // -> no, it's no longer used (nuocnam)
		writeD(_clan.getLevel());
		writeD(_clan.getHasCastle());
		writeD(_clan.getHasHideout());
		writeD(0);
		writeD(_activeChar.getLevel()); 
		writeD(0);
		writeD(0);
		
		writeD(0);//_clan.getAllyId()
		writeS("");//_clan.getAllyName()
		writeD(0);//_clan.getAllyCrestId()
		
		L2ClanMember[] members = _clan.getMembers();
		writeD(members.length-1);
		for (int i=0; i<members.length; i++)
		{
			if (!members[i].getName().equals(_activeChar.getName()))
			{
				writeS(members[i].getName());
				writeD(members[i].getLevel());
				writeD(members[i].getClassId());
				writeD(0); 
				writeD(1);
				if (members[i].isOnline())  // 1=online 0=offline
				{
					writeD(1);
				}
				else
				{
					writeD(0);
				}
			}
		}		
		
		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__68_PLEDGESHOWMEMBERLISTALL;
	}

}
