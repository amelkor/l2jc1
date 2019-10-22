/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/SellList.java,v 1.4 2004/10/17 06:46:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:21 $
 * $Revision: 1.4 $
 * $Log: SellList.java,v $
 * Revision 1.4  2004/10/17 06:46:21  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.3  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.2  2004/08/04 21:55:04  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.1  2004/07/28 23:56:11  l2chef
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
package net.sf.l2j.gameserver.serverpackets;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/17 06:46:21 $
 */
public class SellList extends ServerBasePacket
{
	private static final String _S__1C_SELLLIST = "[S] 1C SellList";
	private static Logger _log = Logger.getLogger(SellList.class.getName());
	private L2PcInstance _char;
	private int _money;
	private ArrayList _selllist = new ArrayList();
	
	public SellList(L2PcInstance player)
	{
		_char = player;
		_money = _char.getAdena();
	}
	
	public byte[] getContent()
	{
		writeC(0x1c);
		writeD(_money);
		writeD(0x00);
		
		L2ItemInstance[] inventory = _char.getInventory().getItems();
		int count = _char.getInventory().getSize();
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance item = inventory[i];
			if ((!item.isEquipped()) && (item.getItemId() != 57) && (item.getItem().getType2() != 3))
			{
				_selllist.add(item);
				_log.fine("item added to selllist: " + item.getItem().getName());
			}
		}
		count = _selllist.size();
		writeH(count);
		
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance item = (L2ItemInstance) _selllist.get(i);
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getCount());
			writeH(item.getItem().getType2());
			writeH(0x00);
			if (item.getItem().getType1() < 4)
			{	
				writeD(item.getItem().getBodyPart());
				writeH(item.getEnchantLevel());
				writeH(0x00);
				writeH(0x00);
			}
			
			writeD(item.getItem().getReferencePrice()/2);
		}
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__1C_SELLLIST;
	}
}
