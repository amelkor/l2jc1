/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Attackable.java,v 1.24 2004/10/23 23:48:50 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 23:48:50 $
 * $Revision: 1.24 $
 * $Log: L2Attackable.java,v $
 * Revision 1.24  2004/10/23 23:48:50  l2chef
 * temporary fix for drops falling thru floor
 * target scan is done 20 times less to reduce cpu load
 *
 * Revision 1.23  2004/10/08 11:00:59  nuocnam
 * fixed exp attribution so you get correct XP when fighting stronger monsters
 *
 * Revision 1.22  2004/09/30 20:07:50  dethx
 * *** empty log message ***
 *
 * Revision 1.21  2004/09/27 08:44:53  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.20  2004/09/24 19:42:19  jeichhorn
 * fixed file comments
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.RatesController;
import net.sf.l2j.gameserver.serverpackets.DropItem;
import net.sf.l2j.gameserver.templates.L2Npc;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * This class ...
 * 
 * @version $Revision: 1.24 $ $Date: 2004/10/23 23:48:50 $
 */
public class L2Attackable extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2Attackable.class.getName());
	
	class AITask extends TimerTask
	{
		L2Character _instance;
		
		public AITask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				int x1 = getX() + getRnd().nextInt(500) -250;
				int y1 = getY() + getRnd().nextInt(500) -250;
				
				moveTo(x1, y1, getZ(), 0);
			}
			catch (Throwable e)
			{
				_log.warning(getObjectId()+": Problem occured in AiTask:"+e);
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	class AIAttackeTask extends TimerTask
	{
		L2Character _instance;
		
		public AIAttackeTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				if (!isInCombat())
				{
					_log.finer(getObjectId()+": monster knows "+getKnownPlayers().size() + " players");
					
					Set knownPlayers = getKnownPlayers();
					for (Iterator iter = knownPlayers.iterator(); iter.hasNext();)
					{
						L2PcInstance player = (L2PcInstance) iter.next();
						
						// dont do further checking if the player is dead or Z coordinate is very different
						// TODO find a good value for the max allowed Z distance
						if (getCondition2(player)) 
						{
							double distance = getDistance(player.getX(), player.getY());
							
							if((distance <= (getCollisionRadius()+200)))
							{
								_log.fine(getObjectId()+": Player "+player.getObjectId()+" in aggro range. Attacking!");
								
								if(_currentAiTask != null)
								{	
									_currentAiTask.cancel();
								}
								
								setTarget(player);
								startAttack(player);
							}
						}
					}				  					
				}
				else
				{
					if(_currentAIAttackeTask != null)
					{
						_currentAIAttackeTask.cancel();
					}
				}
			}
			catch (Throwable e)
			{
				_log.warning(getObjectId()+": Problem occured in AiAttackTask:"+e);
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	private int _moveRadius;
	private boolean _active;
	private AITask _currentAiTask;
	private AIAttackeTask _currentAIAttackeTask;
	
	private static Timer _aiTimer = new Timer(true);
	private static Timer _attackTimer = new Timer(true);
	
	private HashMap _aggroList = new HashMap();
	private L2Weapon _dummyWeapon;
	
	private boolean _sweepActive;
	private boolean _killedAlready = false;
	
	
	/**
	 * @param template
	 */
	public L2Attackable(L2Npc template)
	{
		super(template);
	}
	
	public boolean getCondition2(L2PcInstance player)
	{
		return false;
	}
	
	/**
	 * event that is called when the destination coordinates are reached
	 */
	public void onTargetReached()
	{	
		super.onTargetReached();
		
		switch (getCurrentState())
		{
		case STATE_PICKUP_ITEM:
			//					doPickupItem();
			break;
		case STATE_ATTACKING:
			startCombat();
			break;
		case STATE_RANDOM_WALK:
			randomWalk();
			break;
		default:
			break;
		}
	}
	
	
	private void randomWalk()
	{
		if (_active)
		{
			_log.fine(getObjectId()+": target reached ! new target calculated.");
			int wait = (10 + getRnd().nextInt(120))*1000;
			
			_currentAiTask = new AITask(this);
			_aiTimer.schedule(_currentAiTask, wait);
		}
		else
		{
			_log.fine(getObjectId()+": target reached ! noone around .. cancel movement.");
		}
	}
	
	protected void startRandomWalking()
	{
		_currentAiTask = new AITask(this);
		
		int wait = (10 + getRnd().nextInt(120))*1000;			
		_aiTimer.schedule(_currentAiTask, wait);
		setCurrentState(STATE_RANDOM_WALK);
	}
	
	protected synchronized void startTargetScan()
	{
		// only one scan task is allowed, and we dont start scanning if we already 
		if (_currentAIAttackeTask == null && getTarget() == null )
		{
			_currentAIAttackeTask = new AIAttackeTask(this);
			_attackTimer.scheduleAtFixedRate(_currentAIAttackeTask,100,1000);
		}
	}
	
	public void startAttack(L2Character target)
	{
		// one target is enough
		stopTargetScan();
		super.startAttack(target);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#setX(int)
	 */
	public void setX(int x)
	{
		super.setX(x);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#setY(int)
	 */
	public void setY(int y)
	{
		super.setY(y);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#setZ(int)
	 */
	public void setZ(int z)
	{
		super.setZ(z);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#removeKnownObject(net.sf.l2j.gameserver.model.L2Object)
	 */
	public void removeKnownObject(L2Object object)
	{
		super.removeKnownObject(object);
		
		// this monster forgets any agro when hostile leaves the area
		if (object instanceof L2Character)
		{
			_aggroList.remove((L2Character) object);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#reduceCurrentHp(int)
	 */
	public void reduceCurrentHp(int damage, L2Character attacker)
	{
		super.reduceCurrentHp(damage, attacker);
		// this is usually only called in combat
		
		calculateAggro(damage, attacker);
		
		if (!isDead() && attacker != null)
		{
			if (!isInCombat())
			{
				stopRandomWalking();
				
				startAttack(attacker);
			}
			else
			{
				_log.fine("already attacking");
			}
		}
		else if (isDead())
		{
			// killing is only possible one time
			synchronized(this)
			{
				if (!_killedAlready)
				{
					_killedAlready = true;
					stopRandomWalking();
					stopTargetScan();
					
					calculateRewards(attacker);
				}
			}
		}
	}
	
	
	/**
	 * @param attacker
	 */
	private void calculateRewards(L2Character lastAttacker)
	{
		Iterator it = _aggroList.keySet().iterator();
		int numberOfAttackers = _aggroList.size();
		int npcID = getNpcTemplate().getNpcId();
		int npcLvl = getLevel();
		
		
		while (it.hasNext())
		{
			
			L2Character attacker = (L2Character) it.next();
			Integer value = (Integer) _aggroList.get(attacker);
			L2Party attackerParty = null;
			if (attacker instanceof L2PcInstance)
			{
				L2PcInstance temp = (L2PcInstance)attacker;
				if (temp.isInParty()) attackerParty = temp.getParty();
			}
			
			//FIXME Pets arent party members yet - they are not considered as party members in l2 (nuocnam)
			if (npcID > 0 && value !=null && attackerParty == null)
			{
				int newXp = 0;
				int newSp = 0;
				int dmg = value.intValue();
				
				if (dmg > getMaxHp())
				{
					dmg = getMaxHp();
				}
				
				//mob = 24, atk = 10, diff = -14 (full xp)
				//mob = 24, atk = 28, diff = 4 (some xp)
				//mob = 24, atk = 50, diff = 26 (no xp)
				int diff = attacker.getLevel() - getLevel();
				
				// mob is 1 to 9 levels below attacker lvl
				if ((diff > 0) && (diff <= 9) && attacker.knownsObject(this))
				{
					newXp = getExpReward()*(dmg / getMaxHp());
					newSp = getSpReward()*(dmg / getMaxHp());
					newXp = (newXp - (int)(((double)diff/10) * newXp));
					newSp = (newSp - (int)(((double)diff/10) * newSp));
				}
				//attacker's level below or equal to mob lvl
				else if ((diff <= 0) && attacker.knownsObject(this)) 
				{
					newXp = getExpReward()*(dmg / getMaxHp());
					newSp = getSpReward()*(dmg / getMaxHp());
				}
				//mob is more than 9 levels below attacker lvl 
				else if ((diff > 9) && attacker.knownsObject(this))
				{
					newXp = 0;
					newSp = 0;
				}
				
				if (newXp <= 0)
				{
					newXp = 0;
					newSp = 0;
				}
				else if (newSp <= 0 && newXp > 0)
				{
					newSp = 0;
				}
				
				attacker.addExpAndSp(newXp, newSp);
				_aggroList.remove(attacker);
				it = _aggroList.keySet().iterator();
			}
			else
			{
				//share with party members
				int partyDmg = 0;
				
				ArrayList members = attackerParty.getPartyMembers();
				for(int i = 0; i < members.size(); i++) {
					L2PcInstance tmp = (L2PcInstance) members.get(i);
					if (_aggroList.containsKey(tmp)) {
						partyDmg += ((Integer) _aggroList.get(tmp)).intValue();
						_aggroList.remove(tmp);
					}
				}
				it = _aggroList.keySet().iterator(); //reset iterator so removed peeps are no longer in
				if (partyDmg > getMaxHp()) partyDmg = getMaxHp();
				
				attackerParty.distributeXpAndSp(partyDmg, getMaxHp(), getExpReward(), getSpReward());
			}
		}
		
		doItemDrop();
	}
	
	/**
	 * @param damage
	 * @param attacker
	 */
	private void calculateAggro(int damage, L2Character attacker)
	{
		int newAggro = damage;
		Integer aggroValue = (Integer) _aggroList.get(attacker);
		if (aggroValue != null)
		{
			// add the old value
			newAggro += aggroValue.intValue();
		}
		
		_aggroList.put(attacker, new Integer(newAggro));
		
		if (_aggroList.size() == 1)
		{
			setTarget(attacker);
		}
		else
		{
			// find highest aggrovalue to decide target should be here
			// now we just use the last attacker is the new target
			setTarget(attacker);
		}
	}
	
	/**
	 */
	public void doItemDrop()
	{
		// FIXME: SWEEP is not implemented... waiting for skills...
		
		List drops = getNpcTemplate().getDropData();
		
		// See how many items we need to drop for a given mobId
		
		_log.finer("this npc has "+drops.size()+" drops defined");
		
		for (Iterator iter = drops.iterator(); iter.hasNext();)
		{
			L2DropData drop = (L2DropData) iter.next();
			if (drop.isSweep())
			{
				// this is a sweep drop, it will not appear on the ground
				// so just skip it
				continue;
			}
			
			// Roll the dice.. between 0 and MAX_CHANCE
			int roll = L2NpcInstance.getRnd().nextInt(L2DropData.MAX_CHANCE);
			
			// compare roll with chance... "are we lucky?"
			if (roll < drop.getChance()*RatesController.getInstance().getDropRate())
			{
				L2ItemInstance dropit = ItemTable.getInstance().createItem(drop.getItemId());
				
				// Get min,max,sweep
				int min = drop.getMinDrop();
				int max = drop.getMaxDrop();
				
				int itemCount = 0;
				if (min < max)
				{
					// getRnd().nextInt(n) requires n > 0
					// Get RND between 0 and (max-min) then add min to get range between min and max...
					itemCount = L2NpcInstance.getRnd().nextInt(max - min) + min;
				}
				else
				{
					itemCount = 1;
				}
				
				if (dropit.getItemId() == 57) 
				{
					itemCount = itemCount * RatesController.getInstance().getAdenaRate();
				}
				
				if (itemCount != 0)	// only happens if min=0 and max=1... but still need to check!
				{
					dropit.setCount(itemCount);
					
					_log.fine("Item id to drop: "+drop.getItemId()+" amount: "+dropit.getCount());
					
					// position where mob was last
					dropit.setX(getX());
					dropit.setY(getY());
					dropit.setZ(getZ()+100);
					
					// otherwise you cannot pick it up
					dropit.setOnTheGround(true);
					DropItem dis = new DropItem(dropit, getObjectId());
					
					// also notify all other players about the drop
					L2Character[] players = broadcastPacket(dis);
					for (int k = 0; k < players.length; k++)
					{
						((L2PcInstance)players[k]).addKnownObjectWithoutCreate(dropit);
					}
					
					L2World.getInstance().addVisibleObject(dropit);
				}
				else
				{
					_log.warning("Roll produced 0 items to drop... Cancelling");
				}
			}
		}
	}
	
	/**
	 * 
	 */
	protected void stopRandomWalking()
	{
		// dont walk around any more
		if (_currentAiTask != null)
		{
			_currentAiTask.cancel();
			_currentAiTask = null;
		}
	}
	
	protected boolean isTargetScanActive()
	{
		return (_currentAIAttackeTask != null);		
	}
	
	protected synchronized void stopTargetScan()
	{
		if (_currentAIAttackeTask != null)
		{
			_currentAIAttackeTask.cancel();
			_currentAIAttackeTask = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#setCurrentHp(double)
	 */
	public void setCurrentHp(double currentHp)
	{
		super.setCurrentHp(currentHp);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeapon()
	 */
	public L2Weapon getActiveWeapon()
	{
		return _dummyWeapon;
	}
	
	/**
	 * this method has to be called AFTER setLevel() was executed
	 */
	public void setPhysicalAttack(int physicalAttack)
	{
		super.setPhysicalAttack(physicalAttack);
		
		// create a dummy weapon 
		_dummyWeapon = new L2Weapon();
		_dummyWeapon.setPDamage(physicalAttack);
		int randDmg = getLevel();
		_dummyWeapon.setRandomDamage(randDmg);
	}
	
	//some exports for guards
	public boolean noTarget()
	{
		return _aggroList.isEmpty();
	}
	
	public boolean containsTarget(L2Character player)
	{
		return _aggroList.containsKey(player);
	}
	
	public void clearAggroList()
	{
		_aggroList.clear();
	}
	
	public boolean isActive()
	{
		return _active;
	}
	
	public void setActive(boolean b)
	{
		_active = b;
	}
	
	public void setMoveRadius(int i)
	{
		_moveRadius = i;
	}
	
	public boolean isSweepActive()
	{
		return _sweepActive;
	}
	
	public void setSweepActive(boolean sweepActive)
	{
		_sweepActive = sweepActive;
	}
}