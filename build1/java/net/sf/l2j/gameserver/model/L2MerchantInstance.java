/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2MerchantInstance.java,v 1.10 2004/08/06 00:22:12 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:22:12 $
 * $Revision: 1.10 $
 * $Log: L2MerchantInstance.java,v $
 * Revision 1.10  2004/08/06 00:22:12  l2chef
 * player trading added (whatev)
 *
 * Revision 1.9  2004/07/28 23:53:09  l2chef
 * Selling items implemented (Deth)
 *
 * Revision 1.8  2004/07/23 02:23:21  l2chef
 * npc chat dialogs unified (NuocNam)
 *
 * Revision 1.7  2004/07/13 22:53:25  l2chef
 * log message added and empty blocks commented
 *
 * Revision 1.6  2004/07/08 22:22:57  l2chef
 * common stuff moved to L2NpcInstance
 *
 * Revision 1.5  2004/07/07 23:41:04  l2chef
 * new merchant conversation files (done by NightMarez)
 * some design changes
 *
 * Revision 1.4  2004/07/04 11:13:27  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/29 18:46:02  l2chef
 * fixed movement freeze bug when interacting with NPCs
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.TradeController;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.BuyList;
import net.sf.l2j.gameserver.serverpackets.SellList;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.10 $ $Date: 2004/08/06 00:22:12 $
 */
public class L2MerchantInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2MerchantInstance.class.getName());
	
	/**
	 * @param template
	 */
	public L2MerchantInstance(L2Npc template)
	{
		super(template);
	}
	
	public void onAction(L2PcInstance player)
	{
		_log.fine("Merchant activated");
		super.onAction(player);
	}
	
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		} 
		else 
		{
			pom = npcId + "-" + val;
		}
		
		return "data/html/merchant/" + pom + ".htm";
	}

	private void showBuyWindow(L2PcInstance player, int val)
	{	
		_log.fine("Showing buylist");
		L2TradeList list = TradeController.getInstance().getBuyList(val);
		
		if (list != null)
		{	
			BuyList bl  = new BuyList(list, player.getAdena());
			player.sendPacket(bl);
		}
		else
		{
			_log.warning("no buylist with id:" +val);
		}
		
		player.sendPacket( new ActionFailed() );
	}
	
	private void showSellWindow(L2PcInstance player)
	{
		_log.fine("Showing selllist");
		SellList sl = new SellList(player);
		player.sendPacket(sl);

		_log.fine("Showing sell window");
		player.sendPacket( new ActionFailed() );
	}
	
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		// first do the common stuff
		// and handle the commands that all NPC classes know
		super.onBypassFeedback(player, command);
		
		if (command.startsWith("Buy"))
		{
			int val = Integer.parseInt(command.substring(4));
			showBuyWindow(player, val);
		}
		else if (command.equals("Sell"))
		{
			showSellWindow(player);
		}
		else 
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			
			super.onBypassFeedback(player, command);
		}
	}
}