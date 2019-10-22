/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/LoginServer.java,v 1.9 2004/09/26 00:17:16 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/26 00:17:16 $
 * $Revision: 1.9 $
 * $Log: LoginServer.java,v $
 * Revision 1.9  2004/09/26 00:17:16  l2chef
 * banned IPs can be defined in a config file
 *
 * Revision 1.8  2004/08/14 22:49:38  l2chef
 * internal hostname can be defined for local network use (modified version of LittleVexys code)
 *
 * Revision 1.7  2004/07/19 22:22:32  j3ster
 * if external hostname is not set (default), use server's local ip (not 127.0.0.1)
 *
 * Revision 1.6  2004/07/09 20:17:51  l2chef
 * workaround to allow router usage and local use at the same time. contributed by  whatev
 *
 * Revision 1.5  2004/06/30 21:51:34  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.4  2004/06/29 22:55:00  l2chef
 * binding ips can be defined independent of public server hostname
 *
 * Revision 1.3  2004/06/27 20:57:14  l2chef
 * autoAccount creation can be set in config file
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
package net.sf.l2j.loginserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.9 $ $Date: 2004/09/26 00:17:16 $
 */
public class LoginServer extends Thread
{
	private LoginController _loginController;
	private ServerSocket _serverSocket;
	private Logins _logins;
	private String _ip;
	private int _port;
	private int _gamePort;
	private String _externalHostname; 
	private String _internalHostname; 
	private boolean _forwardLocalClient;   
	
	static Logger _log = Logger.getLogger(LoginServer.class.getName());

	public static void main(String[] args) throws IOException
	{
		LoginServer server = new LoginServer();
		_log.config("LoginServer Listening on port 2106");
		server.start();
	}
	
	/**
	 * 
	 */
	public void run()
	{
		while (true)
		{
			try
			{
				_log.fine("waiting for client connection...");
				Socket connection = _serverSocket.accept();
				_log.fine("connection from "+connection.getInetAddress());
				
				
				// use local ip addess if no external hostname is set or if
				// the the connection is requested from localhost (for router compatibility)
				String connectedIp = connection.getInetAddress().getHostAddress(); 
	            if (connectedIp.startsWith("192.168.") || 
						connectedIp.startsWith("10."))
	            {
					_log.fine("using internal ip as server ip " + _internalHostname); 
					new ClientThread(connection, _logins, _internalHostname, _gamePort); 
				} 
				else
				{
					_log.fine("using external ip as server ip " + _externalHostname);
					new ClientThread(connection, _logins, _externalHostname, _gamePort);
				}
			} 
			catch (IOException e)
			{
				// not a real problem
			}
		}
	}

	public LoginServer() throws IOException
	{
		super("LoginServer");

		Properties serverSettings = new Properties();
		InputStream is = getClass().getResourceAsStream("/server.cfg");  
		serverSettings.load(is);
		is.close();
		
		String loginIp = serverSettings.getProperty("LoginserverHostname");
		_externalHostname = serverSettings.getProperty("ExternalHostname");
		if (_externalHostname == null)
		{
			_externalHostname = "localhost";
		}

		_internalHostname = serverSettings.getProperty("InternalHostname");
		if (_internalHostname == null)
		{
			_internalHostname = "localhost";
		}
		String gamePort = serverSettings.getProperty("GameserverPort");
		String createAccounts = serverSettings.getProperty("AutoCreateAccounts");
		
		
		if (!"*".equals(loginIp))
		{
			InetAddress adr = InetAddress.getByName(loginIp);
			_ip = adr.getHostAddress();
			_log.config("LoginServer listening on IP:"+_ip + " Port 2106");
			_serverSocket = new ServerSocket(2106, 50, adr);
		}
		else
		{
			_log.config("LoginServer listening on all available IPs on Port 2106");
			_serverSocket = new ServerSocket(2106);
		}

		_log.config("Hostname for external connections is: "+_externalHostname);
		_log.config("Hostname for internal connections is: "+_internalHostname);
		
		_logins = new Logins( Boolean.valueOf(createAccounts).booleanValue() );
		
		_gamePort = Integer.parseInt(gamePort);
		
		
		try
		{
			InputStream bannedFile = getClass().getResourceAsStream("/banned_ip.cfg");
			if (bannedFile != null)
			{
				int count = 0;
				InputStreamReader reader = new InputStreamReader( bannedFile );
				LineNumberReader lnr = new LineNumberReader(reader);
				String line = null;
				while ( (line = lnr.readLine()) != null)
				{
					line = line.trim();
					if (line.length() > 0)
					{
						count++;
						ClientThread.addBannedIP(line);
					}
				}
				
				_log.info(count + " banned IPs defined");
			}
			else
			{
				_log.info("banned_ip.cfg not found");
			}
			
		}
		catch (Exception e)
		{
			_log.warning("error while reading banned file:"+e);
		}
		
	}
}
