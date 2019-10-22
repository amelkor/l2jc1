/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PledgeShowMemberListAdd.java,v 1.3 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.3 $
 * $Log: PledgeShowMemberListAdd.java,v $
 * Revision 1.3  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.2  2004/08/10 20:37:43  l2chef
 * added members are always online
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

import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 02:44:16 $
 */
public class PledgeShowMemberListAdd extends ServerBasePacket
{
	private static final String _S__6A_PLEDGESHOWMEMBERLISTADD = "[S] 6a PledgeShowMemberListAdd";
	private L2PcInstance _player;
	
	public PledgeShowMemberListAdd(L2PcInstance player)
	{
		_player = player;
	}	
	
	public byte[] getContent()
	{
		writeC(0x6a);
		writeS(_player.getName());
		writeD(_player.getLevel());
		writeD(_player.getClassId());
		writeD(0); 
		writeD(1);
		writeD(1); // 1=online 0=offline		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__6A_PLEDGESHOWMEMBERLISTADD;
	}

}
