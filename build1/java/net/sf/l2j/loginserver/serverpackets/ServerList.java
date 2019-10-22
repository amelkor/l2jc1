/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/serverpackets/ServerList.java,v 1.3 2004/08/01 22:39:03 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/01 22:39:03 $
 * $Revision: 1.3 $
 * $Log: ServerList.java,v $
 * Revision 1.3  2004/08/01 22:39:03  l2chef
 * testserver flag possible
 *
 * Revision 1.2  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.loginserver.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/08/01 22:39:03 $
 */
public class ServerList extends ServerBasePacket
{
	// cc (cddcchhcd)

	// 04 
	// 10
	// 05
	 
	// 01 
	// dc 4c 0c 33 
	// 61 1e 00 00 
	// 0f 
	// 01 
	// f7 0a 
	// 7c 15 
	// 01 
	
	// 02 
	// dc 4c 0c 69 
	// 61 1e 00 00 
	// 0f 
	// 01 
	// 3c 09
	// 7c 15
	// 01
	
	private ArrayList _servers;
	 
//	private byte[] _content = {
//		(byte)0x04,
//		(byte)0x02,			// number of servers in list
//		(byte)0x00,			// login server num ... seems to be unused
//				
//		(byte)0x01,									// server id
//		(byte)0xc0,(byte)0xa8,(byte)0x00,(byte)0x01,   // 192.68.0.1
//		
//		(byte)0x61,(byte)0x1e,(byte)0x00,(byte)0x00,   // port 7777
//		(byte)0x12,
//		(byte)0x01,
//		(byte)0xf7,(byte)0x0a,
//		
//		(byte)0x7c,(byte)0x15,
//		(byte)0x01,
//
//		(byte)0x02,									// server id
//		(byte)0xc0,(byte)0xa8,(byte)0x00,(byte)0x01,   // 192.68.0.1
//		
//		(byte)0x61,(byte)0x1e,(byte)0x00,(byte)0x00,   // port 7777
//		(byte)0x0f,				// age limit
//		(byte)0x00,				// pvp possible
//		(byte)0x00,(byte)0x00,	// current player count
//
//		(byte)0x10,(byte)0x27,  // max players
//		(byte)0x01,		// testing == 0
//		
//		0x00,0x00,0x00,0x00,	// align and checksum
//		0x00,
//		
//		0x00,0x00,0x00,
//		0x00,0x00,0x00,0x00,
//		0x00
//	};	

	class ServerData
	{
		String ip;
		int port;
		boolean pvp;
		int currentPlayers;
		int maxPlayers;
		boolean testServer;
		
		ServerData(String ip, int port, boolean pvp, boolean testServer, int currentPlayers, int maxPlayers)
		{
			this.ip = ip;
			this.port = port;
			this.pvp = pvp;
			this.testServer = testServer;
			this.currentPlayers = currentPlayers;
			this.maxPlayers = maxPlayers;
		}
	}
	


	public ServerList() 
	{
		_servers = new ArrayList(); 
	}
	
	public void addServer(String ip, int port, boolean pvp, boolean testServer, int currentPlayer, int maxPlayer)
	{
		_servers.add(new ServerData(ip, port, testServer, pvp, currentPlayer, maxPlayer));
	}
	
	public byte[] getContent()
	{
		writeC(0x04);
		writeC(_servers.size());
		writeC(0x00);
		for (int i = 0; i < _servers.size(); i++) 
		{
			ServerData server = (ServerData) _servers.get(i);
			
			writeC(i+1);	// server id
			try 
			{
				InetAddress i4 = InetAddress.getByName(server.ip);
				byte[] raw = i4.getAddress();
				writeC(raw[0] &0xff);
				writeC(raw[1] &0xff);
				writeC(raw[2] &0xff);
				writeC(raw[3] &0xff);
			} 
			catch (UnknownHostException e) 
			{
				e.printStackTrace();
				writeC(127);
				writeC(0);
				writeC(0);
				writeC(1);
			}
			
			writeD(server.port);
			writeC(0x0f);	// age limit
			if (server.pvp)
			{
				writeC(0x01);
			}
			else
			{
				writeC(0x00);
			}
			
			writeH(server.currentPlayers);
			writeH(server.maxPlayers);
			writeC(0x01);	// testing - nope
			if (server.testServer)
			{
				writeD(0x04);
			}
			else
			{
				writeD(0x00);
			}
		}
		
		return getBytes();
	}
}
