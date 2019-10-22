/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/EquipUpdate.java,v 1.4 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.4 $
 * $Log: EquipUpdate.java,v $
 * Revision 1.4  2004/07/04 11:14:53  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.templates.L2Item;

/**
 * 5e 
 * 01 00 00 00 		01 - added ?  02 - modified 
 * 7b 86 73 42      object id
 * 08 00 00 00      body slot
 * 
 * 
 * 
 * body slot
 * 0000  ?? underwear
 * 0001  ear
 * 0002  ear
 * 0003  neck
 * 0004  finger   (magic ring)
 * 0005  finger   (magic ring)
 * 0006  head     (l.cap)
 * 0007  r.hand   (dagger)
 * 0008  l.hand   (arrows)
 * 0009  hands    (short gloves)
 * 000a  chest    (squire shirt)
 * 000b  legs     (squire pants)
 * 000c  feet
 * 000d  ?? back 
 * 000e  lr.hand   (bow)
 *  
 * 
 * 
 * format  ddd 
 * 
 * @version $Revision: 1.4 $ $Date: 2004/07/04 11:14:53 $
 */
public class EquipUpdate extends ServerBasePacket
{
	private static final String _S__5E_EQUIPUPDATE = "[S] 5E EquipUpdate";
	private static Logger _log = Logger.getLogger(EquipUpdate.class.getName());
			
	private L2ItemInstance _item;
	private int _change;
	

	public EquipUpdate(L2ItemInstance item, int change)
	{
		_item = item;
		_change = change;
	}
	
	public byte[] getContent()
	{
		int bodypart = 0;
		writeC(0x5e);
		writeD(_change);
		writeD(_item.getObjectId());
		switch (_item.getItem().getBodyPart())
		{
			case L2Item.SLOT_L_EAR:
				bodypart = 0x01;
				break;
			case L2Item.SLOT_R_EAR:
				bodypart = 0x02;
				break;
			case L2Item.SLOT_NECK:
				bodypart = 0x03;
				break;
			case L2Item.SLOT_R_FINGER:
				bodypart = 0x04;
				break;
			case L2Item.SLOT_L_FINGER:
				bodypart = 0x05;
				break;
			case L2Item.SLOT_HEAD:
				bodypart = 0x06;
				break;
			case L2Item.SLOT_R_HAND:
				bodypart = 0x07;
				break;
			case L2Item.SLOT_L_HAND:
				bodypart = 0x08;
				break;
			case L2Item.SLOT_GLOVES:
				bodypart = 0x09;
				break;
			case L2Item.SLOT_CHEST:
				bodypart = 0x0a;
				break;
			case L2Item.SLOT_LEGS:
				bodypart = 0x0b;
				break;
			case L2Item.SLOT_FEET:
				bodypart = 0x0c;
				break;
			case L2Item.SLOT_BACK:
				bodypart = 0x0d;
				break;
			case L2Item.SLOT_LR_HAND:
				bodypart = 0x0e;
				break;
		}
		
		_log.fine("body:" +bodypart);
		writeD(bodypart);
		return _bao.toByteArray();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__5E_EQUIPUPDATE;
	}
}
