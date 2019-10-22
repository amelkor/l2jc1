/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestGiveItemToPet.java,v 1.3 2004/10/17 06:46:22 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:22 $
 * $Revision: 1.3 $
 * $Log: RequestGiveItemToPet.java,v $
 * Revision 1.3  2004/10/17 06:46:22  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.2  2004/09/28 01:50:14  nuocnam
 * Added header and copyright notice.
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.Inventory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.PetItemList;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/10/17 06:46:22 $
 */
public class RequestGiveItemToPet extends ClientBasePacket
{
	private static final String REQUESTGETITEMFROMPET__C__8C = "[C] 8C RequestGetItemFromPet";
	private static Logger _log = Logger.getLogger(RequestGetItemFromPet.class
			.getName());

	public RequestGiveItemToPet(byte[] decrypt, ClientThread client)
			throws IOException
	{
		super(decrypt);
		int objectId = readD();
		int amount = readD();

		Inventory petInventory = client.getActiveChar().getPet().getInventory();
		Inventory playerInventory = client.getActiveChar().getInventory();
			
		L2ItemInstance playerItem = playerInventory.getItem(objectId);
		

		if (amount >= playerItem.getCount())
		{
			playerInventory.dropItem(objectId, playerItem.getCount());
			petInventory.addItem(playerItem);

			InventoryUpdate playerUI = new InventoryUpdate();
			playerUI.addRemovedItem(playerItem);
			client.getActiveChar().sendPacket(playerUI);
			PetItemList petiu = new PetItemList(client.getActiveChar().getPet());
			client.getActiveChar().sendPacket(petiu);
		}
		else
		{
			L2ItemInstance newPetItem = playerInventory.dropItem(objectId, amount);
			//deal with pet
			petInventory.addItem(newPetItem);

			PetItemList petiu = new PetItemList(client.getActiveChar().getPet());

			InventoryUpdate playerUI = new InventoryUpdate();
			playerUI.addModifiedItem(playerItem);
			//send updates
			client.getActiveChar().sendPacket(petiu);
			client.getActiveChar().sendPacket(playerUI);
		}

	}

	public String getType()
	{
		return REQUESTGETITEMFROMPET__C__8C;
	}
}