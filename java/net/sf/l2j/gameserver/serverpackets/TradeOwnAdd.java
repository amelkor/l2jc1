/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/TradeOwnAdd.java,v 1.2 2004/09/28 03:01:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 03:01:08 $
 * $Revision: 1.2 $
 * $Log: TradeOwnAdd.java,v $
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

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * This class ...
 * 
 * @author Yme
 * @version $Revision: 1.2 $ $Date: 2004/09/28 03:01:08 $
 */
public class TradeOwnAdd extends ServerBasePacket
{
	private static final String _S__30_TRADEOWNADD = "[S] 30 TradeOwnAdd";
	private L2ItemInstance temp;
	private int _amount;

	public TradeOwnAdd(L2ItemInstance x,int amount)
	{
		temp = x;
		_amount = amount;
	}	
	
	public byte[] getContent()
	{
		writeC(0x30);
		
		
		writeH(1);
		
		
		//L2ItemInstance temp = (L2ItemInstance) items.get(i);
		int type = temp.getItem().getType1();
		writeH(type); // item type1
		writeD(temp.getObjectId());
		writeD(temp.getItemId());
		writeD(_amount);
		writeH(temp.getItem().getType2());	// item type2
		writeH(0x00);	// ?

		writeD(temp.getItem().getBodyPart());	// rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
		writeH(temp.getEnchantLevel());	// enchant level
		writeH(0x00);	// ?
		writeH(0x00);
		
			
		

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__30_TRADEOWNADD;
	}

}
