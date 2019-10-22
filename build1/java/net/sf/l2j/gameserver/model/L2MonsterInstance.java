/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2MonsterInstance.java,v 1.20 2004/10/09 19:25:19 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/09 19:25:19 $
 * $Revision: 1.20 $
 * $Log: L2MonsterInstance.java,v $
 * Revision 1.20  2004/10/09 19:25:19  whatev66
 * monsters should keep attacking now when removed from known
 *
 * Revision 1.19  2004/10/06 14:55:55  whatev66
 * fixed still moving to you bug when they are removed from known
 *
 * Revision 1.18  2004/10/05 19:31:57  whatev66
 * monsters take care of known instance properly now.
 *
 * Revision 1.17  2004/09/19 01:37:31  nuocnam
 * added .invul for admins (sh1ny)
 *
 * Revision 1.16  2004/07/29 23:30:16  l2chef
 * dont start random walking if dead (whatev)
 *
 * Revision 1.15  2004/07/25 23:00:22  l2chef
 * refactored the aggressive npcs into a new class
 *
 * Revision 1.14  2004/07/23 22:52:14  l2chef
 * target scan was scheduled multiple times resulting in multiple parallel attacks
 *
 * Revision 1.13  2004/07/19 23:18:13  l2chef
 * z coordinate is used for aggro check
 * tasks are terminated when not needed
 *
 * Revision 1.12  2004/07/19 02:06:21  l2chef
 * dummy weapon is only created once
 *
 * Revision 1.11  2004/07/18 17:38:25  l2chef
 * aggressive monsters scan for targets in range (FTPW)
 *
 * Revision 1.10  2004/07/17 23:19:11  l2chef
 * some refactorings
 *
 * Revision 1.9  2004/07/14 22:10:44  l2chef
 * exp calculation changed
 *
 * Revision 1.8  2004/07/13 23:01:29  l2chef
 * dont cancel walking if its not initialized
 *
 * Revision 1.7  2004/07/12 20:58:34  l2chef
 * indention fixed
 *
 * Revision 1.6  2004/07/11 11:51:30  l2chef
 * new exp reduction and random item drop added  (NightMarez)
 *
 * Revision 1.5  2004/07/04 17:41:41  l2chef
 * exp and sp are rewarded according to the level of the attacker
 *
 * Revision 1.4  2004/07/04 14:41:17  jeichhorn
 * implemented adding exp & sp to the attackers when the monster is dead
 *
 * Revision 1.3  2004/07/04 11:13:27  l2chef
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
package net.sf.l2j.gameserver.model;

import java.util.logging.Logger;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.20 $ $Date: 2004/10/09 19:25:19 $
 */
public class L2MonsterInstance extends L2Attackable
{
	private static Logger _log = Logger.getLogger(L2MonsterInstance.class.getName());
		
	
	/**
	 * @param template
	 */
	public L2MonsterInstance(L2Npc template)
	{
		super(template);
		setMoveRadius(2000);
		setCurrentState(STATE_RANDOM_WALK);
	}	
		
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#addKnownObject(net.sf.l2j.gameserver.model.L2Object)
	 */
	public void addKnownObject(L2Object object)
	{
		super.addKnownObject(object);
		if (object instanceof L2PcInstance && !isActive()) 
		{
			setActive(true);
			startRandomWalking();
			
			if (isAggressive() && !isTargetScanActive())
			{
				startTargetScan();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#removeKnownObject(net.sf.l2j.gameserver.model.L2Object)
	 */
	public void removeKnownObject(L2Object object)
	{
		super.removeKnownObject(object);
		L2Character temp = (L2Character)object;
		if (getTarget() == temp)
		{
			setTarget(null);
			stopMove();
			setPawnTarget(null);
			setMovingToPawn(false);
		}	
	
		
		if (getKnownPlayers().isEmpty())
		{
			setActive(false);
			clearAggroList();
			removeAllKnownObjects();
			stopRandomWalking();
			
			if (isAggressive())
			{
				stopTargetScan();
			}
			return;
		}
		if (getCurrentState() != STATE_RANDOM_WALK)
		{
			if (!isDead() && getTarget() == null)
			{	
				startRandomWalking();
				if (isAggressive())
				{
					startTargetScan();
				}
			}
		}
	}
	
	public boolean getCondition2(L2PcInstance player)
	{
		return (!player.isInvul() && !player.isDead() && (Math.abs(getZ()-player.getZ()) <= 100 ));
	}
	
}