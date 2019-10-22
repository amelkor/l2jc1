/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/itemhandlers/PetSummon.java,v 1.9 2004/09/20 22:39:47 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/09/20 22:39:47 $
 * $Revision: 1.9 $
 * $Log: PetSummon.java,v $
 * Revision 1.9  2004/09/20 22:39:47  whatev66
 * you are added to pets knownobjects on summon
 *
 * Revision 1.8  2004/09/20 12:27:57  dalrond
 * Now sets the level upper and lower limits (needed in packet)
 *
 * Revision 1.7  2004/09/19 21:46:21  dalrond
 * Sets a pets default experience in relation to its base level in data files.
 *
 * Revision 1.6  2004/09/16 23:17:11  dalrond
 * Added setControlItemId to pass the control item's OID to the new pet
 *
 * Revision 1.5  2004/08/14 22:30:54  l2chef
 * unknown methods renamed
 *
 * Revision 1.4  2004/08/12 23:59:42  l2chef
 * owner lookup removed (L2Chef)
 *
 * Revision 1.3  2004/08/12 22:32:34  l2chef
 * pet is following owner by default (Deth)
 * pet is running (Deth)
 *
 * Revision 1.2  2004/08/08 00:49:00  l2chef
 * npc datatable merged with npctable
 *
 * Revision 1.1  2004/08/07 14:11:44  l2chef
 * new item handlers
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ExperienceTable;
import net.sf.l2j.gameserver.IdFactory;
import net.sf.l2j.gameserver.NpcTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2PetInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.serverpackets.PetInfo;
import net.sf.l2j.gameserver.serverpackets.PetItemList;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.9 $ $Date: 2004/09/20 22:39:47 $
 */

public class PetSummon implements IItemHandler
{
	private static Logger _log = Logger.getLogger(PetSummon.class.getName());
	
	// all the items ids that this handler knowns
	private static int[] _itemIds = { 2375, 3500, 3501, 3502 };

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public int useItem(L2PcInstance activeChar, L2ItemInstance item) throws IOException
	{
		int npcId;
		
		if (activeChar.getPet() != null)
		{
			_log.fine("player has a pet already. ignore use item");
			return 0;
		}
		
		switch (item.getItemId())
		{
			// wolf pet a
			case 2375:
				npcId = 12077;
				break;
			
			// hatchling of wind
			case 3500:
				npcId = 12311;
				break;
				
			// hatchling of star
			case 3501:
				npcId = 12312;
				break;
				
			// hatchling of twilight
			case 3502:
				npcId = 12313;
				break;
				
			// unknown item id.. should never happen
			default:
				return 0;
		}
		
		
		L2Npc petTemplate = NpcTable.getInstance().getTemplate(npcId);
		
		
		L2PetInstance newpet = new L2PetInstance(petTemplate);
		
		newpet.setTitle(activeChar.getName());
		newpet.setControlItemId(item.getObjectId());
		newpet.setObjectId(IdFactory.getInstance().getNextId());
		newpet.setX(activeChar.getX()+50);
		newpet.setY(activeChar.getY()+100);
		newpet.setZ(activeChar.getZ());
		newpet.setLevel(petTemplate.getLevel());
		newpet.setExp(ExperienceTable.getInstance().getExp(newpet.getLevel()));
		newpet.setLastLevel(ExperienceTable.getInstance().getExp(newpet.getLevel()));
		newpet.setNextLevel(ExperienceTable.getInstance().getExp(newpet.getLevel() + 1));
		newpet.setMaxHp(petTemplate.getHp());
		newpet.setSummonHp(petTemplate.getHp());
		newpet.setWalkSpeed(petTemplate.getWalkSpeed());// these values are not correct and will cause miscalulation
		newpet.setRunSpeed(petTemplate.getRunSpeed());
		newpet.setPhysicalAttack(petTemplate.getPatk());
		newpet.setPhysicalDefense(petTemplate.getPdef());
		newpet.setHeading(activeChar.getHeading());
		newpet.setMovementMultiplier(1.08);
		newpet.setAttackSpeedMultiplier(0.9983664);
		newpet.setAttackRange(petTemplate.getAttackRange());
		newpet.setRunning(true);
		
		L2World.getInstance().storeObject(newpet); // this is just a hack to make the id globally known
		L2World.getInstance().addVisibleObject(newpet);	
		
		MagicSkillUser msk = new MagicSkillUser(activeChar, 2046, 1, 1000, 600000);
		activeChar.sendPacket(msk);
		
		PetInfo ownerni = new PetInfo(newpet);
		NpcInfo ni = new NpcInfo(newpet);
		
		activeChar.broadcastPacket(ni);
		activeChar.sendPacket(ownerni);
		activeChar.sendPacket(new PetItemList(newpet));
		try
		{
			Thread.sleep(900);
		}
		catch (InterruptedException e)
		{
			// ignore interruptions
		}
		
		activeChar.sendPacket(new MagicSkillLaunched(activeChar,2046,1));
		
		activeChar.setPet(newpet);
		newpet.setOwner(activeChar);
		newpet.addKnownObject(activeChar);
		newpet.setFollowStatus(true);
		newpet.followOwner(activeChar);
		
		return 0;
	}
	
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
