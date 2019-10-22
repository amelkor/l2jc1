/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/itemhandlers/ScrollOfEscape.java,v 1.1 2004/08/07 14:11:44 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/07 14:11:44 $
 * $Revision: 1.1 $
 * $Log: ScrollOfEscape.java,v $
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

import net.sf.l2j.gameserver.MapRegionTable;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.serverpackets.TeleportToLocation;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/07 14:11:44 $
 */

public class ScrollOfEscape implements IItemHandler
{
	// all the items ids that this handler knowns
	private static int[] _itemIds = { 736 };

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public int useItem(L2PcInstance activeChar, L2ItemInstance item) throws IOException
	{
		String townCordsString = MapRegionTable.getInstance().getClosestTownCords(activeChar);
		String[] temp = null;
		temp = townCordsString.split("!");
		
		int townX = Integer.parseInt(temp[0]);
		int townY = Integer.parseInt(temp[1]);
		int townZ = Integer.parseInt(temp[2]);
		
		//SoE Animation section
		activeChar.setTarget(activeChar);
		L2Skill skill = SkillTable.getInstance().getInfo(1050, 1);
		MagicSkillUser msk = new MagicSkillUser(activeChar, 1050, 1,20000,0);
		activeChar.sendPacket(msk);
		activeChar.broadcastPacket(msk);
		SetupGauge sg = new SetupGauge(0, skill.getSkillTime());
		activeChar.sendPacket(sg);
		
		if (skill.getSkillTime() > 200)
		{
			try
			{
				Thread.sleep(skill.getSkillTime()-200);
			}
			catch (InterruptedException e)
			{
				// ignore interrupts
			}
			
		}
		
		//End SoE Animation section
		
		
		//Packets and other updates, teleport also
		StopMove sm = new StopMove(activeChar);
		activeChar.sendPacket(sm);
		activeChar.broadcastPacket(sm);

		ActionFailed af = new ActionFailed();
		activeChar.sendPacket(af);
		
		L2World.getInstance().removeVisibleObject(activeChar);
		activeChar.removeAllKnownObjects();
		
		
		TeleportToLocation teleport = new TeleportToLocation(activeChar, townX, townY, townZ);
		activeChar.sendPacket(teleport);
		activeChar.broadcastPacket(teleport);
		activeChar.setX(townX);
		activeChar.setY(townY);
		activeChar.setZ(townZ);

		//Disable movement right after the animation is done to prevent the exploit that lets you pick up the items you dropped on death
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			// ignore interrupts
		}
		
		return 1;
	}
	
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
