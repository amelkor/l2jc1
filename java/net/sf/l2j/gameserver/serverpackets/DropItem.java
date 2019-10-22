/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/DropItem.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: DropItem.java,v $
 * Revision 1.3  2004/07/04 11:14:53  l2chef
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
package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * 16 
 * d6 6d c0 4b		player id who dropped it 
 * ee cc 11 43 		object id
 * 39 00 00 00 		item id
 * 8f 14 00 00 		x
 * b7 f1 00 00 		y
 * 60 f2 ff ff 		z
 * 01 00 00 00 		show item-count 1=yes
 * 7a 00 00 00      count                                         .
 *  
 * format  dddddddd    rev 377
 *         ddddddddd   rev 417
 *  
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class DropItem extends ServerBasePacket
{
	private static final String _S__16_DROPITEM = "[S] 16 DropItem";
	private L2ItemInstance _item;
	private int _playerId;

	public DropItem(L2ItemInstance item, int playerId)
	{
		_item=item;
		_playerId = playerId;
	}
	
	public byte[] getContent()
	{
		writeC(0x16);
		writeD(_playerId);
		writeD(_item.getObjectId());
		writeD(_item.getItemId());
		
		writeD(_item.getX());
		writeD(_item.getY());
		writeD(_item.getZ());
		// only show item count if it is a stackable item
		if (_item.isStackable())
		{	
			writeD(0x01);
		}
		else
		{
			writeD(0x00);
		}
		writeD(_item.getCount());
		
		writeD(1); // unknown
		
		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__16_DROPITEM;
	}

}
