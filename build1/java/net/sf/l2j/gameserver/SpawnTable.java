/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/SpawnTable.java,v 1.4 2004/09/28 01:56:34 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:56:34 $
 * $Revision: 1.4 $
 * $Log: SpawnTable.java,v $
 * Revision 1.4  2004/09/28 01:56:34  nuocnam
 * Added javadoc header.
 *
 * Revision 1.3  2004/08/08 00:49:30  l2chef
 * npc datatable merged with npctable
 *
 * Revision 1.2  2004/07/23 21:25:10  l2chef
 * spawns check for correct handler class at creation
 *
 * Revision 1.1  2004/07/23 01:51:03  l2chef
 * *** empty log message ***
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @author Nightmare
 * @version $Revision: 1.4 $ $Date: 2004/09/28 01:56:34 $
 */
public class SpawnTable
{
	private static Logger _log = Logger.getLogger(SpawnTable.class.getName());
	
	private static SpawnTable _instance;
	
	private HashMap _spawntable;
	private int _npcSpawnCount;
	
	private int _highestId;
	
	public static SpawnTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SpawnTable();
		}
		return _instance;
	}
	
	private SpawnTable()
	{
		_spawntable = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File spawnDataFile = new File("data/spawnlist.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(spawnDataFile)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				try
				{
					if (line.trim().length() == 0 || line.startsWith("#"))
					{
						continue;
					}
					
					L2Spawn spawn = parseList(line);
					_spawntable.put(new Integer(spawn.getId()), spawn);
					if (spawn.getId() > _highestId)
					{
						_highestId = spawn.getId();
					}
				}
				catch (Exception e1)
				{
					// problem with initializing spawn, go to next one
					_log.warning("spawn couldnt be initialized:"+e1);
				}
			}
			
			_log.config("created " + _spawntable.size() + " Spawn handlers");
			_log.fine("Spawning completed, total number of NPCs in the world: "+_npcSpawnCount);
		}
		catch (FileNotFoundException e)
		{
			_log.warning("spawnlist.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating spawn list "+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignored
			}
		}
	}
	
	private L2Spawn parseList(String line) throws SecurityException, ClassNotFoundException
	{
		StringTokenizer st = new StringTokenizer(line,";");
		
		int spawnId = Integer.parseInt(st.nextToken());
		String location = st.nextToken();
		int count =  Integer.parseInt(st.nextToken());
		int npcId = Integer.parseInt(st.nextToken());
		
		L2Npc template1 = NpcTable.getInstance().getTemplate(npcId);
		if (template1 == null)
		{
			_log.warning("mob data for id:"+npcId+" missing in npc.csv");
			return null;
		}
		
		L2Spawn spawnDat = new L2Spawn(template1);
		spawnDat.setId(spawnId);
		spawnDat.setAmount(count);
		spawnDat.setLocx(Integer.parseInt(st.nextToken()) );
		spawnDat.setLocy(Integer.parseInt(st.nextToken()) );
		spawnDat.setLocz(Integer.parseInt(st.nextToken()) );
		spawnDat.setRandomx(Integer.parseInt(st.nextToken()) );
		spawnDat.setRandomy(Integer.parseInt(st.nextToken()) );
		spawnDat.setHeading(Integer.parseInt(st.nextToken()) );
		spawnDat.setRespawnDelay(Integer.parseInt(st.nextToken()) );
		// start the spawning
		_npcSpawnCount += spawnDat.init();
		
		return spawnDat;
	}
	
	/**
	 * @param template id
	 * @return
	 */
	public L2Spawn getTemplate(int Id)
	{
		return (L2Spawn) _spawntable.get(new Integer(Id));
	}
	
	public void addNewSpawn(L2Spawn spawn)
	{
		_highestId++;
		spawn.setId(_highestId);
		_spawntable.put(new Integer(spawn.getId()), spawn);
	}
	
}
