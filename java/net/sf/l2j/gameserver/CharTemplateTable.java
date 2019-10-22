/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/CharTemplateTable.java,v 1.5 2004/07/13 23:12:57 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:12:57 $
 * $Revision: 1.5 $
 * $Log: CharTemplateTable.java,v $
 * Revision 1.5  2004/07/13 23:12:57  l2chef
 * empty blocks commented
 *
 * Revision 1.4  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
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

import net.sf.l2j.gameserver.templates.L2CharTemplate;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/07/13 23:12:57 $
 */
public class CharTemplateTable
{
	private static Logger _log = Logger.getLogger(CharTemplateTable.class.getName());
			
	private static CharTemplateTable _instance;
	
	private HashMap _templates;
	
	public static CharTemplateTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new CharTemplateTable();
		}
		return _instance;
	}
	
	private CharTemplateTable()
	{
		_templates = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File skillData = new File("data/char_templates.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));

			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2CharTemplate ct = new L2CharTemplate();
				
				StringTokenizer st = new StringTokenizer(line,";");
				ct.setClassId( Integer.parseInt(st.nextToken()) );
				ct.setClassName( st.nextToken() );
				ct.setRaceId( Integer.parseInt(st.nextToken()) );
				ct.setStr( Integer.parseInt(st.nextToken()) );
				ct.setCon( Integer.parseInt(st.nextToken()) );
				ct.setDex( Integer.parseInt(st.nextToken()) );
				ct.setInt( Integer.parseInt(st.nextToken()) );
				ct.setWit( Integer.parseInt(st.nextToken()) );
				ct.setMen( Integer.parseInt(st.nextToken()) );
				ct.setHp( Integer.parseInt(st.nextToken()) );
				ct.setMp( Integer.parseInt(st.nextToken()) );
				ct.setPatk( Integer.parseInt(st.nextToken()) );
				ct.setPdef( Integer.parseInt(st.nextToken()) );
				ct.setMatk( Integer.parseInt(st.nextToken()) );
				ct.setMdef( Integer.parseInt(st.nextToken()) );
				ct.setPspd( Integer.parseInt(st.nextToken()) );
				ct.setMspd( Integer.parseInt(st.nextToken()) );
				ct.setAcc( Integer.parseInt(st.nextToken()) );
				ct.setCrit( Integer.parseInt(st.nextToken()) );
				ct.setEvas( Integer.parseInt(st.nextToken()) );
				ct.setMoveSpd( Integer.parseInt(st.nextToken()) );
				ct.setLoad( Integer.parseInt(st.nextToken()) );

				ct.setX( Integer.parseInt(st.nextToken()) );
				ct.setY( Integer.parseInt(st.nextToken()) );
				ct.setZ( Integer.parseInt(st.nextToken()) );

				ct.setCanCraft( Integer.parseInt(st.nextToken()) );

				ct.setMUnk1( Double.parseDouble(st.nextToken()) );
				ct.setMUnk2( Double.parseDouble(st.nextToken()) );
				ct.setMColR( Double.parseDouble(st.nextToken()) );
				ct.setMColH( Double.parseDouble(st.nextToken()) );

				ct.setFUnk1( Double.parseDouble(st.nextToken()) );
				ct.setFUnk2( Double.parseDouble(st.nextToken()) );
				ct.setFColR( Double.parseDouble(st.nextToken()) );
				ct.setFColH( Double.parseDouble(st.nextToken()) );
				
				while(st.hasMoreTokens())
				{
					ct.addItem(Integer.parseInt(st.nextToken()));
				}
				
				_templates.put(new Integer(ct.getClassId()), ct);
			}

			_log.config("loaded " + _templates.size() + " char templates");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("char_templates.csv is missing in data folder, char creation will fail.");
		}
		catch (Exception e)
		{
			_log.warning("error while loading char templates "+e);
			e.printStackTrace();
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
	
	public L2CharTemplate getTemplate(int classId)
	{
		return (L2CharTemplate) _templates.get(new Integer(classId));
	}
	
	public L2CharTemplate[] getAllTemplates()
	{
		return (L2CharTemplate[]) _templates.values().toArray(new L2CharTemplate[_templates.size()]);
	}
}
