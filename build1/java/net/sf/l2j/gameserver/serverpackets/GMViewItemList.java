/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/GMViewItemList.java,v 1.1 2004/10/22 23:41:06 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/22 23:41:06 $
 * $Revision: 1.1 $
 * $Log: GMViewItemList.java,v $
 * Revision 1.1  2004/10/22 23:41:06  l2chef
 * alt-g packets added
 *
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;


/**
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/22 23:41:06 $
 */
public class GMViewItemList extends ServerBasePacket
{
	private static Logger _log = Logger.getLogger(GMViewItemList.class.getName());
	private static final String _S__AD_GMVIEWITEMLIST = "[S] AD GMViewItemList";
	private L2ItemInstance[] _items;
	private String _playerName;



	public GMViewItemList(L2PcInstance cha)
	{
		_items = cha.getInventory().getItems();
		_playerName = cha.getName();
	}	


	public byte[] getContent()
	{
		writeC(0xad);
		writeS(_playerName);
		writeH(0x01); // show window ??
		
		int count = _items.length; 
		writeH(count);

		for (int i = 0; i < count; i++)
		{
			L2ItemInstance temp = _items[i];
			writeH(temp.getItem().getType1()); // item type1
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(temp.getItem().getType2());	// item type2
			writeH(0xff);	// ?
			if (temp.isEquipped())
			{
				writeH(0x01);
			}
			else
			{
				writeH(0x00);
			}
			writeD(temp.getItem().getBodyPart());	// rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
//			writeH(temp.getItem().getBodyPart());	// rev 377  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			writeH(temp.getEnchantLevel());	// enchant level
			writeH(0x00);	// ?
		}

		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__AD_GMVIEWITEMLIST;
	}
}
