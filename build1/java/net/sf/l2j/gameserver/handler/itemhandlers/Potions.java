/*
* $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/itemhandlers/Potions.java,v 1.2 2004/08/08 16:40:42 l2chef Exp $
*
* $Author: l2chef $
* $Date: 2004/08/08 16:40:42 $
* $Revision: 1.2 $
* $Log: Potions.java,v $
* Revision 1.2  2004/08/08 16:40:42  l2chef
* potion effect fixed
*
* Revision 1.1  2004/08/08 03:02:51  l2chef
* potion handler added (imwookie)
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
import net.sf.l2j.gameserver.model.L2Potion;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/08 16:40:42 $
 */

public class Potions implements IItemHandler
{
	private static int[] _itemIds = { 65, 725, 727, 1060, 1061, 1073, 1539, 1540, 726, 728  };
	
	public int useItem(L2PcInstance activeChar, L2ItemInstance item) throws IOException
	{
		L2Potion Potion = new L2Potion();
	    int itemId = item.getItemId();
		if (itemId == 65 || itemId == 725 || itemId == 727 || itemId == 1060 || itemId == 1061 || itemId == 1539 || itemId == 1540 || itemId == 1073)
		{
			L2Object OldTarget = activeChar.getTarget();
			activeChar.setTarget(activeChar);
			MagicSkillUser MSU = new MagicSkillUser(activeChar,2038,1,0,0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			activeChar.setTarget(OldTarget);
			Potion.setCurrentHpPotion1(activeChar, itemId);
		}
		else if (itemId == 726 || itemId == 728)
		{
			L2Object OldTarget = activeChar.getTarget();
			activeChar.setTarget(activeChar);
			MagicSkillUser MSU = new MagicSkillUser(activeChar,2038,1,0,0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			activeChar.setTarget(OldTarget);
			Potion.setCurrentMpPotion1(activeChar, itemId);
		}
		return 1;
	}
	public int[] getItemIds()
	{
		return _itemIds;
	}
}