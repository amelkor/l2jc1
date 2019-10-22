/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Party.java,v 1.4 2004/10/17 19:01:53 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/17 19:01:53 $
 * $Revision: 1.4 $
 * $Log: L2Party.java,v $
 * Revision 1.4  2004/10/17 19:01:53  nuocnam
 * few optimizations (dragon666)
 *
 * Revision 1.3  2004/10/05 14:37:14  nuocnam
 * added recalculatePartyLevel() method
 *
 * Revision 1.2  2004/09/28 02:23:46  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/09/27 08:42:30  nuocnam
 * New class regrouping all party methods.
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

import java.util.ArrayList;
import java.util.Random;

import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.PartySmallWindowAdd;
import net.sf.l2j.gameserver.serverpackets.PartySmallWindowAll;
import net.sf.l2j.gameserver.serverpackets.PartySmallWindowDelete;
import net.sf.l2j.gameserver.serverpackets.PartySmallWindowDeleteAll;
import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @author nuocnam
 * @version $Revision: 1.4 $ $Date: 2004/10/17 19:01:53 $
 */
public class L2Party {
	private static Random _rnd = new Random();

	private ArrayList _members = null;
	private boolean _randomLoot = false;
	private int _partyLvl = 0; //party level = sum of squares of levels of all party members
	
	/**
	 * constructor ensures party has always one member - leader
	 * @param leader
	 * @param randomLoot
	 */
	public L2Party(L2PcInstance leader, boolean randomLoot) {
		_members = new ArrayList();
		_randomLoot = randomLoot;
		_members.add(leader);
		_partyLvl = leader.getLevel()*leader.getLevel();
	}
	
	/**
	 * returns number of party members
	 * @return
	 */
	public int getMemberCount() {
		return _members.size();
	}

	/**
	 * returns all party members
	 * @return
	 */
	public ArrayList getPartyMembers() {
		return _members;
	}
	
	/**
	 * get random member from party
	 * @return
	 */
	private L2PcInstance getRandomMember() {
		return (L2PcInstance) _members.toArray()[_rnd.nextInt(_members.size())];
	}

	/**
	 * true if player is party leader
	 * @param player
	 * @return
	 */
	public boolean isLeader(L2PcInstance player) {
		return (_members.get(0).equals(player));
	}
		
	/**
	 * Broadcasts packet to every party member 
	 * @param msg
	 */
	public void broadcastToPartyMembers(ServerBasePacket msg) {
		for(int i = 0; i < _members.size(); i++) {
			L2PcInstance member = (L2PcInstance) _members.get(i);
			member.sendPacket(msg);
		}
	}

	/**
	 * overloaded method broadcasts packet to every party member but player
	 * @param player
	 * @param msg
	 */
	public void broadcastToPartyMembers(L2PcInstance player, ServerBasePacket msg) {
		for(int i = 0; i < _members.size(); i++) {
			L2PcInstance member = (L2PcInstance) _members.get(i);
			if (!member.equals(player)) {
				member.sendPacket(msg);
			}
		}
	}
	
	/**
	 * adds new member to party
	 * @param player
	 */
	public void addPartyMember(L2PcInstance player) {		
		//sends new member party window for all members
		//we do all actions before adding member to a list, this speeds things up a little
		PartySmallWindowAll window = new PartySmallWindowAll();
		window.setPartyList(_members);
		player.sendPacket(window);
		
		SystemMessage msg = new SystemMessage(SystemMessage.YOU_JOINED_S1_PARTY);
		msg.addString(((L2PcInstance)_members.get(0)).getName());
		player.sendPacket(msg);
		
		msg = new SystemMessage(SystemMessage.S1_JOINED_PARTY);
		msg.addString(player.getName());
		broadcastToPartyMembers(msg);
		broadcastToPartyMembers(new PartySmallWindowAdd(player));
		
		//add player to party, adjust party level
		_members.add(player);
		_partyLvl += player.getLevel()*player.getLevel();
	}
	
	/**
	 * removes player from party
	 * @param player
	 */
	public void removePartyMember(L2PcInstance player) {
		if (_members.contains(player)) {
			_members.remove(player);
			_partyLvl -= player.getLevel()*player.getLevel();
			
			SystemMessage msg = new SystemMessage(SystemMessage.YOU_LEFT_PARTY);
			player.sendPacket(msg);
			player.sendPacket(new PartySmallWindowDeleteAll());
			player.setParty(null);
			
			msg = new SystemMessage(SystemMessage.S1_LEFT_PARTY);
			msg.addString(player.getName());
			broadcastToPartyMembers(msg);
			broadcastToPartyMembers(new PartySmallWindowDelete(player));
			
			if (_members.size() == 1) {
				((L2PcInstance)_members.get(0)).setParty(null);
			}
		}
	}

	/**
	 * finds a player in the party by name
	 * @param name
	 * @return
	 */
	private L2PcInstance getPlayerByName(String name) {
		for(int i = 0; i < _members.size(); i++) {
			L2PcInstance temp = (L2PcInstance) _members.get(i);
			if (temp.getName().equals(name)) return temp;
		}
		return null;
	}

	/**
	 * Oust player from party
	 * @param player
	 */
	public void oustPartyMember(L2PcInstance player) {
		if (_members.contains(player)) {
			if (isLeader(player)) {
				dissolveParty();
			} else {
				removePartyMember(player);
			}
		}
	}

	/**
	 * Oust player from party
	 * Overloaded method that takes player's name as parameter
	 * @param name
	 */
	public void oustPartyMember(String name) {
		L2PcInstance player = getPlayerByName(name);
		
		if (player != null) {
			if (isLeader(player)) {
				dissolveParty();
			} else {
				removePartyMember(player);
			}
		}
	}
	
	/**
	 * dissolves entire party
	 *
	 */
	private void dissolveParty() {
		SystemMessage msg = new SystemMessage(SystemMessage.PARTY_DISPERSED);
		for(int i = 0; i < _members.size(); i++) {
			L2PcInstance temp = (L2PcInstance) _members.get(i);
			temp.sendPacket(msg);
			temp.sendPacket(new PartySmallWindowDeleteAll());
			temp.setParty(null);
		}
	}

	
	/**
	 * distribute item(s) to party members
	 * @param player
	 * @param item
	 */
	public void distributeItem(L2PcInstance player, L2ItemInstance item) {
		L2PcInstance target = null;
		if (_randomLoot) {
			target = getRandomMember();
		} else {
			target = player;
		}
		
		if (item.getCount() == 1) {
			SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1);
			smsg.addItemName(item.getItemId());
			target.sendPacket(smsg);

			smsg = new SystemMessage(SystemMessage.S1_PICKED_UP_S2);
			smsg.addString(target.getName());
			smsg.addItemName(item.getItemId());
			broadcastToPartyMembers(target, smsg);
		} else {
			SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_S2);
			smsg.addNumber(item.getCount());
			smsg.addItemName(item.getItemId());
			target.sendPacket(smsg);

			smsg = new SystemMessage(SystemMessage.S1_PICKED_UP_S2_S3);
			smsg.addString(target.getName());
			smsg.addNumber(item.getCount());
			smsg.addItemName(item.getItemId());
			broadcastToPartyMembers(target, smsg);
		}
	
		L2ItemInstance item2 = target.getInventory().addItem(item);
		InventoryUpdate iu = new InventoryUpdate();
		if (item2.getLastChange() == L2ItemInstance.ADDED) {
			iu.addNewItem(item);
		} else {
			iu.addModifiedItem(item2);
		}
		
		target.sendPacket(iu);
		
		//TODO send only weight update, if possible
		UserInfo ci = new UserInfo(target);
		target.sendPacket(ci);
	}
	
	/**
	 * distribute adena to party members
	 * @param adena
	 */
	public void distributeAdena(L2ItemInstance adena) {
		adena.setCount(adena.getCount()/_members.size());
		
		SystemMessage smsg = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_ADENA);
		smsg.addNumber(adena.getCount());
		
		for(int i = 0; i < _members.size(); i++) {
			L2PcInstance member = (L2PcInstance) _members.get(i);
			L2ItemInstance item2 = member.getInventory().addItem(adena);
			InventoryUpdate iu = new InventoryUpdate();
			if (item2.getLastChange() == L2ItemInstance.ADDED) {
				iu.addNewItem(adena);
			} else {
				iu.addModifiedItem(item2);
			}
			member.sendPacket(smsg);
			member.sendPacket(iu);
		}
	}
		
	/**
	 * distributes XP and SP to party members
	 * takes total party dmg and monsters maxHp, xp rewars and sp reward as parameter
	 * @param partyDmg
	 * @param maxHp
	 * @param xpReward
	 * @param spReward
	 */
	public void distributeXpAndSp(int partyDmg, int maxHp, int xpReward, int spReward) {
		
		double mul = (1+.07*(_members.size()-1))*partyDmg / maxHp;
				
		double xpTotal = mul*xpReward;
		double spTotal = mul*spReward;
		
		for(int i = 0; i < _members.size(); i++){
			L2PcInstance player = (L2PcInstance) _members.get(i);
			mul = player.getLevel()*player.getLevel()/_partyLvl;
			int xp = (int)(mul*xpTotal);
			int sp = (int)(mul*spTotal); 
			player.addExpAndSp(xp, sp);
		}		
	}
	
	/**
	 * refresh party level
	 *
	 */
	public void recalculatePartyLevel() {
		int newlevel = 0;
		int plLevel;
		for (int i = 0; i < _members.size(); i++)
		{
			plLevel = ((L2PcInstance)_members.get(i)).getLevel();
			newlevel += plLevel*plLevel;
		}
		_partyLvl = newlevel;
	}
}
