/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2World.java,v 1.18 2004/10/23 21:06:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 21:06:53 $
 * $Revision: 1.18 $
 * $Log: L2World.java,v $
 * Revision 1.18  2004/10/23 21:06:53  l2chef
 * character names are stored lowercase to make the player search non-casesensitive
 *
 * Revision 1.17  2004/09/20 00:23:33  whatev66
 * mobs now appear to you when they spawn without you having to move.
 *
 * Revision 1.16  2004/09/19 00:30:09  whatev66
 * *** empty log message ***
 *
 * Revision 1.15  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.14  2004/08/11 22:22:14  l2chef
 * using threadsafe collection classes
 *
 * Revision 1.13  2004/08/08 00:48:31  l2chef
 * world.csv handling removed
 *
 * Revision 1.12  2004/08/02 00:08:32  l2chef
 * better solution to drop item problem
 *
 * Revision 1.11  2004/07/28 23:57:24  l2chef
 * loglevels changed
 * client crash fixed when picking up items twice
 *
 * Revision 1.10  2004/07/23 01:51:47  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.9  2004/07/18 17:39:35  l2chef
 * aggressive monsters scan for targets in range (FTPW)
 *
 * Revision 1.8  2004/07/17 23:12:00  l2chef
 * attack range is set for monsters
 *
 * Revision 1.7  2004/07/13 22:59:27  l2chef
 * world init removed
 *
 * Revision 1.6  2004/07/11 11:48:54  l2chef
 * additional npc data is used when populating the world
 *
 * Revision 1.5  2004/07/04 11:13:27  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.4  2004/06/29 22:55:35  l2chef
 * tell and say works now. say is actually a server broadcast now
 *
 * Revision 1.3  2004/06/27 20:50:03  l2chef
 * better error message when data file is missing. skipping of comment lines
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.IdFactory;
import net.sf.l2j.util.ConcurrentHashMap;

/**
 * This class ...
 * 
 * @version $Revision: 1.18 $ $Date: 2004/10/23 21:06:53 $
 */
public class L2World
{
	private static Logger _log = Logger.getLogger(L2World.class.getName());
	
	private Map _allPlayers;
	
	private Map _allObjects;
	
	private Map _visibleObjects;
	
	private IdFactory _idFactory;
	
	private static L2World _instance;
	
	private L2World()
	{
		_allPlayers = new ConcurrentHashMap();
		_allObjects = new ConcurrentHashMap();
		_visibleObjects = new ConcurrentHashMap();
		_idFactory = IdFactory.getInstance();
	}
	
	public static L2World getInstance()
	{
		if (_instance == null)
		{
			_instance = new L2World();
		}
		return _instance;
	}
	
	
	
	public void storeObject(L2Object temp)
	{
		_allObjects.put(new Integer(temp.getObjectId()), temp);
	}
	
	public void removeObject(L2Object object)
	{
		_allObjects.remove(new Integer(object.getObjectId()));  // suggestion by whatev
	}
	
	
	/**
	 * find the object that belongs to an ID
	 * @param oID
	 * @return  null if no object was found. 
	 */
	public L2Object findObject(int oID)
	{
		return (L2Object) _allObjects.get(new Integer(oID));
	}
	
	public void addVisibleObject(L2Object object)
	{
		if (object instanceof L2PcInstance)
		{
			_allPlayers.put(((L2PcInstance)object).getName().toLowerCase(), object);
			L2Object[] visible = getVisibleObjects(object, 2000);
			_log.finest("objects in range:"+visible.length);
			
			// tell the player about the surroundings
			for (int i = 0; i < visible.length; i++)
			{
				object.addKnownObject(visible[i]);
				if (object instanceof L2ItemInstance && visible[i].getKnownObjects().contains(object))
				{
					// special case for droped items. they are already known when they appear in the world
				}
				else
				{
					visible[i].addKnownObject(object);
				}
			}
		}
		else if (_allPlayers.size() != 0 && !(object instanceof L2PetInstance)&& !(object instanceof L2ItemInstance))
		{//for npcs to be seen by players if they pop up in world.
			int x = object.getX();
			int y = object.getY();
			int sqRadius = 2000*2000;
			for (Iterator iter = _allPlayers.values().iterator(); iter.hasNext();)
			{
				L2PcInstance player = (L2PcInstance) iter.next();
				
				
				int x1 = player.getX();
				int y1 = player.getY();
				
				long dx = x1 - x;
				long dy = y1 - y;
				long sqDist = dx*dx + dy*dy;
				if (sqDist < sqRadius)
				{
					player.addKnownObject(object);
					object.addKnownObject(player);
				}	
			
			}
		}
		
		_visibleObjects.put(new Integer(object.getObjectId()), object);
		
		
	}
	
	public void removeVisibleObject(L2Object object)
	{
		_visibleObjects.remove(new Integer(object.getObjectId()));
		_log.fine("World has now " + _visibleObjects.size() + " visible objects");
		
		Object[] temp = object.getKnownObjects().toArray();
		for (int i = 0; i < temp.length; i++)
		{
			L2Object temp1 = (L2Object) temp[i];
			temp1.removeKnownObject(object);
			object.removeKnownObject(temp1);
		}
		
		if (object instanceof L2PcInstance)
		{
			_allPlayers.remove(((L2PcInstance)object).getName().toLowerCase());
		}
	}
	
	/**
	 * returns all visible objects in range
	 * @param object
	 * @param radius
	 * @return
	 */
	public L2Object[] getVisibleObjects(L2Object object, int radius)
	{
		int x = object.getX();
		int y = object.getY();
		int sqRadius = radius*radius;
		ArrayList result = new ArrayList();
		for (Iterator iter = _visibleObjects.values().iterator(); iter.hasNext();)
		{
			L2Object element = (L2Object) iter.next();
			
			if (element.equals(object)) continue;	// skip our own character
			
			int x1 = element.getX();
			int y1 = element.getY();
			
			long dx = x1 - x;
			long dy = y1 - y;
			long sqDist = dx*dx + dy*dy;
			if (sqDist < sqRadius)
			{
				result.add(element);
			}
		}
		
		return (L2Object[]) result.toArray(new L2Object[result.size()]);
	}
	
	public L2PcInstance[] getAllPlayers()
	{
		return (L2PcInstance[]) _allPlayers.values().toArray(new L2PcInstance[_allPlayers.size()]);
	}
	
	public L2PcInstance getPlayer(String name)
	{
		return (L2PcInstance) _allPlayers.get(name.toLowerCase());
	}
}
