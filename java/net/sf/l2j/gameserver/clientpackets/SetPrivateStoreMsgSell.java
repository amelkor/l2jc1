/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/SetPrivateStoreMsgSell.java,v 1.2 2004/09/28 01:40:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:40:56 $
 * $Revision: 1.2 $
 * $Log: SetPrivateStoreMsgSell.java,v $
 * Revision 1.2  2004/09/28 01:40:56  nuocnam
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
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:40:56 $
 */
public class SetPrivateStoreMsgSell extends ClientBasePacket{
	private static final String _C__77_SETPRIVATESTOREMSGSELL = "[C] 77 SetPrivateStoreMsgSell";
	private static Logger _log = Logger.getLogger(SetPrivateStoreMsgSell.class.getName());
	

	
	public SetPrivateStoreMsgSell(byte[] decrypt, ClientThread client)
	{
		super(decrypt);
		String storename = readS();
		L2PcInstance player = client.getActiveChar();
		
		player.getTradeList().setSellStoreName(storename);
		

	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__77_SETPRIVATESTOREMSGSELL;
	}

}
