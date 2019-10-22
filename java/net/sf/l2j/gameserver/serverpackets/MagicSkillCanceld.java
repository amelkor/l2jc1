/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/MagicSkillCanceld.java,v 1.2 2004/10/20 17:57:04 dethx Exp $
 *
 * $Author: dethx $
 * $Date: 2004/10/20 17:57:04 $
 * $Revision: 1.2 $
 * $Log: MagicSkillCanceld.java,v $
 * Revision 1.2  2004/10/20 17:57:04  dethx
 * changed skillId to objectId and _skillId to _objectId to avoid misunderstandings in the future
 *
 * Revision 1.1  2004/10/20 08:23:47  nuocnam
 * New class describing packet used to inform clients that someone stopped casting magic.
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
 * @version $Revision: 1.2 $ $Date: 2004/10/20 17:57:04 $
 */
public class MagicSkillCanceld extends ServerBasePacket {
	private static final String _S__5B_MAGICSKILLCANCELD = "[S] 5B MagicSkillCanceld";
	
	private int _objectId;

	public MagicSkillCanceld (int objectId) {
		_objectId = objectId; 
	}
	
	public byte[] getContent() {
		writeC(0x5b);
		writeD(_objectId);
		return _bao.toByteArray();
	}
	
	public String getType()
	{
		return _S__5B_MAGICSKILLCANCELD;
	}
}
