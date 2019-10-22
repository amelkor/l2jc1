/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Character.java,v 1.48 2004/10/21 08:57:02 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/21 08:57:02 $
 * $Revision: 1.48 $
 * $Log: L2Character.java,v $
 * Revision 1.48  2004/10/21 08:57:02  nuocnam
 * distance > attackRange is no longer checked in onTargetReached when character si not in combat (dragon666, nuocnam)
 *
 * Revision 1.47  2004/10/21 00:01:22  nuocnam
 * Target being attacked is stored internally, so using setTarget(null) (notably after pressing esc key) won't cease attack. This also prevents few minor exploits while changing targets quick during combat.
 *
 * Revision 1.46  2004/10/20 16:59:41  whatev66
 * interact state now used when talking to npcs which are not attackable
 *
 * Revision 1.45  2004/10/08 19:05:10  whatev66
 * fixed attacktimer bug that stoped attacking. Timers execution is not accurate so don't depend on them to execute tasks in the correct order relative to other timers.bow don't use that timer anymore no need.
 *
 * Revision 1.44  2004/10/05 19:46:42  whatev66
 * fixed null pointer exception when dead and restarting
 *
 * Revision 1.43  2004/10/03 16:57:35  whatev66
 * fixed movement somewhat using timer based system now.
 *
 * Revision 1.42  2004/09/27 14:13:47  whatev66
 * changed states from in to byte
 *
 * Revision 1.41  2004/09/21 18:36:56  dethx
 * fixed some skills related bugs
 *
 * Revision 1.40  2004/09/20 12:30:16  dalrond
 * Added a dummy addExpAndSp so we don't need to check character is a PcInstance or PetInstance every time we call it
 *
 * Revision 1.39  2004/09/19 00:30:09  whatev66
 * *** empty log message ***
 *
 * Revision 1.38  2004/09/17 22:36:25  whatev66
 * regen time and rate changed
 *
 * * Revision 1.37  2004/09/16 23:05:01  dalrond
 * Moved DecayTask to here from L2NpcInstance because it applies to pets
 *
 * Revision 1.36  2004/09/15 23:56:34  l2chef
 * attack fixes (Deth)
 *
 * Revision 1.35  2004/08/18 00:51:04  l2chef
 * attack timer is checked (Deth)
 * bow handling added (Deth/L2Chef)
 *
 * Revision 1.34  2004/08/15 22:32:23  l2chef
 * speed is calculated correct
 *
 * Revision 1.33  2004/08/15 13:08:19  l2chef
 * current movetask is nulled when task completed
 *
 * Revision 1.32  2004/08/15 03:38:13  l2chef
 * attack canceld if no target selected
 *
 * Revision 1.31  2004/08/14 22:22:52  l2chef
 * unknown methods renamed
 * fixes to attack (Dalrond)
 *
 * Revision 1.30  2004/08/14 16:20:12  l2chef
 * wrong target types are ignored
 *
 * Revision 1.29  2004/08/13 23:58:30  l2chef
 * extended logging to find bug
 *
 * Revision 1.28  2004/08/12 23:51:12  l2chef
 * all times have exception stacktraces printed to logfile
 *
 * Revision 1.27  2004/08/04 20:43:46  l2chef
 * closest town calculation moved (NuocNam)
 *
 * Revision 1.26  2004/08/02 22:34:26  l2chef
 * divide by zero prevented when pdef of npc2.csv is zero
 *
 * Revision 1.25  2004/08/02 00:07:35  l2chef
 * better solution to drop item problem
 * moveto uses the original z coord
 *
 * Revision 1.24  2004/08/01 12:17:18  l2chef
 * teleport array fixed (NuocNam)
 *
 * Revision 1.23  2004/07/30 22:28:29  l2chef
 * teleport destinations bases on maptile (NuocNam)
 *
 * Revision 1.22  2004/07/30 10:24:30  l2chef
 * no regeneration when dead, z coordinate is updated to requested z when target is reached.
 *
 * Revision 1.21  2004/07/29 23:36:58  l2chef
 * invalid speed fix (whatev)
 *
 * Revision 1.20  2004/07/29 20:08:20  l2chef
 * damage calculation considers Pdef (NuocNam)
 *
 * Revision 1.19  2004/07/29 00:00:04  l2chef
 * knowPlayers is now a Set to prevent duplicates
 *
 * Revision 1.18  2004/07/25 22:59:09  l2chef
 * pet system started (whatev)
 *
 * Revision 1.17  2004/07/25 00:38:09  l2chef
 * moving objects appear now as moving when they get into viewrange (NuocNam)
 *
 * Revision 1.16  2004/07/23 22:51:33  l2chef
 * multiple attacks at the same time are now prevented
 *
 * Revision 1.15  2004/07/23 01:46:55  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 * knownobject handling is moved to L2Object
 *
 * Revision 1.14  2004/07/19 23:15:37  l2chef
 * removed comment
 *
 * Revision 1.13  2004/07/19 02:05:57  l2chef
 * decay timer moved to npcinstance
 * soulshot code added
 * movement calculation enhanced
 *
 * Revision 1.12  2004/07/18 17:37:38  l2chef
 * removeAllKnownObjects will cause a notify to all object to remove this instance
 * hp/mp regeneration is now triggered differently.
 *
 * Revision 1.11  2004/07/17 23:22:44  l2chef
 * moveTo with offset is working correct now.
 * all timer tasks should be crash safe.
 * attacker is not warped to target on ranged attacks any more.
 *
 * Revision 1.10  2004/07/14 22:09:57  l2chef
 * enabled move state again
 *
 * Revision 1.9  2004/07/12 20:56:00  l2chef
 * fixed a int overflow problem
 * fixed disapprear bug. destination was not set when too close. so the mob ended up at 0,0,0
 *
 * Revision 1.8  2004/07/11 23:02:21  l2chef
 * temporary nearest town calucation added  (MetalRabbit)
 * damage calculation returned to old one
 *
 * Revision 1.7  2004/07/11 11:46:48  l2chef
 * damage calculation by MetalRabbit. not final
 *
 * Revision 1.6  2004/07/04 17:43:35  l2chef
 * level up animation is shown
 *
 * Revision 1.5  2004/07/04 14:35:07  jeichhorn
 * added methods to increase and decrease the level
 *
 * Revision 1.4  2004/07/04 11:13:27  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:32  l2chef
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
package net.sf.l2j.gameserver.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.Attack;
import net.sf.l2j.gameserver.serverpackets.AutoAttackStart;
import net.sf.l2j.gameserver.serverpackets.AutoAttackStop;
import net.sf.l2j.gameserver.serverpackets.CharMoveToLocation;
import net.sf.l2j.gameserver.serverpackets.Die;
import net.sf.l2j.gameserver.serverpackets.FinishRotation;
import net.sf.l2j.gameserver.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;
import net.sf.l2j.gameserver.serverpackets.SetToLocation;
import net.sf.l2j.gameserver.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * This class ...
 * 
 * @version $Revision: 1.48 $ $Date: 2004/10/21 08:57:02 $
 */
public abstract class L2Character extends L2Object
{	
	private static final Logger _log = Logger.getLogger(L2Character.class.getName());
	
	class ArriveTask extends TimerTask
	{
		L2Character _instance;
		
		public ArriveTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				_instance.onTargetReached();
			}
			catch (Throwable e)
			{
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
			
			_currentMoveTask = null;
		}
	}
	
	class AttackTask extends TimerTask
	{
		L2Character _instance;
		
		public AttackTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				_instance.onAttackTimer();
			}
			catch (Throwable e)
			{
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	class HitTask extends TimerTask
	{
		L2Character _instance;
		L2Character _target;
		int _damage;
		boolean _crit;
		boolean _miss;
		boolean _soulshot;
		
		public HitTask(L2Character instance, L2Character target, int damage, boolean crit, boolean miss, boolean soulshot)
		{
			_instance = instance;
			_target = target;
			_damage = damage;
			_crit = crit;
			_miss = miss;
			_soulshot = soulshot;
		}
		
		public void run()
		{
			try
			{
				_instance.onHitTimer(_target, _damage, _crit, _miss, _soulshot);
			}
			catch (Throwable e)
			{
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	class HpRegenTask extends TimerTask
	{
		L2Character _instance;
		
		
		public HpRegenTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				synchronized(_hpLock)
				{
					double nowHp = _instance.getCurrentHp();
					if (_instance.getCurrentHp() < _instance.getMaxHp())
					{
						nowHp += _instance.getMaxHp() * 0.018;//TODO:bonus variable + this rate should be added for skills
						if (nowHp > _instance.getMaxHp())
						{
							nowHp = _instance.getMaxHp();
						}
						
						_instance.setCurrentHp(nowHp);
					}
				}
			}
			catch (Throwable e)
			{
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	class MpRegenTask extends TimerTask
	{
		L2Character _instance;
		
		
		public MpRegenTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				synchronized(_mpLock)
				{
					double nowMp = _instance.getCurrentMp();
					
					if (_instance.getCurrentMp() < _instance.getMaxMp())
					{
						nowMp += _instance.getMaxMp() * 0.014;//TODO:bonus variable + this rate should be added for skills
						_instance.setCurrentMp(nowMp);
					}
				}
			}
			catch (Throwable e)
			{
				StringWriter pw = new StringWriter();
				PrintWriter prw = new PrintWriter(pw);
				e.printStackTrace(prw);
				_log.severe(pw.toString());
			}
		}
	}
	
	
	public long serialVersionUID = 0x12341234; 
	// this array contains all clients that need to be notified about hp/mp updates
	private ArrayList _statusListener = new ArrayList();
	
	private static Timer _attackTimer = new Timer(true);
	private AttackTask _currentAttackTask;
	
	private static Timer _hitTimer = new Timer(true);
	private HitTask _currentHitTask;
	
	private static Timer _regenTimer = new Timer(true);
	private MpRegenTask _mpRegTask;
	private Object _mpLock = new Object();
	private boolean _mpRegenActive;
	private HpRegenTask _hpRegTask = new HpRegenTask(this);
	private Object _hpLock = new Object();
	private boolean _hpRegenActive;
	
	private static Timer _bowAttack = new Timer(true);
	
	// this is needed for movement target updates
	private int _moveOffset;
	
	
	private float _effectiveSpeed;
	private float _dx;
	private float _dy;
	private float _dz;
	private long _moveStartTime;
	private double _xAddition;
	private double _yAddition;
	private long _timeToTarget;
	private static Timer _moveTimer = new Timer(true);
	private ArriveTask _currentMoveTask;
	
	private static Random _rnd = new Random();
	
	private String _name;
	private int _level = 1;
	private int _maxHp;
	private double _currentHp;
	private int _maxMp;
	private double _currentMp;
	private int _accuracy;
	private int _criticalHit;
	private int _evasionRate;
	private int _magicalAttack;
	private int _magicalDefense;
	private int _magicalSpeed;
	private int _physicalAttack;
	private int _physicalDefense;
	private int _physicalSpeed;
	private int _runSpeed;
	private int _walkSpeed;
	private boolean _running;
	private int _flyingRunSpeed;
	private int _floatingWalkSpeed;
	private int _flyingWalkSpeed;
	private int _floatingRunSpeed;
	
	private int _int;
	private int _str;
	private int _con;
	private int _dex;
	private int _men;
	private int _wit;
	
	
	private int _face;
	private int _hairStyle;
	private int _hairColor;
	
	private int _sex;
	private int _heading;
	
	private int _xDestination;
	private int _yDestination;
	private int _zDestination;
	
	private double _movementMultiplier;  
	private double _attackSpeedMultiplier;  
	private double _collisionRadius;   
	private double _collisionHeight; // this is  positioning the model relative to the ground 
	
	
	private L2Object _target;
	private int _activeSoulShotGrade;
	
	
	private byte _currentState = STATE_IDLE;
	
	public static final byte STATE_IDLE = 0;
	public static final byte STATE_PICKUP_ITEM = 1;
	public static final byte STATE_CASTING = 2;
	public static final byte STATE_RESTING = 3;
	//	public static final byte STATE_MOVING = 4;
	public static final byte STATE_ATTACKING = 5;
	public static final byte STATE_RANDOM_WALK = 6;
	public static final byte STATE_INTERACT = 7;
	public static final byte STATE_FOLLOW = 8;
	
	
	private boolean _inCombat;
	private boolean _moving;
	private boolean _movingToPawn;
	
	private int _pawnOffset;
	private L2Character _pawnTarget;
	
	private boolean _2ndHit = false;
	private boolean _currentlyAttacking = false;
	
	private L2Object _attackTarget;
	
	public boolean knownsObject(L2Object object)
	{
		return _knownObjects.contains(object);
	}
	
	/**
	 * 
	 */
	public void onDecay()
	{
		L2World.getInstance().removeVisibleObject(this);
	}
	
	class DecayTask extends TimerTask
	{
		L2Character _instance;
		
		public DecayTask(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			_instance.onDecay();
		}
	}
	
	public void addStatusListener(L2Character object)
	{
		_statusListener.add(object);
	}
	
	private ArrayList getStatusListener()
	{
		return _statusListener;
	}
	
	public void removeStatusListener(L2Character object)
	{
		_statusListener.remove(object);
	}
	
	/**
	 * @return
	 */
	public int getHeading()
	{
		return _heading;
	}
	
	/**
	 * @param heading
	 */
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	
	/**
	 * @return
	 */
	public int getXdestination()
	{
		return _xDestination;
	}
	
	/**
	 * @param x1
	 */
	public void setXdestination(int x1)
	{
		_xDestination = x1;
	}
	
	/**
	 * @return
	 */
	public int getYdestination()
	{
		return _yDestination;
	}
	
	/**
	 * @param y1
	 */
	public void setYdestination(int y1)
	{
		_yDestination = y1;
	}
	
	/**
	 * @return
	 */
	public int getZdestination()
	{
		return _zDestination;
	}
	
	/**
	 * @param z1
	 */
	public void setZdestination(int z1)
	{
		_zDestination = z1;
	}
	
	
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#getX()
	 */
	public int getX()
	{
		if (!isMoving())
		{	
			return super.getX();
		}
		else
		{
			long elapsed = System.currentTimeMillis() - _moveStartTime;
			int diff = (int) (elapsed * _xAddition);
			int remain = Math.abs(getXdestination()-super.getX()) - Math.abs(diff);
			if ( remain > 0)
			{	
				return super.getX() + diff;
			}
			else
			{
				return getXdestination();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#getY()
	 */
	public int getY()
	{
		if (!isMoving())
		{	
			return super.getY();
		}
		else
		{
			long elapsed = System.currentTimeMillis() - _moveStartTime;
			int diff = (int) (elapsed * _yAddition);
			int remain = Math.abs(getYdestination()-super.getY()) - Math.abs(diff);
			if ( remain > 0)
			{	
				return super.getY() + diff;
			}
			else
			{
				return getYdestination();
			}
		}
	}
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#getZ()
	 */
	public int getZ()
	{
		if (!isMoving())
		{	
			return super.getZ();
		}
		else
		{
			return super.getZ();
		}
	}
	
	/**
	 * @return
	 */
	public boolean isMoving()
	{
		return _moving; 
	}
	
	public void stopMove()
	{
		if (_currentMoveTask != null)
		{
			_currentMoveTask.cancel();
			_currentMoveTask = null;
		}
		
		// copy calculated position to current position
		setX(getX());
		setY(getY());
		setZ(getZ()); // TODO this the initially requested z coord, it has to be replaced with the real Z 
		setIsMoving(false);
	}
	
	/**
	 * @return
	 */
	public int getCon()
	{
		return _con;
	}
	
	/**
	 * @param con
	 */
	public void setCon(int con)
	{
		_con = con;
	}
	
	/**
	 * @return
	 */
	public int getDex()
	{
		return _dex;
	}
	
	/**
	 * @param dex
	 */
	public void setDex(int dex)
	{
		_dex = dex;
	}
	
	/**
	 * @return
	 */
	public int getInt()
	{
		return _int;
	}
	
	/**
	 * @param int1
	 */
	public void setInt(int int1)
	{
		_int = int1;
	}
	
	/**
	 * @return
	 */
	public int getMen()
	{
		return _men;
	}
	
	/**
	 * @param men
	 */
	public void setMen(int men)
	{
		_men = men;
	}
	
	/**
	 * @return
	 */
	public int getStr()
	{
		return _str;
	}
	
	/**
	 * @param str
	 */
	public void setStr(int str)
	{
		_str = str;
	}
	
	/**
	 * @return
	 */
	public int getWit()
	{
		return _wit;
	}
	
	/**
	 * @param wit
	 */
	public void setWit(int wit)
	{
		_wit = wit;
	}
	
	/**
	 * @return
	 */
	public double getCurrentHp()
	{
		return _currentHp;
	}
	
	/**
	 * @param currentHp
	 */
	public void setCurrentHp(double currentHp)
	{
		_currentHp = currentHp;
		if (_currentHp >= getMaxHp())
		{
			stopHpRegeneration();
			
			_currentHp = getMaxHp();
		}
		else if (!_hpRegenActive && !isDead())
		{
			startHpRegeneration();
		}
		
		broadcastStatusUpdate();
	}
	
	/**
	 * 
	 */
	public void stopHpRegeneration()
	{
		if (_hpRegenActive)
		{
			_hpRegTask.cancel();
			_hpRegTask = null;
			_hpRegenActive = false;
			_log.fine("HP regen stop");
		}
	}
	
	/**
	 * 
	 */
	private void startHpRegeneration()
	{
		_log.fine("HP regen started");
		_hpRegTask = new HpRegenTask(this);
		_regenTimer.scheduleAtFixedRate(_hpRegTask, 3000, 3000);
		_hpRegenActive = true;
	}
	
	/**
	 * @return
	 */
	public double getCurrentMp()
	{
		return _currentMp;
	}
	
	/**
	 * @param currentMp
	 */
	public void setCurrentMp(double currentMp)
	{
		_currentMp = currentMp;
		
		if (_currentMp >= getMaxMp())
		{
			stopMpRegeneration();
			
			_currentMp = getMaxMp();
		}
		else if (!_mpRegenActive && !isDead())
		{
			startMpRegeneration();
		}
		
		broadcastStatusUpdate();
	}
	
	/**
	 * 
	 */
	private void startMpRegeneration()
	{
		_mpRegTask = new MpRegenTask(this);
		_log.fine("MP regen started");
		
		_regenTimer.scheduleAtFixedRate(_mpRegTask, 3000, 3000);
		_mpRegenActive = true;
	}
	
	/**
	 * 
	 */
	public void stopMpRegeneration()
	{
		if (_mpRegenActive)
		{
			_mpRegTask.cancel();
			_mpRegTask = null;
			_log.fine("Mp regen stopped");
			_mpRegenActive = false;
		}
	}
	
	public void broadcastStatusUpdate()
	{
		ArrayList list = getStatusListener();
		if (list.isEmpty())
		{
			return;
		}
		
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.CUR_HP,(int)getCurrentHp());
		su.addAttribute(StatusUpdate.CUR_MP,(int)getCurrentMp());
		
		for (int i=0; i<list.size(); i++)
		{
			L2Character temp = (L2Character) list.get(i);
			if (temp instanceof L2PcInstance)
			{
				L2PcInstance player = (L2PcInstance) temp;
				try
				{
					player.
					sendPacket(su);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * @return
	 */
	public int getMaxHp()
	{
		return _maxHp;
	}
	
	/**
	 * @param maxHp
	 */
	public void setMaxHp(int maxHp)
	{
		_maxHp = maxHp;
	}
	
	/**
	 * @return
	 */
	public int getMaxMp()
	{
		return _maxMp;
	}
	
	/**
	 * @param maxMp
	 */
	public void setMaxMp(int maxMp)
	{
		_maxMp = maxMp;
	}
	
	/**
	 * @return
	 */
	public int getAccuracy()
	{
		return _accuracy;
	}
	
	/**
	 * @param accuracy
	 */
	public void setAccuracy(int accuracy)
	{
		_accuracy = accuracy;
	}
	
	/**
	 * @return
	 */
	public int getCriticalHit()
	{
		return _criticalHit;
	}
	
	/**
	 * @param criticalHit
	 */
	public void setCriticalHit(int criticalHit)
	{
		_criticalHit = criticalHit;
	}
	
	/**
	 * @return
	 */
	public int getEvasionRate()
	{
		return _evasionRate;
	}
	
	/**
	 * @param evasionRate
	 */
	public void setEvasionRate(int evasionRate)
	{
		_evasionRate = evasionRate;
	}
	
	/**
	 * @return
	 */
	public int getFace()
	{
		return _face;
	}
	
	/**
	 * @param face
	 */
	public void setFace(int face)
	{
		_face = face;
	}
	
	/**
	 * @return
	 */
	public int getHairColor()
	{
		return _hairColor;
	}
	
	/**
	 * @param hairColor
	 */
	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}
	
	/**
	 * @return
	 */
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	/**
	 * @param hairStyle
	 */
	public void setHairStyle(int hairStyle)
	{
		_hairStyle = hairStyle;
	}
	
	/**
	 * @return
	 */
	public int getLevel()
	{
		return _level;
	}
	
	public void increaseLevel()
	{
		_log.finest("increasing level of " + getName());
		_level++;
		
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.LEVEL, _level);
		sendPacket(su);
		
		sendPacket(new SystemMessage(SystemMessage.YOU_INCREASED_YOUR_LEVEL));
		
		SocialAction sa = new SocialAction(getObjectId(), 15);
		broadcastPacket(sa);
		sendPacket(sa);
	}
	
	public void decreaseLevel()
	{
		_log.finest("increasing level of " + getName());
		_level--;
		
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.LEVEL, _level);
		sendPacket(su);
	}
	
	/**
	 * @param level
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	
	/**
	 * @return
	 */
	public int getMagicalAttack()
	{
		return _magicalAttack;
	}
	
	/**
	 * @param magicalAttack
	 */
	public void setMagicalAttack(int magicalAttack)
	{
		_magicalAttack = magicalAttack;
	}
	
	/**
	 * @return
	 */
	public int getMagicalDefense()
	{
		return _magicalDefense;
	}
	
	/**
	 * @param magicalDefense
	 */
	public void setMagicalDefense(int magicalDefense)
	{
		_magicalDefense = magicalDefense;
	}
	
	/**
	 * @return
	 */
	public int getMagicalSpeed()
	{
		return _magicalSpeed;
	}
	
	/**
	 * @param magicalSpeed
	 */
	public void setMagicalSpeed(int magicalSpeed)
	{
		_magicalSpeed = magicalSpeed;
	}
	
	/**
	 * @return
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * @return
	 */
	public int getPhysicalAttack()
	{
		return _physicalAttack;
	}
	
	/**
	 * @param physicalAttack
	 */
	public void setPhysicalAttack(int physicalAttack)
	{
		_physicalAttack = physicalAttack;
	}
	
	/**
	 * @return
	 */
	public int getPhysicalDefense()
	{
		return _physicalDefense;
	}
	
	/**
	 * @param physicalDefense
	 */
	public void setPhysicalDefense(int physicalDefense)
	{
		_physicalDefense = physicalDefense;
	}
	
	/**
	 * @return
	 */
	public int getPhysicalSpeed()
	{
		return _physicalSpeed;
	}
	
	/**
	 * @param physicalSpeed
	 */
	public void setPhysicalSpeed(int physicalSpeed)
	{
		_physicalSpeed = physicalSpeed;
	}
	
	public boolean isMale()
	{
		return _sex==0;
	}
	
	/**
	 * @return
	 */
	public int getSex()
	{
		return _sex;
	}
	
	/**
	 * @param sex
	 */
	public void setSex(int sex)
	{
		_sex = sex;
	}
	
	/**
	 * @return
	 */
	public int getWalkSpeed()
	{
		return _walkSpeed;
	}
	
	/**
	 * @param walkSpeed
	 */
	public void setWalkSpeed(int walkSpeed)
	{
		_walkSpeed = walkSpeed;
		updateEffectiveSpeed();
	}
	
	private void updateEffectiveSpeed()
	{
		if (isRunning())
		{
			_effectiveSpeed = getRunSpeed() * (float)getMovementMultiplier();
		}
		else
		{
			_effectiveSpeed = getWalkSpeed() * (float)getMovementMultiplier();
		}
	}
	
	/**
	 * if the target is canceld, also the attack is canceld
	 * @param i
	 */
	public void setTarget(L2Object object)
	{
		if (object == null && isInCombat())
		{
			setInCombat(false);
		}
		
		_target = object;
	}
	
	/**
	 * @return
	 */
	public int getTargetId()
	{
		if (_target != null)
		{
			return _target.getObjectId();
		}
		return -1;
	}
	
	public L2Object getTarget()
	{
		return _target;
	}
	
	/**
	 * @return Returns the currentState.
	 */
	public byte getCurrentState()
	{
		return _currentState;
	}
	
	
	/**
	 * @param currentState The currentState to set.
	 */
	public void setCurrentState(byte currentState)
	{
		_currentState = currentState;
	}
	
	/**
	 * @return Returns the zOffset.
	 */
	public double getCollisionHeight()
	{
		return _collisionHeight;
	}
	
	/**
	 * @param offset The zOffset to set.
	 */
	public void setCollisionHeight(double offset)
	{
		_collisionHeight = offset;
	}
	
	
	
	/**
	 * @return Returns the collisionRadius.
	 */
	public double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	/**
	 * @param collisionRadius The collisionRadius to set.
	 */
	public void setCollisionRadius(double collisionRadius)
	{
		_collisionRadius = collisionRadius;
	}
	
	/**
	 * @return Returns the unknown1.
	 */
	public double getMovementMultiplier()
	{
		return _movementMultiplier;
	}
	
	/**
	 * @param unknown1 The unknown1 to set.
	 */
	public void setMovementMultiplier(double unknown1)
	{
		_movementMultiplier = unknown1;
		updateEffectiveSpeed();
	}
	
	/**
	 * @return Returns the unknown2.
	 */
	public double getAttackSpeedMultiplier()
	{
		return _attackSpeedMultiplier;
	}
	
	/**
	 * @param unknown2 The unknown2 to set.
	 */
	public void setAttackSpeedMultiplier(double unknown2)
	{
		_attackSpeedMultiplier = unknown2;
	}
	
	protected String title;
	/**
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @param clanName
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * @return Returns the runSpeed.
	 */
	public int getRunSpeed()
	{
		return _runSpeed;
	}
	/**
	 * @param runSpeed The runSpeed to set.
	 */
	public void setRunSpeed(int runSpeed)
	{
		_runSpeed = runSpeed;
		updateEffectiveSpeed();
	}
	
	/**
	 * @return
	 */
	public boolean isRunning()
	{
		return _running;
	}
	
	/**
	 * @param b
	 */
	public void setRunning(boolean b)
	{
		_running = b;
		updateEffectiveSpeed();
	}
	
	/**
	 * @return
	 */
	public int getFloatingRunSpeed()
	{
		return _floatingRunSpeed;
	}
	
	/**
	 * @return
	 */
	public int getFloatingWalkSpeed()
	{
		return _floatingWalkSpeed;
	}
	
	/**
	 * @return
	 */
	public int getFlyingRunSpeed()
	{
		return _flyingRunSpeed;
	}
	
	/**
	 * @return
	 */
	public int getFlyingWalkSpeed()
	{
		return _flyingWalkSpeed;
	}
	/**
	 * @param floatingRunSpeed The floatingRunSpeed to set.
	 */
	public void setFloatingRunSpeed(int floatingRunSpeed)
	{
		_floatingRunSpeed = floatingRunSpeed;
	}
	/**
	 * @param floatingWalkSpeed The floatingWalkSpeed to set.
	 */
	public void setFloatingWalkSpeed(int floatingWalkSpeed)
	{
		_floatingWalkSpeed = floatingWalkSpeed;
	}
	/**
	 * @param flyingRunSpeed The flyingRunSpeed to set.
	 */
	public void setFlyingRunSpeed(int flyingRunSpeed)
	{
		_flyingRunSpeed = flyingRunSpeed;
	}
	/**
	 * @param flyingWalkSpeed The flyingWalkSpeed to set.
	 */
	public void setFlyingWalkSpeed(int flyingWalkSpeed)
	{
		_flyingWalkSpeed = flyingWalkSpeed;
	}
	
	/**
	 * @param i
	 */
	public void reduceCurrentMp(int i)
	{
		synchronized(_mpLock)
		{
			_currentMp -= i;
			if (!_mpRegenActive && !isDead())
			{
				startMpRegeneration();
			}
		}
		
		broadcastStatusUpdate();
	}
	
	/**
	 * @param i
	 */
	public void reduceCurrentHp(int i, L2Character attacker)
	{
		synchronized(_hpLock)
		{
			_currentHp -= i;
			
			if (_currentHp <= 0)
			{
				_log.fine("char is dead.");
				_currentHp = 0;
				
				stopHpRegeneration();
				stopMpRegeneration();
				
				if (_currentAttackTask != null)
				{
					_currentAttackTask.cancel();
				}
				
				if (_currentHitTask != null)
				{
					_currentHitTask.cancel();
				}
				
				if (_currentMoveTask != null)
				{
					_currentMoveTask.cancel();
				}
				broadcastStatusUpdate();
				StopMove stop = new StopMove(this);
				Die die = new Die(this);
				broadcastPacket(stop);
				sendPacket(stop);
				broadcastPacket(die);
				sendPacket(die);
				if (attacker != null)
				{
					attacker.setTarget(null);
				}
				return;
			}
			else if (!_hpRegenActive)
			{
				startHpRegeneration();
			}
		}
		
		broadcastStatusUpdate();
	}
	
	public void moveTo(int x, int y, int z, int offset)
	{
		_moveOffset = offset;
		
		double distance = getDistance(x, y);
		
		if (distance> 0 || offset >0)
		{
			if (offset == 0)
			{
				_log.fine("moveToLoc: x:"+x+" y:"+y + " from x:"+ getX() + " y:" + getY()+ " z:" + getZ());
				if (this.isMovingToPawn())
				{
					setMovingToPawn(false);
					setPawnTarget(null);
				}
				calculateMovement(x,y,z,distance);
				CharMoveToLocation mov = new CharMoveToLocation(this);
				if (getCurrentState() == STATE_CASTING)
				{
					setCurrentState(STATE_IDLE);
				}
				/*if (getCurrentState() == STATE_FOLLOW && getPawnTarget() ==null)
				 {
				 setMovingToPawn(true);
				 setPawnTarget(getTarget());
				 setPawnOffset(offset);
				 _moveToPawn = (L2PcInstance)getTarget(); 
				 _moveToPawn.addPositionListener(this);
				 }*/
				enableAllSkills();
				broadcastPacket(mov);
				sendPacket(mov);
			}
			else
			{
				if (distance <= offset)
				{
					onTargetReached();
					return;
				}
				if (getPawnTarget() == null || getPawnTarget() != getTarget())
				{
					_log.fine("moveToPawn: x:"+x+" y:"+y + " from x:"+ getX() + " y:" + getY() + " z:" + getZ());
					setMovingToPawn(true);
					setPawnTarget((L2Character)getTarget());
					setPawnOffset(offset);
					calculateMovement(x,y,z,distance);
					MoveToPawn mov = new MoveToPawn(this, getTarget(), offset);
					broadcastPacket(mov);
					sendPacket(mov);
					return;
				}
				calculateMovement(x,y,z,distance);
			}
		}
		else
		{
			sendPacket(new ActionFailed());
			// we are already at destination
			onTargetReached();
			return;
		}
	}
	
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private synchronized void calculateMovement(int x, int y, int z,double distance)
	{
		if (isMoving())
		{
			_log.fine(getName()+" ::current movement interrupted .. setting new target");
			stopMove();
		}
		if (getPawnTarget() != null)
		{
			if (distance <= getAttackRange() && getCurrentState() == STATE_FOLLOW)
			{
			ArriveTask newMoveTask = new ArriveTask(this);
			_moveTimer.schedule(newMoveTask, 3000);
			_currentMoveTask = newMoveTask;
			return;
			}
		
		}
		_log.fine("distance to target:" + distance);
		
		int dx = x-getX();
		int dy = y-getY();
		
		if (_moveOffset >0)
		{
			if (distance - _moveOffset  <= 0)
			{
				distance = 0;
				_log.fine("already in range, no movement needed.");
			}
			else
			{
				distance -= (_moveOffset-5); // due to rounding error, we have to move a bit closer to be in range 
			}
			
			double angle = Math.atan2(-dy, -dx);
			dy = (int) -(Math.sin(angle) * distance);
			dx = (int) -(Math.cos(angle) * distance);
		}
		
		
		if (distance > 0 || getPawnTarget() != null)
		{
			float speed = _effectiveSpeed;
			
			// workaround to prevent divide by zero
			if (speed == 0)
			{
				_log.warning("speed is 0 for Character oid:"+getObjectId()+" movement canceld");
				return; // no movement is done
			}
			
			//			_log.fine("dx "+dx+" dy "+dy);
			_timeToTarget = (long) (distance * 1000 / speed);
			_xAddition = (dx/distance*speed)/1000;
			_yAddition = (dy/distance*speed)/1000;
			int heading = (int) (Math.atan2(-dy, -dx) * 10430.378350470452724949566316381);
			heading += 32768;
			setHeading(heading);
			_log.fine("dist:"+ distance +"speed:" + speed + " ttt:" +_timeToTarget + " dx:"+_xAddition + " dy:"+_yAddition + " heading:" + heading);
			int destinationX = getX() + (int) (_xAddition * _timeToTarget);
			int destinationY = getY() + (int) (_yAddition * _timeToTarget);
			setXdestination(destinationX);
			setYdestination(destinationY);
			setZdestination(z); // this is what was requested from client
			//			_log.fine("destination: x "+destinationX+" y "+destinationY);
			
			_moveStartTime = System.currentTimeMillis();
			if (_timeToTarget < 0 )
			{
				_timeToTarget = 0;
			}
			
			_log.fine("time to target:" + _timeToTarget);
			ArriveTask newMoveTask = new ArriveTask(this);
			if (getPawnTarget() != null)
			{
				if (getCurrentState() == STATE_INTERACT)
				{
					_moveTimer.schedule(newMoveTask, _timeToTarget);
					_currentMoveTask = newMoveTask;
					setIsMoving(true);
					return;
				}
				if ((_timeToTarget < 2000) && (distance > getAttackRange()))
				{
					_moveTimer.schedule(newMoveTask, _timeToTarget);
				}
				else
				{
					if (getPawnTarget().isMoving())
					{
						_moveTimer.schedule(newMoveTask, 2000);
					}
					else
					{
						_moveTimer.schedule(newMoveTask, 3000);
					}
				}
			}
			else
			{
				_moveTimer.schedule(newMoveTask, _timeToTarget);
			}
			_currentMoveTask = newMoveTask;
			setIsMoving(true);
		}
		
		return;
	}
	
	/**
	 * 
	 */
	protected void stopHitTask()
	{
		if (_currentHitTask != null)
		{
			_currentHitTask.cancel();
			_currentHitTask = null;
		}
	}
	
	/**
	 * 
	 */
	protected void stopAttackTask()
	{
		if (_currentAttackTask != null)
		{
			_currentAttackTask.cancel();
			_currentAttackTask = null;
		}
	}
	
	/**
	 * @param b
	 */
	public void setIsMoving(boolean b)
	{
		_moving = b;
	}
	
	/**
	 * @param x   target x
	 * @param y   target y
	 * @return
	 */
	public double getDistance(int x, int y) 
	{
		long dx = x-getX();
		long dy = y-getY();
		double distance = Math.sqrt(dx*dx + dy*dy);
		return distance;
	}
	
	public L2Character[] broadcastPacket(ServerBasePacket mov)
	{
		Set list = getKnownPlayers();
		L2Character[] players = (L2Character[]) list.toArray(new L2Character[list.size()]);
		_log.fine("players to notify:" + players.length + " packet:"+mov.getType());
		
		for (int i=0; i < players.length; i++)
		{
			players[i].sendPacket(mov);
		}
		
		return players;
	}
	
	public void sendPacket(ServerBasePacket mov)
	{
		// default implementation
	}
	
	/**
	 * event that is called when the destination coordinates are reached
	 */
	public void onTargetReached()
	{
		
		
		if (getPawnTarget() != null)
		{
			int x = getPawnTarget().getX(), y=getPawnTarget().getY(),z = getPawnTarget().getZ();
			
			double distance = getDistance(x,y);
			if (getCurrentState() == STATE_FOLLOW)
			{
				calculateMovement(x,y,z,distance);
				return;
			}
			
			//			takes care of moving away but distance is 0 so i won't follow problem
			
			
			if (((distance > getAttackRange()) && (getCurrentState() == STATE_ATTACKING)) || (getPawnTarget().isMoving() && getCurrentState() != STATE_ATTACKING))
			{
				calculateMovement(x,y,z,distance);
				return;
			}
			
		}
		//		 update x,y,z with the current calculated position 
		stopMove();
		
		_log.fine(this.getName() +":: target reached at: x "+getX()+" y "+getY()+ " z:" + getZ());
		
		if (getPawnTarget() != null)
		{
			
			setPawnTarget(null); 
			setMovingToPawn(false);
		}
		
	}
	
	public void setTo(int x, int y, int z, int heading)
	{
		setX(x);
		setY(y);
		setZ(z);
		setHeading(heading);
		if (isMoving())
		{	
			setCurrentState(STATE_IDLE);
			StopMove setto = new StopMove(this);
			broadcastPacket(setto);
		}
		else
		{
			SetToLocation setto = new SetToLocation(this);
			broadcastPacket(setto);
		}	
		
		FinishRotation fr = new FinishRotation(this);
		broadcastPacket(fr);
	}
	
	/**
	 * @return
	 */
	public boolean isDead()
	{
		return _currentHp <= 0;
	}
	/**
	 * @return Returns the rnd.
	 */
	public static Random getRnd()
	{
		return _rnd;
	}
	
	
	protected void startCombat()
	{
		if (_currentAttackTask == null )//&& !isInCombat())
		{
			_currentAttackTask = new AttackTask(this);
			_attackTimer.schedule(_currentAttackTask, 0);
		}
		else
		{
			_log.warning("multiple attacks want to start in parallel. prevented.");
		}
	}
	
	/**
	 * 
	 */
	private void onAttackTimer()
	{
		_currentAttackTask = null;
		
		_log.fine("onAttack: state="+getCurrentState() + "  target="+_attackTarget.getObjectId());

		/*
		 * thos shlold no longer be needed as only L2Character contains startAttack(this) methods :p
		if (!(_attackTarget instanceof L2Character))
		{
			_log.severe("target is not a character. attack canceld:" + getTarget());
			setInCombat(false);
			setCurrentState(STATE_IDLE);
			ActionFailed af = new ActionFailed();
			sendPacket(af);
			return;
		}
		*/
		
		//TODO: will need improvement for doors in the future
		L2Character target = (L2Character)_attackTarget;
		
		if (isDead() || target == null || target.isDead() || (getCurrentState() != STATE_ATTACKING && getCurrentState() != STATE_CASTING) || !target.knownsObject(this) || !knownsObject(target))
		{
			// if we are dead or the target is dead, we stop the attack 
			setInCombat(false);
			setCurrentState(STATE_IDLE);
			ActionFailed af = new ActionFailed();
			sendPacket(af);
			return;
		}
		if (getActiveWeapon().getWeaponType() == L2Weapon.WEAPON_TYPE_BOW)
		{
			if (!checkAndEquipArrows())
			{
				// char has no arrows, stop the attack
				setInCombat(false);
				setCurrentState(STATE_IDLE);
				ActionFailed af = new ActionFailed();
				sendPacket(af);
				sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_ARROWS));
				return;
			}
		}
		double distance = getDistance(target.getX(), target.getY());
		if (distance > getAttackRange())
		{
			// follow the mob
			moveTo(_attackTarget.getX(), _attackTarget.getY(), _attackTarget.getZ(), getAttackRange());
			return;
		}
		if ((getCurrentState() == STATE_ATTACKING) && (_currentlyAttacking == false))
		{
			L2Weapon weaponItem = getActiveWeapon();
			
			if (weaponItem == null)
			{
				// can't normally happen, but if it does, stop the attack
				setInCombat(false);
				setCurrentState(STATE_IDLE);
				ActionFailed af = new ActionFailed();
				sendPacket(af);
				return;
			}
			
			if (_currentlyAttacking == false)
			{
				
				_currentlyAttacking = true;
				
				int baseDamage = weaponItem.getPDamage();
				int randomDamage = weaponItem.getRandomDamage();
				
				int damage = 0;
				boolean crit = false;
				
				int hitTarget = getRnd().nextInt(100);
				boolean miss = false; //(getAccuracy()< hitTarget);
				boolean soulShotUse = false;
				
				
				if (!miss)
				{
					int pDef = target.getPhysicalDefense();
					if (pDef == 0)
					{
						pDef = 300;
						if (target instanceof L2NpcInstance)
						{
							_log.warning("target has bogus stats. check npc2.csv: id " + ((L2NpcInstance)target).getNpcTemplate().getNpcId());
						}
						else
						{
							_log.warning("target has bogus stats. Pdef was 0. temporary increased to 300");
						}
					}
					damage = (baseDamage + getRnd().nextInt(randomDamage)) * 70 / pDef ;
					
					int critHit = getRnd().nextInt(100);
					crit = (getCriticalHit() > critHit);
					
					if (crit)
					{
						damage *= 2;
					}
					
					// additional check to prevent users from activating low grade SS 
					// and switch weapon before attack
					if (getActiveSoulshotGrade() == weaponItem.getCrystalType())
					{
						soulShotUse = true;
						damage *= 2;
					}
				}
				
				if (!isInCombat() && !miss )
				{
					setInCombat(true);
				}
				
				if (isUsingDualWeapon())
				{
					_hitTimer.schedule(new HitTask(this, target, (int)damage, crit, miss, false), calculateHitSpeed(weaponItem,1));
					_hitTimer.schedule(new HitTask(this, target, (int)damage, crit, miss, false), calculateHitSpeed(weaponItem,2));
					
				}
				else if (getActiveWeapon().getWeaponType() == L2Weapon.WEAPON_TYPE_BOW)
				{
					   if (getCurrentMp() < weaponItem.getMpConsume())
						{
							// player doesn't have enough mp, stop the attack
							sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_MP));
							setInCombat(false);
							setCurrentState(STATE_IDLE);
							ActionFailed af = new ActionFailed();
							sendPacket(af);
							return;
						}
						else
						{
							reduceCurrentMp(weaponItem.getMpConsume());
						}
						
						sendPacket(new SystemMessage(SystemMessage.GETTING_READY_TO_SHOOT_AN_ARROW));
						SetupGauge sg = new SetupGauge(SetupGauge.RED, calculateAttackSpeed(weaponItem)*2);
						sendPacket(sg);
						_hitTimer.schedule(new HitTask(this, target, (int)damage, crit, miss, false), calculateHitSpeed(weaponItem,1));
					
				}
				else
				{
					_hitTimer.schedule(new HitTask(this, target, (int)damage, crit, miss, false), calculateHitSpeed(weaponItem,1));
				}
				
				Attack attack = new Attack(getObjectId(), _attackTarget.getObjectId(), damage, miss, crit, soulShotUse, getX(), getY(), getZ());
				setActiveSoulshotGrade(0);
				broadcastPacket(attack);
				sendPacket(attack);
			}
		}
	}
	
	/**
	 * @return true if arrows are available
	 */
	protected boolean checkAndEquipArrows()
	{
		return true;
	}
	
	public void addExpAndSp(int addToExp, int addToSp)
	{
		// Dummy method (overridden by players and pets)
	}
	
	/**
	 * @return
	 */
	public abstract L2Weapon getActiveWeapon();
	
	protected void onHitTimer(L2Character target, int damage, boolean crit, boolean miss, boolean soulshot)
	{
		if (isDead() || target.isDead() || !target.knownsObject(this) || !knownsObject(target))
		{
			setInCombat(false);
			setTarget(null);
			setCurrentState(STATE_IDLE);
			ActionFailed af = new ActionFailed();
			sendPacket(af);
			return;
		}
		else
		{
			if (_currentlyAttacking == true)
			{
				if (getActiveWeapon().getWeaponType() == L2Weapon.WEAPON_TYPE_BOW)
				{
					reduceArrowCount();
					_attackTimer.schedule(new AttackTask(this), calculateAttackSpeed(getActiveWeapon()));
				}
				
				displayHitMessage(damage, crit, miss);
				
				if (!miss)
				{
					target.reduceCurrentHp(damage, this);
				}
				
				if (isUsingDualWeapon())
				{
					if (_2ndHit == true)
					{
						_2ndHit = false;
						_currentlyAttacking = false;
						_attackTimer.schedule(new AttackTask(this), calculateAttackSpeed(getActiveWeapon()));
					}
					else
					{
						_2ndHit = true;
					}
				}
				if (!isUsingDualWeapon())
				{
					_currentlyAttacking = false;
					_attackTimer.schedule(new AttackTask(this), calculateAttackSpeed(getActiveWeapon()));
				}
			}
		}
	}
	
	/**
	 * 
	 */
	protected void reduceArrowCount()
	{
		// default is to do nothin
	}
	
	/**
	 * @param damage
	 * @param crit
	 * @param miss
	 */
	protected void displayHitMessage(int damage, boolean crit, boolean miss)
	{
		// default is to do nothing
	}
	
	public int getAttackRange()
	{
		return -1;
	}
	
	
	
	
	/**
	 * @return Returns the inCombat.
	 */
	public boolean isInCombat()
	{
		return _inCombat;
	}
	
	/**
	 * @param inCombat The inCombat to set.
	 */
	public void setInCombat(boolean inCombat)
	{
		if (inCombat)
		{
			sendPacket( new AutoAttackStart(getObjectId()));
			broadcastPacket(new AutoAttackStart(getObjectId()));
		}
		else
		{
			stopAttackTask();
			stopHitTask();
			
			sendPacket(new AutoAttackStop(getObjectId()));
			broadcastPacket(new AutoAttackStop(getObjectId()));
			
			_currentlyAttacking = false;
		}
		
		_inCombat = inCombat;
	}
	
	public void startAttack(L2Character target)
	{
		if (target == null)
		{
			_log.fine("no target ??");
			setInCombat(false);
			setCurrentState(STATE_IDLE);
			ActionFailed af = new ActionFailed();
			sendPacket(af);
			return;
		}
		
		setTarget(target);
		_attackTarget = target;
		setCurrentState(L2Character.STATE_ATTACKING);
		moveTo(target.getX(), target.getY(), target.getZ(), getAttackRange()); // melee range
	}
	
	
	public void onForcedAttack(L2PcInstance player)
	{
		player.startAttack(this);
	}
	
	public int getActiveSoulshotGrade()
	{
		return _activeSoulShotGrade;
	}
	
	public void setActiveSoulshotGrade(int soulshotGrade)
	{
		_activeSoulShotGrade = soulshotGrade;
	}
	
	
	public void setMovingToPawn(boolean val)
	{
		_movingToPawn = val;
	}
	
	public void setPawnTarget(L2Character target)
	{
		_pawnTarget = target;
	}
	
	public void setPawnOffset(int offset)
	{
		_pawnOffset = offset;
	}
	
	public boolean isMovingToPawn()
	{
		return _movingToPawn;
	}
	
	public L2Character getPawnTarget()
	{
		return _pawnTarget;
	}
	
	public int getPawnOffset()
	{
		return _pawnOffset;
	}
	
	public float getEffectiveSpeed()
	{
		return _effectiveSpeed;
	}
	
	public int calculateAttackSpeed(L2Weapon weaponItem)
	{
		int atkspd = weaponItem.getAttackSpeed();
		if (atkspd == 0)
		{
			atkspd = 325;
		}
		atkspd = (int)((886-atkspd)*5)/2;
		// wait time till next attack
		if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DAGGER)
		{
			atkspd += 50;
		}
		else if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUALFIST)
		{
			atkspd += 100;
		}
		else if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUAL)
		{
			atkspd += 100;
		}
		else if ((weaponItem.getItemId() == 248) || (weaponItem.getItemId() == 252))
		{
			atkspd += 100;
		}
		else
		{
			atkspd += 50;
		}
		return atkspd;
	}
	
	public int calculateHitSpeed(L2Weapon weaponItem, int hit)
	{
		int hitspd = weaponItem.getAttackSpeed();
		if (hitspd == 0)
		{
			hitspd = 325;
		}
		hitspd = (int)((886-hitspd)*5)/2;
		if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DAGGER)
		{
			hitspd -= 50;
		}
		else if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUALFIST)
		{
			if (hit == 1)
			{
				hitspd -= 750;
			}
			else
			{
				hitspd -= 100;	
			}
		}
		else if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUAL)
		{
			if (hit == 1)
			{
				hitspd -= 750;
			}
			else
			{
				hitspd -= 100;	
			}
		}
		else if (((weaponItem.getItemId() == 248) || (weaponItem.getItemId() == 252)) && (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_FIST))
		{
			if (hit == 1)
			{
				hitspd -= 750;
			}
			else
			{
				hitspd -= 150;	
			}
		}
		else if ((weaponItem.getItemId() != 248) && (weaponItem.getItemId() != 252) && (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_FIST))
		{
			hitspd -= 250;
		}
		else
		{
			hitspd -= 200;
		}
		return hitspd;
	}
	
	protected boolean isUsingDualWeapon()
	{
		return false;
	}
	
	public void setAttackStatus(boolean status)
	{
		_currentlyAttacking = status;
	}
	
	protected void enableAllSkills()
	{
		// nothing
	}
}	
