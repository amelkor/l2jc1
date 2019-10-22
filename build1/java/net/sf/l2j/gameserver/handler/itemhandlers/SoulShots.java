/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/itemhandlers/SoulShots.java,v 1.1 2004/08/07 14:11:44 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/07 14:11:44 $
 * $Revision: 1.1 $
 * $Log: SoulShots.java,v $
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

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/07 14:11:44 $
 */

public class SoulShots implements IItemHandler
{
	// all the items ids that this handler knowns
	private static int[] _itemIds = { 1835, 1463, 1464, 1465, 1466, 1467 };

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public int useItem(L2PcInstance activeChar, L2ItemInstance item) throws IOException
	{
		if (activeChar.getActiveSoulshotGrade() != 0)
		{
			// soulshot is already active
			return 0;
		}
		
		int SoulshotId = item.getItemId();
		L2Weapon weapon = activeChar.getActiveWeapon();
		if (weapon == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SOULSHOTS));
			return 0;
		}
		int grade = weapon.getCrystalType();
		int soulShotConsumption = weapon.getSoulShotCount();
		int count = item.getCount();
		
		
		if (soulShotConsumption == 0)
		{
			//	 Can't use soulshots
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SOULSHOTS));
			return 0;
		}
		
		
		if ((grade == L2Item.CRYSTAL_NONE && SoulshotId != 1835) || 
			(grade == L2Item.CRYSTAL_D && SoulshotId != 1463) || 
			(grade == L2Item.CRYSTAL_C && SoulshotId != 1464) || 
			(grade == L2Item.CRYSTAL_B && SoulshotId != 1465) || 
			(grade == L2Item.CRYSTAL_A && SoulshotId != 1466) || 
			(grade == L2Item.CRYSTAL_S && SoulshotId != 1467))
		{
			//	 wrong grade for weapon
			activeChar.sendPacket(new SystemMessage(SystemMessage.SOULSHOTS_GRADE_MISMATCH));
			return 0;
		}
		
		
		if (!(count >= soulShotConsumption))
		{
			//	 Not Enough Soulshots
			activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SOULSHOTS));
			return 0;
		}
		
		activeChar.setActiveSoulshotGrade(grade);
		activeChar.sendPacket(new SystemMessage(342));
		L2Object OldTarget = activeChar.getTarget();
		activeChar.setTarget(activeChar);
		MagicSkillUser MSU = new MagicSkillUser(activeChar,2039,1,0,0);
		activeChar.sendPacket(MSU);
		activeChar.broadcastPacket(MSU);
		activeChar.setTarget(OldTarget);
		
		return soulShotConsumption;
	}
	
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
