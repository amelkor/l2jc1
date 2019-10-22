/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPartyMatchConfig.java,v 1.1 2004/07/04 11:09:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:09:46 $
 * $Revision: 1.1 $
 * $Log: RequestPartyMatchConfig.java,v $
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

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:09:46 $
 */

public class RequestPartyMatchConfig extends ClientBasePacket
{
	private static final String _C__6F_REQUESTPARTYMATCHCONFIG = "[C] 6F RequestPartyMatchConfig";
	private static Logger _log = Logger.getLogger(RequestPartyMatchConfig.class.getName());

	/**
	 * packet type id 0x6f
	 * 
	 * sample
	 * 
	 * 6f
	 * 01 00 00 00 
	 * 00 00 00 00 
	 * 00 00 00 00 
	 * 00 00 
	 * 
	 * format:		cdddS 
	 * @param decrypt
	 */
	public RequestPartyMatchConfig(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int automaticRegistration = readD();
		int showLevel = readD();
		int showClass = readD();
		String memo = readS();
		
		client.getActiveChar().setPartyMatchingAutomaticRegistration(automaticRegistration == 1);
		client.getActiveChar().setPartyMatchingShowLevel(showLevel == 1);
		client.getActiveChar().setPartyMatchingShowClass(showClass == 1);
		client.getActiveChar().setPartyMatchingMemo(memo);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__6F_REQUESTPARTYMATCHCONFIG;
	}
}
