/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AttackCanceld.java,v 1.1 2004/10/20 23:56:41 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/20 23:56:41 $
 * $Revision: 1.1 $
 * $Log: AttackCanceld.java,v $
 * Revision 1.1  2004/10/20 23:56:41  nuocnam
 * New class to handle removing attack animation if attack was stopped
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
package net.sf.l2j.gameserver.serverpackets;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/20 23:56:41 $
 */
public class AttackCanceld extends ServerBasePacket {
	private static final String _S__0A_MAGICSKILLCANCELD = "[S] 0a AttackCanceld";
	
	private int _objectId;

	public AttackCanceld (int objectId) {
		_objectId = objectId; 
	}
	
	public byte[] getContent() {
		writeC(0x0a);
		writeD(_objectId);
		return _bao.toByteArray();
	}
	
	public String getType()
	{
		return _S__0A_MAGICSKILLCANCELD;
	}
}
