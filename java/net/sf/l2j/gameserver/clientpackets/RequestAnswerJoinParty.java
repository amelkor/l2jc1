/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestAnswerJoinParty.java,v 1.7 2004/09/27 08:44:51 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/27 08:44:51 $
 * $Revision: 1.7 $
 * $Log: RequestAnswerJoinParty.java,v $
 * Revision 1.7  2004/09/27 08:44:51  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.6  2004/09/19 02:36:09  whatev66
 * loot system added
 *
 * Revision 1.5  2004/08/08 22:59:27  l2chef
 * fixed transaction setting
 *
 * Revision 1.4  2004/08/06 00:23:22  l2chef
 * transaction controller is used for party requests (whatev)
 *
 * Revision 1.3  2004/07/19 02:01:35  l2chef
 * party code completed (whatev)
 *
 * Revision 1.2  2004/07/04 19:09:47  l2chef
 * party window is updated
 *
 * Revision 1.1  2004/07/04 11:12:18  l2chef
 * new party related handers
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.JoinParty;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 *  sample
 *  2a 
 *  01 00 00 00
 * 
 *  format  cdd
 * 
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/27 08:44:51 $
 */
public class RequestAnswerJoinParty extends ClientBasePacket
{
	private static final String _C__2A_REQUESTANSWERPARTY = "[C] 2A RequestAnswerParty";
	private static Logger _log = Logger.getLogger(RequestAnswerJoinParty.class.getName());
	
	public RequestAnswerJoinParty(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int response = readD();
		
		L2PcInstance player = client.getActiveChar();
		L2PcInstance requestor = player.getTransactionRequester();
		
		JoinParty join = new JoinParty(response);
		requestor.sendPacket(join);	
			
		if (response == 1) {
			player.joinParty(requestor.getParty());
		} else {
			SystemMessage msg = new SystemMessage(SystemMessage.PLAYER_DECLINED);
			requestor.sendPacket(msg);
			//activate garbage collection if there are no other members in party (happens when we were creating new one) 
			if (requestor.getParty().getMemberCount() == 1) requestor.setParty(null);
		}
		
		player.setTransactionRequester(null);
		requestor.setTransactionRequester(null);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__2A_REQUESTANSWERPARTY;
	}
}
