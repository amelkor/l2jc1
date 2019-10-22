/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestDropItem.java,v 1.10 2004/09/18 01:41:34 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/09/18 01:41:34 $
 * $Revision: 1.10 $
 * $Log: RequestDropItem.java,v $
 * Revision 1.10  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.9  2004/08/04 21:14:24  l2chef
 * new flag for items added to prevent duping
 *
 * Revision 1.8  2004/08/02 00:06:24  l2chef
 * better solution for item drop problem
 *
 * Revision 1.7  2004/08/01 17:36:50  l2chef
 * maximum drop distance is enforced (Nightmarez)
 *
 * Revision 1.6  2004/07/28 23:53:54  l2chef
 * workaround for ghost items (whatev)
 *
 * Revision 1.5  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.4  2004/07/04 17:33:07  l2chef
 * prevent NPE if invalid object id is provided by client
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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.DropItem;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.10 $ $Date: 2004/09/18 01:41:34 $
 */
public class RequestDropItem extends ClientBasePacket
{
	private static final String _C__12_REQUESTDROPITEM = "[C] 12 RequestDropItem";
	private static Logger _log = Logger.getLogger(RequestDropItem.class.getName());

	/**
	 * packet type id 0x12
	 * 
	 * sample
	 * 
	 * 12 
	 * 09 00 00 40 		// object id
	 * 01 00 00 00 		// count ??
	 * fd e7 fe ff 		// x
	 * e5 eb 03 00 		// y
	 * bb f3 ff ff 		// z 
	 * 
	 * format:		cdd ddd 
	 * @param decrypt
	 */
	public RequestDropItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();
		int count = readD();
		int x = readD();
		int y = readD();
		int z = readD();
		if (client.getActiveChar().getPrivateStoreType() == 0 && client.getActiveChar().getTransactionRequester() == null)
		{
			Connection con = client.getConnection();
		

		L2PcInstance activeChar = client.getActiveChar();
		L2ItemInstance oldItem = activeChar.getInventory().getItem(objectId);
		if (oldItem == null)
		{
			_log.warning("tried to drop an item that is not in the inventory ?!?:" + objectId);
			return;
		}
		
		int oldCount = oldItem.getCount();
		_log.fine("requested drop item " + objectId + "("+ oldCount+") at "+x+"/"+y+"/"+z);
		L2ItemInstance dropedItem = null;
		
		if (oldCount < count)
		{
			_log.warning("player tried to drop more items than he has");
			return;
		}
		
		if ( activeChar.getDistance(x, y) > 150  || Math.abs(z-activeChar.getZ())> 50 )
		{ 
			_log.fine("trying to drop too far away");
			SystemMessage sm = new SystemMessage(151);
			activeChar.sendPacket(sm);
			return;
		}
		
		if (oldItem.isEquipped())
		{
			_log.fine("item is equiped");
			dropedItem = activeChar.getInventory().dropItem(objectId, count);
			// show the unequip update in the inventory. without this, the client still shows
			// the equiped item ! stupid system
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(oldItem);
			con.sendPacket(iu);
			
			UserInfo ui = new UserInfo(activeChar);
			con.sendPacket(ui);
		
//			EquipUpdate eu = new EquipUpdate(oldItem, L2ItemInstance.REMOVED);
//			con.sendPacket(eu);
		}
		else
		{
			dropedItem = activeChar.getInventory().dropItem(objectId, count);
		}
		
		dropedItem.setX(x);
		dropedItem.setY(y);
		dropedItem.setZ(z);
		
		// mark it as 
		dropedItem.setOnTheGround(true);
		
		_log.fine("dropping " + objectId + " item("+count+") at: " + x + " " + y + " " + z);
		DropItem di = new DropItem(dropedItem, activeChar.getObjectId()); 
		activeChar.sendPacket(di);
		activeChar.addKnownObjectWithoutCreate(dropedItem);
		
		// also notify all other players about our movement
		L2Character[] players = activeChar.broadcastPacket(di);
		for (int i = 0; i < players.length; i++)
		{
			((L2PcInstance)players[i]).addKnownObjectWithoutCreate(dropedItem);
		}
		
		InventoryUpdate iu = new InventoryUpdate();
		if (oldCount == dropedItem.getCount())	// we drop all the had, so remove the item
		{
			_log.fine("remove item from inv");
			iu.addRemovedItem(oldItem); 
		}
		else
		{
			_log.fine("reducing item in inv");
			iu.addModifiedItem(oldItem);
		}
		con.sendPacket(iu);

		SystemMessage sm = new SystemMessage(298);
		sm.addItemName(dropedItem.getItemId());
		con.sendPacket(sm);
		con.sendPacket(new UserInfo(activeChar));
		
		L2World.getInstance().addVisibleObject(dropedItem);
	
		}
		else
		{
			SystemMessage msg = new SystemMessage(SystemMessage.NOTHING_HAPPENED);
			client.getActiveChar().sendPacket(msg);
		}
	
	}
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__12_REQUESTDROPITEM;
	}
}
