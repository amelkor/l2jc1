/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Clan.java,v 1.7 2004/09/22 15:06:33 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/22 15:06:33 $
 * $Revision: 1.7 $
 * $Log: L2Clan.java,v $
 * Revision 1.7  2004/09/22 15:06:33  nuocnam
 * Reverted ClanTable changes, titles are back in L2PcInstance
 *
 * Revision 1.4  2004/08/10 20:33:32  l2chef
 * added broadcast method
 *
 * Revision 1.3  2004/08/10 00:50:09  l2chef
 * some clan methods moved from ClanTable
 *
 * Revision 1.2  2004/06/27 08:51:42  jeichhorn
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
package net.sf.l2j.gameserver.model;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/22 15:06:33 $
 */
public class L2Clan
{
	private static final Logger _log = Logger.getLogger(L2Clan.class.getName());

	private String _name;
	private int _clanId;
	private L2ClanMember _leader;
	private Map _members = new TreeMap();
	
	private String _allyName;
	private int _allyId;
	private int _level;
	private int _hasCastle;
	private int _hasHideout;
	
	/**
	 * @return Returns the clanId.
	 */
	public int getClanId()
	{
		return _clanId;
	}
	/**
	 * @param clanId The clanId to set.
	 */
	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}
	/**
	 * @return Returns the leaderId.
	 */
	public int getLeaderId()
	{
		return _leader.getObjectId();
	}
	/**
	 * @param leaderId The leaderId to set.
	 */
	public void setLeader(L2ClanMember leader)
	{
		_leader = leader;
		_members.put(leader.getName(), leader);
	}
	/**
	 * @return Returns the leaderName.
	 */
	public String getLeaderName()
	{
		return _leader.getName();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return _name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	public void addClanMember(L2ClanMember member)
	{
		_members.put(member.getName(), member);
	}
	
	public void addClanMember(L2PcInstance player)
	{
		L2ClanMember member = new L2ClanMember(player);
		addClanMember(member);
	}
	
	public L2ClanMember getClanMember(String name)
	{
		return (L2ClanMember) _members.get(name);
	}
	
	public void removeClanMember(String name)
	{
		_members.remove(name);
	}

	public L2ClanMember[] getMembers()
	{
		return (L2ClanMember[]) _members.values().toArray(new L2ClanMember[_members.size()]);
	}
	
	public L2PcInstance[] getOnlineMembers(String exclude)
	{
		ArrayList result = new ArrayList();
		for (Iterator iter = _members.values().iterator(); iter.hasNext();)
		{
			L2ClanMember temp = (L2ClanMember) iter.next();
			if (temp.isOnline() && !temp.getName().equals(exclude))
			{
				result.add(temp.getPlayerInstance());
			}
		}
		
		return (L2PcInstance[]) result.toArray(new L2PcInstance[result.size()]);
		
	}
	
	/**
	 * @return
	 */
	public int getAllyId()
	{
		return _allyId;
	}
	/**
	 * @return
	 */
	public String getAllyName()
	{
		return _allyName;
	}
	/**
	 * @return
	 */
	public int getAllyCrestId()
	{
		return _allyId;
	}
	/**
	 * @return
	 */
	public int getLevel()
	{
		return _level;
	}
	/**
	 * @return
	 */
	public int getHasCastle()
	{
		return _hasCastle;
	}
	/**
	 * @return
	 */
	public int getHasHideout()
	{
		return _hasHideout;
	}
	/**
	 * @return Returns the clanCrestId.
	 */
	public int getCrestId()
	{
		return _clanId;
	}
	/**
	 * @param allyId The allyId to set.
	 */
	public void setAllyId(int allyId)
	{
		_allyId = allyId;
	}
	/**
	 * @param allyName The allyName to set.
	 */
	public void setAllyName(String allyName)
	{
		_allyName = allyName;
	}
	/**
	 * @param hasCastle The hasCastle to set.
	 */
	public void setHasCastle(int hasCastle)
	{
		_hasCastle = hasCastle;
	}
	/**
	 * @param hasHideout The hasHideout to set.
	 */
	public void setHasHideout(int hasHideout)
	{
		_hasHideout = hasHideout;
	}
	/**
	 * @param level The level to set.
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	
	/**
	 * @param player name
	 * @return
	 */
	public boolean isMember(String name)
	{
		return _members.containsKey(name);
	}
	
	public void store()
	{
		File clanFile = new File("data/clans/"+getName()+".csv");
		FileWriter out = null;
		try
		{
			out = new FileWriter(clanFile);
			out.write("#clanId;clanName;clanLevel;hasCastle;hasHideout;allianceId;allianceName\r\n");
			out.write(getClanId()+";");
			out.write(getName()+";");
			out.write(getLevel()+";");
			out.write(getHasCastle()+";");
			out.write(getHasHideout()+";");
			out.write(getAllyId()+";");
			out.write("none\r\n");
			out.write("#memberName;memberLevel;classId;objectId\r\n");
			
			L2ClanMember[] members = getMembers();
			for (int i=0; i< members.length; i++)
			{
				if (members[i].getObjectId() == getLeaderId() )
				{
					out.write(members[i].getName()+";");
					out.write(members[i].getLevel()+";");
					out.write(members[i].getClassId()+";");
					out.write(members[i].getObjectId()+"\r\n");
				}
			}
			
			for (int i=0; i< members.length; i++)
			{
				if (members[i].getObjectId() != getLeaderId() )
				{
					out.write(members[i].getName()+";");
					out.write(members[i].getLevel()+";");
					out.write(members[i].getClassId()+";");
					out.write(members[i].getObjectId()+"\r\n");
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("could not store clan:" + e.toString());
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	public void broadcastToOnlineMembers(ServerBasePacket packet)
	{
		for (Iterator iter = _members.values().iterator(); iter.hasNext();)
		{
			L2ClanMember member	 = (L2ClanMember) iter.next();
			if (member.isOnline())
			{
				member.getPlayerInstance().sendPacket(packet);
			}
		}
	}
	
	public String toString()
	{
		return getName();
	}
}
