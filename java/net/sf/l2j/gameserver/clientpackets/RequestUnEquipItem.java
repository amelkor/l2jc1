/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestUnEquipItem.java,v 1.7 2004/09/28 20:32:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/28 20:32:21 $
 * $Revision: 1.7 $
 * $Log: RequestUnEquipItem.java,v $
 * Revision 1.7  2004/09/28 20:32:21  l2chef
 * equip update packet is not needed any more
 *
 * Revision 1.6  2004/09/15 23:59:30  l2chef
 * *** empty log message ***
 *
 * Revision 1.5  2004/07/29 20:07:38  l2chef
 * patk/matk/pdef/mdef gets updates  (NuocNam/Nightmarez)
 *
 * Revision 1.4  2004/07/17 00:06:49  l2chef
 * equip and unequip of items it broadcasted to other players
 *
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/28 20:32:21 $
 */
public class RequestUnEquipItem extends ClientBasePacket
{
	private static final String _C__11_REQUESTUNEQUIPITEM = "[C] 11 RequestUnequipItem";
	private static Logger _log = Logger.getLogger(RequestUnEquipItem.class.getName());

	/**
	 * packet type id 0x11
	 * format:		cd 
	 * @param decrypt
	 */
	public RequestUnEquipItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int slot = readD();
		
		_log.fine("request unequip slot " + slot);
		L2PcInstance activeChar = client.getActiveChar();
		L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInBodySlot(slot);
		
		// show the update in the inventory
		InventoryUpdate iu = new InventoryUpdate();
		for (int i = 0; i < unequiped.length; i++)
		{
			iu.addModifiedItem(unequiped[i]);
		}
		activeChar.sendPacket(iu);
		
//		ItemList il = new ItemList(activeChar, true);
//		con.sendPacket(il);

		activeChar.updatePDef();
		activeChar.updatePAtk();
		activeChar.updateMDef();
		activeChar.updateMAtk();

		UserInfo ui = new UserInfo(activeChar);
		activeChar.sendPacket(ui);
		
		activeChar.setAttackStatus(false);
		
		CharInfo info = new CharInfo(activeChar);
		activeChar.broadcastPacket(info);
		
		// this can be 0 if the user pressed the right mousebutton twice very fast
		if (unequiped.length > 0)
		{
			SystemMessage sm = new SystemMessage(0x1a1); // unequip
			sm.addItemName(unequiped[0].getItemId());
			activeChar.sendPacket(sm);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__11_REQUESTUNEQUIPITEM;
	}
}
