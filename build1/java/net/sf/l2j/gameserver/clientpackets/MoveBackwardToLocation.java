/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/MoveBackwardToLocation.java,v 1.10 2004/10/20 23:59:05 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/20 23:59:05 $
 * $Revision: 1.10 $
 * $Log: MoveBackwardToLocation.java,v $
 * Revision 1.10  2004/10/20 23:59:05  nuocnam
 * attack animation is canceled properly when player moves
 *
 * Revision 1.9  2004/10/20 08:17:05  nuocnam
 * player can no longer move while casting a spell/using a skill (dragon666)
 *
 * Revision 1.8  2004/10/03 16:56:09  whatev66
 *  follow is timer based now
 *
 * Revision 1.7  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.6  2004/07/17 23:10:50  l2chef
 * uses the server controlled move method instead of trusting the client
 *
 * Revision 1.5  2004/07/14 22:06:59  l2chef
 * rubberband effect when stop attacking fixed
 *
 * Revision 1.4  2004/07/12 20:54:26  l2chef
 * movement state removed
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
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.AttackCanceld;
/**
 * This class ...
 * 
 * @version $Revision: 1.10 $ $Date: 2004/10/20 23:59:05 $
 */
public class MoveBackwardToLocation extends ClientBasePacket
{
	// cdddddd

	private static final String _C__01_MOVEBACKWARDTOLOC = "[C] 01 MoveBackwardToLoc";
	/**
	 * packet type id 0x01
	 * @param decrypt
	 */
	public MoveBackwardToLocation(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int targetX  = readD();
		int targetY  = readD();
		int targetZ  = readD();
		int originX  = readD();
		int originY  = readD();
		int originZ  = readD();
		
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar.getCurrentState() == L2Character.STATE_CASTING) {
			activeChar.sendPacket(new ActionFailed());
		} else {
			if (activeChar.getCurrentState() == L2Character.STATE_ATTACKING) {
				AttackCanceld ac = new AttackCanceld(activeChar.getObjectId());
				activeChar.sendPacket(ac);
				activeChar.broadcastPacket(ac);
			}
			activeChar.setInCombat(false);
			activeChar.setCurrentState(L2Character.STATE_IDLE);
			activeChar.setX(originX);
			activeChar.setY(originY);
			activeChar.setZ(originZ);
			activeChar.moveTo(targetX, targetY, targetZ, 0);
		}		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__01_MOVEBACKWARDTOLOC;
	}
}
