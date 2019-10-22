/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/TeleportLocationTable.java,v 1.2 2004/07/13 23:16:22 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:16:22 $
 * $Revision: 1.2 $
 * $Log: TeleportLocationTable.java,v $
 * Revision 1.2  2004/07/13 23:16:22  l2chef
 * empty blocks commented
 *
 * Revision 1.1  2004/07/08 22:20:00  l2chef
 * teleporter npcs added by NightMarez and Nuocnam
 *
 * Revision 1.1  2004/07/07 22:09:51  nuocnam
 * Initial release
 *
 * modified 2004/08/07 by NightMarez. 
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

import net.sf.l2j.gameserver.model.L2TeleportLocation;

/**
 * This class ...
 *
 * @version $Revision: 1.2 $ $Date: 2004/07/13 23:16:22 $
 */
public class TeleportLocationTable
{
	private static Logger _log = Logger.getLogger(TeleportLocationTable.class.getName());
	
	private static TeleportLocationTable _instance;
	
	private HashMap _teleports;
	
	public static TeleportLocationTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new TeleportLocationTable();
		}
		return _instance;
	}
	
	private TeleportLocationTable()
	{
		_teleports = new HashMap();
		LineNumberReader lnr = null;
		try
		{
			File teleData = new File("data/teleport.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(teleData)));
			
			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2TeleportLocation tele = parseList(line);
				_teleports.put(new Integer(tele.getTeleId()), tele);
			}

			_log.config("created " + _teleports.size() + " Teleport templates");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("teleport.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating teleport table "+e);
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
	
	private L2TeleportLocation parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		L2TeleportLocation teleport = new L2TeleportLocation();
		teleport.setTeleId(Integer.parseInt(st.nextToken()) );
		teleport.setLocX(Integer.parseInt(st.nextToken()) );
		teleport.setLocY(Integer.parseInt(st.nextToken()) );
		teleport.setLocZ(Integer.parseInt(st.nextToken()) );
		teleport.setPrice(Integer.parseInt(st.nextToken()) );

		return teleport;
	}

	/**
	 * @param template id
	 * @return
	 */
	public L2TeleportLocation getTemplate(int id)
	{
		return (L2TeleportLocation) _teleports.get(new Integer(id));
	}
}
