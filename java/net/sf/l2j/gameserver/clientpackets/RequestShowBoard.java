/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestShowBoard.java,v 1.2 2004/09/18 22:08:12 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/18 22:08:12 $
 * $Revision: 1.2 $
 * $Log: RequestShowBoard.java,v $
 * Revision 1.2  2004/09/18 22:08:12  nuocnam
 * Changed showboard_ to bbs_ (sh1ny)
 *
 * Revision 1.1  2004/09/15 23:43:49  l2chef
 *  community board added (Deth)
 *
 * Revision 1.2  2004/08/04 21:54:52  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.1  2004/07/28 23:53:07  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.CommunityBoard;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/18 22:08:12 $
 */
public class RequestShowBoard extends ClientBasePacket
{
	private static final String _C__57_REQUESTSHOWBOARD = "[C] 57 RequestShowBoard";

	/**
	 * packet type id 0x57
	 * 
	 * sample
	 * 
	 * 57
	 * 01 00 00 00		// unknown (always 1?)
	 * 
	 * format:		cd
	 * @param decrypt
	 */
	public RequestShowBoard(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int unknown = readD();
		CommunityBoard.getInstance().handleCommands(client,"bbs_default");
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__57_REQUESTSHOWBOARD;
	}
}
