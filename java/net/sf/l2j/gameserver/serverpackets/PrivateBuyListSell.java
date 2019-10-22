/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PrivateBuyListSell.java,v 1.2 2004/09/28 03:01:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 03:01:08 $
 * $Revision: 1.2 $
 * $Log: PrivateBuyListSell.java,v $
 * Revision 1.2  2004/09/28 03:01:08  nuocnam
 * Added header and copyright notice.
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

import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.TradeItem;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 03:01:08 $
 */
public class PrivateBuyListSell extends ServerBasePacket
{
	
	private static final String _S__B4_PRIVATEBUYLISTSELL = "[S] B4 PrivateBuyListSell";
	private L2PcInstance _buyer,_seller;
	
	public PrivateBuyListSell(L2PcInstance buyer,L2PcInstance seller)
	{
		_buyer = buyer;
		_seller = seller;
	}
	
	public byte[] getContent()
	{
		
		writeC(0xB4);
		writeD(_seller.getObjectId());
		writeD(_buyer.getAdena());
		int count = _seller.getSellList().size();
		
		TradeItem temp2; 
			
		 
		writeD(count);
		int type;
		for (int i = 0; i < count; i++)
		{
			temp2 = (TradeItem) _seller.getSellList().get(i);
			
			writeD(0x00);
			writeD(temp2.getObjectId());
			writeD(temp2.getItemId());
			writeD(temp2.getCount());
			writeD(0);
			writeH(0x00);
			writeH(0x00);
			writeH(0x00);
			writeD(temp2.getOwnersPrice());
			writeD(temp2.getStorePrice());
		
		}
			
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__B4_PRIVATEBUYLISTSELL;
	}

}
