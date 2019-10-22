/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPledgeMemberList.java,v 1.5 2004/08/10 00:48:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/10 00:48:46 $
 * $Revision: 1.5 $
 * $Log: RequestPledgeMemberList.java,v $
 * Revision 1.5  2004/08/10 00:48:46  l2chef
 * extended clan functions
 *
 * Revision 1.4  2004/08/09 00:07:45  l2chef
 * new clan related packet handlers added (NuocNam)
 *
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListAll;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/10 00:48:46 $
 */
public class RequestPledgeMemberList extends ClientBasePacket
{
	private static final String _C__3C_REQUESTPLEDGEMEMBERLIST = "[C] 3C RequestPledgeMemberList";
	private static Logger _log = Logger.getLogger(RequestPledgeMemberList.class.getName());

	/**
	 * packet type id 0x3c
	 * format:		c
	 * @param rawPacket
	 */
	public RequestPledgeMemberList(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);

		L2PcInstance activeChar = client.getActiveChar();
		L2Clan clan = activeChar.getClan();
		
		if (clan != null)
		{
			PledgeShowMemberListAll pm = new PledgeShowMemberListAll(clan, activeChar);
			activeChar.sendPacket(pm);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__3C_REQUESTPLEDGEMEMBERLIST;
	}
}
