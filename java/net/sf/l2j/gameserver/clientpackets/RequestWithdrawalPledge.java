/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestWithdrawalPledge.java,v 1.3 2004/09/28 01:40:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:40:56 $
 * $Revision: 1.3 $
 * $Log: RequestWithdrawalPledge.java,v $
 * Revision 1.3  2004/09/28 01:40:56  nuocnam
 * Added header and copyright notice.
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListDeleteAll;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 01:40:56 $
 */
public class RequestWithdrawalPledge extends ClientBasePacket
{
	private static final String _C__26_REQUESTWITHDRAWALPLEDGE = "[C] 26 RequestWithdrawalPledge";
	static Logger _log = Logger.getLogger(RequestWithdrawalPledge.class.getName());
			
	public RequestWithdrawalPledge(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		
		//is the guy in a clan  ?
		if (activeChar.getClanId() == 0) 
		{	
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		L2ClanMember member = clan.getClanMember(activeChar.getName());
		clan.removeClanMember(activeChar.getName());
		clan.store();

		activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_MEMBERSHIP_TERMINATED));
		
		L2PcInstance player = member.getPlayerInstance();
		player.setClan(null);
		player.setClanId(0);
		player.setTitle("");
			
		player.sendPacket(new UserInfo(player));
		player.broadcastPacket(new CharInfo(player));
		
		// disable clan tab
		player.sendPacket(new PledgeShowMemberListDeleteAll());
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__26_REQUESTWITHDRAWALPLEDGE;
	}
}
