/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/itemhandlers/WorldMap.java,v 1.1 2004/08/07 14:11:44 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/07 14:11:44 $
 * $Revision: 1.1 $
 * $Log: WorldMap.java,v $
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
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ShowMiniMap;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/07 14:11:44 $
 */

public class WorldMap implements IItemHandler
{
	// all the items ids that this handler knowns
	private static int[] _itemIds = { 1665, 1863 };

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public int useItem(L2PcInstance activeChar, L2ItemInstance item) throws IOException
	{
		activeChar.sendPacket(new ShowMiniMap(item.getItemId()));
		return 0;
	}
	
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
