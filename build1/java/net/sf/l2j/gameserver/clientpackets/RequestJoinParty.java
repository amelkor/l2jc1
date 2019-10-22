/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestJoinParty.java,v 1.7 2004/09/27 08:44:51 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/27 08:44:51 $
 * $Revision: 1.7 $
 * $Log: RequestJoinParty.java,v $
 * Revision 1.7  2004/09/27 08:44:51  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.6  2004/09/19 02:36:22  whatev66
 * loot system added
 *
 * Revision 1.5  2004/08/08 22:59:56  l2chef
 * added system messages and fixed transaction setting
 *
 * Revision 1.4  2004/08/08 16:13:21  l2chef
 * some reformatting
 *
 * Revision 1.3  2004/08/06 00:23:22  l2chef
 * transaction controller is used for party requests (whatev)
 *
 * Revision 1.2  2004/07/19 02:01:35  l2chef
 * party code completed (whatev)
 *
 * Revision 1.1  2004/07/04 11:09:46  l2chef
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
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.AskJoinParty;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 *  sample
 *  29 
 *  42 00 00 10 
 *  01 00 00 00
 * 
 *  format  cdd
 * 
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/27 08:44:51 $
 */
public class RequestJoinParty extends ClientBasePacket
{
	private static final String _C__29_REQUESTJOINPARTY = "[C] 29 RequestJoinParty";
	private static Logger _log = Logger.getLogger(RequestJoinParty.class.getName());
	
	public RequestJoinParty(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		int id = readD();
		int itemDistribution = readD();
		
		SystemMessage msg;
		L2PcInstance target = (L2PcInstance) L2World.getInstance().findObject(id);
		L2PcInstance requestor = client.getActiveChar();

		if (requestor.isTransactionInProgress())
		{
			msg = new SystemMessage(SystemMessage.WAITING_FOR_REPLY);
			requestor.sendPacket(msg);
			_log.fine("player already invited someone.");
			return;
		}

		if( target.isInParty()) {
			msg = new SystemMessage(SystemMessage.S1_IS_ALREADY_IN_PARTY);
			msg.addString(target.getName());
			requestor.sendPacket(msg);
			return;
		}

		
		if (!client.getActiveChar().isInParty())//asker has no party
		{
			createNewParty(client, itemDistribution, target, requestor);
		}
		else//asker has a party
		{
			addTargetToParty(client, itemDistribution, target, requestor);
		}
	}
	
	/**
	 * @param client
	 * @param itemDistribution
	 * @param target
	 * @param requestor
	 */
	private void addTargetToParty(ClientThread client, int itemDistribution, L2PcInstance target, L2PcInstance requestor)
	{
		SystemMessage msg;

		if(requestor.getParty().getMemberCount() >= 9) {
			requestor.sendPacket(new SystemMessage(SystemMessage.PARTY_FULL));
			return;
		}
		
		if(!requestor.getParty().isLeader(requestor)) {
			requestor.sendPacket(new SystemMessage(SystemMessage.ONLY_LEADER_CAN_INVITE));
			return;
		}
		
		if (target.getKnownPlayers().contains( requestor )) 
		{
			// only continue if the client actually knowns the id
			
			if (!target.isTransactionInProgress()) 
			{
				target.setTransactionRequester(requestor);
				requestor.setTransactionRequester(target);
				
				AskJoinParty ask = new AskJoinParty( requestor.getObjectId(), itemDistribution);
				target.sendPacket(ask);
				_log.fine("sent out a party invitation to:"+target.getName());
				msg = new SystemMessage(SystemMessage.YOU_INVITED_S1_TO_PARTY);
				msg.addString(target.getName());
				requestor.sendPacket(msg);
			}
			else
			{
				msg = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
				requestor.sendPacket(msg);
				_log.warning( requestor.getName() + " already received a party invitation");
			}
		}
		else
		{
			_log.warning(client.getActiveChar().getName()+ " invited someone who doesn't know him.");
		}
	}

	/**
	 * @param client
	 * @param itemDistribution
	 * @param target
	 * @param requestor
	 */
	private void createNewParty(ClientThread client, int itemDistribution, L2PcInstance target, L2PcInstance requestor)
	{
		SystemMessage msg;
		if(target.getKnownPlayers().contains( requestor ))
		{
			if (!target.isTransactionInProgress())
			{
				requestor.setParty(new L2Party(requestor, (itemDistribution == 1)));
				
				target.setTransactionRequester(requestor);
				requestor.setTransactionRequester(target);
				AskJoinParty ask = new AskJoinParty( requestor.getObjectId(), itemDistribution);
				target.sendPacket(ask);
				_log.fine("sent out a party invitation to:"+target.getName());

				msg = new SystemMessage(SystemMessage.YOU_INVITED_S1_TO_PARTY);
				msg.addString(target.getName());
				requestor.sendPacket(msg);
			}
			else
			{
				msg = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
				msg.addString(target.getName());
				requestor.sendPacket(msg);
				_log.warning( requestor.getName() + " already received a party invitation");
			}
		}
		else
		{
			if(target.getKnownPlayers().contains( requestor ))
			{
				_log.warning(client.getActiveChar().getName()+ " invited someone who doesn't know him.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__29_REQUESTJOINPARTY;
	}
}
