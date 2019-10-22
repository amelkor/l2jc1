/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/SkillTable.java,v 1.7 2004/09/24 20:39:28 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/09/24 20:39:28 $
 * $Revision: 1.7 $
 * $Log: SkillTable.java,v $
 * Revision 1.7  2004/09/24 20:39:28  jeichhorn
 * added simple init check
 *
 * Revision 1.6  2004/08/17 00:48:05  l2chef
 * skill2.csv needs another column containing spell power
 *
 * Revision 1.5  2004/07/13 23:15:34  l2chef
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

import net.sf.l2j.gameserver.model.L2Skill;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/24 20:39:28 $
 */
public class SkillTable
{
	private static Logger _log = Logger.getLogger(SkillTable.class.getName());
	private static SkillTable _instance;
	
	private HashMap _skills;
	private boolean _initialized = true;
	
	public static SkillTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SkillTable();
		}
		return _instance;
	}
	
	private SkillTable()
	{
		_skills = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File skillData = new File("data/skills.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					// skip comments
					continue;
				}
				
				L2Skill skill = parseList(line);
				_skills.put(new Integer(skill.getId()*100 + skill.getLevel()), skill);
			}

			_log.config("created " + _skills.size() + " skills");

			
			// fetch additional data that is not in skillgrp.txt
			skillData = new File("data/skills2.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					// skip comments
					continue;
				}

				parseList2(line);
			}
			
		}
		catch (FileNotFoundException e)
		{
		    _initialized = false;
			_log.warning("skills.csv or skills2.csv is missing in data folder: " + e.toString());
		}
		catch (Exception e)
		{
		    _initialized = false;
			_log.warning("error while creating skill table: " + e.toString());
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
	
	public boolean isInitialized()
	{
	    return _initialized;
	}
	
	private void parseList2(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		int id = Integer.parseInt(st.nextToken());
		st.nextToken();  // skip name
		int level = Integer.parseInt(st.nextToken());
		Integer key = new Integer(id*100 + level);		
		L2Skill skill = (L2Skill) _skills.get(key);
		if (skill == null)
		{
			return;
		}
		
		String target = st.nextToken();
		if (target.equalsIgnoreCase("self"))
		{
			skill.setTargetType(L2Skill.TARGET_SELF);
		}
		else if (target.equalsIgnoreCase("one"))
		{
			skill.setTargetType(L2Skill.TARGET_ONE);
		}
		else if (target.equalsIgnoreCase("party"))
		{
			skill.setTargetType(L2Skill.TARGET_PARTY);
			
		}
		else if (target.equalsIgnoreCase("clan"))
		{
			skill.setTargetType(L2Skill.TARGET_CLAN);
			
		}
		else if (target.equalsIgnoreCase("pet"))
		{
			skill.setTargetType(L2Skill.TARGET_PET);
			
		}

		skill.setPower( Integer.parseInt(st.nextToken()) );
	}

	
	private L2Skill parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		L2Skill skill = new L2Skill();
		skill.setId( Integer.parseInt(st.nextToken()) );
		skill.setName( st.nextToken() );
		skill.setLevel( Integer.parseInt(st.nextToken()) );
		String opType = st.nextToken();
		if (opType.equalsIgnoreCase("once"))
		{
			skill.setOperateType(L2Skill.OP_ONCE);
		}
		else if (opType.equalsIgnoreCase("always"))
		{
			skill.setOperateType(L2Skill.OP_ALWAYS);
		}
		else if (opType.equalsIgnoreCase("duration"))
		{
			skill.setOperateType(L2Skill.OP_DURATION);
		}
		else if (opType.equalsIgnoreCase("toggle"))
		{
			skill.setOperateType(L2Skill.OP_TOGGLE);
		}
		
		boolean isMagic = Boolean.valueOf(st.nextToken()).booleanValue();
		
		skill.setMpConsume( Integer.parseInt(st.nextToken()) );
		skill.setHpConsume( Integer.parseInt(st.nextToken()) );
		skill.setItemConsumeId( Integer.parseInt(st.nextToken()) );
		skill.setItemConsume( Integer.parseInt(st.nextToken()) );
		
		skill.setCastRange( Integer.parseInt(st.nextToken()) );
		skill.setSkillTime( Integer.parseInt(st.nextToken()) );
		skill.setReuseDelay( Integer.parseInt(st.nextToken()) );
		skill.setBuffDuration( Integer.parseInt(st.nextToken()) );
		skill.setHitTime( Integer.parseInt(st.nextToken()) );
		return skill;
	}

	/**
	 * @param magicId
	 * @param level
	 * @return
	 */
	public L2Skill getInfo(int magicId, int level)
	{
		return (L2Skill) _skills.get(new Integer(magicId*100 + level));
	}
}
