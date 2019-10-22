/*
 * Revision 1.1  2004/06/27 08:51:42  nuocnam
 * initial release, based on L2MercahntInstance
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

import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.WareHouseWithdrawalList;
import net.sf.l2j.gameserver.serverpackets.WareHouseDepositList;
import net.sf.l2j.gameserver.templates.L2Npc;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/23 02:23:21 $
 */
public class L2WarehouseInstance extends L2NpcInstance
{
	private static Logger _log = Logger.getLogger(L2WarehouseInstance.class.getName());
	
	/**
	 * @param template
	 */
	public L2WarehouseInstance(L2Npc template)
	{
		super(template);
	}
	
	public void onAction(L2PcInstance player)
	{
		_log.fine("Warehouse activated");
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
		return "data/html/warehouse/" + pom + ".htm";
	}

	
	private void showRetrieveWindow(L2PcInstance player)
	{	
		_log.fine("Showing stored items");
		//model
		Warehouse list = player.getWarehouse();
		
		if (list != null)
		{	
			//serverpacket
			WareHouseWithdrawalList wl  = new WareHouseWithdrawalList(player);
			player.sendPacket(wl);
		}
		else
		{
			_log.warning("no items stored");
		}
		
		player.sendPacket( new ActionFailed() );
	}
	
	private void showDepositWindow(L2PcInstance player)
	{
		
		_log.fine("Showing items to deposit");
		
		WareHouseDepositList dl  = new WareHouseDepositList(player);
		player.sendPacket(dl);		
		
		player.sendPacket( new ActionFailed() );
	}
	
	private void showDepositWindowClan(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setHtml("<html><body>Clans are not supported yet.</body></html>");
		player.sendPacket(html);
		
		_log.fine("Showing items to deposit - clan");
		player.sendPacket( new ActionFailed() );
	}

	private void showWithdrawWindowClan(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setHtml("<html><body>Clans are not supported yet.</body></html>");
		player.sendPacket(html);
		
		_log.fine("Showing items to deposit - clan");
		player.sendPacket( new ActionFailed() );
	}

	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("WithdrawP"))
		{
			showRetrieveWindow(player);
		} else if (command.equals("DepositP"))
		{
			showDepositWindow(player);
		}
		else if (command.equals("WithdrawC"))
		{
			showWithdrawWindowClan(player);
		}
		else if (command.equals("DepositC"))
		{
			showDepositWindowClan(player);
		}
		else 
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			
			super.onBypassFeedback(player, command);
		}
	}
}