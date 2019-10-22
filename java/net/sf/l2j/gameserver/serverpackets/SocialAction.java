/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/SocialAction.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: SocialAction.java,v $
 * Revision 1.3  2004/07/04 11:14:52  l2chef
 * logging is used instead of system.out
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
package net.sf.l2j.gameserver.serverpackets;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class SocialAction extends ServerBasePacket
{
	private static final String _S__3D_SOCIALACTION = "[S] 3D SocialAction";
	private int _playerId;
	private int _actionId;

	/**
	 * 0x3d SocialAction         dd 
	 * @param _characters
	 */
	public SocialAction(int playerId, int actionId)
	{
		_playerId = playerId;
		_actionId = actionId;
	}


	public byte[] getContent()
	{
		writeC(0x3d);
		writeD(_playerId);
		writeD(_actionId);

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__3D_SOCIALACTION;
	}
}
