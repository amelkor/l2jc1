/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/ExperienceTable.java,v 1.3 2004/07/13 23:13:45 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:13:45 $
 * $Revision: 1.3 $
 * $Log: ExperienceTable.java,v $
 * Revision 1.3  2004/07/13 23:13:45  l2chef
 * empty blocks commented
 *
 * Revision 1.2  2004/07/04 14:32:10  jeichhorn
 * *** empty log message ***
 *
 * Revision 1.1  2004/07/04 11:43:48  jeichhorn
 * initial import
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

/**
 * This class ...
 * 
 * Copyright (c) 2004
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/13 23:13:45 $
 * @author  <a href="mailto:joerg@finalize.de">J&ouml;rg Eichhorn</a>
 */
public class ExperienceTable
{
    private static ExperienceTable _instance;
    private static Logger _log = Logger.getLogger(NpcTable.class.getName());
    private HashMap _exp;
    
    private ExperienceTable()
    {
        _exp = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File skillData = new File("data/exp.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				StringTokenizer expLine = new StringTokenizer(line, ";");
				_exp.put(new Integer(expLine.nextToken()), new Integer(expLine.nextToken()));
			}

			_log.config("loaded " + _exp.size() + " exp mappings");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("exp.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating exp map " + e);
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
    
    public static ExperienceTable getInstance()
    {
        if (_instance == null)
        {
            _instance = new ExperienceTable();
        }
        return _instance;            
    }
    
    public int getExp(int level)
    {
        if (_exp.containsKey(new Integer(level)))
        {
            return ((Integer) _exp.get(new Integer(level))).intValue();          
        }
        else
        {
            return Integer.MAX_VALUE;
        }
    }
    
    
}
