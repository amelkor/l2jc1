/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2PetInstance.java,v 1.15 2004/10/23 23:47:43 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 23:47:43 $
 * $Revision: 1.15 $
 * $Log: L2PetInstance.java,v $
 * Revision 1.15  2004/10/23 23:47:43  l2chef
 * temporary fix for drops falling thru floor
 *
 * Revision 1.14  2004/10/17 06:46:22  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.13  2004/10/05 19:37:32  whatev66
 * new function to deal with removal of known objects added over add remove known changed
 *
 * Revision 1.12  2004/09/20 12:31:45  dalrond
 * Pet status update and exp/level increase fixes
 *
 * Revision 1.11  2004/09/20 06:59:56  dalrond
 * Pet attacking fixed. Pets now add themselves to mobs known list and vice-versa when initiating an attack run.
 * The converse action is done when a pet arrives at a new target. Not ideal, but works for now.
 *
 * Revision 1.10  2004/09/20 00:13:43  dalrond
 * Pets now pickup items correctly (needs testing). Attacking still broken.
 *
 * Revision 1.9  2004/09/19 21:44:06  dalrond
 * Added some minimum stats to avoid 0 data errors and DeleteItem on pickup
 *
 * Revision 1.8  2004/09/16 23:28:23  dalrond
 * Pets now earn exp and level up (Still needs work. Exaggerated for testing).
 * Decay timer added for when pets die (not full 3 mins yet since no res spell)
 * Pets drop all items on the ground when they decay
 * Returned pets give all items to owner. If owner cant carry, then drop.
 * Control item is destroyed when pet dies.
 *
 * Revision 1.7  2004/08/14 22:20:37  l2chef
 * pet status update packet changed
 *
 * Revision 1.6  2004/08/13 23:57:54  l2chef
 * pet also uses check for multiple item pickups
 *
 * Revision 1.5  2004/08/12 23:58:10  l2chef
 * follow mode added (Deth)
 * owner lookup removed (L2Chef)
 *
 * Revision 1.4  2004/08/08 03:03:40  l2chef
 * added header
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

import java.io.IOException;
import java.util.Timer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ExperienceTable;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.ChangeMoveType;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.DeleteObject;
import net.sf.l2j.gameserver.serverpackets.DropItem;
import net.sf.l2j.gameserver.serverpackets.GetItem;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.serverpackets.PetDelete;
import net.sf.l2j.gameserver.serverpackets.PetInventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.PetItemList;
import net.sf.l2j.gameserver.serverpackets.PetStatusShow;
import net.sf.l2j.gameserver.serverpackets.PetStatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2Npc;
import net.sf.l2j.gameserver.templates.L2Weapon;



/**
 * 
 * This class ...
 * 
 * @version $Revision: 1.15 $ $Date: 2004/10/23 23:47:43 $
 */
public class L2PetInstance extends L2Character
{
	private static Logger _log = Logger.getLogger(L2PetInstance.class.getName());
	
	private byte _petId = 1;
	private int _exp = 0;
	private int _sp = 0;
	private int _pvpKills;
	private int _pkKills;
	private byte _pvpFlag;
	private int _maxFed=5; //TODO Food needs implementing
	private int _curFed=5;
	private L2PcInstance _owner;
	private int _karma = 0;
	private Inventory _inventory = new Inventory();
	private L2Weapon _dummyWeapon; 
	private L2Npc _template;
	private int _attackRange = 36; //Melee range
	private boolean _follow = true;
	private DecayTask _decayTask;
	private static Timer _decayTimer = new Timer(true);
	private int _controlItemId;
	private L2Character _lastTarget;
	private int _nextLevel;
	private int _lastLevel;
	private byte updateKnownCounter = 0;
	
	
	public L2PetInstance(L2Npc template)
	{
		setCollisionHeight(template.getHeight());
		setCollisionRadius(template.getRadius());
		setCurrentState(STATE_IDLE);
		setPhysicalAttack(9999);
		_template = template;
	}
	
	
	public void onAction(L2PcInstance player)
	{
		if (player == _owner)
		{
			player.sendPacket(new PetStatusShow(2));
			player.sendPacket(new PetStatusUpdate(this));
			player.sendPacket(new ActionFailed());
		}
		
		player.setCurrentState(L2Character.STATE_IDLE);
		_log.fine("new target selected:"+getObjectId());
		player.setTarget(this);
		MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
		player.sendPacket(my);
	}
	
	public void setSummonHp(int hp)
	{
		//FIXME Temp fix to avoid zero speed data errors
		if (hp == 0)
			hp = 5000;
		if (getMaxHp() < hp)
			super.setMaxHp(hp);
		super.setCurrentHp(hp);
	}
	
	public void setNextLevel(int level)
	{
		_nextLevel = level;
	}
	
	public int getNextLevel()
	{
		return _nextLevel;
	}
	
	public void setLastLevel(int level)
	{
		_lastLevel = level;
	}
	
	public int getLastLevel()
	{
		return _lastLevel;
	}
	
	public void setWalkSpeed(int walkSpeed)
	{
		//FIXME Temp fix to avoid zero speed data errors
		if (walkSpeed < 24)
			walkSpeed = 24;
		super.setWalkSpeed(walkSpeed);
	}
	
	public void setPhysicalDefense(int pdef)
	{
		//FIXME Temp fix to avoid zero speed data errors
		if (pdef < 100)
			pdef = 100;
		super.setPhysicalDefense(pdef);
	}
	
	public void setRunSpeed(int runSpeed)
	{
		//FIXME Temp fix to avoid zero speed data errors
		if (runSpeed < 125)
			runSpeed = 125;
		super.setRunSpeed(runSpeed);
	}
	
	public int getPetId()
	{
		return _petId;
	}
	public void setPetId(byte petid)
	{
		_petId = petid;
	}
	public int getControlItemId()
	{
		return _controlItemId;
	}
	public void setControlItemId(int controlItemId)
	{
		_controlItemId = controlItemId;
	}
	public int getKarma()
	{
		return _karma;
	}
	public void setKarma(int karma)
	{
		_karma = karma;
	}
	public int getMaxFed()
	{
		return _maxFed;
	}
	public void setMaxFed(int num)
	{
		_maxFed = num;
	}
	public int getCurrentFed()
	{
		return _curFed;
	}
	public void setCurrentFed(int num)
	{
		_curFed = num;
	}
	public void setOwner(L2PcInstance owner)
	{
		_owner = owner;
	}
	public L2Character getOwner()
	{
		return _owner;
	}
	public int getNpcId()
	{
		return _template.getNpcId();
	}
	public void setPvpFlag(byte pvpFlag)
	{
		_pvpFlag = pvpFlag;
	}
	public void setPkKills(int pkKills)
	{
		_pkKills = pkKills;
	}
	public int getPkKills()
	{
		return _pkKills;
	}
	public int getExp()
	{
		return _exp;
	}
	public void setExp(int exp)
	{
		_exp = exp;
	}
	public int getSp()
	{
		return _sp;
	}
	
	public void setSp(int sp)
	{
		_sp = sp;
	}
	public void addExpAndSp(int addToExp, int addToSp)
	{
		_log.fine("adding " + addToExp + " exp and " +addToSp+" sp to " + getName());
		_exp += addToExp;
		_sp += addToSp;
		
		PetStatusUpdate su = new PetStatusUpdate(this);
		_owner.sendPacket(su);
		
		while (_exp >= getNextLevel())
		{
			// Looks like the level should be increased.
			increaseLevel();
		}
	}
	
	
	public void increaseLevel()
	{
		//TODO: Need to put pet stats increase stuff here
		_log.finest("Increasing level of " + getName());
		setLastLevel(getNextLevel());
		setLevel(getLevel() + 1);
		setNextLevel(ExperienceTable.getInstance().getExp(getLevel() + 1));
		PetStatusUpdate ps = new PetStatusUpdate(this);
		_owner.sendPacket(ps);
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.LEVEL, getLevel());
		broadcastPacket(su);
		SocialAction sa = new SocialAction(getObjectId(), 15);
		broadcastPacket(sa);
		//TODO: Need the pet level message
		_owner.sendPacket(new SystemMessage(SystemMessage.YOU_INCREASED_YOUR_LEVEL));
	}
	
	public void followOwner(L2PcInstance owner)
	{
		setCurrentState(STATE_FOLLOW);
		setTarget(owner);
		moveTo(owner.getX(),owner.getY(),owner.getZ(), 30);
		broadcastPacket(new PetStatusUpdate(this));
	}
	public void onTargetReached()
	{	
		super.onTargetReached();
		updateKnownObjects();		
		try
		{
			switch (getCurrentState())
			{
			case STATE_PICKUP_ITEM:
				doPickupItem();
				break;
			case STATE_ATTACKING:
				startCombat();
				break;
			default:
				break;
			}
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
	}
	
	public L2Weapon getActiveWeapon()
	{
		return _dummyWeapon;
	}
	public void setPhysicalAttack(int physicalAttack)
	{
		
		//FIXME Temp fix to avoid zero speed data errors
		if (physicalAttack < 100)
			physicalAttack = 100;
		super.setPhysicalAttack(physicalAttack);
		
		// create a dummy weapon 
		_dummyWeapon = new L2Weapon();
		_dummyWeapon.setPDamage(physicalAttack);
		int randDmg = getLevel();
		_dummyWeapon.setRandomDamage(getLevel());
		
	}
	public Inventory getInventory()
	{
		return _inventory;
	}
	private void doPickupItem() throws IOException
	{
		StopMove sm = new StopMove(getObjectId(), getX(), getY(), getZ(), getHeading()); 
		_log.fine("Pet pickup pos: "+ getTarget().getX() + " "+getTarget().getY()+ " "+getTarget().getZ() );
		broadcastPacket(sm);
		
		if (!(getTarget() instanceof L2ItemInstance))
		{
			// dont try to pickup anything that is not an item :)
			_log.warning("trying to pickup wrong target."+getTarget());
			_owner.sendPacket(new ActionFailed());
			return;
		}
		
		L2ItemInstance target = (L2ItemInstance) getTarget();
		
		boolean pickupOk = false;
		
		synchronized (target)
		{
			if (target.isOnTheGround())
			{
				pickupOk = true;
				target.setOnTheGround(false);
			}
		}
		
		if (!pickupOk)
		{
			// someone else was faster
			_owner.sendPacket(new ActionFailed());
			setCurrentState(STATE_IDLE);
			return;
		}
		
		GetItem gi = new GetItem(target, getObjectId());
		broadcastPacket(gi);
		L2World.getInstance().removeVisibleObject(target);
		DeleteObject del = new DeleteObject(target);
		broadcastPacket(del);
		L2ItemInstance target2 = getInventory().addItem(target);
		//FIXME Just send the updates if possible (old way wasn't working though)
		PetItemList iu = new PetItemList(this);
		_owner.sendPacket(iu);
		
		setCurrentState(STATE_IDLE);
		if (getFollowStatus() == true)
		{
			followOwner(_owner);
		}
	}
	public void reduceCurrentHp(int damage, L2Character attacker)
	{
		super.reduceCurrentHp(damage, attacker);
		// this is usually only called in combat
		
		
		if (!isDead() && attacker != null)
		{
			if (!isInCombat())
			{
				startAttack(attacker);
			}
			else
			{
				_log.fine("already attacking");
			}
		}
		
		if (isDead())
		{
			synchronized(this)
			{
				if (_decayTask == null)
				{
					// you have 3 minutes to resurrect the pet before it disappears
					// timer should be set to 180000 but this is pointless
					// until we have resurrection so set to 10000 for now.
					_decayTask = new DecayTask(this);
					_decayTimer.schedule(_decayTask, 10000);
				}
			}
		}
	}
	
	public void onDecay()
	{
		deleteMe(_owner);
	}
	
	public void giveAllToOwner()
	{
		try
		{
			Inventory petInventory = getInventory();
			L2ItemInstance[] items = petInventory.getItems();
			for (int i = 0; (i < items.length); i++)
			{ 
				L2ItemInstance giveit = items[i];
				if (((giveit.getItem().getWeight() * giveit.getCount())
						+ _owner.getInventory().getTotalWeight()) 
						< _owner.getMaxLoad())
				{
					// If the owner can carry it give it to them
					giveItemToOwner(giveit);
				}
				else 
				{
					// If they can't carry it, chuck it on the floor :)
					dropItemHere(giveit);
				}
			}
		}
		catch(Exception e)
		{
			_log.fine("Give all items error " + e);
		}
	}
	
	public void giveItemToOwner(L2ItemInstance item)
	{
		try
		{
			_owner.getInventory().addItem(item);
			getInventory().dropItem(item, item.getCount());	
			PetInventoryUpdate petiu = new PetInventoryUpdate();
			ItemList PlayerUI = new ItemList(_owner, false);
			petiu.addRemovedItem(item);
			_owner.sendPacket(petiu);
			_owner.sendPacket(PlayerUI);	
		}
		catch (Exception e){_log.fine("Error while giving item to owner: " + e);}
	}
	
	public void broadcastStatusUpdate()
	{
		super.broadcastStatusUpdate();
		if (getOwner() != null)
		{
			getOwner().sendPacket(new PetStatusUpdate(this));
		}
	}
	public void deleteMe(L2PcInstance owner)
	{
		unSummon(owner);
		destroyControlItem(owner);
		owner.sendPacket(new PetDelete(getObjectId(), 2));
	}
	
	public void unSummon (L2PcInstance owner)
	{
		giveAllToOwner();
		L2World.getInstance().removeVisibleObject(this);
		removeAllKnownObjects();
		owner.setPet(null);
		setTarget(null);
	}
	
	public void destroyControlItem(L2PcInstance owner)
	{
		try 
		{
			L2ItemInstance removedItem = owner.getInventory().destroyItem(getControlItemId(), 1);
			
			InventoryUpdate iu = new InventoryUpdate();
			iu.addRemovedItem(removedItem);
			
			//client.getConnection().sendPacket(iu);
			owner.sendPacket(iu);
			
			StatusUpdate su = new StatusUpdate(owner.getObjectId());
			su.addAttribute(StatusUpdate.CUR_LOAD, owner.getCurrentLoad());
			owner.sendPacket(su);
			
			UserInfo ui = new UserInfo(owner);
			owner.sendPacket(ui);
			CharInfo info = new CharInfo(owner);
			owner.broadcastPacket(info);
			
			L2World world = L2World.getInstance();
			world.removeObject(removedItem);
		}
		catch (Exception e){
			_log.fine("Error while destroying control item: " + e);
		}
	}
	
	public void dropAllItems()
	{
		try
		{
			L2ItemInstance[] items = getInventory().getItems();
			for (int i = 0; (i < items.length); i++)
			{ 
				dropItemHere(items[i]);
			}
		}
		catch(Exception e)
		{
			_log.fine("Pet Drop Error: " + e);
		}
	}
	
	public void dropItemHere(L2ItemInstance dropit)
	{
		dropit = getInventory().dropItem(dropit.getObjectId(), dropit.getCount());
		
		if (dropit != null)
		{
			_log.fine("Item id to drop: "+dropit.getItemId()+" amount: "+dropit.getCount());
			
			// position where pet was last
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
	}
	
	
	public void startAttack(L2Character target)
	{  
		if (!knownsObject(target))
		{
			target.addKnownObject(this);
			this.addKnownObject(target);
		}
		
		if (!isRunning())
		{
			setRunning(true);
			ChangeMoveType move = new ChangeMoveType(this, ChangeMoveType.RUN);
			broadcastPacket(move);
		}
		
		super.startAttack(target);
	}
	public int getAttackRange()
	{
		return _attackRange; 
	}
	
	public void setAttackRange(int range)
	{
		if (range < 36)
			range = 36;
		_attackRange = range;
	}
	
	public void setFollowStatus(boolean state)
	{
		_follow = state;
	}
	
	public boolean getFollowStatus()
	{
		return _follow;
	}
	
	public void updateKnownObjects()
	{
		updateKnownCounter += 1;
		if (updateKnownCounter >3)
		{
			if(getKnownObjects().size() !=0)
			{
				L2Object[] knownobjects = (L2Object[]) _knownObjects.toArray(new L2Object[_knownObjects.size()]);
				for (int x=0; x< knownobjects.length; x++)
				{
					if (getDistance(knownobjects[x].getX(),knownobjects[x].getY()) > 4000)
					{
						if (knownobjects[x] instanceof L2MonsterInstance)
						{
							removeKnownObject(knownobjects[x]);
							((L2MonsterInstance)knownobjects[x]).removeKnownObject(this);
							
						}
						else
						{
							removeKnownObject(knownobjects[x]);	
							knownobjects[x].removeKnownObject(this);
						}
					}
				}   
			}
			updateKnownCounter = 0;
		}
	}
}

