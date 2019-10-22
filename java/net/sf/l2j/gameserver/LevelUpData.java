/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/LevelUpData.java,v 1.2 2004/09/28 02:13:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:13:08 $
 * $Revision: 1.2 $
 * $Log: LevelUpData.java,v $
 * Revision 1.2  2004/09/28 02:13:08  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/07/14 22:04:57  l2chef
 * Hp/Mp increase added (NightMarez)
 *
 *
 * Revision 1.1  2004/07/10 16:09:51  NightMarez.
 * Initial release
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

import net.sf.l2j.gameserver.model.L2LvlupData;

/**
 * This class ...
 * 
 * @author NightMarez
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:13:08 $
 */
public class LevelUpData
{
	private static Logger _log = Logger.getLogger(LevelUpData.class.getName());
	
	private static LevelUpData _instance;
	
	private HashMap _lvltable;
	
	public static LevelUpData getInstance()
	{
		if (_instance == null)
		{
			_instance = new LevelUpData();
		}
		return _instance;
	}
	
	private LevelUpData()
	{
		_lvltable = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File spawnDataFile = new File("data/lvlupgain.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(spawnDataFile)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2LvlupData lvlupData = parseList(line);
				_lvltable.put(new Integer(lvlupData.getClassid()), lvlupData);
			}

			_log.config("created " + _lvltable.size() + " Lvl up data templates");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("lvlupgain.csv is missing in data folder");
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
				// ignored
			}
		}
	}

	private L2LvlupData parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");
		
		L2LvlupData lvlDat = new L2LvlupData();
		lvlDat.setClassid(Integer.parseInt(st.nextToken()) );
		lvlDat.setDefaulthp(Double.parseDouble(st.nextToken()) );
		lvlDat.setDefaulthpadd(Double.parseDouble(st.nextToken()) );
		lvlDat.setDefaulthpbonus(Double.parseDouble(st.nextToken()) );
		lvlDat.setDefaultmp(Double.parseDouble(st.nextToken()) );
		lvlDat.setDefaultmpadd(Double.parseDouble(st.nextToken()) );
		lvlDat.setDefaultmpbonus(Double.parseDouble(st.nextToken()) );

		
		return lvlDat;
	}
	
	/**
	 * @param template id
	 * @return
	 */
	public L2LvlupData getTemplate(int classId)
	{
		return (L2LvlupData) _lvltable.get(new Integer(classId));
	}
}