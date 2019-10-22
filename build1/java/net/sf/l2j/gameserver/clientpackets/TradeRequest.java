/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/TradeRequest.java,v 1.2 2004/08/08 23:00:36 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/08 23:00:36 $
 * $Revision: 1.2 $
 * $Log: TradeRequest.java,v $
 * Revision 1.2  2004/08/08 23:00:36  l2chef
 * added system messages and fixed transaction setting
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
import net.sf.l2j.gameserver.serverpackets.SendTradeRequest;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;

/**
 * 
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/08 23:00:36 $
 */
public class TradeRequest extends ClientBasePacket
{
	private static final String TRADEREQUEST__C__15 = "[C] 15 TradeRequest";
	private static Logger _log = Logger.getLogger(TradeRequest.class.getName());
	
	public TradeRequest(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();

		L2PcInstance player = client.getActiveChar();
		L2World world = L2World.getInstance();
		L2Object target = world.findObject(objectId);
		
		if (target == null || !(target instanceof L2PcInstance) || target.getObjectId() != objectId)
		{
			player.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
			return;
		}
		
		if (client.getActiveChar().getTransactionRequester() != null)
		{
			_log.fine("already trading with someone");
			player.sendPacket(new SystemMessage(SystemMessage.ALREADY_TRADING));
			return;
		}

		L2PcInstance pcTarget = (L2PcInstance) target;
		
		if (player.knownsObject(target) 
				&& !pcTarget.isTransactionInProgress()) 
		{
			pcTarget.setTransactionRequester(player);
			player.setTransactionRequester(pcTarget);
			pcTarget.sendPacket(new SendTradeRequest(player.getObjectId()));
			SystemMessage sm = new SystemMessage(SystemMessage.REQUEST_S1_FOR_TRADE);
			sm.addString(pcTarget.getName());
			player.sendPacket(sm);
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_BUSY_TRY_LATER);
			sm.addString(pcTarget.getName());
			player.sendPacket(sm);
			_log.info("transaction already in progress.");
		}
	} 
	
	public String getType()
	{
		return TRADEREQUEST__C__15;
	}
}
