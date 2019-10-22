/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/GMViewPledgeInfo.java,v 1.1 2004/10/22 23:41:06 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/22 23:41:06 $
 * $Revision: 1.1 $
 * $Log: GMViewPledgeInfo.java,v $
 * Revision 1.1  2004/10/22 23:41:06  l2chef
 * alt-g packets added
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
package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * format   SdSS dddddddd d (Sddddd)
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/22 23:41:06 $
 */
public class GMViewPledgeInfo extends ServerBasePacket
{
	private static final String _S__A9_GMVIEWPLEDGEINFO = "[S] A9 GMViewPledgeInfo";
	private L2Clan _clan;
	private L2PcInstance _activeChar;
	
	public GMViewPledgeInfo(L2Clan clan, L2PcInstance activeChar)
	{
		_clan = clan;
		_activeChar = activeChar;
	}	
	
	public byte[] getContent()
	{
		writeC(0xa9);
		writeS(_activeChar.getName());
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
		return _S__A9_GMVIEWPLEDGEINFO;
	}

}
