/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/GameTimeController.java,v 1.1 2004/07/25 02:18:19 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/25 02:18:19 $
 * $Revision: 1.1 $
 * $Log: GameTimeController.java,v $
 * Revision 1.1  2004/07/25 02:18:19  l2chef
 * correct ingame time is managed (Deth/L2Chef)
 *
 * Revision 1.1  2004/07/25 00:35:56  l2chef
 * charnames are now checked for duplicates when creating char
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
package net.sf.l2j.gameserver;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;
import net.sf.l2j.gameserver.serverpackets.SunRise;
import net.sf.l2j.gameserver.serverpackets.SunSet;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/25 02:18:19 $
 */
public class GameTimeController extends Thread
{
	private static Logger _log = Logger.getLogger(GameTimeController.class.getName());
	
	private static GameTimeController _instance;
	private long _gameStartTime;
	
	/**
	 * one ingame day is 240 real minutes
	 */
	public static GameTimeController getInstance()
	{
		if (_instance == null)
		{
			_instance = new GameTimeController();
			_instance.start();
		}
		return _instance;
	}
	
	private GameTimeController()
	{
		super("GameTimeController");
		_gameStartTime = System.currentTimeMillis() - 3600000; // offset so that the server starts a day begin
	}
	
	public int getGameTime()
	{
		long time = (System.currentTimeMillis()-_gameStartTime)/10000;
		return (int) time;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				broadcastToPlayers(new SunRise());
				_log.fine("SunRise");
				sleep(360*60000);
				// reset the count
				_gameStartTime = System.currentTimeMillis();
				broadcastToPlayers(new SunSet());
				_log.fine("SunSet");
				sleep(60*60000);
			}
		}
		catch (InterruptedException e1)
		{
			// might happen if this thread is interrupted on shutdown
		}
	}

	/**
	 * @param rise
	 */
	private void broadcastToPlayers(ServerBasePacket packet)
	{
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();
		for (int i = 0; i < players.length; i++)
		{
			players[i].sendPacket(packet);
		}
	}
}
