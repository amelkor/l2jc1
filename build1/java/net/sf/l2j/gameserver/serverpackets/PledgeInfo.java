/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PledgeInfo.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: PledgeInfo.java,v $
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

import net.sf.l2j.gameserver.model.L2Clan;

/**
 * 
 *
 * sample
 * 0000: 9c c10c0000 48 00 61 00 6d 00 62 00 75 00 72    .....H.a.m.b.u.r
 * 0010: 00 67 00 00 00 00000000 00000000 00000000 00000000 00000000 00000000 
 * 00 00 
 * 00000000                                           ...
 
 *  
 * format   dSddddddSd 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class PledgeInfo extends ServerBasePacket
{
	private static final String _S__9C_PLEDGEINFO = "[S] 9C PledgeInfo";
	private L2Clan _clan;
	
	public PledgeInfo(L2Clan clan)
	{
		_clan = clan;
	}	
	
	public byte[] getContent()
	{
		writeC(0x9c);
		writeD(_clan.getClanId());
		writeS(_clan.getName());
		writeS(_clan.getAllyName());
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__9C_PLEDGEINFO;
	}

}
