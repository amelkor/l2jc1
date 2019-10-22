/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/ClanTable.java,v 1.10 2004/09/22 15:06:38 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/22 15:06:38 $
 * $Revision: 1.10 $
 * $Log: ClanTable.java,v $
 * Revision 1.10  2004/09/22 15:06:38  nuocnam
 * Reverted ClanTable changes, titles are back in L2PcInstance
 *
 * Revision 1.8  2004/08/10 00:46:06  l2chef
 * some clan methods moved to L2Clan
 *
 * Revision 1.7  2004/08/09 00:06:56  l2chef
 * clans can be created and are stored on disk (NuocNam)
 *
 * Revision 1.6  2004/07/13 23:13:14  l2chef
 * empty blocks commented
 *
 * Revision 1.5  2004/07/11 22:10:16  l2chef
 * stacktrace converted to log
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
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;	
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.10 $ $Date: 2004/09/22 15:06:38 $
 */
public class ClanTable
{
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());
	
	private static ClanTable _instance;
	
	private HashMap _clans;
	
	public static ClanTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new ClanTable();
		}
		return _instance;
	}
	
	private ClanTable()
	{
		_clans = new HashMap();
		try
		{
			File clanFolder = new File("data/clans");
			clanFolder.mkdirs();
			File[] clans = clanFolder.listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".csv");
				}
			});
			for (int i = 0; i < clans.length; i++)
			{
				L2Clan clan = restoreClan(clans[i]);
				_clans.put(new Integer(clan.getClanId()), clan);

			}
			
			_log.config("restored " + _clans.size() + " clans");
		}
		catch (Exception e)
		{
			_log.warning("error while creating clan table "+e);
		}
	}
	
	/**
	 * @param file
	 * @return
	 */
	private L2Clan restoreClan(File file)
	{
		LineNumberReader lnr = null;
		
		L2Clan clan = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(file)));
			// skip the header line
			lnr.readLine();
			
			clan = parseClanData(lnr.readLine());

			// skip the header line
			lnr.readLine();
			

			String line = null;
			boolean first = true;
			while ( (line=lnr.readLine()) != null)
			{
				L2ClanMember member = parseMembers(line);
				if (first)
				{
					clan.setLeader(member);
					first = false;
				}
				else
				{
					clan.addClanMember(member);
				}
			}
		}
		catch (IOException e)
		{
			_log.warning("Could not read clan file:"+e.getMessage());
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

		return clan;
	}

	private L2Clan parseClanData(String line)
	{
		L2Clan clan = new L2Clan();
		StringTokenizer st = new StringTokenizer(line,";");

		clan.setClanId(Integer.parseInt(st.nextToken()) );
		clan.setName( st.nextToken() );
		clan.setLevel(Integer.parseInt(st.nextToken()) );
		clan.setHasCastle(Integer.parseInt(st.nextToken()) );
		clan.setHasHideout(Integer.parseInt(st.nextToken()) );
		clan.setAllyId(Integer.parseInt(st.nextToken()) );
		clan.setAllyName(st.nextToken() );
//		clan.setAllyCrestId(Integer.parseInt(st.nextToken()) );
		
		// crests always have the same id as the clan or ally, so it is not required
		
		return clan;
	}

	
	private L2ClanMember parseMembers(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		String name = st.nextToken();
		int level =  Integer.parseInt(st.nextToken());
		int classId = Integer.parseInt(st.nextToken());
		int objectId = Integer.parseInt(st.nextToken());
		
		return new L2ClanMember(name, level, classId, objectId);
	}

	
	/**
	 * @param clanId
	 * @return
	 */
	public L2Clan getClan(int clanId)
	{
		return (L2Clan) _clans.get(new Integer(clanId));
	}
	
	
	/**
	 * @param player
	 * @return NULL if clan with same name already exists
	 */
	public L2Clan createClan(L2PcInstance player, String clanName)
	{
		for (Iterator iter = _clans.values().iterator(); iter.hasNext();)
		{
			L2Clan oldClans = (L2Clan) iter.next();
			if (oldClans.getName().equalsIgnoreCase(clanName))
			{
				return null;
			}
		}
		
		L2Clan clan = new L2Clan();
		clan.setClanId(IdFactory.getInstance().getNextId());
		clan.setName(clanName);
		clan.setLevel(0);
		clan.setHasCastle(0);
		clan.setHasHideout(0);
		clan.setAllyId(0);
		clan.setAllyName(" ");
		_log.fine("New clan created: "+clan.getClanId());
		
		L2ClanMember leader = new L2ClanMember(player.getName(), 
				player.getLevel(), player.getClassId(), player.getObjectId());
		clan.setLeader(leader);

		_clans.put(new Integer(clan.getClanId()), clan);

		clan.store();
		return clan;
	}
}
