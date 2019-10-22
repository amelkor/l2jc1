/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestBuyItem.java,v 1.12 2004/10/23 02:29:14 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 02:29:14 $
 * $Revision: 1.12 $
 * $Log: RequestBuyItem.java,v $
 * Revision 1.12  2004/10/23 02:29:14  l2chef
 * fixed overflow problem with large count numbers. should be impossible to exploit now
 *
 * Revision 1.11  2004/10/01 12:17:31  nuocnam
 * GM shop "you don't have enough adena" should be fixed
 *
 * Revision 1.10  2004/09/30 04:18:00  nuocnam
 * - added (currentMoney <= 0) check so player can't buy items when he has 0 adena.
 * - unified all checks to make just one condition
 *
 * Revision 1.9  2004/09/26 21:44:11  l2chef
 * reduced number overflow risk
 *
 * Revision 1.8  2004/09/26 21:14:52  nuocnam
 * added negative adena check in RequestBuyItem (badk0re) changed some system messages to use constants instead of numbers (nuocnam)
 *
 * Revision 1.7  2004/09/15 23:46:24  l2chef
 * load is updated when buying items (Deth)
 *
 * Revision 1.6  2004/08/06 00:23:22  l2chef
 * transaction controller is used for party requests (whatev)
 *
 * Revision 1.5  2004/07/11 22:43:09  l2chef
 * update inventory does not work right, so revert to old code
 *
 * Revision 1.4  2004/07/09 20:15:49  l2chef
 * less traffic by using update packet instead of full list. contributed by  whatev
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
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.12 $ $Date: 2004/10/23 02:29:14 $
 */
public class RequestBuyItem extends ClientBasePacket
{
	private static final String _C__1F_REQUESTBUYITEM = "[C] 1F RequestBuyItem";
	private static Logger _log = Logger.getLogger(RequestBuyItem.class.getName());

	/**
	 * packet type id 0x1f
	 * 
	 * sample
	 * 
	 * 1f
	 * 44 22 02 01		// list id
	 * 02 00 00 00		// items to buy
	 * 
	 * 27 07 00 00		// item id
	 * 06 00 00 00		// count
	 * 
	 * 83 06 00 00
	 * 01 00 00 00
	 * 
	 * format:		cdd (dd) 
	 * @param decrypt
	 */
	public RequestBuyItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int listId = readD();
		int count = readD();
		L2ItemInstance[] items = new L2ItemInstance[count];
		for (int i=0;i<count;i++)
		{
			int itemId = readD();
			int cnt = readD();
			L2ItemInstance inst = ItemTable.getInstance().createItem(itemId);
			inst.setCount(cnt);
			items[i] = inst;
		}
		


		//TODO check if valid buylist, stackable items ?
		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();
		
		double neededMoney = 0;
		long currentMoney = activeChar.getAdena(); 
		L2TradeList list = TradeController.getInstance().getBuyList(listId);

		for (int i = 0; i < items.length; i++)
		{
			double count2 = items[i].getCount();
			int id = items[i].getItemId();
			int price = list.getPriceForItemId(id);
			if (price == -1)
			{
				_log.warning("ERROR, no price found .. wrong buylist ??");
				price = 1000000; // fallback price

			}
			neededMoney += Math.abs(count2) * price;
		}

		if ((neededMoney > currentMoney) || (neededMoney < 0) || (currentMoney <= 0))
		{
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA);
			con.sendPacket(sm);
			return;
		}

		activeChar.reduceAdena((int)neededMoney);
		for (int i = 0; i < items.length; i++)
		{
			activeChar.getInventory().addItem(items[i]);
		}

		//TODO only send Inventory update instead of full list !
		ItemList il = new ItemList(activeChar, false);
		con.sendPacket(il);
		
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__1F_REQUESTBUYITEM;
	}
}
