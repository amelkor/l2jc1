/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetDelete.java,v 1.2 2004/08/14 22:47:19 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/14 22:47:19 $
 * $Revision: 1.2 $
 * $Log: PetDelete.java,v $
 * Revision 1.2  2004/08/14 22:47:19  l2chef
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
 */
public class PetDelete extends ServerBasePacket
{
	private static final String _S__CF_PETDELETE = "[S] CF PetDelete";
	private int _petId;
	private int _petnum;
	
	public PetDelete(int petId, int petnum)
	{
		_petId = petId;
		_petnum= petnum;
	}
	
	public byte[] getContent()
	{
		writeC(0xcf);
		writeD(_petId);// dont really know what these two are since i never needed them
		writeD(_petnum);
	
		return getBytes();
	}
	
	public String getType()
	{
		return _S__CF_PETDELETE;
	}
}
