/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetStatusUpdate.java,v 1.4 2004/09/28 02:44:16 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:44:16 $
 * $Revision: 1.4 $
 * $Log: PetStatusUpdate.java,v $
 * Revision 1.4  2004/09/28 02:44:16  nuocnam
 * Added javadoc header.
 *
 * Revision 1.3  2004/09/20 12:26:40  dalrond
 * Updated packet with exp value methods
 *
 * Revision 1.2  2004/08/14 22:48:20  l2chef
 * fixed the packet structure (L2Chef)
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

import net.sf.l2j.gameserver.model.L2PetInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/09/28 02:44:16 $
 */
public class PetStatusUpdate extends ServerBasePacket
{
	private static final String _S__CE_PETSTATUSSHOW = "[S] CE PetStatusShow";
	
	private L2PetInstance _pet;
	
	public PetStatusUpdate(L2PetInstance pet)
	{
		_pet = pet;
	}
	
	public byte[] getContent()
	{
		writeC(0xCE);
		writeD(_pet.getPetId());
		writeD(_pet.getObjectId());
		writeD(_pet.getX());
		writeD(_pet.getY());
		writeD(_pet.getZ());
		writeS(_pet.getTitle());
		writeD(_pet.getCurrentFed());
		writeD( _pet.getMaxFed());
		writeD((int)_pet.getCurrentHp());
		writeD((int)_pet.getMaxHp());
		writeD((int)_pet.getCurrentMp());
		writeD((int)_pet.getMaxMp());
		writeD(_pet.getLevel()); 
		writeD(_pet.getExp());
		writeD(_pet.getLastLevel());// 0% absolute value
		writeD(_pet.getNextLevel());// 100% absolute value
		
		return getBytes();
	}
	
	public String getType()
	{
		return _S__CE_PETSTATUSSHOW;
	}
}
