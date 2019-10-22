/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/NpcTable.java,v 1.7 2004/09/24 20:39:28 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/09/24 20:39:28 $
 * $Revision: 1.7 $
 * $Log: NpcTable.java,v $
 * Revision 1.7  2004/09/24 20:39:28  jeichhorn
 * added simple init check
 *
 * Revision 1.6  2004/08/08 00:45:24  l2chef
 * merged with npcdatatable
 * droptable from LittleVexy added
 *
 * Revision 1.5  2004/07/13 23:15:12  l2chef
 * empty blocks commented
 *
 * Revision 1.4  2004/07/04 11:08:08  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/27 20:50:02  l2chef
 * better error message when data file is missing. skipping of comment lines
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/24 20:39:28 $
 */
public class NpcTable
{
	private static Logger _log = Logger.getLogger(NpcTable.class.getName());
	
	private static NpcTable _instance;
	
	private HashMap _npcs;
	private boolean _initialized = true;
	
	public static NpcTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new NpcTable();
		}
		return _instance;
	}
	
	private NpcTable()
	{
		_npcs = new HashMap();
		parseData();
		parseAdditionalData();
		parseDropData();
	}
	
	public boolean isInitialized()
	{
	    return _initialized;
	}
	
	/**
	 * 
	 */
	private void parseData()
	{
		LineNumberReader lnr = null;
		try
		{
			File skillData = new File("data/npc.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2Npc npc = parseList(line);
				_npcs.put(new Integer(npc.getNpcId()), npc);
			}

			_log.config("created " + _npcs.size() + " NPC templates");
		}
		catch (FileNotFoundException e)
		{
		    _initialized = false;
			_log.warning("npc.csv is missing in data folder");
		}
		catch (Exception e)
		{
		    _initialized = false;
			_log.warning("error while creating npc table "+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	private L2Npc parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		L2Npc npc = new L2Npc();
		int id = Integer.parseInt(st.nextToken());
		if (id > 1000000)
		{
			id -= 1000000;
		}
		npc.setNpcId(id);
		npc.setName( st.nextToken() );
		npc.setType( st.nextToken() );
		npc.setRadius( Double.parseDouble(st.nextToken()) );
		npc.setHeight( Double.parseDouble(st.nextToken()) );
		
		return npc;
	}

	private void parseAdditionalData()
	{
		LineNumberReader lnr = null;
		try
		{
			File npcDataFile = new File("data/npc2.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(npcDataFile)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				try
				{
					parseAdditionalDataLine(line);
				}
				catch (Exception e)
				{
					_log.warning("parsing error in npc2.csv, line " + lnr.getLineNumber() +" / " + e.toString());
				}
			}
		}
		catch (FileNotFoundException e)
		{
			_log.warning("npc2.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating npc data table "+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	/**
	 * this updates the generated L2Npc with additional data that is not 
	 * available in the client data
	 * @param line
	 */
	private void parseAdditionalDataLine(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		int id = Integer.parseInt(st.nextToken());
		L2Npc npcDat = (L2Npc)_npcs.get(new Integer(id));
		if (npcDat == null)
		{
			_log.warning("missing npc template id:"+id );
			return;
		}
		// skip name
		st.nextToken();
		npcDat.setLevel(Integer.parseInt(st.nextToken()) );
		npcDat.setSex( st.nextToken() );
		npcDat.setType( st.nextToken() );
		npcDat.setAttackRange(Integer.parseInt(st.nextToken()) );
		npcDat.setHp(Integer.parseInt(st.nextToken()) );
		npcDat.setMp(Integer.parseInt(st.nextToken()) );
		npcDat.setExp(Integer.parseInt(st.nextToken()) );
		npcDat.setSp(Integer.parseInt(st.nextToken()) );
		npcDat.setPatk(Integer.parseInt(st.nextToken()) );
		npcDat.setPdef(Integer.parseInt(st.nextToken()) );
		npcDat.setMatk(Integer.parseInt(st.nextToken()) );
		npcDat.setMdef(Integer.parseInt(st.nextToken()) );
		npcDat.setAtkspd(Integer.parseInt(st.nextToken()) );
		npcDat.setAgro( Integer.parseInt(st.nextToken())==1 );
		npcDat.setMatkspd(Integer.parseInt(st.nextToken()) );
		npcDat.setRhand(Integer.parseInt(st.nextToken()));
		npcDat.setLhand(Integer.parseInt(st.nextToken()));
		npcDat.setArmor(Integer.parseInt(st.nextToken()));
		npcDat.setWalkSpeed(Integer.parseInt(st.nextToken()));
		npcDat.setRunSpeed(Integer.parseInt(st.nextToken()));
	}
	
	
	private void parseDropData()
	{
		LineNumberReader lnr = null;
		try
		{
			File dropDataFile = new File("data/droplist.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(dropDataFile)));
			
			String line = null;
			int n = 0;			// simple count
			
			HashMap _datatable_inner = null;
			
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				try
				{
					parseDropLine(line);
					n++;
				}
				catch (Exception e)
				{
					_log.warning("parsing error in droplist.csv, line " + lnr.getLineNumber() +" / " + e.toString());
				}
			}

			_log.config("created "+n+" drop data templates");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("droplist.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating drop data table "+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private void parseDropLine(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");
		int mobId = Integer.parseInt(st.nextToken());
		L2Npc npc = (L2Npc) _npcs.get(new Integer(mobId));

		if (npc == null)
		{
			_log.warning("could not add drop data for npcid:"+mobId);
			return;
		}
		
		L2DropData dropDat = new L2DropData();
		
		dropDat.setItemId(Integer.parseInt(st.nextToken()));
		dropDat.setMinDrop(Integer.parseInt(st.nextToken()));
		dropDat.setMaxDrop(Integer.parseInt(st.nextToken()));
		dropDat.setSweep(Integer.parseInt(st.nextToken()) == 1);
		dropDat.setChance(Integer.parseInt(st.nextToken()));
		
		npc.addDropData(dropDat);
	}
	
	
	/**
	 * @param template id
	 * @return
	 */
	public L2Npc getTemplate(int id)
	{
		return (L2Npc) _npcs.get(new Integer(id));
	}
}
