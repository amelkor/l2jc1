/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestSetPledgeCrest.java,v 1.2 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.2 $
 * $Log: RequestSetPledgeCrest.java,v $
 * Revision 1.2  2004/09/28 01:50:14  nuocnam
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestSetPledgeCrest extends ClientBasePacket
{
	private static final String _C__53_REQUESTSETPLEDGECREST = "[C] 53 RequestSetPledgeCrest";
	static Logger _log = Logger.getLogger(RequestSetPledgeCrest.class.getName());
			
	public RequestSetPledgeCrest(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		int length  = readD();
		byte[] data = readB(length);
		
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		
		//is the guy leader of the clan ?
		if (!activeChar.isClanLeader()) 
		{	
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		
		File crestFile = new File("data/crests/Pledge_"+clan.getClanId()+".bmp");
		FileOutputStream out = new FileOutputStream(crestFile);
		out.write(data);
		out.close();
			
		UserInfo ui = new UserInfo(activeChar);
		activeChar.sendPacket(ui);
		CharInfo ci = new CharInfo(activeChar);
		activeChar.broadcastPacket(ci);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__53_REQUESTSETPLEDGECREST;
	}
}
