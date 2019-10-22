/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/GetItem.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: GetItem.java,v $
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
 * sample
 * 0000: 17  1a 95 20 48  9b da 12 40  44 17 02 00  03 f0 fc ff  98 f1 ff ff                                     .....
 *   
 * format  ddddd
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $ 
 */
public class GetItem extends ServerBasePacket
{
	private static final String _S__17_GETITEM = "[S] 17 GetItem";
	private L2ItemInstance _item;
	private int _playerId;

	public GetItem(L2ItemInstance item, int playerId)
	{
		_item=item;
		_playerId = playerId;
	}
	
	public byte[] getContent()
	{
		writeC(0x17);
		writeD(_playerId);
		writeD(_item.getObjectId());
		
		writeD(_item.getX());
		writeD(_item.getY());
		writeD(_item.getZ());

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__17_GETITEM;
	}

}
