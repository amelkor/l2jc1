/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/GmListTable.java,v 1.1 2004/10/11 17:25:01 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:25:01 $
 * $Revision: 1.1 $
 * $Log: GmListTable.java,v $
 * Revision 1.1  2004/10/11 17:25:01  nuocnam
 * New class that handles events related to active GM's. Also stores list of all GM's online.
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

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class stores references to all online game masters. (access level > 100)
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/11 17:25:01 $
 */
public class GmListTable
{
	private static Logger _log = Logger.getLogger(GmListTable.class.getName());
	private static GmListTable _instance;
	
	private ArrayList _gmList;
	
	public static GmListTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new GmListTable();
		}
		return _instance;
	}
	
	private GmListTable()
	{
		_gmList = new ArrayList();
	}
	
	public void addGm(L2PcInstance player)
	{
		_log.fine("added gm: "+player.getName());
		_gmList.add(player);
	}
	
	public void deleteGm(L2PcInstance player)
	{
		_log.fine("deleted gm: "+player.getName());
		_gmList.remove(player);
	}
	
	public void sendListToPlayer (L2PcInstance player){
		if (_gmList.isEmpty()) {
			SystemMessage sm = new SystemMessage(614);
			sm.addString("No GM online");
		} else {
			SystemMessage sm = new SystemMessage(614);
			sm.addString(""+_gmList.size()+" GM's online:");
			player.sendPacket(sm);
			for (int i=0; i <_gmList.size(); i++) {
				sm = new SystemMessage(614);
				sm.addString(((L2PcInstance)_gmList.get(i)).getName());
				player.sendPacket(sm);
			}
		}
	}
	
	public void broadcastToGMs(ServerBasePacket packet) {
		for (int i=0; i <_gmList.size(); i++) {
			((L2PcInstance)_gmList.get(i)).sendPacket(packet);
		}
	}
}
