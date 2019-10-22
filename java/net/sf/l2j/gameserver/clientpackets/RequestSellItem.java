/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestSellItem.java,v 1.3 2004/09/16 00:00:54 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/16 00:00:54 $
 * $Revision: 1.3 $
 * $Log: RequestSellItem.java,v $
 * Revision 1.3  2004/09/16 00:00:54  l2chef
 * load is updated when selling items (Deth)
 *
 * Revision 1.2  2004/08/04 21:54:52  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.1  2004/07/28 23:53:07  l2chef
 * Selling items implemented (Deth)
 *
 * Revision 1.0  2004/07/28 15:11:47  deth
 * first release
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
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/16 00:00:54 $
 */
public class RequestSellItem extends ClientBasePacket
{
	private static final String _C__1E_REQUESTSELLITEM = "[C] 1E RequestSellItem";
	private static Logger _log = Logger.getLogger(RequestSellItem.class.getName());

	/**
	 * packet type id 0x1e
	 * 
	 * sample
	 * 
	 * 1e
	 * 00 00 00 00		// list id
	 * 02 00 00 00		// number of items
	 * 
	 * 71 72 00 10		// object id
	 * ea 05 00 00		// item id
	 * 01 00 00 00		// item count
	 * 
	 * 76 4b 00 10		// object id
	 * 2e 0a 00 00		// item id
	 * 01 00 00 00		// item count
	 * 
	 * format:		cdd (ddd)
	 * @param decrypt
	 */
	public RequestSellItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int listId = readD();
		int count = readD();
		L2PcInstance activeChar = client.getActiveChar();
		L2ItemInstance[] items = new L2ItemInstance[count];
		for (int i = 0; i < count; i++)
		{
			int objectId = readD();
			int itemId = readD();
			int cnt = readD();
			if ((activeChar.getInventory().getItem(objectId).getItemId() == itemId) && (activeChar.getInventory().getItem(objectId).getCount() >= cnt))
			{
				L2ItemInstance item = activeChar.getInventory().getItem(objectId);
				activeChar.addAdena((item.getItem().getReferencePrice()/2)*cnt);
				
				activeChar.getInventory().destroyItem(objectId, cnt);
			}
		}
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__1E_REQUESTSELLITEM;
	}
}
