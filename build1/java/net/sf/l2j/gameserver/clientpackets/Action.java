/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/Action.java,v 1.7 2004/09/18 01:41:34 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/09/18 01:41:34 $
 * $Revision: 1.7 $
 * $Log: Action.java,v $
 * Revision 1.7  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.6  2004/09/16 00:01:10  l2chef
 * npc info is only shown to gms (Deth)
 *
 * Revision 1.5  2004/08/04 21:24:41  l2chef
 * actions prevented if dead
 *
 * Revision 1.4  2004/07/04 11:12:33  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:27  l2chef
 * using jdk logger instead of println
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
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/18 01:41:34 $
 */
public class Action extends ClientBasePacket
{
	private static final String ACTION__C__04 = "[C] 04 Action";
	private static Logger _log = Logger.getLogger(Action.class.getName());
	
	// cddddc

	/**
	 * @param decrypt
	 */
	public Action(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId  = readD();
		int originX  = readD();
		int originY  = readD();
		int originZ  = readD();
		int actionId  = readC();// 0 for simple click  1 for shift click
		
		
		_log.fine("Action:" + actionId);
		_log.fine("oid:" + objectId);
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		
		L2Object obj = L2World.getInstance().findObject(objectId);
		if (obj != null && !activeChar.isDead()&&activeChar.getPrivateStoreType() ==0 && activeChar.getTransactionRequester() ==null)
		{
			switch (actionId)
			{
				case 0:
					obj.onAction(activeChar);
					break;
				case 1:
					obj.onActionShift(client);
					break;
			}
		}
		else
		{
			_log.warning("object not found, oid "+objectId+ " or player is dead");
			activeChar.sendPacket(new ActionFailed());
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return ACTION__C__04;
	}
}
