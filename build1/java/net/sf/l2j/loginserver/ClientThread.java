/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/ClientThread.java,v 1.14 2004/09/26 00:17:58 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/26 00:17:58 $
 * $Revision: 1.14 $
 * $Log: ClientThread.java,v $
 * Revision 1.14  2004/09/26 00:17:58  l2chef
 * hacking protection added. IP banning added
 *
 * Revision 1.13  2004/09/16 18:59:20  nuocnam
 * Old connection is dropped if you try to log in and account is already in use. (nuocnam)
 *
 * Revision 1.12  2004/08/01 22:39:05  l2chef
 * testserver flag possible
 *
 * Revision 1.11  2004/07/19 23:11:09  l2chef
 * accounts can be banned if accesslevel is set to negative value (Deth)
 *
 * Revision 1.10  2004/07/13 23:17:33  l2chef
 * empty blocks commented
 *
 * Revision 1.9  2004/07/11 11:45:36  l2chef
 * no limit on client packet size
 *
 * Revision 1.8  2004/07/05 23:02:32  l2chef
 * access levels are stored for logins
 *
 * Revision 1.7  2004/07/05 20:24:52  l2chef
 * better handling of incoming corrupt data
 *
 * Revision 1.6  2004/07/04 19:04:39  l2chef
 * dont log password
 *
 * Revision 1.5  2004/07/04 11:15:23  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.4  2004/06/29 22:55:00  l2chef
 * binding ips can be defined independent of public server hostname
 *
 * Revision 1.3  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.loginserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.loginserver.clientpackets.RequestAuthLogin;
import net.sf.l2j.loginserver.clientpackets.RequestServerList;
import net.sf.l2j.loginserver.clientpackets.RequestServerLogin;
import net.sf.l2j.loginserver.serverpackets.Init;
import net.sf.l2j.loginserver.serverpackets.LoginFail;
import net.sf.l2j.loginserver.serverpackets.LoginOk;
import net.sf.l2j.loginserver.serverpackets.PlayOk;
import net.sf.l2j.loginserver.serverpackets.ServerBasePacket;
import net.sf.l2j.loginserver.serverpackets.ServerList;

/**
 * This class ...
 * 
 * @version $Revision: 1.14 $ $Date: 2004/09/26 00:17:58 $
 */
public class ClientThread extends Thread
{
	static Logger _log = Logger.getLogger(ClientThread.class.getName());
	
	private InputStream _in;
	private OutputStream _out;
	private NewCrypt _crypt;
	private Logins _logins;
	
	private Socket _csocket;
	private String _gameServerHost;
	private int _gameServerPort;
	private static List _bannedIPs = new ArrayList();
	
	public ClientThread(Socket client, Logins logins, String host, int port) throws IOException
	{
		_csocket = client;
		
		String ip = client.getInetAddress().getHostAddress();
		if (_bannedIPs.contains(ip))
		{
			throw new IOException("banned IP");
		}
		
		_in = client.getInputStream();
		_out = new BufferedOutputStream(client.getOutputStream());
		_crypt = new NewCrypt("[;'.]94-31==-%&@!^+]\000");
		_logins = logins;
		_gameServerHost = host;
		_gameServerPort = port;

		start();
	}
	
	public void run()
	{
		_log.fine("loginserver thread[C] started");
		
		int lengthHi =0;
		int lengthLo =0;
		int length = 0;
		boolean checksumOk = false;
		int sessionKey = -1;
		String account = null;
		
		String gameServerIp = null;
		
		try
		{
			// external ip address gets resolved again for each new connection
			// this should help users with dynamic IPs
			InetAddress adr = InetAddress.getByName(_gameServerHost);
			gameServerIp = adr.getHostAddress();

			Init startPacket = new Init();
			_out.write(startPacket.getLength() & 0xff);
			_out.write(startPacket.getLength() >> 8 &0xff);
			_out.write(startPacket.getContent());
			_out.flush();

			while (true)
			{
				lengthLo = _in.read();
				lengthHi = _in.read();
				length= lengthHi*256 + lengthLo;  
				
				if (lengthHi < 0 )
				{
					_log.warning("Client terminated the connection.");
					break;
				}
				
				byte[] incoming = new byte[length];
				incoming[0] = (byte) lengthLo;
				incoming[1] = (byte) lengthHi;
				
				int receivedBytes = 0;
				int newBytes = 0;
				while (newBytes != -1 && receivedBytes<length-2)
				{
					newBytes =  _in.read(incoming, 2, length-2);
					receivedBytes = receivedBytes + newBytes;
				}
				
				if (receivedBytes != length-2)
				{
					_log.warning("Incomplete Packet is sent to the server, closing connection.");
					break;
				}
				
				byte[] decrypt = new byte[length - 2];
				System.arraycopy(incoming, 2, decrypt, 0, decrypt.length);
				// decrypt if we have a key
				decrypt = _crypt.decrypt(decrypt);
				checksumOk = _crypt.checksum(decrypt);
				
				_log.finest("[C]\n"+printData(decrypt, decrypt.length));
				
				int packetType = decrypt[0]&0xff;
				switch (packetType)
				{
					case 00:
					{
						RequestAuthLogin ral = new RequestAuthLogin(decrypt);
						account = ral.getUser().toLowerCase();
						_log.fine("RequestAuthLogin from user:" + account);
						
						LoginController lc = LoginController.getInstance();
						if (_logins.loginValid(account, ral.getPassword(), _csocket.getInetAddress()))
						{	
							if (!lc.isAccountInGameServer(account) && !lc.isAccountInLoginServer(account) )
							{
								int accessLevel = _logins.getAccessLevel(account);

								if (accessLevel < 0)
								{
									LoginFail lok = new LoginFail(LoginFail.REASON_ACCOUNT_BANNED);
									sendPacket(lok);
								}
								else
								{
									sessionKey = lc.assignSessionKeyToLogin(account, accessLevel, _csocket);
									_log.fine("assigned SessionKey:" + Integer.toHexString(sessionKey));
									LoginOk lok = new LoginOk();
									sendPacket(lok);
								}
							}
							else
							{
								_log.fine("KICKING!");
								if (lc.isAccountInLoginServer(account)) {
									_log.warning("account is in use on Login server (kicking off):" + account);
									lc.getLoginServerConnection(account).close();
									lc.removeLoginServerLogin(account);
								}
								if (lc.isAccountInGameServer(account)) {
									_log.warning("account is in use on Gamea server (kicking off):" + account);
									lc.getClientConnection(account).close();
									lc.removeGameServerLogin(account);
								}
								LoginFail lok = new LoginFail(LoginFail.REASON_ACCOUNT_IN_USE);
								sendPacket(lok);
							}
						}
						else
						{
							LoginFail lok = new LoginFail(LoginFail.REASON_USER_OR_PASS_WRONG);
							sendPacket(lok);
						}

						break;
					}
					
					case 02:
					{
						_log.fine("RequestServerLogin");
						RequestServerLogin rsl = new RequestServerLogin(decrypt);
						_log.fine("login to server:" + rsl.getData3());
						
						//PlayFail po = new PlayFail(PlayFail.REASON_TOO_MANY_PLAYERS);
						PlayOk po = new PlayOk(sessionKey);
						sendPacket(po);
						break;
					}
					
					case 05:
					{
						_log.fine("RequestServerList");
						RequestServerList rsl = new RequestServerList(decrypt);
						
						ServerList sl = new ServerList();
						int current = LoginController.getInstance().getOnlinePlayerCount();
						int max = LoginController.getInstance().getMaxAllowedOnlinePlayers();
						sl.addServer(gameServerIp, _gameServerPort, true, false, current, max);
						sendPacket(sl);
						break;
					}
					
					default:
					{
						_log.warning("Unknown Packet:" + packetType);
						_log.warning(printData(decrypt, decrypt.length));
					}
				}
			}
		}
		catch (HackingException e)
		{
			_bannedIPs.add(e.getIP());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				_csocket.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
			
			LoginController.getInstance().removeLoginServerLogin(account);
			_log.fine("loginserver thread[C] stopped");
		}
	}
	
	/**
	 * @param sl
	 * @throws IOException
	 */
	private void sendPacket(ServerBasePacket sl) throws IOException
	{
		byte[] data = sl.getContent();
		_crypt.checksum(data);
		_log.finest("[S]\n"+printData(data, data.length));
		data = _crypt.crypt(data);

		int len = data.length+2;
		_out.write(len & 0xff);
		_out.write(len >> 8 &0xff);
		_out.write(data);
		_out.flush();
	}

	private String printData(byte[] data, int len)
	{ 
		StringBuffer result = new StringBuffer();
		
		int counter = 0;
		
		for (int i=0;i< len;i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i,4)+": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i-15;
				for (int a=0; a<16;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0 )
		{
			for (int i=0; i<17-rest;i++ )
			{
				result.append("   ");
			}

			int charpoint = data.length-rest;
			for (int a=0; a<rest;a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char)t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}

		
		return result.toString();
	}
	
	private String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i=number.length(); i< digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
	
	private String getTerminatedString( byte[] data, int offset )
	{
		StringBuffer result = new StringBuffer();
		
		for( int i=offset; i<data.length; i++)
		{
			if( data[i] == 0x00 )
			{
				break;
			}
			result.append( (char) data[i] );
		}

		return result.toString();
	}

	/**
	 * @param line
	 */
	public static void addBannedIP(String ip)
	{
		_bannedIPs.add(ip);
	}
}
