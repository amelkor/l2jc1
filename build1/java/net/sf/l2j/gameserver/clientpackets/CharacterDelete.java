/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/CharacterDelete.java,v 1.5 2004/07/25 00:37:19 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/25 00:37:19 $
 * $Revision: 1.5 $
 * $Log: CharacterDelete.java,v $
 * Revision 1.5  2004/07/25 00:37:19  l2chef
 * charnames are now checked for duplicates when creating char
 *
 * Revision 1.4  2004/07/11 23:37:38  l2chef
 * chars are always reloaded from disk  (whatev)
 *
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.serverpackets.CharDeleteOk;
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/07/25 00:37:19 $
 */
public class CharacterDelete extends ClientBasePacket
{
	private static final String _C__0C_CHARACTERDELETE = "[C] 0C CharacterDelete";
	private static Logger _log = Logger.getLogger(CharacterDelete.class.getName());

	// cd

	/**
	 * @param decrypt
	 */
	public CharacterDelete(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int charSlot = readD();
		
		_log.fine("deleting slot:" + charSlot);

		Connection con = client.getConnection();
		client.deleteCharFromDisk(charSlot);
		CharDeleteOk ccf = new CharDeleteOk();
		con.sendPacket(ccf);

		CharSelectInfo cl = new CharSelectInfo(client.getLoginName(), client.getSessionId());
		con.sendPacket(cl);
	
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__0C_CHARACTERDELETE;
	}
}
