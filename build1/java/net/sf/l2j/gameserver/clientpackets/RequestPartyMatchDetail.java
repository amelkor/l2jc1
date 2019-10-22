/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPartyMatchDetail.java,v 1.1 2004/07/04 11:09:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:09:46 $
 * $Revision: 1.1 $
 * $Log: RequestPartyMatchDetail.java,v $
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
import net.sf.l2j.gameserver.serverpackets.PartyMatchDetail;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:09:46 $
 */

public class RequestPartyMatchDetail extends ClientBasePacket
{
	private static final String _C__71_REQUESTPARTYMATCHDETAIL = "[C] 71 RequestPartyMatchDetail";
	private static Logger _log = Logger.getLogger(RequestPartyMatchDetail.class.getName());

	/**
	 * packet type id 0x71
	 * 
	 * sample
	 * 
	 * 71
	 * d8 a8 10 41  object id 
	 * 
	 * format:		cd 
	 * @param decrypt
	 */
	public RequestPartyMatchDetail(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();
		
		L2PcInstance player = (L2PcInstance) L2World.getInstance().findObject(objectId);
		PartyMatchDetail details = new PartyMatchDetail(player);
		client.getConnection().sendPacket(details);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__71_REQUESTPARTYMATCHDETAIL;
	}
}
