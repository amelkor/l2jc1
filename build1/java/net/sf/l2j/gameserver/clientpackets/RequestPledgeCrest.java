/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPledgeCrest.java,v 1.4 2004/08/10 00:48:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/10 00:48:46 $
 * $Revision: 1.4 $
 * $Log: RequestPledgeCrest.java,v $
 * Revision 1.4  2004/08/10 00:48:46  l2chef
 * extended clan functions
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.serverpackets.PledgeCrest;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/08/10 00:48:46 $
 */
public class RequestPledgeCrest extends ClientBasePacket
{
	private static Logger _log = Logger.getLogger(RequestPledgeCrest.class.getName());
	private static final String _C__68_REQUESTPLEDGECREST = "[C] 68 RequestPledgeCrest";
	/**
	 * packet type id 0x68 format: cd
	 * 
	 * @param rawPacket
	 */
	public RequestPledgeCrest(byte[] rawPacket, ClientThread client)
		throws IOException
	{
		super(rawPacket);
		int crestId = readD();

		_log.fine("crestid " + crestId + " requested");

		File crestFile = new File("data/crests/Pledge_"+crestId+".bmp");
		if (crestFile.exists())
		{
			PledgeCrest pc = new PledgeCrest(crestId, crestFile);
			client.getConnection().sendPacket(pc);
		}
		else
		{
			_log.warning("crest file is missing:" + crestFile.getAbsolutePath());
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__68_REQUESTPLEDGECREST;
	}
}
