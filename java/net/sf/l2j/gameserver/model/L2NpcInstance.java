/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2NpcInstance.java,v 1.30 2004/10/21 00:03:43 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/21 00:03:43 $
 * $Revision: 1.30 $
 * $Log: L2NpcInstance.java,v $
 * Revision 1.30  2004/10/21 00:03:43  nuocnam
 * skill animation is canceled properly when changing target during casting
 *
 * Revision 1.29  2004/10/20 19:22:31  nuocnam
 * PLayers now remember L2NpcInstance they interact with before moving, so getTarget() is not used when they reach INTERACTION_DISTANCE
 *
 * Revision 1.28  2004/10/20 16:59:42  whatev66
 * interact state now used when talking to npcs which are not attackable
 *
 * Revision 1.27  2004/10/20 00:54:40  nuocnam
 * Temporary removed newTarget and anything related ot it. Seems that check done in L2NpcInstance in onAction is not necessary as attack handler refuses to attack twice elsewhere.
 *
 * Revision 1.26  2004/10/17 19:02:38  nuocnam
 * fixed getSpReward() (Myrdos)
 *
 * Revision 1.25  2004/10/15 18:27:31  nuocnam
 * putting interaction distance back to 150 as testing showed 50 was really too little
 *
 * Revision 1.24  2004/10/14 23:37:43  nuocnam
 * interaction distance set to 50
 *
 * Revision 1.23  2004/10/14 16:45:51  nuocnam
 * set&getNewTarget methods works with L2Object now
 *
 * Revision 1.22  2004/10/08 19:06:13  whatev66
 * attackable onaction changed to do nothing on repetition.
 *
 * Revision 1.21  2004/09/30 02:35:21  nuocnam
 * - corrected interaction distance (sh1ny, nuocnam)
 *
 * Revision 1.20  2004/09/19 04:42:22  nuocnam
 * Added configurable xp, sp and adena rates (myrdos, nuocnam)
 *
 * Revision 1.19  2004/09/16 23:02:40  dalrond
 * Moved DecayTask from here to L2Character because it applies to pets
 *
 * Revision 1.18  2004/09/15 23:51:34  l2chef
 * npc info is only shown to gms (Deth)
 *
 * Revision 1.17  2004/08/14 22:08:38  l2chef
 * spawnhandler is notified on decay, not on death
 * decay is started only once
 *
 * Revision 1.16  2004/08/12 23:52:01  l2chef
 * player cannot attack creatures if they are too far away in z coord
 *
 * Revision 1.15  2004/08/11 22:20:48  l2chef
 * if html files are missing the npc will tell the player
 *
 * Revision 1.14  2004/07/23 02:23:21  l2chef
 * npc chat dialogs unified (NuocNam)
 *
 * Revision 1.13  2004/07/23 01:47:51  l2chef
 * spawn handler is notified when npc dies
 *
 * Revision 1.12  2004/07/19 02:06:45  l2chef
 * decay timer got moved here
 *
 * Revision 1.11  2004/07/18 17:56:41  l2chef
 * npcs change to running if they attack
 *
 * Revision 1.10  2004/07/18 17:38:38  l2chef
 * aggressive monsters scan for targets in range (FTPW)
 *
 * Revision 1.9  2004/07/17 23:17:41  l2chef
 * attackrange and forced attack added
 *
 * Revision 1.8  2004/07/11 22:12:50  l2chef
 * changed comment
 *
 * Revision 1.7  2004/07/11 11:48:20  l2chef
 * exp and sp values for NPCs added
 *
 * Revision 1.6  2004/07/08 22:22:57  l2chef
 * common stuff moved to L2NpcInstance
 *
 * Revision 1.5  2004/07/07 23:41:04  l2chef
 * new merchant conversation files (done by NightMarez)
 * some design changes
 *
 * Revision 1.4  2004/07/04 19:05:25  l2chef
 * show default response message when talking to npc
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

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.RatesController;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.ChangeMoveType;
import net.sf.l2j.gameserver.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.SetToLocation;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.templates.*;

/**
 * This class represents a Non-Player-Character in the world. it can be 
 * a monster or a friendly character.
 * it also uses a template to fetch some static values.
 * the templates are hardcoded in the client, so we can rely on them.
 * 
 * @version $Revision: 1.30 $ $Date: 2004/10/21 00:03:43 $
 */
public class L2NpcInstance extends L2Character
{
	private static Logger _log = Logger.getLogger(L2NpcInstance.class.getName());
	private static final int INTERACTION_DISTANCE = 150;
	private L2Npc _npcTemplate;
	private boolean _attackable;
	private int _rightHandItem;
	private int _leftHandItem;
	
	private int _expReward;
	private int _spReward;
	private int _attackRange;
	
	private boolean _aggressive;
	private DecayTask _decayTask;
	private static Timer _decayTimer = new Timer(true);
	private L2Spawn _spawn;

	
	public L2NpcInstance(L2Npc template)
	{
		_npcTemplate = template;
		setCollisionHeight(template.getHeight());
		setCollisionRadius(template.getRadius());
	}
	
	public boolean isAggressive()
	{
		return _aggressive;
	}
	
	public void startAttack(L2Character target)
	{
		if (!isRunning())
		{
			setRunning(true);
			ChangeMoveType move = new ChangeMoveType(this, ChangeMoveType.RUN);
			broadcastPacket(move);
		}
		
		super.startAttack(target);
	}
	
	public void setAggressive(boolean aggressive)
	{
		_aggressive = aggressive;
	}

	/**
	 * @return
	 */
	public L2Npc getNpcTemplate()
	{
		return _npcTemplate;
	}
	
	

	/**
	 * @return
	 */
	public boolean isAttackable()
	{
		return _attackable;
	}

	/**
	 * @param b
	 */
	public void setAttackable(boolean b)
	{
		_attackable = b;
	}

	/**
	 * @return
	 */
	public int getLeftHandItem()
	{
		return _leftHandItem;
	}

	/**
	 * @return
	 */
	public int getRightHandItem()
	{
		return _rightHandItem;
	}

	/**
	 * @param i
	 */
	public void setLeftHandItem(int i)
	{
		_leftHandItem = i;
	}

	/**
	 * @param i
	 */
	public void setRightHandItem(int i)
	{
		_rightHandItem = i;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Object#onAction(net.sf.l2j.gameserver.model.L2PcInstance)
	 */
	public void onAction(L2PcInstance player)
	{
		//if (this != player.getNewTarget())
		if (this != player.getTarget())
		{
			//TODO: see if we can avoid setting STATE_IDLE here and use player.setTarget(this) to stop casting magic exclusively
			if (player.getCurrentState() == L2Character.STATE_CASTING) {
				player.cancelCastMagic();
			}
			player.setCurrentState(L2Character.STATE_IDLE);
			_log.fine("new target selected:"+getObjectId());
			//player.setNewTarget(this);
			player.setTarget(this);
			MyTargetSelected my = new MyTargetSelected(getObjectId(), player.getLevel() - getLevel());
			player.sendPacket(my);
			
			if (isAttackable())
			{	
				StatusUpdate su = new StatusUpdate(getObjectId());
				su.addAttribute(StatusUpdate.CUR_HP, (int)getCurrentHp() );
				su.addAttribute(StatusUpdate.MAX_HP, getMaxHp() );
				player.sendPacket(su);
			}
			
			// correct location
			player.sendPacket(new SetToLocation(this));
		}
		else
		{
			if (isAttackable() && !isDead() && (!player.isInCombat()))// || player.getNewTarget() != player.getTarget()))
			{
				if (Math.abs(player.getZ() - getZ()) < 200)
				{
					player.startAttack(this);
				}
				else
				{
					//player.sendPacket(new ActionFailed());
				}
			}
			if(!isAttackable())
			{
				double distance = getDistance(player.getX(), player.getY());
				if (distance > INTERACTION_DISTANCE) {
					player.setCurrentState(L2Character.STATE_INTERACT);
					player.setInteractTarget(this);
					player.moveTo(this.getX(), this.getY(), this.getZ(), INTERACTION_DISTANCE);
				} else {
					showChatWindow(player, 0);
					player.sendPacket(new ActionFailed());					
					player.setCurrentState(L2Character.STATE_IDLE);
				}
			}
		}
	}
	
	public void onActionShift(ClientThread client)
	{
		L2PcInstance player = client.getActiveChar();
		if (client.getAccessLevel() >= 100)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(1);
			StringBuffer html1 = new StringBuffer("<html><body><table border=0>");
			html1.append("<tr><td>Current Target:</td></tr>");
			html1.append("<tr><td><br></td></tr>");
			
			html1.append("<tr><td>Object ID: "+getObjectId()+"</td></tr>");
			html1.append("<tr><td>Template ID: "+getNpcTemplate().getNpcId()+"</td></tr>");
			html1.append("<tr><td><br></td></tr>");
			
			html1.append("<tr><td>HP: "+getCurrentHp()+"</td></tr>");
			html1.append("<tr><td>MP: "+getCurrentMp()+"</td></tr>");
			html1.append("<tr><td>Level: "+getLevel()+"</td></tr>");
			html1.append("<tr><td><br></td></tr>");
			
			html1.append("<tr><td>Class: "+getClass().getName()+"</td></tr>");
			html1.append("<tr><td><br></td></tr>");
			
			html1.append("<tr><td><button value=\"Kill\" action=\"bypass -h admin_kill "+getObjectId()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			html1.append("<tr><td><button value=\"Delete\" action=\"bypass -h admin_delete "+getObjectId()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			html1.append("</table></body></html>");
			
			html.setHtml(html1.toString());
			player.sendPacket(html);
		}
		else
		{
			// attack the mob without moving?
		}
		
		player.sendPacket(new ActionFailed());
	}

	/**
	 * this gets called by the RequestBypassToServer packet handler
	 * override it in subclasses to use a different behavior
	 * @param command
	 */
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		double distance = getDistance(player.getX(), player.getY());
		if (distance > INTERACTION_DISTANCE) {
			player.moveTo(this.getX(), this.getY(), this.getZ(), INTERACTION_DISTANCE);
		} else {
			if (command.startsWith("Quest"))
			{
				int val = Integer.parseInt(command.substring(6));
				showQuestWindow(player, val);
			}
			else if (command.startsWith("Chat"))
			{
				int val = Integer.parseInt(command.substring(5));
				showChatWindow(player, val);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#getActiveWeapon()
	 */
	public L2Weapon getActiveWeapon()
	{
		// regular NPCs dont have weapons, so they cant fight
		return null;
	}

	/**
	 * @param player
	 * @param content
	 */
	public void insertObjectIdAndShowChatWindow(L2PcInstance player, String content)
	{
		
		content = content.replaceAll("%objectId%", String.valueOf(getObjectId()));
		NpcHtmlMessage npcReply = new NpcHtmlMessage(5);
		
		npcReply.setHtml(content);
		player.sendPacket(npcReply);
	}

	protected void showQuestWindow(L2PcInstance player, int val)
	{
		_log.fine("Showing quest window");
		
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setHtml("<html><head><body>There is no quests here yet.</body></html>");
		player.sendPacket(html);
		
		player.sendPacket( new ActionFailed() );
	}

	
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
		
		String temp = "data/html/default/" + pom + ".htm"; 
		File mainText = new File(temp);
		if (mainText.exists())
		{
			return temp;
		}
		else
		{
			return "data/html/npcdefault.htm";
		}
	}

	public void showChatWindow(L2PcInstance player, int val)
	{
		int npcId = getNpcTemplate().getNpcId();
		
		String filename = (getHtmlPath(npcId, val));
		
		File file = new File(filename);
		if (!file.exists())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(1);
			html.setHtml("<html><head><body>My Text is missing:<br>"+filename+"</body></html>");
			player.sendPacket(html);
			
			player.sendPacket( new ActionFailed() );
		}
		else
		{
			FileInputStream fis = null;		
			try
			{
				fis = new FileInputStream(file);
				byte[] raw = new byte[fis.available()];
				fis.read(raw);
				
				String content = new String(raw, "UTF-8");
				
				insertObjectIdAndShowChatWindow(player, content);
				
			}
			catch (Exception e)
			{
				_log.warning("problem with npc text "+e);
			}
			finally
			{
				try	{ fis.close(); } catch (Exception e1) {/* ignore this */}
			}
		}
		
		player.sendPacket( new ActionFailed() );
	}

	/**
	 * @param exp
	 */
	public void setExpReward(int exp)
	{
		_expReward = exp;
	}

	/**
	 * @param sp
	 */
	public void setSpReward(int sp)
	{
		_spReward = sp;
	}
	
	public int getExpReward()
	{
		return (_expReward * RatesController.getInstance().getExpRate());
	}
	
	public int getSpReward()
	{
		return (_spReward * RatesController.getInstance().getSpRate());
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#getAtkRange()
	 */
	public int getAttackRange()
	{
		return _attackRange; 
	}
	
	public void setAttackRange(int range)
	{
		_attackRange = range;
	}
	
	public void reduceCurrentHp(int i, L2Character attacker)
	{
		super.reduceCurrentHp(i, attacker);

		if (isDead())
		{
			synchronized(this)
			{
				if (_decayTask == null)
				{
					// in 7 seconds this corpse will disappear
					_decayTask = new DecayTask(this);
					_decayTimer.schedule(_decayTask, 7000);
				}
			}
		}
	}

	/**
	 * @param spawn
	 */
	public void setSpawn(L2Spawn spawn)
	{
		_spawn = spawn;
	}
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.model.L2Character#onDecay()
	 */
	
	public void onDecay()
	{
		super.onDecay();

		// tell my creator that I'm gone
		_spawn.decreaseCount(_npcTemplate.getNpcId());
	}
	
	public void deleteMe()
	{
		//FIXME this is just a temp hack, we should find a better solution
		L2World.getInstance().removeVisibleObject(this);
		L2World.getInstance().removeObject(this);
		removeAllKnownObjects();
	}
}
