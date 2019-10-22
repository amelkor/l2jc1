/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/WareHouseDepositList.java,v 1.4 2004/10/21 20:42:17 dethx Exp $
 *
 * $Author: dethx $
 * $Date: 2004/10/21 20:42:17 $
 * $Revision: 1.4 $
 * $Log: WareHouseDepositList.java,v $
 * Revision 1.4  2004/10/21 20:42:17  dethx
 * *** empty log message ***
 *
 * Revision 1.3  2004/10/17 06:46:21  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.2  2004/09/28 02:48:21  nuocnam
 * corrected header
 *
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

package net.sf.l2j.gameserver.serverpackets;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * 0x53 WareHouseDepositList  dh (h dddhh dhhh d)
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/21 20:42:17 $
 */
public class WareHouseDepositList extends ServerBasePacket
{
	private static Logger _log = Logger.getLogger(WareHouseDepositList.class.getName());
	private static final String _S__53_WAREHOUSEDEPOSITLIST = "[S] 53 WareHouseDepositList";
	private L2PcInstance _cha;
	private int _money;

	public WareHouseDepositList(L2PcInstance cha)
	{
		_cha = cha;
		_money = cha.getAdena();
	}	
	
	public byte[] getContent()
	{
		writeC(0x53);
		writeD(_money);
		ArrayList itemlist = new ArrayList();
		L2ItemInstance[] inventory = _cha.getInventory().getItems();
		int count = _cha.getInventory().getSize();
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance item = inventory[i];
			if ((!item.isEquipped()) && (item.getItem().getType2() != L2Item.TYPE2_QUEST))
			{
				itemlist.add(item);
			}
		}
		count = itemlist.size();
		_log.fine("count:"+count);
		writeH(count);
		
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance temp = (L2ItemInstance) itemlist.get(i);
			
			_log.fine("item:" + temp.getItem().getName() + " type1:" + temp.getItem().getType1() + " type2:" + temp.getItem().getType2());
			writeH(temp.getItem().getType1()); // item type1 //unconfirmed, works
			writeD(temp.getObjectId()); //unconfirmed, works
			writeD(temp.getItemId()); //unconfirmed, works
			writeD(temp.getCount()); //unconfirmed, works
			writeH(temp.getItem().getType2());	// item type2 //unconfirmed, works
			writeH(100);	// ?
			writeD(400);	// ?
			writeH(temp.getEnchantLevel());	// enchant level -confirmed
			writeH(300);	// ?
			writeH(200);	// ?
			writeD(temp.getItemId()); // item id - confimed			
		}

		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__53_WAREHOUSEDEPOSITLIST;
	}
}
