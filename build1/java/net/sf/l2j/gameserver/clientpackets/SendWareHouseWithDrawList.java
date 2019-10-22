/*
 * Revision 1.1  2004/06/27 08:51:42  nuocnam
 * initial release
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
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
//import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;

/**
 * This class ...
 *
 * 32  SendWareHouseWithDrawList  cd (dd)
 *  
 * @version $Revision: 1.2 $ $Date: 2004/07/17 11:30:00 $
 */
public class SendWareHouseWithDrawList extends ClientBasePacket
{
	private static final String _C__32_SENDWAREHOUSEWITHDRAWLIST = "[C] 32 SendWareHouseWithDrawList";
	private static Logger _log = Logger.getLogger(SendWareHouseWithDrawList.class.getName());

	public SendWareHouseWithDrawList(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		L2PcInstance activeChar = client.getActiveChar();

		int itemCount = readD();
		int weight = 0;
		L2ItemInstance[] items = new L2ItemInstance[itemCount];

		for (int i=0; i<itemCount; i++)
		{
			int itemId = readD();
			int count = readD();
			L2ItemInstance inst = ItemTable.getInstance().createItem(itemId);
			inst.setCount(count);
			items[i] = inst;
			weight += items[i].getItem().getWeight() * count;
		}
		
		
		if (activeChar.getMaxLoad() - activeChar.getCurrentLoad() >= weight)
		{
			//InventoryUpdate updateForInventory = new InventoryUpdate();
			for (int i = 0; i < items.length; i++)
			{
				activeChar.getInventory().addItem(items[i]);
				activeChar.getWarehouse().destroyItem(items[i].getItemId(), items[i].getCount());
				//updateForInventory.addNewItem(items[i]);
			}
						
			StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
			su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
			activeChar.sendPacket(su);

			ItemList il = new ItemList(activeChar, false);
			activeChar.sendPacket(il);			
			//causes doubled items problem
			//con.sendPacket(updateForInventory);			
		} 
		else
		{
			for (int i = 0; i < items.length; i++)
			{
				L2World.getInstance().removeVisibleObject(items[i]);
			}
			
			SystemMessage sm = new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED);
			activeChar.sendPacket(sm);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__32_SENDWAREHOUSEWITHDRAWLIST;
	}
}
