/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestActionUse.java,v 1.11 2004/10/03 16:54:47 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/03 16:54:47 $
 * $Revision: 1.11 $
 * $Log: RequestActionUse.java,v $
 * Revision 1.11  2004/10/03 16:54:47  whatev66
 * no more position listener its all timer based
 *
 * Revision 1.10  2004/09/21 00:29:21  whatev66
 * fixed pet follow and stop
 *
 * Revision 1.9  2004/09/16 23:08:46  dalrond
 * Added a !null check to avoid a null pointer error
 *
 * Revision 1.8  2004/08/13 00:01:49  l2chef
 * follow mode added (Deth)
 *
 * Revision 1.7  2004/08/04 21:25:51  l2chef
 * actions prevented if dead
 *
 * Revision 1.6  2004/08/01 22:41:44  l2chef
 * broadcast method of l2character used
 *
 * Revision 1.5  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
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
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.ChangeMoveType;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.model.L2Character;
/**
 * This class ...
 * 
 * @version $Revision: 1.11 $ $Date: 2004/10/03 16:54:47 $
 */
public class RequestActionUse extends ClientBasePacket
{
	private static final String _C__45_REQUESTACTIONUSE = "[C] 45 RequestActionUse";
	private static Logger _log = Logger.getLogger(RequestActionUse.class.getName());
	
	/**
	 * packet type id 0x45
	 * format:		cddc
	 * @param rawPacket
	 */
	public RequestActionUse(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		int actionId  = readD();
		int data2  = readD();
		int data3 =  readC();
		
		
		_log.fine("request Action use: id "+actionId + " 2:" + data2 + " 3:"+data3);
		L2PcInstance activeChar = client.getActiveChar();
		// dont do anything if player is dead
		if (activeChar.isDead())
		{
			activeChar.sendPacket(new ActionFailed());
			return;
		}
		
		switch (actionId)
		{
			case 0:
			{
				int waitType = activeChar.getWaitType() ^ 0x01; // toggle
				_log.fine("new wait type: "+waitType);
				
				ChangeWaitType cmt = new ChangeWaitType(activeChar, waitType);
				activeChar.setWaitType(waitType);
				activeChar.sendPacket(cmt);
				
				// also notify all other players about our state change
				activeChar.broadcastPacket(cmt);
				
				break;
			}
			case 1:
			{
				int moveType = activeChar.getMoveType() ^ 0x01; // toggle
				_log.fine("new move type: "+moveType);
				
				ChangeMoveType cmt = new ChangeMoveType(activeChar, moveType);
				activeChar.setMoveType(moveType);
				activeChar.sendPacket(cmt);
				
				// also notify all other players about our state change
				activeChar.broadcastPacket(cmt);
				
				break;
			}
			case 15:
			{
				if (activeChar.getPet()!=null)
				{
					if (activeChar.getPet().getCurrentState() != L2Character.STATE_FOLLOW)
					{
						activeChar.getPet().setCurrentState(L2Character.STATE_FOLLOW);
						activeChar.getPet().setFollowStatus(true);
						activeChar.getPet().followOwner(activeChar);
					}
					else
					{
						
						activeChar.getPet().setCurrentState(L2Character.STATE_IDLE);
						activeChar.getPet().setFollowStatus(false);
						activeChar.getPet().setMovingToPawn(false);
						activeChar.getPet().setPawnTarget(null);
						activeChar.getPet().stopMove();
						activeChar.getPet().broadcastPacket(new StopMove(activeChar.getPet()));
						
					}
				}
				
				break;
			}
			case 16:
			{
				if (activeChar.getTarget() != null && activeChar.getPet()!=null && activeChar.getPet()!= activeChar.getTarget())
				{
					 activeChar.getPet().startAttack((L2Character)activeChar.getTarget());
				}
				break;
			}
			case 17:
			{
				if (activeChar.getPet()!=null)
				{
					if (activeChar.getPet().getCurrentState() == L2Character.STATE_FOLLOW)
					{
						activeChar.getPet().setFollowStatus(false);
						activeChar.getPet().setMovingToPawn(false);
						activeChar.getPet().setPawnTarget(null);
					}
					activeChar.getPet().setCurrentState(L2Character.STATE_IDLE);
					activeChar.getPet().stopMove();
					activeChar.getPet().broadcastPacket(new StopMove(activeChar.getPet()));
					
				}
				break;
			}
			case 18:
			{
				_log.warning("unhandled action type 18");
				break;
			}
			case 19:
			{
				if (activeChar.getPet()!=null)
				{
					//returns pet to control item
					activeChar.getPet().unSummon(activeChar); 
				}
				break;
			}
			case 20:
			{
				_log.warning("unhandled action type 20");
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__45_REQUESTACTIONUSE;
	}
}
