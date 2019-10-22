/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/ShowBoard.java,v 1.1 2004/09/15 23:45:06 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/15 23:45:06 $
 * $Revision: 1.1 $
 * $Log: ShowBoard.java,v $
 * Revision 1.1  2004/09/15 23:45:06  l2chef
 *  community board added (Deth)
 *
 * Revision 1.1  2004/08/06 00:24:20  l2chef
 * cursor movement added (Deth)
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

import net.sf.l2j.gameserver.model.L2PcInstance;

public class ShowBoard extends ServerBasePacket
{
	private static final String _S__86_SHOWBOARD = "[S] 86 ShowBoard";
	private L2PcInstance _player;
	private String _htmlCode;
	
	public ShowBoard(L2PcInstance player, String htmlCode)
	{
		_player = player;
		_htmlCode = htmlCode;
	}
	
	public byte[] getContent()
	{
		writeC(0x86);
		writeS(""); //writeS("bypass bbs_top"); // top
		writeS(""); //writeS("bypass bbs_up"); // up
		writeS(""); //writeS("bypass bbs_favorate"); // favorate (favorite?)
		writeS(""); //writeS("bypass bbs_add_fav"); // add fav.
		writeS(""); //writeS("bypass bbs_region"); // region
		String clan = "";
		if (_player.getClan() != null)
		{
			clan = "bypass bbs_clan";
		}
		writeS(clan); // clan
		writeS(_htmlCode); // current page
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__86_SHOWBOARD;
	}
}
