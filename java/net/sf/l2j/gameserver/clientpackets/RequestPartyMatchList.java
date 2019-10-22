/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPartyMatchList.java,v 1.1 2004/07/04 11:09:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:09:46 $
 * $Revision: 1.1 $
 * $Log: RequestPartyMatchList.java,v $
 * Revision 1.1  2004/07/04 11:09:46  l2chef
 * new party related handers
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.PartyMatchList;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:09:46 $
 */

public class RequestPartyMatchList extends ClientBasePacket
{
	private static final String _C__70_REQUESTPARTYMATCHLIST = "[C] 70 RequestPartyMatchList";
	private static Logger _log = Logger.getLogger(RequestPartyMatchList.class.getName());

	/**
	 * packet type id 0x70
	 * 
	 * sample
	 * 
	 * 70
	 * 01 00 00 00 
	 * 
	 * format:		cd 
	 * @param decrypt
	 */
	public RequestPartyMatchList(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int status = readD();
		
		if (status == 1)
		{
			// window is open fill the list  
			// actually the client should get automatic updates for the list
			// for now we only fill it once
			L2PcInstance[] allPlayers = L2World.getInstance().getAllPlayers();
			PartyMatchList matchList = new PartyMatchList(allPlayers);
			client.getConnection().sendPacket(matchList);
		}
		else if (status == 3)
		{
			// client does not need any more updates
			_log.fine("PartyMatch window was closed.");
		}
		else
		{
			_log.fine("party match status: "+status);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__70_REQUESTPARTYMATCHLIST;
	}
}
