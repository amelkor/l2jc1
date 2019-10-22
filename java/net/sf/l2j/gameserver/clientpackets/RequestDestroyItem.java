/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestDestroyItem.java,v 1.6 2004/07/30 22:29:35 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/30 22:29:35 $
 * $Revision: 1.6 $
 * $Log: RequestDestroyItem.java,v $
 * Revision 1.6  2004/07/30 22:29:35  l2chef
 * player status gets updated when destroying equiped items (NuocNam)
 *
 * Revision 1.5  2004/07/28 23:54:23  l2chef
 * destroy item bug fixed (NuocNam)
 *
 * Revision 1.4  2004/07/17 11:30:37  l2chef
 * current load is updated (NuocNam)
 *
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.6 $ $Date: 2004/07/30 22:29:35 $
 */
public class RequestDestroyItem extends ClientBasePacket
{

	private static final String _C__59_REQUESTDESTROYITEM = "[C] 59 RequestDestroyItem";
	/**
	 * packet type id 0x1f
	 * 
	 * sample
	 * 
	 * 59 
	 * 0b 00 00 40		// object id 
	 * 01 00 00 00		// count ??
	 * 
	 * 
	 * format:		cdd  
	 * @param decrypt
	 */
	public RequestDestroyItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();
		int count = readD();
		
		L2PcInstance activeChar = client.getActiveChar();

		if (count > activeChar.getInventory().getItem(objectId).getCount())
		{
			count = activeChar.getInventory().getItem(objectId).getCount();
		}
		
		L2ItemInstance itemToRemove = activeChar.getInventory().getItem(objectId);
		if (itemToRemove.isEquipped())
		{
			
			L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemOnPaperdoll(itemToRemove.getEquipSlot());			
			InventoryUpdate iu = new InventoryUpdate();
			for (int i = 0; i < unequiped.length; i++)
			{
				iu.addModifiedItem(unequiped[i]);
			}
			activeChar.sendPacket(iu);
			activeChar.updatePDef();
			activeChar.updatePAtk();
			activeChar.updateMDef();
			activeChar.updateMAtk();
		}


		L2ItemInstance removedItem = activeChar.getInventory().destroyItem(objectId, count);
		
		InventoryUpdate iu = new InventoryUpdate();
		if (removedItem.getCount() == 0)
		{
			iu.addRemovedItem(removedItem);
		}
		else
		{
			iu.addModifiedItem(removedItem);
		}
		//client.getConnection().sendPacket(iu);
		activeChar.sendPacket(iu);
		
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);

		UserInfo ui = new UserInfo(activeChar);
		activeChar.sendPacket(ui);
		CharInfo info = new CharInfo(activeChar);
		activeChar.broadcastPacket(info);

		L2World world = L2World.getInstance();
		world.removeObject(removedItem);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__59_REQUESTDESTROYITEM;
	}
}
