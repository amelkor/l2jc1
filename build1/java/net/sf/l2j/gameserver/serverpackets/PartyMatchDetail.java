/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PartyMatchDetail.java,v 1.1 2004/07/04 11:14:32 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:32 $
 * $Revision: 1.1 $
 * $Log: PartyMatchDetail.java,v $
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
 * b0 
 * d8 a8 10 48  objectId 
 * 00 00 00 00 
 * 00 00 00 00 
 * 00 00  
 * 
 * format   ddddS
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:14:32 $
 */
public class PartyMatchDetail extends ServerBasePacket
{
	private static final String _S__B0_PARTYMATCHDETAIL = "[S] B0 PartyMatchDetail";
	private L2PcInstance _player;
	
	/**
	 * @param allPlayers
	 */
	public PartyMatchDetail(L2PcInstance player)
	{
		_player = player;
	}


	public byte[] getContent()
	{
		writeC(0xb0);
		
		writeD(_player.getObjectId());
		if (_player.isPartyMatchingShowLevel())
		{
			writeD(1); // show level
		}
		else
		{
			writeD(0); // hide level 
		}
		
		if (_player.isPartyMatchingShowClass())
		{
			writeD(1); // show class
		}
		else
		{
			writeD(0); // hide class
		}
		
		writeS("  " + _player.getPartyMatchingMemo()); // seems to be bugged.. first 2 chars get stripped away
		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__B0_PARTYMATCHDETAIL;
	}
}
