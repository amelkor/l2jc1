/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPledgeInfo.java,v 1.5 2004/08/10 00:48:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/10 00:48:46 $
 * $Revision: 1.5 $
 * $Log: RequestPledgeInfo.java,v $
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

import net.sf.l2j.gameserver.ClanTable;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.PledgeInfo;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListAll;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/10 00:48:46 $
 */
public class RequestPledgeInfo extends ClientBasePacket
{
	private static final String _C__66_REQUESTPLEDGEINFO = "[C] 66 RequestPledgeInfo";
	private static Logger _log = Logger.getLogger(RequestPledgeInfo.class.getName());
	
	/**
	 * packet type id 0x66
	 * format:		cd
	 * @param rawPacket
	 */
	public RequestPledgeInfo(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		int clanId  = readD();
		
		_log.fine("infos for clan " + clanId + " requested");

		L2PcInstance activeChar = client.getActiveChar();
		L2Clan clan = ClanTable.getInstance().getClan(clanId);
		if (clan == null)
		{
			_log.warning("Clan data for clanId "+ clanId + " is missing");
			return; // we have no clan data ?!? should not happen
		}
			
		PledgeInfo pc = new PledgeInfo(clan);
		activeChar.sendPacket(pc);
		
		if (clan.getClanId() == activeChar.getClanId())
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
		return _C__66_REQUESTPLEDGEINFO;
	}
}
