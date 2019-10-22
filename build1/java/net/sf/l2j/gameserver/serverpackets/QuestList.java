/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/QuestList.java,v 1.4 2004/07/13 23:00:14 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:00:14 $
 * $Revision: 1.4 $
 * $Log: QuestList.java,v $
 * Revision 1.4  2004/07/13 23:00:14  l2chef
 * removed empty constructor
 *
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

/**
 * 
 * sample for rev 377:
 * 
 * 98 
 * 05 00 		number of quests
 * ff 00 00 00 
 * 0a 01 00 00 
 * 39 01 00 00 
 * 04 01 00 00 
 * a2 00 00 00 
 * 
 * 04 00 		number of quest items
 * 
 * 85 45 13 40 	item obj id
 * 36 05 00 00 	item id
 * 02 00 00 00 	count
 * 00 00 		?? bodyslot
 * 
 * 23 bd 12 40 
 * 86 04 00 00 
 * 0a 00 00 00 
 * 00 00 
 * 
 * 1f bd 12 40 
 * 5a 04 00 00 
 * 09 00 00 00 
 * 00 00 
 * 
 * 1b bd 12 40 
 * 5b 04 00 00 
 * 39 00 00 00 
 * 00 00                                                 .
 * 
 * format h (d) h (dddh)   rev 377
 * format h (dd) h (dddd)  rev 417
 * 
 * @version $Revision: 1.4 $ $Date: 2004/07/13 23:00:14 $
 */
public class QuestList extends ServerBasePacket
{
	private static final String _S__98_QUESTLIST = "[S] 98 QuestList";
	private int _questCount;
	private int _itemCount;

	
	public byte[] getContent()
	{
		writeC(0x98);
		writeH(_questCount);
		writeH(_itemCount);
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__98_QUESTLIST;
	}
}
