/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/AuthLogin.java,v 1.8 2004/09/19 00:51:23 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/19 00:51:23 $
 * $Revision: 1.8 $
 * $Log: AuthLogin.java,v $
 * Revision 1.8  2004/09/19 00:51:23  nuocnam
 * user with access level 50+ can log in even if server is full (miegalius, nuocnam)
 *
 * Revision 1.7  2004/09/16 18:59:20  nuocnam
 * Old connection is dropped if you try to log in and account is already in use. (nuocnam)
 *
 * Revision 1.6  2004/07/19 23:07:29  l2chef
 * account names are no longer casesensitive (NuocNam)
 *
 * Revision 1.5  2004/07/11 23:37:38  l2chef
 * chars are always reloaded from disk  (whatev)
 *
 * Revision 1.4  2004/07/05 23:02:43  l2chef
 * access levels are stored for logins
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
import net.sf.l2j.gameserver.serverpackets.AuthLoginFail;
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;
import net.sf.l2j.loginserver.LoginController;

/**
 * This class ...
 * 
 * @version $Revision: 1.8 $ $Date: 2004/09/19 00:51:23 $
 */
public class AuthLogin extends ClientBasePacket
{
	private static final String _C__08_AUTHLOGIN = "[C] 08 AuthLogin";
	private static Logger _log = Logger.getLogger(AuthLogin.class.getName());
	
	// loginName + keys must match what the loginserver used.  

	/**
	 * @param decrypt
	 */
	public AuthLogin(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		
		String loginName = readS().toLowerCase();
		long key1  = readD();	// these
		long key2  = readD();	// are given by login server after server selection
		
		int access = LoginController.getInstance().getGmAccessLevel(loginName);
		if (!LoginController.getInstance().loginPossible(access))
		{
			_log.warning("Server is full. client is blocked: "+loginName);
			client.getConnection().sendPacket(new AuthLoginFail(AuthLoginFail.SYSTEM_ERROR_LOGIN_LATER));
			return;
		}

		_log.fine("user:" + loginName);
		_log.fine("key:" + Long.toHexString(key1) + " " + Long.toHexString(key2));
		
		client.setLoginName(loginName);
		client.setLoginFolder(loginName);
		
		int sessionKey = LoginController.getInstance().getKeyForAccount(loginName);
		if (sessionKey != key2)
		{
			_log.warning("session key is not correct. closing connection");
			client.getConnection().sendPacket(new AuthLoginFail(1));
		}
		else
		{
			LoginController.getInstance().addGameServerLogin(loginName, client.getConnection());
	
			// send char list
			CharSelectInfo cl = new CharSelectInfo(loginName, client.getSessionId());
			client.getConnection().sendPacket(cl);
		}
		
		// store the accessLevel for this login
		client.setAccessLevel(access);
		_log.fine("access level is set to "+access);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__08_AUTHLOGIN;
	}
}
