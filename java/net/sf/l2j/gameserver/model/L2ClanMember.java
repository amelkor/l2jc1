/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2ClanMember.java,v 1.5 2004/09/22 15:06:33 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/22 15:06:33 $
 * $Revision: 1.5 $
 * $Log: L2ClanMember.java,v $
 * Revision 1.5  2004/09/22 15:06:33  nuocnam
 * Reverted ClanTable changes, titles are back in L2PcInstance
 *
 * Revision 1.3  2004/08/10 00:49:30  l2chef
 * online status is reflected
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

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/09/22 15:06:33 $
 */
public class L2ClanMember
{
	private int _objectId;
	private String _name;
	private int _level;
	private int _classId;
	private L2PcInstance _player;
	
	public L2ClanMember(String name, int level, int classId, int objectId)
	{
		_name = name;
		_level = level;
		_classId = classId;
		_objectId = objectId;
	}
	
	public L2ClanMember(L2PcInstance player)
	{
		_player = player;
	}

		
	public void setPlayerInstance(L2PcInstance player)
	{
		if (player == null && _player != null)
		{
			// this is here to keep the data when the player logs off
			_name = _player.getName();
			_level = _player.getLevel();
			_classId = _player.getClassId();
			_objectId = _player.getObjectId();
		}

		_player = player;
	}

	public L2PcInstance getPlayerInstance()
	{
		return _player;
	}
	
	public boolean isOnline()
	{
		return _player != null;
	}
	
	/**
	 * @return Returns the classId.
	 */
	public int getClassId()
	{
		if (_player != null)
		{
			return _player.getClassId();
		}
		else
		{
			return _classId;
		}
	}

	/**
	 * @return Returns the level.
	 */
	public int getLevel()
	{
		if (_player != null)
		{
			return _player.getLevel();
		}
		else
		{
			return _level;
		}
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		if (_player != null)
		{
			return _player.getName();
		}
		else
		{
			return _name;
		}
	}

	/**
	 * @return Returns the objectId.
	 */
	public int getObjectId()
	{
		if (_player != null)
		{
			return _player.getObjectId();
		}
		else
		{
			return _objectId;
		}
	}
	
	public String getTitle() {
		if (_player != null) {
			return _player.getTitle();
		} else {
			return " ";
		}
	}
}
