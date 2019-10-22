/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestGiveNickName.java,v 1.3 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.3 $
 * $Log: RequestGiveNickName.java,v $
 * Revision 1.3  2004/09/28 01:50:14  nuocnam
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
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestGiveNickName extends ClientBasePacket
{
	private static final String _C__55_REQUESTGIVENICKNAME = "[C] 55 RequestGiveNickName";
	static Logger _log = Logger.getLogger(RequestGiveNickName.class.getName());
			
	public RequestGiveNickName(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		String target  = readS();
		String title  = readS();
		
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		
		//is the guy leader of the clan ?
		if (activeChar.isClanLeader()) 
		{	
			if (activeChar.getClan().getLevel() < 3)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.CLAN_LVL_3_NEEDED_TO_ENDOVE_TITLE);
				activeChar.sendPacket(sm);
				
				//return; //TODO: put return here ^^;
				sm = new SystemMessage(SystemMessage.S1_S2);
				sm.addString("But you can do it freely");
				sm.addString("for now ;)");
				activeChar.sendPacket(sm);
			}
			
			L2PcInstance member = L2World.getInstance().getPlayer(target);
			//is target from the same clan?
			if (member.getClanId() == activeChar.getClanId())
			{
				member.setTitle(title);
				UserInfo ui = new UserInfo(member);
				member.sendPacket(ui);
				CharInfo ci = new CharInfo(member);
				member.broadcastPacket(ci);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__55_REQUESTGIVENICKNAME;
	}
}
