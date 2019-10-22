/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2GuardInstance.java,v 1.11 2004/10/20 16:59:41 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/20 16:59:41 $
 * $Revision: 1.11 $
 * $Log: L2GuardInstance.java,v $
 * Revision 1.11  2004/10/20 16:59:41  whatev66
 * interact state now used when talking to npcs which are not attackable
 *
 * Revision 1.10  2004/10/15 18:27:31  nuocnam
 * putting interaction distance back to 150 as testing showed 50 was really too little
 *
 * Revision 1.9  2004/10/14 23:37:43  nuocnam
 * interaction distance set to 50
 *
 * Revision 1.8  2004/09/30 02:27:26  nuocnam
 * - corrected interaction distance (sh1ny, nuocnam)
 * - added javadoc comments (nuocnam)
 *
 * Revision 1.7  2004/09/28 02:25:48  nuocnam
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
package net.sf.l2j.gameserver.model;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.serverpackets.SetToLocation;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class represents all guards in the world. It inherits all methods from
 * L2Attackable and adds some more such as tracking PK's or custom interactions.
 * 
 * @version $Revision: 1.11 $ $Date: 2004/10/20 16:59:41 $
 */
public class L2GuardInstance extends L2Attackable
{
	private static Logger _log = Logger.getLogger(L2GuardInstance.class.getName());
	private static final int INTERACTION_DISTANCE = 150;

	private int _homeX;
	private int _homeY;
	private int _homeZ;
	private boolean _hasHome;

	public L2GuardInstance(L2Npc template)
	{
		super(template);
		setCurrentState(STATE_IDLE);
	}

	public void addKnownObject(L2Object object)
	{
		if (!_hasHome) getHomeLocation();
		super.addKnownObject(object);
		if (object instanceof L2PcInstance) 
		{
			L2PcInstance player = (L2PcInstance) object;
			if ( (player.getKarma() > 0) && !isTargetScanActive())
			{
				_log.fine(getObjectId()+": PK "+player.getObjectId()+" entered scan range");
				startTargetScan();
			}
		}
	}		

	public void removeKnownObject(L2Object object)
	{
		super.removeKnownObject(object);
		
		if (getKnownPlayers().isEmpty())
		{
			setActive(false);
			clearAggroList();
			removeAllKnownObjects();
			stopTargetScan();
		}
		
		if (noTarget()) returnHome();
	}

	/**
	 * Sets home location of guard. Guard will always try to return to this location after
	 * it has killed all PK's in range.
	 *
	 */
	private void getHomeLocation()
	{
		_hasHome = true;
		_homeX = getX();
		_homeY = getY();
		_homeZ = getZ();

		_log.finer(getObjectId()+": Home location set to X:" + _homeX + " Y:" + _homeY + " Z:"
				+ _homeZ);
	}

	/**
	 * This method forces guard to return to home location previously set
	 *
	 */
	private void returnHome()
	{
		_log.fine(getObjectId()+": moving home");
		clearAggroList();
		moveTo(_homeX, _homeY, _homeZ, 0);
	}
	
	/**
	 * Override L2Attackable method to mark PK's as valid targets.
	 * 
	 */
	public boolean getCondition2(L2PcInstance player)
	{
		return (!player.isDead() && (player.getKarma() >0) && (Math.abs(getZ()-player.getZ()) <= 100 ));
	}

	/**
	 * Override L2NpcInstance method to get correct html path
	 * 
	 */
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		} 
		else 
		{
			pom = npcId + "-" + val;
		}
		return "data/html/guard/" + pom + ".htm";
	}

	/**
	 * Custom onAction behaviour. Note that super() is not called because guards need
	 * extra check to see if a player should interract ot attack them when clicked.
	 * 
	 */
	public void onAction(L2PcInstance player)
	{
		if (getObjectId() != player.getTargetId())
		{
			player.setCurrentState(L2Character.STATE_IDLE);
			_log.fine(player.getObjectId()+": Targetted guard "+getObjectId());
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
			player.sendPacket(my);

			// correct location
			player.sendPacket(new SetToLocation(this));
		}
		else
		{
			if (containsTarget((L2Character) player)) {
				_log.fine(player.getObjectId()+": Attacked guard "+getObjectId());
				player.startAttack(this);
			} else {
				double distance = getDistance(player.getX(), player.getY());
				if (distance > INTERACTION_DISTANCE) {
					player.setCurrentState(L2Character.STATE_INTERACT);
					player.moveTo(this.getX(), this.getY(), this.getZ(), INTERACTION_DISTANCE);
				} else {
					showChatWindow(player, 0);
					player.sendPacket(new ActionFailed());					
					player.setCurrentState(L2Character.STATE_IDLE);
				}
			}
		}
	}
}