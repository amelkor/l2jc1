/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/AttackRequest.java,v 1.7 2004/09/18 01:41:34 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/09/18 01:41:34 $
 * $Revision: 1.7 $
 * $Log: AttackRequest.java,v $
 * Revision 1.7  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.6  2004/08/13 00:02:20  l2chef
 * additional target checks (Deth)
 *
 * Revision 1.5  2004/07/28 23:55:05  l2chef
 * self attack prevented (NuocNam)
 *
 * Revision 1.4  2004/07/17 23:05:31  l2chef
 * calling forced attack on target objects
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

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/18 01:41:34 $
 */
public class AttackRequest extends ClientBasePacket
{
	// cddddc

	private static final String _C__0A_ATTACKREQUEST = "[C] 0A AttackRequest";
	/**
	 * @param decrypt
	 */
	public AttackRequest(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		
		int objectId  = readD();
		int originX  = readD();
		int originY  = readD();
		int originZ  = readD();
		int attackId  = readC(); 	 // 0 for simple click   1 for shift-click


		L2PcInstance activeChar = client.getActiveChar();
		L2Object target = L2World.getInstance().findObject(objectId);

		if ((target != null) && (activeChar.getTarget() != target))
		{
			target.onAction(activeChar);
		}
		else
		{
			if ((target != null) && (target.getObjectId() != activeChar.getObjectId())&&activeChar.getPrivateStoreType() ==0 && activeChar.getTransactionRequester() ==null)
			{
				target.onForcedAttack(activeChar);				
			} 
			else
			{
				activeChar.sendPacket(new ActionFailed());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__0A_ATTACKREQUEST;
	}
}
