/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PledgeShowMemberListDelete.java,v 1.2 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.2 $
 * $Log: PledgeShowMemberListDelete.java,v $
 * Revision 1.2  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/08/10 20:38:32  l2chef
 * init
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

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:44:16 $
 */
public class PledgeShowMemberListDelete extends ServerBasePacket
{
	private static final String _S__6B_PLEDGESHOWMEMBERLISTDELETE = "[S] 6b PledgeShowMemberListDelete";
	private String _player;
	
	public PledgeShowMemberListDelete(String playerName)
	{
		_player = playerName;
	}	
	
	public byte[] getContent()
	{
		writeC(0x6b);
		writeS(_player);
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__6B_PLEDGESHOWMEMBERLISTDELETE;
	}
}
