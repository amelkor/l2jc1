/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/GameServer.java,v 1.25 2004/10/11 17:27:33 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:27:33 $
 * $Revision: 1.25 $
 * $Log: GameServer.java,v $
 * Revision 1.25  2004/10/11 17:27:33  nuocnam
 * Initalizes GmListTable
 *
 * Revision 1.24  2004/09/24 20:40:16  jeichhorn
 * added check if ItemTable, NpcTable and SkillTable are initialized.
 *
 * Revision 1.23  2004/09/19 04:42:22  nuocnam
 * Added configurable xp, sp and adena rates (myrdos, nuocnam)
 *
 * Revision 1.22  2004/08/17 00:47:32  l2chef
 * new skill handlers contributed by Angel Kira.
 *
 * Revision 1.21  2004/08/10 00:47:19  l2chef
 * clan folders are created
 *
 * Revision 1.20  2004/08/08 03:01:10  l2chef
 * potion handler added (imwookie)
 *
 * Revision 1.19  2004/08/08 00:45:42  l2chef
 * npc datatable merged with npctable
 *
 * Revision 1.18  2004/08/07 14:12:35  l2chef
 * new item handlers
 *
 * Revision 1.17  2004/08/04 21:54:28  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.16  2004/07/30 22:28:59  l2chef
 * teleport destinations bases on maptile (NuocNam)
 *
 * Revision 1.15  2004/07/29 21:19:54  l2chef
 * announcement added (Deth)
 *
 * Revision 1.14  2004/07/28 23:55:35  l2chef
 * load modifiers for caluculations (Nightmarez)
 *
 * Revision 1.13  2004/07/25 02:18:19  l2chef
 * correct ingame time is managed (Deth/L2Chef)
 *
 * Revision 1.12  2004/07/25 00:37:19  l2chef
 * charnames are now checked for duplicates when creating char
 *
 * Revision 1.11  2004/07/23 01:51:19  l2chef
 * spawntable is used
 *
 * Revision 1.10  2004/07/14 22:05:26  l2chef
 * Hp/Mp increase added (NightMarez)
 *
 * Revision 1.9  2004/07/13 22:59:20  l2chef
 * world init removed
 *
 * Revision 1.8  2004/07/11 11:32:44  l2chef
 * npc data table added
 *
 * Revision 1.7  2004/07/08 22:20:00  l2chef
 * teleporter npcs added by NightMarez and Nuocnam
 *
 * Revision 1.6  2004/07/04 11:43:35  jeichhorn
 * Added loading of the experience mappings
 *
 * Revision 1.5  2004/07/04 11:07:05  l2chef
 * user limit is enforced
 *
 * Revision 1.4  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.3  2004/06/29 22:55:00  l2chef
 * binding ips can be defined independent of public server hostname
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
package net.sf.l2j.gameserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.handler.itemhandlers.PetSummon;
import net.sf.l2j.gameserver.handler.itemhandlers.Potions;
import net.sf.l2j.gameserver.handler.itemhandlers.ScrollOfEscape;
import net.sf.l2j.gameserver.handler.itemhandlers.SoulShots;
import net.sf.l2j.gameserver.handler.itemhandlers.WorldMap;
import net.sf.l2j.gameserver.handler.skillhandlers.DamageSkill;
import net.sf.l2j.gameserver.handler.skillhandlers.HealSkill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.loginserver.LoginController;

/**
 * This class ...
 * 
 * @version $Revision: 1.25 $ $Date: 2004/10/11 17:27:33 $
 */
public class GameServer extends Thread
{
	private ServerSocket _serverSocket;
	private TradeController _tradeController;
	private SkillTable _skillTable;
	private SkillTreeTable _skillTreeTable;
	private ClanTable _clanTable;
	private ItemTable _itemTable;
	private NpcTable _npcTable;
	private ExperienceTable _expTable;
	private TeleportLocationTable _teleTable;
	private LevelUpData _levelUpData;
	private Modifiers _modifiers;
	
	private L2World _world;
	private CharTemplateTable _charTemplates;
	private IdFactory _idFactory;
	
	private String _ip;
	private int _port;
	static Logger _log = Logger.getLogger(GameServer.class.getName());
	private LoginController _loginController;
	private SpawnTable _spawnTable;
	private CharNameTable _charNametable;
	private GameTimeController _gameTimeController;
	private Announcements _announcements;
	private MapRegionTable _mapRegions;
	private PriceList _pricelist;
	private ItemHandler _itemHandler;
	private SkillHandler _skillHandler;
	private RatesController _RatesController;
	private GmListTable _gmList;
	 
	public static void main(String[] args) throws Exception
	{
		GameServer server = new GameServer();
		_log.config("GameServer Listening on port 7777");
		//server.testPlayback();
		server.start();
	}

	/**
	 *  
	 */
	private void testPlayback()
	{
		long diff = 0;
		long first = -1;

		try
		{
			LineNumberReader lnr =
				new LineNumberReader(new FileReader("playback.dat"));

			String line = null;
			while ((line = lnr.readLine()) != null)
			{
				if (line.length() > 0 && line.substring(0, 1).equals("1"))
				{
					String timestamp = line.substring(0, 13);
					long time = Long.parseLong(timestamp);
					if (first == -1)
					{
						long start = System.currentTimeMillis();
						first = time;
						diff = start - first;
					}
					
					String cs = line.substring(14, 15);
					// read packet definition
					ByteArrayOutputStream bais = new ByteArrayOutputStream();

					while (true)
					{
						String temp = lnr.readLine();
						if (temp.length() == 0)
						{
							break;
						}

						String bytes = temp.substring(6, 53);
						StringTokenizer st = new StringTokenizer(bytes);
						while (st.hasMoreTokens())
						{
							String b = st.nextToken();
							int number = Integer.parseInt(b, 16);
							bais.write(number);
						}
					}

					if (cs.equals("S"))
					{
						//wait for timestamp and send packet
						int wait =
							(int) (time + diff - System.currentTimeMillis());
						if (wait > 0)
						{
							_log.fine("waiting"+ wait);
							Thread.sleep(wait);
						}
						
						_log.fine("sending:"+ time);

					}
					else
					{
						// skip packet
					}
				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
				_log.fine("used mem:" + getUsedMemoryMB()+"MB" );
				_log.info("waiting for client connection");
				Socket connection = _serverSocket.accept();
				new ClientThread(connection);
			} 
			catch (IOException e)
			{
				// not a real problem
			}
		}
	}
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024;		
	}

	public GameServer() throws Exception
	{
		super("GameServer");
		_log.finest("used mem:" + getUsedMemoryMB()+"MB" );
		
		
		Properties serverSettings = new Properties();
		InputStream is = getClass().getResourceAsStream("/server.cfg");  
		serverSettings.load(is);
		is.close();

		String hostname = serverSettings.getProperty("GameserverHostname");
		String port = serverSettings.getProperty("GameserverPort");
		_port = Integer.parseInt(port);
		
		if (!"*".equals(hostname))
		{
			InetAddress adr = InetAddress.getByName(hostname);
			_ip = adr.getHostAddress();
			_serverSocket = new ServerSocket(_port, 50, adr);
			_log.config("GameServer listening on IP:"+_ip + " Port "+_port);
		}
		else
		{
			_serverSocket = new ServerSocket(_port);
			_log.config("GameServer listening on all available IPs on Port "+_port);
		}

		String maxPlayerStr = serverSettings.getProperty("MaximumOnlineUsers");
		int maxPlayers = Integer.parseInt(maxPlayerStr);
		_log.config("Maximum Numbers of Connected Players: "+ maxPlayers);

		
		new File("data/clans").mkdirs();
		new File("data/crests").mkdirs();

		
		// keep the references of Singletons to prevent garbage collection
		_loginController = LoginController.getInstance();
		_loginController.setMaxAllowedOnlinePlayers(maxPlayers);
		_charNametable = CharNameTable.getInstance();

		_idFactory = IdFactory.getInstance();
		_itemTable = ItemTable.getInstance();
		if (!_itemTable.isInitialized())
		{
		    _log.severe("Could not find the extraced files. Please run convertData.");
		    throw new Exception("Could not initialize the item table");
		}
		
		_tradeController = TradeController.getInstance();
		_skillTable = SkillTable.getInstance();
		if (!_skillTable.isInitialized())
		{
		    _log.severe("Could not find the extraced files. Please run convertData.");
		    throw new Exception("Could not initialize the skill table");
		}
		
		_skillTreeTable = SkillTreeTable.getInstance();
		_charTemplates = CharTemplateTable.getInstance();
		_clanTable = ClanTable.getInstance();

		_npcTable = NpcTable.getInstance();
		if (!_npcTable.isInitialized())
		{
		    _log.severe("Could not find the extraced files. Please run convertData.");
		    throw new Exception("Could not initialize the npc table");
		}
		
		_expTable = ExperienceTable.getInstance();
		_teleTable = TeleportLocationTable.getInstance();
		_levelUpData = LevelUpData.getInstance();
		_modifiers = Modifiers.getInstance();
		_world = L2World.getInstance();	
	//	_world.initWorldContent();
		_spawnTable = SpawnTable.getInstance();
		_gameTimeController = GameTimeController.getInstance();
		_announcements = Announcements.getInstance();
		_mapRegions = MapRegionTable.getInstance();
		_pricelist = PriceList.getInstance();
		_RatesController = RatesController.getInstance();
		
		_itemHandler = ItemHandler.getInstance();
		_itemHandler.registerItemHandler(new PetSummon());
		_itemHandler.registerItemHandler(new ScrollOfEscape());
		_itemHandler.registerItemHandler(new SoulShots());
		_itemHandler.registerItemHandler(new WorldMap());
		_itemHandler.registerItemHandler(new Potions());
		
		_skillHandler = SkillHandler.getInstance();
		_skillHandler.registerSkillHandler(new HealSkill());
		_skillHandler.registerSkillHandler(new DamageSkill());
		
		_gmList = GmListTable.getInstance();
	}
}
