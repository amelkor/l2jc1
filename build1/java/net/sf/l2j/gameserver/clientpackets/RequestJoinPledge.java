/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestJoinPledge.java,v 1.3 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.3 $
 * $Log: RequestJoinPledge.java,v $
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
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.AskJoinPledge;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestJoinPledge extends ClientBasePacket
{
	private static final String _C__24_REQUESTJOINPLEDGE = "[C] 24 RequestJoinPledge";
	static Logger _log = Logger.getLogger(ProtocolVersion.class.getName());
			
	public RequestJoinPledge(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		int target  = readD();
		
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		
		if (activeChar.isTransactionInProgress())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.WAITING_FOR_REPLY));
			_log.fine("player is already doing some other action");
			return;
		}
		
		if (target == activeChar.getObjectId())
		{
			SystemMessage sm = new SystemMessage(SystemMessage.CANNOT_INVITE_YOURSELF);
			activeChar.sendPacket(sm);
			return;
		}
		
		//is the guy leader of the clan ?
		if (activeChar.isClanLeader()) 
		{	
			L2Object object = L2World.getInstance().findObject(target);
			if (object instanceof L2PcInstance)
			{
				L2PcInstance member = (L2PcInstance) object;

				if (member.getClanId() != 0)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_WORKING_WITH_ANOTHER_CLAN);
					sm.addString(member.getName());
					activeChar.sendPacket(sm);
					return;				
				}
				else if (member.isTransactionInProgress())
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
					sm.addString(member.getName());
					activeChar.sendPacket(sm);
					return;
				} 
				else
				{
					member.setTransactionRequester(activeChar);
					activeChar.setTransactionRequester(member);
					
					AskJoinPledge ap = new AskJoinPledge(activeChar.getObjectId(), activeChar.getClan().getName()); 
					member.sendPacket(ap);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__24_REQUESTJOINPLEDGE;
	}
}
