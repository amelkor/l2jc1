/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/Server.java,v 1.4 2004/07/08 22:42:28 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/08 22:42:28 $
 * $Revision: 1.4 $
 * $Log: Server.java,v $
 * Revision 1.4  2004/07/08 22:42:28  l2chef
 * logfolder is created automatically
 *
 * Revision 1.3  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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
package net.sf.l2j;

import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;

import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.loginserver.LoginServer;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/07/08 22:42:28 $
 */
public class Server
{
	public static void main(String[] args) throws Exception
	{
		File logFolder = new File("log");
		logFolder.mkdir();
		InputStream is = new Server().getClass().getResourceAsStream("/log.cfg");  
		LogManager.getLogManager().readConfiguration(is);
		is.close();

		GameServer gameServer = new GameServer();
		gameServer.start();
		LoginServer loginServer = new LoginServer();
		loginServer.start();
	}/
}
