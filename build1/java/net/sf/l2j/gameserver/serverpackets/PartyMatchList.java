/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PartyMatchList.java,v 1.1 2004/07/04 11:14:32 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:32 $
 * $Revision: 1.1 $
 * $Log: PartyMatchList.java,v $
 * Revision 1.1  2004/07/04 11:14:32  l2chef
 * party matching added as to show online players
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

import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * 
 *
 * sample
 * af 
 * 02 00 00 00   count 
 * 
 * 71 b3 70 4b  object id
 * 44 00 79 00 66 00 65 00 72 00 00 00   name
 * 14 00 00 00  level
 * 0f 00 00 00  class id
 * 00 00 00 00  sex ??
 * 00 00 00 00  clan id
 * 02 00 00 00  ??
 * 6f 5f 00 00  x 
 * af a9 00 00  y 
 * f7 f1 ff ff  z 
 *  
 *
 * c1 9c c0 4b object id
 * 43 00 6a 00 6a 00 6a 00 6a 00 6f 00 6e 00 00 00   
 * 0b 00 00 00  level
 * 12 00 00 00  class id
 * 00 00 00 00  sex ??
 * b1 01 00 00  clan id
 * 00 00 00 00 
 * 13 af 00 00 
 * 38 b8 00 00 
 * 4d f4 ff ff 
 * *  
 * format   d (dSdddddddd)
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:14:32 $
 */
public class PartyMatchList extends ServerBasePacket
{
	private static final String _S__AF_PARTYMATCHLIST = "[S] AF PartyMatchList";
	private L2PcInstance[] _matchingPlayers;
	
	/**
	 * @param allPlayers
	 */
	public PartyMatchList(L2PcInstance[] allPlayers)
	{
		_matchingPlayers = allPlayers;
	}


	public byte[] getContent()
	{
		writeC(0xaf);
		
		int size = _matchingPlayers.length;
		if (size > 40)
		{
			size = 40; // the client only displays 40 players, so we also limit the list to 40
		}
		
		writeD(size);  
		for (int i=0;i<size;i++)
		{
			writeD(_matchingPlayers[i].getObjectId());
			writeS(_matchingPlayers[i].getName());
			writeD(_matchingPlayers[i].getLevel());
			writeD(_matchingPlayers[i].getClassId());
			writeD(00);   // 00 -white name   01-red name
			writeD(_matchingPlayers[i].getClanId());
			writeD(00); //  00 - no affil  01-party  02-party pending  03-
			writeD(_matchingPlayers[i].getX());
			writeD(_matchingPlayers[i].getY());
			writeD(_matchingPlayers[i].getZ());
		}
		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__AF_PARTYMATCHLIST;
	}
}
