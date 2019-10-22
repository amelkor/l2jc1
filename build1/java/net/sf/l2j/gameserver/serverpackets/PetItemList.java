/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetItemList.java,v 1.4 2004/10/17 06:46:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:21 $
 * $Revision: 1.4 $
 * $Log: PetItemList.java,v $
 * Revision 1.4  2004/10/17 06:46:21  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.3  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.2  2004/08/14 22:47:31  l2chef
 * commet update
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
import net.sf.l2j.gameserver.model.L2PetInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/17 06:46:21 $
 */
public class PetItemList extends ServerBasePacket 
{
	private static Logger _log = Logger.getLogger(PetItemList.class.getName());
	private static final String _S__cb_PETITEMLIST = "[S] cb  PetItemList";
	private L2PetInstance _cha;

	public PetItemList(L2PetInstance cha)
	{
		_cha = cha;
		
	}	
	
	public byte[] getContent()
	{
		writeC(0xCB);
		
		L2ItemInstance[] items = _cha.getInventory().getItems();
		int count = items.length; 
		writeH(count);
		
		for (int i = 0; i < count; i++)
		{
			L2ItemInstance temp = items[i];
			_log.fine("item:" + temp.getItem().getName() + " type1:" + temp.getItem().getType1() + " type2:" + temp.getItem().getType2());
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
		return _S__cb_PETITEMLIST;
	}
}
