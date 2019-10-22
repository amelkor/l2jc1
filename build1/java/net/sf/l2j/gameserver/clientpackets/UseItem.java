/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/UseItem.java,v 1.16 2004/10/08 17:36:41 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/08 17:36:41 $
 * $Revision: 1.16 $
 * $Log: UseItem.java,v $
 * Revision 1.16  2004/10/08 17:36:41  whatev66
 * can not change equipment while in combat.
 *
 * Revision 1.15  2004/09/28 20:32:08  l2chef
 * equip update packet is not needed any more
 *
 * Revision 1.14  2004/09/15 23:45:42  l2chef
 * *** empty log message ***
 *
 * Revision 1.13  2004/08/07 14:12:12  l2chef
 * new item handlers
 *
 * Revision 1.12  2004/08/04 21:13:52  l2chef
 * closest town calculation moved (NuocNam)
 *
 * Revision 1.11  2004/07/29 20:07:38  l2chef
 * patk/matk/pdef/mdef gets updates  (NuocNam/Nightmarez)
 *
 * Revision 1.10  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.9  2004/07/23 01:45:56  l2chef
 * fixed inventoy update bug
 *
 * Revision 1.8  2004/07/19 02:03:40  l2chef
 * soulshot code added (MetalRabbit)
 *
 * Revision 1.7  2004/07/17 00:06:48  l2chef
 * equip and unequip of items it broadcasted to other players
 *
 * Revision 1.6  2004/07/13 23:05:40  l2chef
 * empty blocks commented
 *
 * Revision 1.5  2004/07/11 23:38:32  l2chef
 * handling for scroll of escape  (MetalRabbit)
 *
 * Revision 1.4  2004/07/07 23:51:55  l2chef
 * pAtk update contributed by MetalRabit
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
import java.util.ArrayList;
import java.util.logging.Logger;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CharInfo;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2Item;

/**
 * This class ...
 * 
 * @version $Revision: 1.16 $ $Date: 2004/10/08 17:36:41 $
 */
public class UseItem extends ClientBasePacket
{
	private static Logger _log = Logger.getLogger(UseItem.class.getName());
	private static final String _C__14_USEITEM = "[C] 14 UseItem";
	/**
	 * packet type id 0x14
	 * format:		cd
	 * @param decrypt
	 */
	public UseItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();
		
		_log.fine("use item:" + objectId);
		L2PcInstance activeChar = client.getActiveChar();
		
		L2ItemInstance item = activeChar.getInventory().getItem(objectId);
		if (item != null && item.isEquipable() && !activeChar.isInCombat())
		{
			ArrayList items = activeChar.getInventory().equipItem(item);
			
			if (item.getItem().getType2() == L2Item.TYPE2_WEAPON)
			{
				activeChar.updatePAtk();
				activeChar.updateMAtk();
			}
			else if (item.getItem().getType2() == L2Item.TYPE2_SHIELD_ARMOR)
			{
				activeChar.updatePDef();
			} 
			else 	if (item.getItem().getType2() == L2Item.TYPE2_ACCESSORY)
			{
				activeChar.updateMDef();
			}
			
			SystemMessage sm = new SystemMessage(49);
			sm.addItemName(item.getItemId());
			activeChar.sendPacket(sm);
			
			InventoryUpdate iu = new InventoryUpdate(items);
			activeChar.sendPacket(iu);
			
			
			UserInfo ui = new UserInfo(activeChar);
			activeChar.sendPacket(ui);
			
			activeChar.setAttackStatus(false);
			
			CharInfo info = new CharInfo(activeChar);
			activeChar.broadcastPacket(info);
		}
		else
		{
			_log.fine("item not equipable id:"+ item.getItemId());
			
			IItemHandler handler = ItemHandler.getInstance().getItemHandler(item.getItemId());
			if (handler == null)
			{
				_log.warning("no itemhandler registered for itemId:" + item.getItemId());
			}
			else
			{
				int count = handler.useItem(activeChar, item);
				if (count > 0)
				{
					removeItemFromInventory(activeChar, item, count);
				}
			}
		}
	}
	
	/**
	 * @param activeChar
	 * @param item
	 */
	private void removeItemFromInventory(L2PcInstance activeChar, L2ItemInstance item, int count)
	{
		L2ItemInstance item2 = activeChar.getInventory().destroyItem(item.getObjectId(), count);
		
		InventoryUpdate iu = new InventoryUpdate();
		if (item2.getCount() == 0)
		{
			iu.addRemovedItem(item2);
		}
		else
		{
			iu.addModifiedItem(item2);
		}
		
		activeChar.sendPacket(iu);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__14_USEITEM;
	}
}
