/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestOustPartyMember.java,v 1.3 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.3 $
 * $Log: RequestOustPartyMember.java,v $
 * Revision 1.3  2004/09/28 01:50:14  nuocnam
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
package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestOustPartyMember extends ClientBasePacket{
	
	private static final String _C__2C_REQUESTOUSTPARTYMEMBER = "[C] 2C RequestOustPartyMember";
	private static Logger _log = Logger.getLogger(RequestJoinParty.class.getName());
	
	public RequestOustPartyMember(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		String name = readS();
		L2PcInstance activeChar = client.getActiveChar();
		
		if (activeChar.isInParty() && activeChar.getParty().isLeader(activeChar)) activeChar.getParty().oustPartyMember(name);
	}
	
	
	public String getType()
	{
		return _C__2C_REQUESTOUSTPARTYMEMBER;
	}
}
