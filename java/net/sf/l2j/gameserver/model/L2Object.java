/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Object.java,v 1.13 2004/10/19 09:04:52 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/19 09:04:52 $
 * $Revision: 1.13 $
 * $Log: L2Object.java,v $
 * Revision 1.13  2004/10/19 09:04:52  nuocnam
 * removed unnecessary & buggy class casts from removeAllKnownObjects() method
 *
 * Revision 1.12  2004/10/05 19:30:46  whatev66
 * condition to let monsters override function remove themselves properly
 *
 * Revision 1.11  2004/09/15 23:51:46  l2chef
 * npc info is only shown to gms (Deth)
 *
 * Revision 1.10  2004/08/12 21:55:48  l2chef
 * known objects use threadsafe list
 *
 * Revision 1.9  2004/08/02 00:08:13  l2chef
 * attempt to fix concurrent modification
 *
 * Revision 1.8  2004/07/28 23:59:33  l2chef
 * knowPlayers is now a Set to prevent duplicates
 *
 * Revision 1.7  2004/07/23 01:48:11  l2chef
 * knownobject handling is moved here
 *
 * Revision 1.6  2004/07/19 02:07:19  l2chef
 * default action handler return now action failed to prevent client freeze
 *
 * Revision 1.5  2004/07/17 23:04:40  l2chef
 * added default method for forced attack
 *
 * Revision 1.4  2004/07/13 22:55:52  l2chef
 * empty blocks commented
 *
 * Revision 1.3  2004/07/11 22:13:20  l2chef
 * removed task
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.util.CopyOnWriteArrayList;

/**
 * This class ...
 * 
 * @version $Revision: 1.13 $ $Date: 2004/10/19 09:04:52 $
 */
public class L2Object implements Serializable
{
	private int _objectId;
	// this is for items/chars that have are seen in the world
	private int _x;
	private int _y;
	private int _z;

	protected List _knownObjects = new CopyOnWriteArrayList();

	/**
	 * @return
	 */
	public int getObjectId()
	{
		return _objectId;
	}

	/**
	 * @param objectId
	 */
	public void setObjectId(int objectId)
	{
		_objectId = objectId;
	}

	/**
	 * @return Returns the x.
	 */
	public int getX()
	{
		return _x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(int x)
	{
		_x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public int getY()
	{
		return _y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(int y)
	{
		_y = y;
	}

	/**
	 * @return Returns the z.
	 */
	public int getZ()
	{
		return _z;
	}

	/**
	 * @param z The z to set.
	 */
	public void setZ(int z)
	{
		_z = z;
	}

	/**
	 * @param activeChar
	 */
	public void onAction(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
	}

	/**
	 * @param player
	 */
	public void onActionShift(ClientThread client)
	{
		client.getActiveChar().sendPacket(new ActionFailed());
	}
	
	public void onForcedAttack(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
	}

	public List getKnownObjects()
	{
		return _knownObjects;
	}

	public void addKnownObject(L2Object object)
	{
		_knownObjects.add(object);
		if (object instanceof L2PcInstance)
		{
			_knownPlayer.add(object);
		}
	}

	public void removeKnownObject(L2Object object)
	{
		_knownObjects.remove(object);
		if (object instanceof L2PcInstance)
		{
			_knownPlayer.remove(object);
		}
	}

	public void removeAllKnownObjects()
	{
		L2Object[] notifyList = (L2Object[]) _knownObjects.toArray(new L2Object[_knownObjects.size()]);
		// clear our own list
		_knownObjects.clear();
		
		for (int i = 0; i < notifyList.length; i++)
		{
				notifyList[i].removeKnownObject(this);
		}
	}

	private Set _knownPlayer = new HashSet();

	public Set getKnownPlayers()
	{
		return _knownPlayer;
	}
}
