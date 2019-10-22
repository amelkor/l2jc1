/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/Modifiers.java,v 1.2 2004/09/28 01:55:37 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:55:37 $
 * $Revision: 1.2 $
 * $Log: Modifiers.java,v $
 * Revision 1.2  2004/09/28 01:55:37  nuocnam
 * Added javadoc header.
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

import net.sf.l2j.gameserver.model.L2Modifiers;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:55:37 $
 */
public class Modifiers
{
	private static Logger _log = Logger.getLogger(Modifiers.class.getName());
	
	private static Modifiers _instance;
	
	private HashMap _modifiers;
	
	public static Modifiers getInstance()
	{
		if (_instance == null)
		{
			_instance = new Modifiers();
		}
		return _instance;
	}
	
	private Modifiers()
	{
		_modifiers = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File ModifierData = new File("data/modifiers.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(ModifierData)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2Modifiers modif = parseList(line);
				_modifiers.put(new Integer(modif.getClassid()), modif);
			}

			_log.config("created " + _modifiers.size() + " Character Modifiers");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("modifiers.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating character modifier table "+e);
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
	
	private L2Modifiers parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		L2Modifiers modifier = new L2Modifiers();

		modifier.setClassid( Integer.parseInt(st.nextToken()) );
		modifier.setModstr( Integer.parseInt(st.nextToken()) );
		modifier.setModcon( Integer.parseInt(st.nextToken()) );
		modifier.setModdex( Integer.parseInt(st.nextToken()) );
		modifier.setModint( Integer.parseInt(st.nextToken()) );
		modifier.setModmen( Integer.parseInt(st.nextToken()) );
		modifier.setModwit( Integer.parseInt(st.nextToken()) );
		
		return modifier;
	}

	/**
	 * @param template id
	 * @return
	 */
	public L2Modifiers getTemplate(int id)
	{
		return (L2Modifiers) _modifiers.get(new Integer(id));
	}
}
