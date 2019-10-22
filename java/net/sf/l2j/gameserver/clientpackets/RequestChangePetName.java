/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestChangePetName.java,v 1.3 2004/09/28 02:05:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:05:56 $
 * $Revision: 1.3 $
 * $Log: RequestChangePetName.java,v $
 * Revision 1.3  2004/09/28 02:05:56  nuocnam
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

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.serverpackets.PetInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/28 02:05:56 $
 */
public class RequestChangePetName extends ClientBasePacket{
	private static final String REQUESTCHANGEPETNAME__C__89 = "[C] 89 RequestChangePetName";
	private static Logger _log = Logger.getLogger(RequestChangePetName.class.getName());
	
	public RequestChangePetName(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		if (client.getActiveChar().getPet() != null)
		{
			if (client.getActiveChar().getPet().getName()==null)
			{
				client.getActiveChar().getPet().setName(readS());
				client.getActiveChar().getPet().broadcastPacket(new NpcInfo(client.getActiveChar().getPet()));
				client.getActiveChar().sendPacket(new PetInfo(client.getActiveChar().getPet()));
				
			}
		}
	} 
	
	public String getType()
	{
		return REQUESTCHANGEPETNAME__C__89;
	}
}