/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetStatusShow.java,v 1.3 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.3 $
 * $Log: PetStatusShow.java,v $
 * Revision 1.3  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.2  2004/08/14 22:47:43  l2chef
 * commet update
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
 * @author Yme
 * @version $Revision: 1.3 $ $Date: 2004/09/28 02:44:16 $
 */
public class PetStatusShow extends ServerBasePacket{
	private static final String _S__C9_PETSTATUSSHOW = "[S] C9 PetStatusShow";
	private int _petId;
	public PetStatusShow(int petId)
	{
		_petId = petId;
	}
	public byte[] getContent()
	{
		writeC(0xC9);
		writeD(_petId);
		
	
		return getBytes();
	}
	public String getType()
	{
		return _S__C9_PETSTATUSSHOW;
	}
}
