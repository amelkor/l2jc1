/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2PcInstance.java,v 1.63 2004/10/24 09:06:32 dethx Exp $
 *
 * $Author: dethx $
 * $Date: 2004/10/24 09:06:32 $
 * $Revision: 1.63 $
 * $Log: L2PcInstance.java,v $
 * Revision 1.63  2004/10/24 09:06:32  dethx
 * reversed to old version (1.62 -> 1.61)
 *
 * Revision 1.61  2004/10/23 23:45:59  l2chef
 * pet is now unsummoned on player death and not completely deleted
 *
 * Revision 1.60  2004/10/23 19:47:52  l2chef
 * player gets notified about stat changes on levelup (badk0re)
 *
 * Revision 1.59  2004/10/21 12:55:10  nuocnam
 * updatePAtk(), updateMatk(), updatePDef() and updateMDef() methods corrected. Few unused & buggy statements removed. Methods no longer use dummy values as defaults, but they look on values from template table.
 *
 * Revision 1.58  2004/10/21 08:59:22  nuocnam
 * small bugfix in startCombat(). Combat is now started when getAttackRange() == getDistance()
 *
 * Revision 1.57  2004/10/21 00:07:54  nuocnam
 * - skill animation is canceled properly when changing target during casting (not only when selected target = null)
 * - code to cancel skill animation moved to public method so we can call it from outside
 *
 * Revision 1.56  2004/10/20 19:22:31  nuocnam
 * PLayers now remember L2NpcInstance they interact with before moving, so getTarget() is not used when they reach INTERACTION_DISTANCE
 *
 * Revision 1.55  2004/10/20 17:40:58  whatev66
 * added follow and interact for private store so that they walk to the store first.
 *
 * Revision 1.54  2004/10/20 16:59:42  whatev66
 * interact state now used when talking to npcs which are not attackable
 *
 * Revision 1.53  2004/10/20 08:19:59  nuocnam
 * Casting magic/using skill can be stopped by pressing esc key (dragon666, nuonam)
 *
 * Revision 1.52  2004/10/20 00:54:40  nuocnam
 * Temporary removed newTarget and anything related ot it. Seems that check done in L2NpcInstance in onAction is not necessary as attack handler refuses to attack twice elsewhere.
 *
 * Revision 1.51  2004/10/19 23:49:24  nuocnam
 * Target is now passed to skill handlers. No skill handler should use activeChar.getTarget() ever
 *
 * Revision 1.50  2004/10/15 17:23:02  nuocnam
 * removed some unnecessary code
 *
 * Revision 1.49  2004/10/14 16:49:15  nuocnam
 * - set&getNewTarget methods works with L2Object now
 * - changed spell code so target is set properly (no need to attack before being able to cast a spell on 'real' target)
 *
 * Revision 1.48  2004/10/11 17:34:58  nuocnam
 * Added setIsGM and isGM methods
 *
 * Revision 1.47  2004/10/08 19:09:09  whatev66
 * added variable to keep track of old and new targets for onactions.
 *
 * Revision 1.46  2004/10/08 08:00:30  nuocnam
 * corrected MENbonus calculation
 *
 * Revision 1.45  2004/10/05 19:14:16  whatev66
 * added counter so that it only does a check of known/visibles every few validate positions
 *
 * Revision 1.44  2004/10/05 14:38:16  nuocnam
 * Party level is recalculated after levelup (if character is in party)
 *
 * Revision 1.43  2004/10/03 16:56:41  whatev66
 * removed positionlistener changed to timer based system
 *
 * Revision 1.42  2004/09/30 07:31:39  nuocnam
 * Fixed minor bug in MP gain calculation.
 *
 * Revision 1.41  2004/09/30 04:34:21  nuocnam
 * minor cahnges to addExpAndSp. Gain more levels/one kill possible. patk/matk/pdef/mdef recalculated on lvlup. (franck, nuocnam)
 *
 * Revision 1.40  2004/09/29 08:26:51  nuocnam
 * Corrected updateMAtk()
 *
 * Revision 1.39  2004/09/27 08:44:52  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.38  2004/09/21 18:36:57  dethx
 * fixed some skills related bugs
 *
 * Revision 1.37  2004/09/19 02:58:52  nuocnam
 * Small mp add fix (sh1ny)
 *
 * Revision 1.36  2004/09/19 02:35:36  whatev66
 * party loot system added
 *
 * Revision 1.35  2004/09/19 01:37:31  nuocnam
 * added .invul for admins (sh1ny)
 *
 * Revision 1.34  2004/09/19 00:30:09  whatev66
 * *** empty log message ***
 *
 * Revision 1.33  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.26  2004/08/10 20:35:04  l2chef
 * pending transactions are cancled on disconnect (whatev)
 *
 * Revision 1.25  2004/08/10 00:49:13  l2chef
 * now contains direct references to clan
 *
 * Revision 1.24  2004/08/08 23:00:51  l2chef
 * fixed transaction setting
 *
 * Revision 1.23  2004/08/08 16:31:00  l2chef
 * delete me also stops all timertasks
 *
 * Revision 1.22  2004/08/06 00:23:48  l2chef
 * transaction controller is used for party requests (whatev)
 *
 * Revision 1.21  2004/08/04 21:15:19  l2chef
 * new flag for items added to prevent duping
 *
 * Revision 1.20  2004/08/02 00:08:47  l2chef
 * better solution to drop item problem
 *
 * Revision 1.19  2004/07/30 00:16:34  l2chef
 * fix for dead respawn problem
 *
 * Revision 1.18  2004/07/29 20:09:20  l2chef
 * Pdef/Matk/Mdef updates added (Nightmarez/Nuocnam)
 *
 * Revision 1.17  2004/07/28 23:58:59  l2chef
 * patk updated and free skills are rewarded automatically (Nightmarez)
 *
 * Revision 1.16  2004/07/25 22:58:24  l2chef
 * pet system started (whatev)
 *
 * Revision 1.15  2004/07/25 00:38:09  l2chef
 * moving objects appear now as moving when they get into viewrange (NuocNam)
 *
 * Revision 1.14  2004/07/23 01:47:21  l2chef
 * all object spawn and delete is now handeld here
 *
 * Revision 1.13  2004/07/19 02:08:08  l2chef
 * postionListeners added
 * party code added
 *
 * Revision 1.12  2004/07/18 17:39:20  l2chef
 * action failed removed for case of successful combat start
 *
 * Revision 1.11  2004/07/17 23:14:52  l2chef
 * damage is now reduced before awarding exp
 * additional check if attack target is valid and range is ok
 *
 * Revision 1.10  2004/07/14 22:11:01  l2chef
 * Hp/Mp increase added (NightMarez)
 *
 * Revision 1.9  2004/07/12 20:57:21  l2chef
 * warehouses added (nuocnan)
 * pickup item is broadcasted
 *
 * Revision 1.8  2004/07/11 22:52:31  l2chef
 * if players connection is down, the connection is NULL
 * just ignore this
 *
 * Revision 1.7  2004/07/11 11:38:32  l2chef
 * crash prevention... dont try to pickup non-items
 *
 * Revision 1.6  2004/07/07 23:51:54  l2chef
 * pAtk update contributed by MetalRabit
 *
 * Revision 1.5  2004/07/04 17:40:28  l2chef
 * addexp and addsp combined to one ... they will never appear separated... i think
 *
 * Revision 1.4  2004/07/04 14:34:24  jeichhorn
 * added method to add exp and sp
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.CharTemplateTable;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.ExperienceTable;
import net.sf.l2j.gameserver.GmListTable;
import net.sf.l2j.gameserver.LevelUpData;
import net.sf.l2j.gameserver.Modifiers;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.handler.skillhandlers.DamageSkill;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.CharMoveToLocation;
import net.sf.l2j.gameserver.serverpackets.DeleteObject;
import net.sf.l2j.gameserver.serverpackets.GetItem;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.MagicSkillCanceld;
import net.sf.l2j.gameserver.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.serverpackets.PartySmallWindowUpdate;
import net.sf.l2j.gameserver.serverpackets.PrivateStoreMsgSell;
import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;
import net.sf.l2j.gameserver.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.serverpackets.SpawnItem;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.serverpackets.PrivateBuyListSell;
import net.sf.l2j.gameserver.serverpackets.PrivateBuyListBuy;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2CharTemplate;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.templates.L2Item;


/**
 * This class represents a player character in the world.
 * there is always a client-thread connected to this 
 * (except if a player-store is activated upon logout)
 * 
 * @version $Revision: 1.63 $ $Date: 2004/10/24 09:06:32 $
 */
public class L2PcInstance extends L2Character
{
	private static Logger _log = Logger.getLogger(L2PcInstance.class.getName());
	
	class MagicUseTask extends TimerTask
	{
		L2Character _target;
		L2Skill _skill;
		
		public MagicUseTask(L2Character target, L2Skill skill)
		{
			_target = target;
			_skill = skill;
		}
		
		public void run()
		{
			try
			{
				onMagicUseTimer(_target, _skill);
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
	private static Timer _magicUseTimer = new Timer(true);
	
	class EnableSkill extends TimerTask
	{
		int _skillId;
		
		public EnableSkill(int skillId)
		{
			_skillId = skillId;
		}
		
		public void run()
		{
			disableSkill(_skillId, false);
		}
	}
	private static Timer _enableSkillTimer = new Timer(true);
	
	class EnableAllSkills extends TimerTask
	{
		L2Skill _skill;
		
		public EnableAllSkills(L2Skill skill)
		{
			_skill = skill;
		}
		
		public void run()
		{
			if (getSkill() == _skill)
			{
				enableAllSkills();
			}
		}
	}
	private static Timer _enableAllSkillsTimer = new Timer(true);
	
	private Map _disabledSkills = new HashMap();
	private boolean _allSkillsDisabled = false;
	
	private Connection _netConnection;
	
	//private L2Object _newTarget = null;
	
	private int _charId = 0x00030b7a;
	private int _canCraft = 0;
	private int _exp;
	private int _sp;
	private int _karma;
	private int _pvpKills;
	private int _pkKills;
	private int _pvpFlag;
	
	private int _maxLoad;
	private int _race;
	private int _classId;
	private int _deleteTimer;
	private Inventory _inventory = new Inventory();
	private Warehouse _warehouse = new Warehouse();
	private int _moveType = 1;
	private int _waitType = 1;
	private int _crestId;
	private Map _skills = new HashMap();
	private L2Skill _skill;
	private Map _quests = new HashMap();
	private Map _shortCuts = new TreeMap();
	private int _allyId;
	private L2TradeList _tradeList;
	private ArrayList _sellList;
	private ArrayList _buyList;
	private int _privatestore = 0;
	//	pet
	private L2PetInstance _pet = null;
	
	// these values are only stored temporarily
	private boolean _partyMatchingAutomaticRegistration;
	private boolean _partyMatchingShowLevel;
	private boolean _partyMatchingShowClass;
	private String _partyMatchingMemo;
	
	private L2Party _party = null;
	// clan related attributes
	private int _clanId;
	private L2Clan _clan;
	private boolean _clanLeader;
	
	//GM Stuff
	private boolean _isInvul = false;
	private boolean _isGm = false;
	
	
	// this is needed to find the inviting player for Party response
	// there can only be one active party request at once
	private L2PcInstance _currentTransactionRequester;
	private L2ItemInstance _arrowItem;
	
	private L2Weapon _fistsWeaponItem;
	
	private long _uptime;
	
	public byte updateKnownCounter = 0;
	
	private L2Character _interactTarget;
	/**
	 * 
	 * @param newSkill
	 * @return L2Skill old skill that was upgraded or NULL if there was no lower skill of same type
	 */
	public L2Skill addSkill(L2Skill newSkill)
	{
		L2Skill oldSkill = (L2Skill) _skills.put(new Integer(newSkill.getId()), newSkill);
		return oldSkill;
	}
	

	/**
	 * 
	 * @param skill
	 * @return L2Skill old skill that was removed or NULL if the skill did not exist
	 */
	public L2Skill removeSkill(L2Skill skill)
	{
		return (L2Skill) _skills.remove(new Integer(skill.getId()));
	}
	
	/**
	 * @return L2Skill[]  all skills that this char knows
	 */
	public L2Skill[] getAllSkills()
	{
		return (L2Skill[]) _skills.values().toArray(new L2Skill[_skills.values().size()]);
	}
	
	public L2ShortCut[] getAllShortCuts()
	{
		return (L2ShortCut[]) _shortCuts.values().toArray(new L2ShortCut[_shortCuts.values().size()]);
	}
	
	public L2ShortCut getShortCut(int slot)
	{
		return (L2ShortCut) _shortCuts.get(new Integer(slot));
	}
	
	public void registerShortCut(L2ShortCut shortcut)
	{
		_shortCuts.put(new Integer(shortcut.getSlot()), shortcut);
	}

	/**
	 * @param slot
	 */
	public void deleteShortCut(int slot)
	{
		_shortCuts.remove(new Integer(slot));
	}
	
	
	/**
	 * Determine the level of a certain skill.
	 * this is used for shortcut reg
	 * @param skillId
	 * @return
	 */
	public int getSkillLevel(int skillId)
	{
		L2Skill skill = (L2Skill)_skills.get(new Integer(skillId));
		if (skill == null)
		{
			return -1;
		}
		else
		{
			return skill.getLevel();
		}
	}
	
	/**
	 * @param pvpFlag
	 */
	public void setPvpFlag(int pvpFlag)
	{
		_pvpFlag = pvpFlag;
	}

	/**
	 * @return
	 */
	public int getCanCraft()
	{
		return _canCraft;
	}

	/**
	 * @param canCraft
	 */
	public void setCanCraft(int canCraft)
	{
		_canCraft = canCraft;
	}

	/**
	 * @return
	 */
	public int getPkKills()
	{
		return _pkKills;
	}

	/**
	 * @param pkKills
	 */
	public void setPkKills(int pkKills)
	{
		_pkKills = pkKills;
	}

	/**
	 * @return
	 */
	public int getDeleteTimer()
	{
		return _deleteTimer;
	}

	/**
	 * @param deleteTimer
	 */
	public void setDeleteTimer(int deleteTimer)
	{
		_deleteTimer = deleteTimer;
	}

	/**
	 * @return
	 */
	public int getCurrentLoad()
	{
		return _inventory.getTotalWeight();
	}

	/**
	 * @return
	 */
	public int getKarma()
	{
		return _karma;
	}

	/**
	 * @param karma
	 */
	public void setKarma(int karma)
	{
		_karma = karma;
	}

	/**
	 * @return
	 */
	public int getMaxLoad()
	{
		return _maxLoad;
	}

	/**
	 * @param maxLoad
	 */
	public void setMaxLoad(int maxLoad)
	{
		_maxLoad = maxLoad;
	}

	/**
	 * @return
	 */
	public int getPvpKills()
	{
		return _pvpKills;
	}

	/**
	 * @param pvp
	 */
	public void setPvpKills(int pvpKills)
	{
		_pvpKills = pvpKills;
	}

	/**
	 * @return
	 */
	public int getClassId()
	{
		return _classId;
	}

	/**
	 * @param className
	 */
	public void setClassId(int classId)
	{
		_classId = classId;
	}

	/**
	 * @return
	 */
	public int getExp()
	{
		return _exp;
	}
	
	public L2Skill getSkill()
	{
		return _skill;
	}

	/**
	 * @param magicId
	 */
	public void setSkill(L2Skill skill)
	{
		_skill = skill;
	}
	
	public void setFistsWeaponItem(L2Weapon weaponItem)
	{
		_fistsWeaponItem = weaponItem;
	}
	
	public L2Weapon getFistsWeaponItem()
	{
		return _fistsWeaponItem;
	}
	
	public L2Weapon findFistsWeaponItem(int classId)
	{
		L2Weapon weaponItem = null;
		if ((classId >= 0x00) && (classId <= 0x09))
		{
			//human fighter fists
			L2Item temp = ItemTable.getInstance().getTemplate(246);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x0a) && (classId <= 0x11))
		{
			//human mage fists
			L2Item temp = ItemTable.getInstance().getTemplate(251);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x12) && (classId <= 0x18))
		{
			//elven fighter fists
			L2Item temp = ItemTable.getInstance().getTemplate(244);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x19) && (classId <= 0x1e))
		{
			//elven mage fists
			L2Item temp = ItemTable.getInstance().getTemplate(249);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x1f) && (classId <= 0x25))
		{
			//dark elven fighter fists
			L2Item temp = ItemTable.getInstance().getTemplate(245);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x26) && (classId <= 0x2b))
		{
			//dark elven mage fists
			L2Item temp = ItemTable.getInstance().getTemplate(250);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x2c) && (classId <= 0x30))
		{
			//orc fighter fists
			L2Item temp = ItemTable.getInstance().getTemplate(248);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x31) && (classId <= 0x34))
		{
			//orc mage fists
			L2Item temp = ItemTable.getInstance().getTemplate(252);
			weaponItem = (L2Weapon)temp;
		}
		else if ((classId >= 0x35) && (classId <= 0x39))
		{
			//dwarven fists
			L2Item temp = ItemTable.getInstance().getTemplate(247);
			weaponItem = (L2Weapon)temp;
		}
		return weaponItem;
	}
	
	public void addExpAndSp(int addToExp, int addToSp)
	{
	    _log.fine("adding " + addToExp + " exp and " +addToSp+" sp to " + getName());
	    _exp += addToExp;
	    _sp += addToSp;
	    
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.EXP, _exp);
		su.addAttribute(StatusUpdate.SP, _sp);
		sendPacket(su);
		
		SystemMessage sm = new SystemMessage(SystemMessage.YOU_EARNED_S1_EXP_AND_S2_SP);
		sm.addNumber(addToExp);
		sm.addNumber(addToSp);
		sendPacket(sm);
	    
		while (_exp >= ExperienceTable.getInstance().getExp(getLevel() + 1))
	    {
	        // Looks like the level should be increased.
	        increaseLevel();
	        rewardSkills();
	        updatePAtk();
	        updateMAtk();
	        updatePDef();
	        updateMDef();
	        sendPacket(new UserInfo(this)); 
	    }
	}
	
	/**
	 *  reward player with free skill at certain lvl's.
	 */
	private void rewardSkills()
	{
		int charclass = getClassId();
		int lvl = getLevel();
		if (lvl == 5) // remove the noobie skills
		{		
			L2Skill skill = SkillTable.getInstance().getInfo(194, 1);
			removeSkill(skill);
			_log.fine("removed skill 'Lucky' from "+getName());
		}
		else if (lvl == 20) // give lvl 20 free skills
		{
			L2Skill skill = SkillTable.getInstance().getInfo(239, 1);
			addSkill(skill);
			_log.fine("awarded "+getName()+" with expertise D.");
		}
		else if (lvl == 40) // give lvl 40 free skills
		{
			L2Skill skill = SkillTable.getInstance().getInfo(239, 2);
			addSkill(skill);
			_log.fine("awarded "+getName()+" with expertise C.");
		}
		else if (lvl == 52) // give lvl 52 free skills
		{
			L2Skill skill = SkillTable.getInstance().getInfo(239, 3);
			addSkill(skill);
			_log.fine("awarded "+getName()+" with expertise B.");
		}		
		else
		{
			_log.fine("No skills awarded at lvl: "+lvl);
		}
	}
	

	/**
	 * @param exp
	 */
	public void setExp(int exp)
	{
		_exp = exp;
	}

	/**
	 * @return
	 */
	public int getRace()
	{
		return _race;
	}

	/**
	 * @param race
	 */
	public void setRace(int race)
	{
		_race = race;
	}

	/**
	 * @return
	 */
	public int getSp()
	{
		return _sp;
	}
	
	/**
	 * @param sp
	 */
	public void setSp(int sp)
	{
		_sp = sp;
	}

	/**
	 * @return
	 */
	public int getPvpFlag()
	{
		return _pvpFlag;
	}

	/**
	 * @return
	 */
	public int getClanId()
	{
		return _clanId;
	}

	/**
	 * @param clanId
	 */
	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}

	/**
	 * @return
	 */
	public Inventory getInventory()
	{
		return _inventory;
	}

	/**
	 * @return
	 */
	public int getMoveType()
	{
		return _moveType;
	}

	/**
	 * @param moveType The moveType to set.
	 */
	public void setMoveType(int moveType)
	{
		_moveType = moveType;
		setRunning(_moveType == 1);
	}

	/**
	 * @return
	 */
	public int getWaitType()
	{
		return _waitType;
	}

	/**
	 * @param waitType The waitType to set.
	 */
	public void setWaitType(int waitType)
	{
		_waitType = waitType;
	}

	public Warehouse getWarehouse()
	{
		return _warehouse;
	}
	
	/**
	 * @return Returns the charId.
	 */
	public int getCharId()
	{
		return _charId;
	}

	/**
	 * @param charId The charId to set.
	 */
	public void setCharId(int charId)
	{
		_charId = charId;
	}

	/**
	 * @return
	 */
	public int getAdena()
	{
		return _inventory.getAdena();
	}

	/**
	 * @param adena The adena to set.
	 */
	public void reduceAdena(int adena)
	{
		_inventory.reduceAdena(adena);
	}

	/**
	 * @param adena The adena to set.
	 */
	public void addAdena(int adena)
	{
		_inventory.addAdena(adena);
	}

	/**
	 * @return
	 */
	public Connection getNetConnection()
	{
		return _netConnection;
	}

	/**
	 * @param connection
	 */
	public void setNetConnection(Connection connection)
	{
		_netConnection = connection;
	}

	/**
	 * @return
	 */
	public int getCrestId()
	{
		return _crestId;
	}

	/**
	 * @param crestId The crestId to set.
	 */
	public void setCrestId(int crestId)
	{
		_crestId = crestId;
	}
	
	public void onAction(L2PcInstance player)
	{
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
			player.sendPacket(my);
		}
		else
		{
			player.setCurrentState(STATE_FOLLOW);
			if (getPrivateStoreType() != 0 )
			{
				player.setCurrentState(STATE_INTERACT);
			}
			
			player.moveTo(getX(), getY(), getZ(), 36);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#broadCastUpdate()
	 */
	public void broadcastStatusUpdate()
	{
		StatusUpdate su = new StatusUpdate(getObjectId());
		su.addAttribute(StatusUpdate.CUR_HP, (int) getCurrentHp());
		su.addAttribute(StatusUpdate.CUR_MP, (int) getCurrentMp());

		super.broadcastStatusUpdate();
		
		if (getNetConnection() != null)
		{
			sendPacket(su);
		}
		
		if (isInParty()) {
			PartySmallWindowUpdate update = new PartySmallWindowUpdate(this);
			getParty().broadcastToPartyMembers(this, update);
		}
	}

	
	/**
	 * @return
	 */
	public int getAllyId()
	{
		return _allyId;
	}
	/**
	 * @param allyId The allyId to set.
	 */
	public void setAllyId(int allyId)
	{
		_allyId = allyId;
	}
	
	protected void onHitTimer(L2Character target, int damage, boolean crit, boolean miss, boolean soulshot)
	{
		super.onHitTimer(target, damage, crit, miss, soulshot);
	}
	
	/**
	 * @param damage
	 * @param crit
	 * @param miss
	 */
	protected void displayHitMessage(int damage, boolean crit, boolean miss)
	{
		if (crit)
		{
			sendPacket(new SystemMessage(SystemMessage.CRITICAL_HIT));
		}
		
		if (miss)
		{
			sendPacket(new SystemMessage(SystemMessage.MISSED_TARGET));
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_DID_S1_DMG);
			sm.addNumber(damage);
			sendPacket(sm);
		}
	}
	
	public void sendPacket(ServerBasePacket packet)
	{
		try
		{
			getNetConnection().sendPacket(packet);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			// the connection for this player seems to be broken. linkdead ? logout ?
			// TODO automatically remove all references to this player
		}
	}
	
	/**
	 * 
	 */
	protected void startCombat()
	{
		L2Character target = (L2Character) getTarget();
		if (target == null)
		{
			_log.warning("failed to start combat without target.");
			sendPacket(new ActionFailed());
		}
		else if (getAttackRange() < getDistance(target.getX(),target.getY()))
		{
			sendPacket(new ActionFailed());
		}
		else
		{
			_log.fine("starting combat");
			super.startCombat();
		}	
		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#onTargetReached()
	 */
	public void onTargetReached()
	{
		super.onTargetReached();
		
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
				case STATE_CASTING:
					//setCurrentState(STATE_IDLE); //XXX
					useMagic(_skill);
				case STATE_INTERACT:
					if (getTarget() instanceof L2PcInstance)
					{
						L2PcInstance temp = (L2PcInstance) getTarget();
						sendPacket(new ActionFailed());
						if (temp.getPrivateStoreType() == 1 )
						{
							sendPacket(new PrivateBuyListSell(this,temp));
						}
						if (temp.getPrivateStoreType() == 3 )
						{
							sendPacket(new PrivateBuyListBuy(temp,this));
						}
						setCurrentState(L2Character.STATE_IDLE);
					}
					else
					{
						//_interactTarget=null should never happen but one never knows ^^;
						if (_interactTarget != null) _interactTarget.onAction(this);
					}
				default:
					break;
			}
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
	}
	
	private void doPickupItem() throws IOException
	{
		setCurrentState(STATE_IDLE);

		if (! (getTarget() instanceof L2ItemInstance))
		{
			// dont try to pickup anything that is not an item :)
			_log.warning("trying to pickup wrong target."+getTarget());
			return;
		}
		
		L2ItemInstance target = (L2ItemInstance) getTarget();
		
		sendPacket(new ActionFailed());
		
		StopMove sm = new StopMove(getObjectId(), getX(), getY(), getZ(), getHeading()); 
		_log.fine("pickup pos: "+ target.getX() + " "+target.getY()+ " "+target.getZ() );
		sendPacket(sm);
		
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
			return;
		}
		
		L2World.getInstance().removeVisibleObject(target);
		
		GetItem gi = new GetItem(target, getObjectId());
		sendPacket(gi);
		broadcastPacket(gi);
		DeleteObject del = new DeleteObject(target);
		sendPacket(del);
		broadcastPacket(del);
		
		if  (!isInParty()) {
			if (target.getItemId() == 57) {
				SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_ADENA); // you picked up $s1 adena
				smsg.addNumber(target.getCount());
				sendPacket(smsg);				
			} else {
				SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_S2); // you picked up $s1$s2
				smsg.addNumber(target.getCount());
				smsg.addItemName(target.getItemId());
				sendPacket(smsg);
			}
			
			L2ItemInstance target2 = getInventory().addItem(target);
			InventoryUpdate iu = new InventoryUpdate();
			if (target2.getLastChange() == L2ItemInstance.ADDED) {
				iu.addNewItem(target);
			} else {
				iu.addModifiedItem(target2);
			}
			
			sendPacket(iu);
			//TODO send only weight update, if possible
			UserInfo ci = new UserInfo(this);
			sendPacket(ci);

			
		} else {
			if (target.getItemId() == 57) {
				getParty().distributeAdena(target);
			} else {
				getParty().distributeItem(this, target);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#setTarget(net.sf.l2j.gameserver.model.L2Object)
	 */
	public void setTarget(L2Object newTarget)
	{
		if (getCurrentState() == L2Character.STATE_CASTING) {
			cancelCastMagic();
		}
			// cancel old target
			L2Object oldTarget = getTarget();
			
			if (oldTarget != null)
			{
				if (oldTarget.equals(newTarget))
				{
					return; // no target change
				}
			
				// remove listener if the target was a char
				if (oldTarget instanceof L2Character)
				{
					((L2Character) oldTarget).removeStatusListener(this);
				}
			}

			if (newTarget != null && newTarget instanceof L2Character)
			{
				((L2Character) newTarget).addStatusListener(this);
			}
			
		super.setTarget(newTarget);
	}


	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeapon()
	 */
	public L2Weapon getActiveWeapon()
	{
		L2ItemInstance weapon = getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (weapon == null)
		{
			return getFistsWeaponItem();
		}
		return (L2Weapon) weapon.getItem();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#reduceCurrentHp(int, net.sf.l2j.gameserver.model.L2Character)
	 */
	public void reduceCurrentHp(int i, L2Character attacker) 
	{
		if (this.isInvul()) return;
		super.reduceCurrentHp(i, attacker);
		if (isDead() && getPet() != null)
		{
			getPet().unSummon(this);
		}
		
		if (attacker != null)
		{
			SystemMessage smsg = new SystemMessage(SystemMessage.S1_GAVE_YOU_S2_DMG);
			_log.fine("Attacker:"+attacker.getName());
			if (attacker instanceof L2MonsterInstance || attacker instanceof L2NpcInstance)
			{
				int mobId = ((L2NpcInstance) attacker).getNpcTemplate().getNpcId();
				_log.fine("mob id:"+mobId);
				smsg.addNpcName(mobId);
			}
			else
			{
				smsg.addString(attacker.getName());
			}
			
			smsg.addNumber(i);
			sendPacket(smsg);
		}
	}


	
	/**
	 * @param b
	 */
	public void setPartyMatchingAutomaticRegistration(boolean b)
	{
		_partyMatchingAutomaticRegistration = b;
	}


	/**
	 * @param b
	 */
	public void setPartyMatchingShowLevel(boolean b)
	{
		_partyMatchingShowLevel = b;
	}


	/**
	 * @param b
	 */
	public void setPartyMatchingShowClass(boolean b)
	{
		_partyMatchingShowClass = b;
	}


	/**
	 * @param memo
	 */
	public void setPartyMatchingMemo(String memo)
	{
		_partyMatchingMemo = memo;
	}
	
	public boolean isPartyMatchingAutomaticRegistration()
	{
		return _partyMatchingAutomaticRegistration;
	}
	
	public String getPartyMatchingMemo()
	{
		return _partyMatchingMemo;
	}
	
	public boolean isPartyMatchingShowClass()
	{
		return _partyMatchingShowClass;
	}
	
	public boolean isPartyMatchingShowLevel()
	{
		return _partyMatchingShowLevel;
	}


	/**
	 * @param client
	 * @return
	 */
	public void setTransactionRequester(L2PcInstance requestor)
	{
		_currentTransactionRequester = requestor;
	}

	/**
	 * @return
	 */
	public L2PcInstance getTransactionRequester()
	{
		return _currentTransactionRequester;
	}
	
	public boolean isTransactionInProgress()
	{
		return _currentTransactionRequester != null;
	}

	/**
	 * this method is called to make an object known to a player without
	 * triggering a spawn packet. its required for item drops, since the
	 * client already registers items when it receives the drop packet.
	 * an additional spawn packet causes duplicate items
	 * @param object
	 */
	public void addKnownObjectWithoutCreate(L2Object object)
	{
		super.addKnownObject(object);
	}
	
	
	public void addKnownObject(L2Object object)
	{
		super.addKnownObject(object);

		if (object instanceof L2ItemInstance)
		{
			SpawnItem si = new SpawnItem((L2ItemInstance) object);
			sendPacket(si);
		}
		else if (object instanceof L2NpcInstance)
		{
			NpcInfo ni = new NpcInfo((L2NpcInstance) object);
			sendPacket(ni);
		}
		else if (object instanceof L2PetInstance)
		{
			NpcInfo ni = new NpcInfo((L2PetInstance) object);
			sendPacket(ni);
		}
		else if (object instanceof L2PcInstance)
		{
			// send player info to our client
			L2PcInstance otherPlayer = (L2PcInstance) object;
			sendPacket(new CharInfo(otherPlayer));
			if (otherPlayer.getPrivateStoreType() ==1 || otherPlayer.getPrivateStoreType() == 3
					)
			{
				sendPacket(new ChangeWaitType (otherPlayer,0));
				sendPacket(new UserInfo(otherPlayer));
				sendPacket(new PrivateStoreMsgSell(otherPlayer));
			}
		
		}
		
		if (object instanceof L2Character)
		{
			L2Character obj = (L2Character) object;
			if (obj.isMoving()) 
			{
				_log.fine("Spotted object in movement, updating status");
				CharMoveToLocation mov = new CharMoveToLocation(obj);
				sendPacket(mov);
			} 
			else if (obj.isMovingToPawn())
			{
				_log.fine("Spotted object in movement to pawn, updating status");
				MoveToPawn mov = new MoveToPawn(obj, obj.getPawnTarget(), obj.getPawnOffset());
				sendPacket(mov);					
			}
		}
		
	}
	
	public void removeKnownObject(L2Object object)
	{
		super.removeKnownObject(object);
		sendPacket(new DeleteObject(object));
	}
	
	
	public void increaseLevel()
	{
		super.increaseLevel();
		
		L2LvlupData lvlData = LevelUpData.getInstance().getTemplate(getClassId());
		
		// the new HP and MP max values
		if (lvlData != null)
		{
			double hp1 = (getLevel() - 1) * lvlData.getDefaulthpadd();
			double hp2 = (getLevel() - 2) * lvlData.getDefaulthpbonus();
			double mp1 = (getLevel() - 1) * lvlData.getDefaultmpadd();
			double mp2 = (getLevel() - 2) * lvlData.getDefaultmpbonus();
			int newhp = (int) Math.rint(lvlData.getDefaulthp()+hp1+hp2);
			int newmp = (int) Math.rint(lvlData.getDefaultmp()+mp1+mp2);
			// increase hp
			setMaxHp(newhp);
			setCurrentHp(newhp);
			StatusUpdate su = new StatusUpdate(getObjectId());
			su.addAttribute(StatusUpdate.MAX_HP, newhp);
			su.addAttribute(StatusUpdate.CUR_HP, newhp);
			// increase mp
			setMaxMp(newmp);
			setCurrentMp(newmp);
			su.addAttribute(StatusUpdate.MAX_MP, newmp);
			su.addAttribute(StatusUpdate.CUR_MP, newmp);
			sendPacket(su);
			// recalculate party lvl
			if (isInParty()) getParty().recalculatePartyLevel();
		}
		else
		{
			_log.warning("No lvl up data for class id: "+getClassId());
		}						
	}
	


	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#getAtkRange()
	 */
	public int getAttackRange()
	{
		L2Weapon weapon = getActiveWeapon();
		if (weapon != null && weapon.getWeaponType() == L2Weapon.WEAPON_TYPE_BOW)
		{
			return 500; //bow range
		}

		return 36;// meelee range
	}
		

	
	public void stopAllTimers()
	{
		stopAttackTask();
		stopHitTask();
		stopMpRegeneration();
		stopHpRegeneration();
		stopMove();
	}
	
	public L2PetInstance getPet()
	{
		return _pet;
	}

	public void setPet(L2PetInstance pet)
	{
		_pet = pet;
	}
	
	public void deleteMe()
	{
		// stop all scheduled events
		stopAllTimers();

		setTarget(null);
		L2World world = L2World.getInstance();
		world.removeVisibleObject(this);
		
		if (isInParty()) leaveParty();
		
		if (getPet() != null)
		{
			getPet().unSummon(this);// returns pet to control item
		}
		
		if (getClanId() != 0)
		{
			// set the status for pledge member list to OFFLINE
			getClan().getClanMember(getName()).setPlayerInstance(null);
		}
		if (getTransactionRequester() != null)
		{
			// deals with sudden exit in the middle of transaction
			setTransactionRequester(null);
		}
		if (isGM()) GmListTable.getInstance().deleteGm(this);
		removeAllKnownObjects();
		setNetConnection(null);
		world.removeObject(this);
	}

	
	/**
	 * 
	 */
	public void updatePAtk()
	{
		//level bonus
		double lvlmod = (100.0 - 11 +getLevel()) / 100.0;
		
		//STR modifier
		L2Modifiers modifier = Modifiers.getInstance().getTemplate(getClassId());
		double strmod = 1;
		if (modifier != null) 
		{
			strmod = (100.0 + modifier.getModstr()) / 100.0;
		} 
		else 
		{
			_log.warning("Missing STR modifier for classId: "+getClassId());			
		}

		//base patk
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(getClassId()); 
		double weapondmg = 1; //dummy value
		if (template != null) 
		{
			weapondmg = template.getPatk();
		} 
		else 
		{
			_log.warning("Missing template for classId: "+getClassId());
		}
		
		//weapon patk
		L2Weapon weapon = getActiveWeapon();
		if (weapon != null) 
		{
			weapondmg += weapon.getPDamage();
		}

		//total
		double pAtk = weapondmg * lvlmod * strmod;
		setPhysicalAttack((int) Math.rint(pAtk));
		
		_log.fine("new patk: "+pAtk + " weapon patk: "+weapondmg);
	}
	
	public void updatePDef()
	{
		L2Armor armorPiece;
		L2ItemInstance dummy;

		//level bonus
		double lvlmod = (100.0 - 11 +getLevel()) / 100.0;
		
		//base pdef
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(getClassId()); 
		double  totalItemDef = 40; //dummy value
		if (template != null) 
		{
			totalItemDef = template.getPdef();
		} 
		else 
		{
			_log.warning("Missing template for classId: "+getClassId());
		}

		//item pdef
		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_BACK);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}
		
		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}
		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_UNDER);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getPDef();
		}

		//total
		double pDef = totalItemDef * lvlmod;
		setPhysicalDefense((int) Math.round(pDef)); 
		_log.fine(getObjectId()+": new pdef: "+pDef);
	}

	public void updateMAtk()
	{
		//level bonus
		double lmod = (100.0 - 11 +getLevel()) / 100.0;
		double lvlmod = Math.sqrt(lmod);

		//INT modifier
		L2Modifiers modifier = Modifiers.getInstance().getTemplate(getClassId());
		double imod  = 1;
		if (modifier != null) 
		{
			imod = (100.0 + modifier.getModint()) / 100.0;
		} 
		else 
		{
			_log.warning("Missing INT modifier for classId: "+getClassId());
		}
		double intmod = Math.sqrt(imod);

		//base matk
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(getClassId()); 
		double weapondmg = 1; //dummy value
		if (template != null) 
		{
			weapondmg = template.getMatk();
		} 
		else 
		{
			_log.warning("Missing template for classId: "+getClassId());
		}

		//weapon matk
		L2Weapon weapon = getActiveWeapon();
		if (weapon != null) weapondmg += weapon.getMDamage();
				
		//total
		double mAtk = weapondmg * lvlmod * intmod; 
		setMagicalAttack((int) Math.rint(mAtk));
		_log.fine("new matk: "+mAtk + " weapon matk: "+weapondmg);
	}
	
	public void updateMDef()
	{
		L2Armor armorPiece;
		L2ItemInstance dummy;

		//lvl bonus
		double lvlBonus = (100.0 - 11 +getLevel()) / 100.0;
		
		//men modifier
		L2Modifiers modifier = Modifiers.getInstance().getTemplate(getClassId());
		double MENbonus = 1;
		if (modifier != null) 
		{
			MENbonus = (100.0 + modifier.getModmen()) / 100.0;
		} 
		else 
		{
			_log.warning("Missing MEN modifier for classId: "+getClassId());
		}
		
		//base mdef
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(getClassId()); 
		double totalItemDef = 40; //dummy value
		if (template != null) 
		{
			totalItemDef = template.getMdef();
		} 
		else 
		{
			_log.warning("Missing template for classId: "+getClassId());
		}
		
		//item mdef
		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEAR);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getMDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LFINGER);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getMDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_NECK);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getMDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_REAR);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getMDef();
		}

		dummy = getInventory().getPaperdollItem(Inventory.PAPERDOLL_RFINGER);
		if (dummy != null)
		{
			armorPiece = (L2Armor) dummy.getItem();
			totalItemDef += armorPiece.getMDef();
		}

		//total
		double mDef = totalItemDef * lvlBonus * MENbonus;
		setMagicalDefense((int) Math.round(mDef)); 
		_log.fine(getObjectId()+": new mdef: "+mDef);
	}
	
	public void setTradeList(L2TradeList x)
	{
		_tradeList = x;
	}

	public L2TradeList getTradeList()
	{
		return _tradeList;
	}
	public void setSellList(ArrayList x)
	{
		_sellList = x;
	}

	public ArrayList getSellList()
	{
		return _sellList;
	}
	public void setBuyList(ArrayList x)
	{
		_buyList = x;
	}

	public ArrayList getBuyList()
	{
		return _buyList;
	}
	public void setPrivateStoreType(int type)
	{
		_privatestore = type;
	}
	public int getPrivateStoreType()//sellmanage=2,sellset=1,buymanage=4,buyset=3
	{
		return _privatestore;
	}

	/**
	 * @param clan
	 */
	public void setClan(L2Clan clan)
	{
		_clan = clan;
	}

	public L2Clan getClan()
	{
		return _clan;
	}

	/**
	 * @param b
	 */
	public void setIsClanLeader(boolean b)
	{
		_clanLeader = b;
	}
	
	public boolean isClanLeader()
	{
		return _clanLeader;
	}

	public void useMagic(L2Skill skill)
	{
		L2Character target = null;
		
		//if (getNewTarget() instanceof L2Character)
		if (getTarget() instanceof L2Character)
		{
			//setTarget(getNewTarget());
			//target = (L2Character)getNewTarget();
			target = (L2Character)getTarget();
		}
		else
		{
			target = this;
		}
		
		if ((skill.getTargetType() == L2Skill.TARGET_SELF) || (skill.getTargetType() == L2Skill.TARGET_PARTY))
		{
			target = this;
		}
		
		if ((skill.getTargetType() == L2Skill.TARGET_ONE) && (target == this))
		{
			_log.fine("Attack magic has no target or target oneself.");
			return;
		}

		
		if (isDead() || target.isDead() || _allSkillsDisabled || isSkillDisabled(skill.getId()) || skill.isPassive())
		{
			return;
		}
		
		int weaponType = getActiveWeapon().getWeaponType();
		int skillId = skill.getId();
		if ((skillId == DamageSkill.POWER_SHOT) && weaponType != L2Weapon.WEAPON_TYPE_BOW)
		{
			return;
		}
	    else if ((skillId == DamageSkill.MORTAL_BLOW) && weaponType != L2Weapon.WEAPON_TYPE_DAGGER)
	    {
	    	return;
	    }
	    else if ((skillId == DamageSkill.POWER_STRIKE) && (weaponType != L2Weapon.WEAPON_TYPE_BLUNT && weaponType != L2Weapon.WEAPON_TYPE_SWORD))
	    {
	    	return;
	    }
	    else if ((skillId == DamageSkill.IRON_PUNCH) && weaponType != L2Weapon.WEAPON_TYPE_DUALFIST)
	    {
	    	return;
	    }
	    else if (SkillHandler.getInstance().getSkillHandler(skill.getId()) == null)
	    {
	    	SystemMessage sm = new SystemMessage(614);
			sm.addString("This skill is not implemented yet"); // temp sysmsg, remove it after we have all the skills implemented
			sendPacket(sm);
	    	return;
	    }
		
		if (getCurrentMp() < skill.getMpConsume())
		{
			sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_MP));
			return;
		}
		
		if (getCurrentHp() < skill.getHpConsume())
		{
			sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_HP));
			return;
		}
				
		setCurrentState(STATE_CASTING);
		setSkill(skill);
		
		double distance = getDistance(target.getX(), target.getY());
		if (skill.getCastRange() > 0 && distance > skill.getCastRange())
		{
			moveTo(target.getX(), target.getY(), target.getZ(), skill.getCastRange());
			return;
		}
		
		int magicId = skill.getId();
		int level = getSkillLevel(magicId);
		ActionFailed af = new ActionFailed();
		sendPacket(af);
		
		//lauching magic
		MagicSkillUser msu = new MagicSkillUser(this, target, magicId, level, skill.getSkillTime(), skill.getReuseDelay());
		sendPacket(msu);
		broadcastPacket(msu);
		
		//gauge bar
		SetupGauge sg = new SetupGauge(0, skill.getSkillTime());
		sendPacket(sg);
		
		//system message
		SystemMessage sm = new SystemMessage(SystemMessage.USE_S1);
		sm.addSkillName(magicId);
		sendPacket(sm);
		
		if (skill.getSkillTime() > 300)
		{
			disableSkill(skill.getId(), true);
			_enableSkillTimer.schedule(new EnableSkill(skill.getId()), skill.getReuseDelay());
			disableAllSkills();
			_enableAllSkillsTimer.schedule(new EnableAllSkills(skill), skill.getSkillTime());
			_magicUseTimer.schedule(new MagicUseTask(target, skill), skill.getSkillTime()-300);
		}
	}
	
	protected void reduceArrowCount()
	{
		L2ItemInstance arrows = getInventory().destroyItem(getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1);
		_log.info("arrow count:" + arrows.getCount());
		if (arrows.getCount() == 0)
		{
			getInventory().unEquipItemOnPaperdoll(Inventory.PAPERDOLL_LHAND);
			_arrowItem = null;
			_log.info("removed arrows count");
			sendPacket(new ItemList(this,false));
		}
		else
		{
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(arrows);
			sendPacket(iu);
		}

//		if (getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND).getCount() < 0)
//		{
//			getInventory().unEquipItemOnPaperdoll(Inventory.PAPERDOLL_LHAND);
//		}
//		else
//		{
//		}
//		
//		ItemList il = new ItemList(this, false);
//		sendPacket(il);
	}
	
	protected boolean checkAndEquipArrows()
	{
		if (getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND) == null)
		{
			_arrowItem = getInventory().findArrowForBow(getActiveWeapon());
			
			if ( _arrowItem != null)
			{
				getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, _arrowItem);
				ItemList il = new ItemList((L2PcInstance)this, false);
				sendPacket(il);
			}
		}
		else
		{
			_arrowItem = getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		}
		
		return _arrowItem != null;
	}
	
	protected boolean isUsingDualWeapon()
	{
		L2Weapon weaponItem = getActiveWeapon();
		if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUAL)
		{
			return true;
		}
		else if (weaponItem.getWeaponType() == L2Weapon.WEAPON_TYPE_DUALFIST)
		{
			return true;
		}
		else if (weaponItem.getItemId() == 248) // orc fighter fists
		{
			return true;
		}
		else if (weaponItem.getItemId() == 252) // orc mage fists
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setUptime(long time)
	{
		_uptime = time;
	}
	
	public long getUptime()
	{
		return System.currentTimeMillis()-_uptime;
	}
	
	public void onMagicUseTimer(L2Character target, L2Skill skill) throws IOException
	{
		//_log.fine("\tcurrentstate:"+getCurrentState());
		//_log.fine("\tallskillsdisabled:"+_allSkillsDisabled);
		//_log.fine("\tisskilldisabled:"+isSkillDisabled(skill.getId()));
		//_log.fine("\tskill.getname:"+skill.getName());
		//_log.fine("\tgetskill.getname:"+getSkill().getName());
		if (getCurrentState() == STATE_CASTING && _allSkillsDisabled && isSkillDisabled(skill.getId()) && getSkill() == skill)
		{
			int magicId = skill.getId();
			int level = getSkillLevel(magicId);
			
			
			if (skill.getTargetType() == L2Skill.TARGET_PARTY && isInParty())
			{
				Iterator it = getParty().getPartyMembers().iterator();
				while (it.hasNext()) {					
					L2PcInstance player = (L2PcInstance)it.next();
					_log.fine("msl: "+getName()+" "+magicId+" "+level+" "+player.getName());
					MagicSkillLaunched msl = new MagicSkillLaunched(this, magicId, level, player);
					sendPacket(msl);
					broadcastPacket(msl);
				}
			}
			else
			{
				MagicSkillLaunched msl = new MagicSkillLaunched(this, magicId, level, target);
				_log.fine("msl: "+getName()+" "+magicId+" "+level+" "+target.getName());
				sendPacket(msl);
				broadcastPacket(msl);
			}
			
			reduceCurrentMp(skill.getMpConsume());
			StatusUpdate su = new StatusUpdate(getObjectId());
			su.addAttribute(StatusUpdate.CUR_MP, (int) getCurrentMp());
			sendPacket(su);
			
			ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getId());
			if (handler == null)
			{
				_log.warning("no skillhandler registered for skillId:" + skill.getId());
			}
			else
			{
				handler.useSkill(this, skill, target);
			}
			
			setCurrentState(STATE_IDLE);
		}
	}
	
	public void disableSkill(int skillId, boolean state)
	{
		_disabledSkills.put(new Integer(skillId), new Boolean(state));
	}
	
	public boolean isSkillDisabled(int skillId)
	{
		try
		{
			return ((Boolean)_disabledSkills.get(new Integer(skillId))).booleanValue();
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public void disableAllSkills()
	{
		_log.fine("all skills disabled");
		_allSkillsDisabled = true;
	}
	
	protected void enableAllSkills()
	{
		_log.fine("all skills enabled");
		_allSkillsDisabled = false;
	}
	
	//invulnerability
	public void setIsInvul(boolean b)
	{
	 	_isInvul = b;
	}
	
	public boolean isInvul()
	{
		return _isInvul;
	}
	
	/**
	 * true if player has a party
	 * @return
	 */
	public boolean isInParty() 
	{
		return _party != null;
	}
	
	/**
	 * set players party without joining it (good for creating)
	 * @param party
	 */
	public void setParty(L2Party party) 
	{
		_party = party;
	}
	
	/**
	 * sets & joins party
	 * @param party
	 */
	public void joinParty(L2Party party) 
	{
		_party = party;
		_party.addPartyMember(this);
	}
	
	/**
	 * leave party
	 *
	 */
	public void leaveParty() 
	{
		if (isInParty()) 
		{
			_party.removePartyMember(this);
			_party = null;
		}
	}
	
	/**
	 * returns players party
	 * @return
	 */
	public L2Party getParty() 
	{
		return _party;
	}
	/*
	public L2Object getNewTarget()
	{
		return _newTarget;
	}
	public void setNewTarget(L2Object object)
	{
		_newTarget = object;
	}
	*/
	public void setIsGM(boolean status) 
	{
		_isGm = status;
	}
	public boolean isGM()
	{
		return _isGm;
	}
	
	public void setInteractTarget(L2Character target) 
	{
		_interactTarget = target;
	}
	
	public void cancelCastMagic() 
	{
		setCurrentState(L2Character.STATE_IDLE);
		enableAllSkills();
		MagicSkillCanceld msc = new MagicSkillCanceld(getObjectId());
		sendPacket(msc);
		broadcastPacket(msc);
	}
}
