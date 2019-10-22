/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Spawn.java,v 1.8 2004/08/18 23:25:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/18 23:25:21 $
 * $Revision: 1.8 $
 * $Log: L2Spawn.java,v $
 * Revision 1.8  2004/08/18 23:25:21  l2chef
 * spawn bug fixed (L2Chef)
 *
 * Revision 1.7  2004/08/14 22:21:06  l2chef
 * unknown methods renamed
 *
 * Revision 1.6  2004/08/08 00:48:13  l2chef
 * npc datatable merged with npctable
 *
 * Revision 1.5  2004/08/02 22:33:54  l2chef
 * too many spawns should be prevented
 *
 * Revision 1.4  2004/07/28 23:57:37  l2chef
 * loglevel changed
 *
 * Revision 1.3  2004/07/23 22:52:50  l2chef
 * default animation speed fixed
 *
 * Revision 1.2  2004/07/23 21:28:29  l2chef
 * spawns use reflection only at init
 *
 * Revision 1.1  2004/07/23 01:50:32  l2chef
 * Spawn instance
 *
 * Revision 1.3  2004/07/13 22:51:12  l2chef
 * removed empty constructor
 *
 * Revision 1.2  2004/06/27 08:51:43  jeichhorn
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

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.IdFactory;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @author Nightmare
 * @version $Revision: 1.8 $ $Date: 2004/08/18 23:25:21 $
 */
public class L2Spawn
{
	private static Logger _log = Logger.getLogger(L2Spawn.class.getName());
	private L2Npc _template;
	
	private int _id; // just to find this in the spawn table
	private String _location;
	private int _maximumCount;
	private int _currentCount;
	private int _scheduledCount;
	private int _npcid;
	private int _locx;
	private int _locy;
	private int _locz;
	private int _randomx;
	private int _randomy;
	private int _heading;
	private int _respawnDelay;
	private static Timer _spawnTimer = new Timer(true);
	private Constructor _constructor;
	
	class SpawnTask extends TimerTask
	{
		L2NpcInstance _instance;
		int _objId;
		
		public SpawnTask(int objid)
		{
			_objId= objid;
		}
		
		public void run()
		{		
			try
			{
				doSpawn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			_scheduledCount--;
		}
	}

	public L2Spawn(L2Npc mobTemplate) throws SecurityException, ClassNotFoundException
	{
		 _template = mobTemplate;
		String implementationName = _template.getType(); // implementing class name
		_constructor = Class.forName("net.sf.l2j.gameserver.model." + implementationName + "Instance").getConstructors()[0];
	}

	/**
	 * @return Returns the amount.
	 */
	public int getAmount()
	{
		return _maximumCount;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return _id;
	}
	/**
	 * @return Returns the location.
	 */
	public String getLocation()
	{
		return _location;
	}
	/**
	 * @return Returns the locx.
	 */
	public int getLocx()
	{
		return _locx;
	}
	/**
	 * @return Returns the locy.
	 */
	public int getLocy()
	{
		return _locy;
	}
	/**
	 * @return Returns the locz.
	 */
	public int getLocz()
	{
		return _locz;
	}
	/**
	 * @return Returns the npcid.
	 */
	public int getNpcid()
	{
		return _npcid;
	}
	/**
	 * @return Returns the randomheading.
	 */
	public int getHeading()
	{
		return _heading;
	}
	/**
	 * @return Returns the randomx.
	 */
	public int getRandomx()
	{
		return _randomx;
	}
	/**
	 * @return Returns the randomy.
	 */
	public int getRandomy()
	{
		return _randomy;
	}
	/**
	 * @param amount
	 *            The amount to set.
	 */
	public void setAmount(int amount)
	{
		_maximumCount = amount;
	}
	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id)
	{
		_id = id;
	}
	/**
	 * @param location
	 *            The location to set.
	 */
	public void setLocation(String location)
	{
		_location = location;
	}
	/**
	 * @param locx
	 *            The locx to set.
	 */
	public void setLocx(int locx)
	{
		_locx = locx;
	}
	/**
	 * @param locy
	 *            The locy to set.
	 */
	public void setLocy(int locy)
	{
		_locy = locy;
	}
	/**
	 * @param locz
	 *            The locz to set.
	 */
	public void setLocz(int locz)
	{
		_locz = locz;
	}
	/**
	 * @param npcid
	 *            The npcid to set.
	 */
	public void setNpcid(int npcid)
	{
		_npcid = npcid;
	}
	/**
	 * @param heading
	 *            The randomheading to set.
	 */
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	/**
	 * @param randomx
	 *            The randomx to set.
	 */
	public void setRandomx(int randomx)
	{
		_randomx = randomx;
	}
	/**
	 * @param randomy
	 *            The randomy to set.
	 */
	public void setRandomy(int randomy)
	{
		_randomy = randomy;
	}
	
	public void decreaseCount(int npcId)
	{
		_currentCount--;
		// this is there just to prevent multiple respawning caused by lag
		if (_scheduledCount + _currentCount < _maximumCount)
		{
			_scheduledCount++;
			SpawnTask task = new SpawnTask(npcId);
			_spawnTimer.schedule(task, _respawnDelay);
		}
	}
	
	/**
	 * this method create the inital spawning  
	 *
	 * @return the number of creatures that were spawned
	 */
	public int init()
	{
		while (_currentCount < _maximumCount)
		{
			doSpawn();
		}
		
		return _currentCount;
	}
	
	public void doSpawn()
	{
		L2NpcInstance mob = null;		
	
		try
		{
			Object[] parameters = {_template};
			mob = (L2NpcInstance) _constructor.newInstance(parameters);
			mob.setObjectId(IdFactory.getInstance().getNextId());
			
			if (mob instanceof L2MonsterInstance)
			{
				mob.setAttackable(true);
			}
			else
			{
				mob.setAttackable(false);
			}
			
			if (getRandomx() > 0)
			{
				int random1 = L2MonsterInstance.getRnd().nextInt(getRandomx());
				int newlocx = getLocx() + L2MonsterInstance.getRnd().nextInt(getRandomx()) -random1;
				mob.setX(newlocx);
			} 
			else 
			{
				mob.setX(getLocx());
			}						
			
			if (getRandomy() > 0)
			{
				int random2 = L2MonsterInstance.getRnd().nextInt(getRandomy());
				int newlocy = getLocy() + L2MonsterInstance.getRnd().nextInt(getRandomy()) -random2;
				mob.setY(newlocy);
			} 
			else 
			{
				mob.setY(getLocy());
			}
			
			mob.setZ(getLocz());
			mob.setLevel(_template.getLevel());
			mob.setExpReward(_template.getExp());
			mob.setSpReward(_template.getSp());
			mob.setMaxHp(_template.getHp());
			mob.setCurrentHp(_template.getHp());
			mob.setWalkSpeed(_template.getWalkSpeed());
			mob.setRunSpeed(_template.getRunSpeed());
			mob.setPhysicalAttack(_template.getPatk());
			mob.setPhysicalDefense(_template.getPdef());
			mob.setMagicalAttack(_template.getMatk());
			mob.setMagicalDefense(_template.getMdef());
			mob.setMagicalSpeed(_template.getMatkspd());
			
			if (getHeading() == -1)
			{
				mob.setHeading(L2MonsterInstance.getRnd().nextInt(61794));	
			} 
			else 
			{
				mob.setHeading(getHeading());
			}
			
			mob.setMovementMultiplier(1.08);
			mob.setAttackSpeedMultiplier(0.983664);
			mob.setAttackRange(_template.getAttackRange());
			mob.setAggressive(_template.getAgro());
			
			mob.setRightHandItem(_template.getRhand());
			mob.setLeftHandItem(_template.getLhand());
			mob.setSpawn(this);
			
			L2World.getInstance().storeObject(mob);
			L2World.getInstance().addVisibleObject(mob);
			
			_log.finest("spawned Mob ID: "+_template.getNpcId()+" ,at: "+mob.getX()+" x, "+mob.getY()+" y, "+mob.getZ()+" z");
			_currentCount++;
		}
		catch (Exception e)
		{
			_log.warning("NPC class not found");
		}		
	}

	/**
	 * @param i delay in seconds
	 */
	public void setRespawnDelay(int i)
	{
		_respawnDelay = i * 1000;
	}
}