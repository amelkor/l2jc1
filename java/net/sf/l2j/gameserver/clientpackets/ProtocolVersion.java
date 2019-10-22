/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/ProtocolVersion.java,v 1.5 2004/07/17 23:09:57 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/17 23:09:57 $
 * $Revision: 1.5 $
 * $Log: ProtocolVersion.java,v $
 * Revision 1.5  2004/07/17 23:09:57  l2chef
 * now minimum protocol version is set to 417 to allow chinese clients to work.
 *
 * Revision 1.4  2004/07/04 11:12:33  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:27  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.serverpackets.KeyPacket;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/07/17 23:09:57 $
 */
public class ProtocolVersion extends ClientBasePacket
{
	private static final String _C__00_PROTOCOLVERSION = "[C] 00 ProtocolVersion";
	static Logger _log = Logger.getLogger(ProtocolVersion.class.getName());
			
	/**
	 * packet type id 0x00
	 * format:	cd
	 *  
	 * @param rawPacket
	 */
	public ProtocolVersion(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		long version  = readD();
		
		// this packet is never encrypted
		_log.fine("Protocol Revision:" + version);
		if (version < 417)
		{
			throw new IOException("Wrong Protocol Version");
		}

		Connection con = client.getConnection();
		
		KeyPacket pk = new KeyPacket();
		pk.setKey(con.getCryptKey());
		con.sendPacket(pk);

		con.activateCryptKey();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__00_PROTOCOLVERSION;
	}
}
