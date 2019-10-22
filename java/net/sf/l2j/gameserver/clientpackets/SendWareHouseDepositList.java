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
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.ItemList;
//import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;

/**
 * This class ...
 *
 * 31  SendWareHouseDepositList  cd (dd)
 *  
 * @version $Revision: 1.3 $ $Date: 2004/10/17 06:46:22 $
 */
public class SendWareHouseDepositList extends ClientBasePacket
{
	private static final String _C__31_SENDWAREHOUSEDEPOSITLIST = "[C] 31 SendWareHouseDepositList";
	private static Logger _log = Logger.getLogger(SendWareHouseDepositList.class.getName());

	public SendWareHouseDepositList(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();

		int count = readD();

		int price = 30;
		int neededMoney = count * price;
		int currentMoney = activeChar.getAdena(); 

		if (neededMoney > currentMoney)
		{
			SystemMessage sm = new SystemMessage(279);
			con.sendPacket(sm);
			return;
		}
		
		L2ItemInstance[] items = new L2ItemInstance[count];
		
		for (int i=0;i<count;i++)
		{
			int itemId = readD();
			int cnt = readD();
			L2ItemInstance inst = ItemTable.getInstance().createItem(itemId);
			inst.setCount(cnt);
			items[i] = inst;
		}
		
		//InventoryUpdate updateForInventory = new InventoryUpdate();
		neededMoney = 0; //this is moved in here because of that adena -_-; we don't want ppl to
						// pay 30a and store nothing if they coose to store adenas
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() != 57)
			{
				activeChar.getWarehouse().addItem(items[i]);
				activeChar.getInventory().destroyItemByItemId(items[i].getItemId(), items[i].getCount());
				neededMoney += price;
				//updateForInventory.addNewItem(items[i]);				
			}
		}
		
		activeChar.reduceAdena(neededMoney);

		
		ItemList il = new ItemList(activeChar, false);
		con.sendPacket(il);
		
		//causes doubled items problem
		//con.sendPacket(updateForInventory);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__31_SENDWAREHOUSEDEPOSITLIST;
	}
}
