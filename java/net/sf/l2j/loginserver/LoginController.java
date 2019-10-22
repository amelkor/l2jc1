/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/LoginController.java,v 1.6 2004/09/19 00:51:23 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/19 00:51:23 $
 * $Revision: 1.6 $
 * $Log: LoginController.java,v $
 * Revision 1.6  2004/09/19 00:51:23  nuocnam
 * user with access level 50+ can log in even if server is full (miegalius, nuocnam)
 *
 * Revision 1.5  2004/09/16 18:59:20  nuocnam
 * Old connection is dropped if you try to log in and account is already in use. (nuocnam)
 *
 * Revision 1.4  2004/07/05 23:02:11  l2chef
 * access levels are stored for logins
 *
 * Revision 1.3  2004/07/04 11:34:58  l2chef
 * user limit is enforced
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

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.Connection;

/**
 * This class ...
 *
 * @version $Revision: 1.6 $ $Date: 2004/09/19 00:51:23 $
 */
public class LoginController
{
    private static LoginController _instance;
    /**
     * this map contains the session ids that belong to one account
     */
    private Map _logins;

    private HashMap _accountsInLoginServer;
    private HashMap _accountsInGameServer;
    private Map _accessLevels;
    private int _maxAllowedOnlinePlayers;
    private HashMap _activeConnections;

    private LoginController()
    {
        _logins = new HashMap();
        _accountsInGameServer = new HashMap();
        _accountsInLoginServer = new HashMap();
        _accessLevels = new HashMap();
        _activeConnections = new HashMap();
    }

    public static LoginController getInstance()
    {
        if (_instance == null)
        {
            _instance = new LoginController();
        }

        return _instance;
    }

    public int assignSessionKeyToLogin(String account, int accessLevel, Socket _csocket)
    {
        int key = -1;

        key = (int) System.currentTimeMillis() & 0xffffff;
        _logins.put(account, new Integer(key));
        _accountsInLoginServer.put(account, _csocket);
        _accessLevels.put(account, new Integer(accessLevel));

        return key;
    }

    public void addGameServerLogin(String account, Connection connection)
    {
        _accountsInGameServer.put(account, connection);
    }

    public void removeGameServerLogin(String account)
    {
        if (account != null)
        {
            _logins.remove(account);
            _accountsInGameServer.remove(account);
        }
    }

    public void removeLoginServerLogin(String account)
    {
        if (account != null)
        {
            _accountsInLoginServer.remove(account);
        }
    }

    public boolean isAccountInLoginServer(String account)
    {
        return _accountsInLoginServer.containsKey(account);
    }

    public boolean isAccountInGameServer(String account)
    {
        return _accountsInGameServer.containsKey(account);
    }

    public int getKeyForAccount(String account)
    {
        int key = 0;

        Integer result = (Integer) _logins.get(account);
        if (result != null)
        {
            key = result.intValue();
        }

        return key;
    }

    public int getOnlinePlayerCount()
    {
        return _accountsInGameServer.size();
    }

    public int getMaxAllowedOnlinePlayers()
    {
        return _maxAllowedOnlinePlayers;
    }

    public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers)
    {
        _maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
    }

    /**
     * @return
     */
    public boolean loginPossible(int access)
    {
        return ((_accountsInGameServer.size() < _maxAllowedOnlinePlayers) || (access >= 50));
    }

    /**
     * @param loginName
     * @return
     */
    public int getGmAccessLevel(String loginName)
    {
        return ((Integer) _accessLevels.get(loginName)).intValue();
    }

    public Connection getClientConnection(String loginName)
    {
        return (Connection) _accountsInGameServer.get(loginName);
    }

    public Socket getLoginServerConnection(String loginName)
    {
        return (Socket) _accountsInLoginServer.get(loginName);
    }
}
