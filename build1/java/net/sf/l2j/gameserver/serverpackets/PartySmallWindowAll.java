/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PartySmallWindowAll.java,v 1.5 2004/09/27 08:40:04 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/27 08:40:04 $
 * $Revision: 1.5 $
 * $Log: PartySmallWindowAll.java,v $
 * Revision 1.5  2004/09/27 08:40:04  nuocnam
 * Removed unused getters and setters.
 *
 * Revision 1.4  2004/07/19 02:01:10  l2chef
 * party code completed (whatev)
 *
 * Revision 1.3  2004/07/13 23:00:14  l2chef
 * removed empty constructor
 *
 * Revision 1.2  2004/07/04 19:10:39  l2chef
 * use list instead of array
 *
 * Revision 1.1  2004/07/04 11:13:58  l2chef
 * new party related handers
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

import java.util.ArrayList;

import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * 
 *
 * sample
 * 63 
 * 01 00 00 00  count 
 * 
 * c1 b2 e0 4a  object id
 * 54 00 75 00 65 00 73 00 64 00 61 00 79 00 00 00  name
 * 5a 01 00 00  hp
 * 5a 01 00 00  hp max
 * 89 00 00 00  mp
 * 89 00 00 00  mp max
 * 0e 00 00 00  level 
 * 12 00 00 00  class
 * 00 00 00 00  
 * 01 00 00 00
 * 
 * 
 * format   d (dSdddddddd)
 *          
 * 
 * @version $Revision: 1.5 $ $Date: 2004/09/27 08:40:04 $
 */
public class PartySmallWindowAll extends ServerBasePacket
{
	private static final String _S__63_PARTYSMALLWINDOWALL = "[S] 63 PartySmallWindowAll";
	private ArrayList _partyMembers = new ArrayList();
	
	public void setPartyList(ArrayList party) {
		_partyMembers = party;
	}

	public byte[] getContent()
	{
		writeC(0x63);
		writeD(_partyMembers.size());
		
		for(int i = 0; i < _partyMembers.size(); i++) {
			L2PcInstance member = (L2PcInstance) _partyMembers.get(i);
			
			writeD(member.getObjectId());
			writeS(member.getName());
			writeD((int) member.getCurrentHp());
			writeD(member.getMaxHp());
			writeD((int) member.getCurrentMp());
			writeD(member.getMaxMp());
			writeD(member.getClassId());
			writeD(member.getLevel());
			writeD(0);
			writeD(0);
		}
		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__63_PARTYSMALLWINDOWALL;
	}
}
