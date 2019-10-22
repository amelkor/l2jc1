/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/CharacterRestore.java,v 1.4 2004/07/11 23:37:38 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/11 23:37:38 $
 * $Revision: 1.4 $
 * $Log: CharacterRestore.java,v $
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
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/07/11 23:37:38 $
 */
public class CharacterRestore extends ClientBasePacket
{
	private static final String _C__62_CHARACTERRESTORE = "[C] 62 CharacterRestore";
	private static Logger _log = Logger.getLogger(CharacterRestore.class.getName());

	// cd

	/**
	 * @param decrypt
	 */
	public CharacterRestore(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int charSlot = readD();
		
		// the code that actually restores a char that is tagged for deletion has to be inserted here
		// for that the deletion timer has to be set to zero
		// TODO add restore code when db is used
		CharSelectInfo cl = new CharSelectInfo(client.getLoginName(), client.getSessionId());
		client.getConnection().sendPacket(cl);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__62_CHARACTERRESTORE;
	}
}
