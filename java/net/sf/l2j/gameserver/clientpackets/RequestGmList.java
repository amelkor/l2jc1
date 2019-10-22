/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestGmList.java,v 1.1 2004/10/11 17:29:11 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:29:11 $
 * $Revision: 1.1 $
 * $Log: RequestGmList.java,v $
 * Revision 1.1  2004/10/11 17:29:11  nuocnam
 * New class that handles RequestGmList packet triggered by /gmlist command
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.GmListTable;

/**
 * This class handles RequestGmLista packet triggered by /gmlist command
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/11 17:29:11 $
 */
public class RequestGmList extends ClientBasePacket
{
	private static final String _C__81_REQUESTGMLIST = "[C] 81 RequestGmList";

	public RequestGmList(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		GmListTable.getInstance().sendListToPlayer(client.getActiveChar());
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__81_REQUESTGMLIST;
	}
}