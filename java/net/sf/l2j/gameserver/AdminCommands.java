/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/AdminCommands.java,v 1.19 2004/10/23 22:55:21 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 22:55:21 $
 * $Revision: 1.19 $
 * $Log: AdminCommands.java,v $
 * Revision 1.19  2004/10/23 22:55:21  l2chef
 * version number updated
 *
 * Revision 1.18  2004/10/17 06:46:22  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.17  2004/09/28 02:14:37  nuocnam
 * corrected header
 *
 * Revision 1.4	 2004/07/13  21:40:32  blurcode
 * Spawn monster code added
 * Common teleport locations for admin added
 * Ability to teleport characters added
 * Logout character code added
 * Extra exception catchers added 
 * 
 * Revision 1.3  2004/07/11 23:39:52  whatev
 * Teleport added to admininterface
 * 
 * Revision 1.2  2004/07/09 22:33:00  blurcode
 * Search for character added
 * List characters per page code added
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

package net.sf.l2j.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.lang.Integer;
import java.util.ArrayList;

import net.sf.l2j.gameserver.CharTemplateTable;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2MonsterInstance;
import net.sf.l2j.gameserver.model.Inventory;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.BuyList;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.LeaveWorld;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.templates.L2Npc;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2NpcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.19 $ $Date: 2004/10/23 22:55:21 $
 */
public class AdminCommands extends Thread
{
	private static Logger _log = Logger.getLogger(AdminCommands.class.getName());
	private static AdminCommands _instance;
	private static ClientThread clientShut;
	private static int secondsShut;
	private static L2Skill[] adminSkills;
	private static Inventory adminInventory;
	private static ArrayList adminItems;
		
	public static AdminCommands getInstance()
	{
		if (_instance == null)
		{
			_instance = new AdminCommands();
		}
		
		return _instance;
	}
	
	private static String _characterToManipulate;
	
	public void handleCommands(ClientThread client, String command)
	{
		L2PcInstance activeChar = client.getActiveChar();
		
		if (command.equals("admin_show"))
		{
			showMainPage(client);
		}
		else if (command.startsWith("admin_kill"))
		{
			int objectId = Integer.parseInt(command.substring(11));
			L2Object temp = L2World.getInstance().findObject(objectId);
			if (temp instanceof L2NpcInstance)
			{
				L2NpcInstance target = (L2NpcInstance)temp;
				target.reduceCurrentHp((int)target.getCurrentHp()+1,target);
			}
		}
		else if (command.startsWith("admin_delete"))
		{
			int objectId = Integer.parseInt(command.substring(13));
			L2Object temp = L2World.getInstance().findObject(objectId);
			if (temp instanceof L2NpcInstance)
			{
				L2NpcInstance target = (L2NpcInstance)temp;
				target.deleteMe();
			}
		}
		else if (command.equals("admin_list_announcements"))
		{
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.equals("admin_reload_announcements"))
		{
			Announcements.getInstance().loadAnnouncements();
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.equals("admin_announce_announcements"))
		{
			L2PcInstance[] players = L2World.getInstance().getAllPlayers();
			for (int i = 0; i < players.length; i++)
			{
				Announcements.getInstance().showAnnouncements(players[i]);
			}
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.startsWith("admin_add_announcement"))
		{
			//FIXME the player can send only 16 chars (if you try to send more it sends null), remove this function or not?
			if (!command.equals("admin_add_announcement"))
			{
				try
				{
					String val = command.substring(23);
					Announcements.getInstance().addAnnouncement(val);
					Announcements.getInstance().listAnnouncements(activeChar);
				} 
				catch(StringIndexOutOfBoundsException e)
				{//ignore errors
				}
			}
		}
		else if (command.startsWith("admin_del_announcement"))
		{
			int val = new Integer(command.substring(23)).intValue();
			Announcements.getInstance().delAnnouncement(val);
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.equals("admin_show_moves"))
		{
			showHelpPage(client, "teleports.htm");
		}
		else if (command.equals("admin_show_spawns"))
		{
			showHelpPage(client, "spawns.htm");
		}
		if (command.startsWith("admin_buy"))
		{
			handleBuyRequest(client, command.substring(10));
		}
		else if (command.equals("admin_show_skills"))
		{
			showSkillsPage(client);
		}else if (command.equals("admin_remove_skills"))
		{
			removeSkillsPage(client);
		}
		else if (command.startsWith("admin_skill_list"))
		{
			showHelpPage(client, "skills.htm");
		}
		else if (command.startsWith("admin_skill_index"))
		{
			String val = command.substring(18);
			showHelpPage(client, "skills/" + val + ".htm");
		}
		else if (command.startsWith("admin_spawn_index"))
		{
			String val = command.substring(18);
			showHelpPage(client, "spawns/" + val + ".htm");
		}
		else if (command.equals("admin_character_disconnect"))
		{ 
			disconnectCharacter(client);			
		}		
		else if (command.equals("admin_show_teleport"))
		{
			showTeleportCharWindow(client);
		}
		else if (command.equals("admin_teleport_to_character"))
		{
			teleportToCharacter(client);
		}
		else if (command.equals("admin_add_exp_sp_to_character"))
		{
			addExpSp(client);
		}
		else if (command.equals("admin_edit_character"))
		{
			editCharacter(client);
		}
		else if (command.equals("admin_current_player"))
		{
			showCharacterList(client,_characterToManipulate);
		}
		else if (command.equals("admin_get_skills"))
		{
			adminGetSkills(client);
		}
		else if (command.equals("admin_reset_skills"))
		{
			adminResetSkills(client);
		}
//		else if (command.equals("admin_set_context"))
//		{
//			adminSetContext(client);
//		}
//		else if (command.equals("admin_reset_context"))
//		{
//			adminResetContext(client);
//		}
		else if (command.startsWith("admin_move_to"))
		{
			try
			{
				String val = command.substring(14);
				teleportTo(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty co-ordinates
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Wrong or no Co-ordinates given.");
				activeChar.sendPacket(sm);
				
				showMainPage(client); //back to admin window
			}		
		}
		else if (command.startsWith("admin_help"))
		{
			try
			{
				String val = command.substring(11);
				showHelpPage(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				//case of empty filename
			}
			
		}
		else if (command.startsWith("admin_character_list"))
		{
			try
			{
				String val = command.substring(21); 
				showCharacterList(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				//Case of empty character name
			}
			
		}
		else if (command.startsWith("admin_show_characters"))
		{
			try
			{   
				String val = command.substring(22);
				int page = Integer.parseInt(val);
				listCharacters(client, page);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				//Case of empty page
			}
		}
		else if (command.startsWith("admin_find_character"))
		{
			try
			{
				String val = command.substring(21); 
				findCharacter(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty character name
				SystemMessage sm = new SystemMessage(614);
				sm.addString("You didnt enter a character name to find.");
				activeChar.sendPacket(sm);
				
				listCharacters(client, 0);
			}			
		}
		else if (command.startsWith("admin_add_exp_sp"))
		{
			try
			{
				String val = command.substring(16); 
				adminAddExpSp(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty character name
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Error while adding Exp-Sp.");
				activeChar.sendPacket(sm);
				listCharacters(client, 0);
			}			
		}
		else if (command.startsWith("admin_add_skill"))
		{
			try
			{
				String val = command.substring(15); 
				adminAddSkill(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty character name
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Error while adding skill.");
				activeChar.sendPacket(sm);
				listCharacters(client, 0);
			}			
		}
		else if (command.startsWith("admin_remove_skill"))
		{
			try
			{
				String id = command.substring(19);
				int idval = Integer.parseInt(id);
				adminRemoveSkill(client, idval);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty character name
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Error while removing skill.");
				activeChar.sendPacket(sm);
				listCharacters(client, 0);
			}			
		}
		else if (command.startsWith("admin_save_modifications"))
		{
			try
			{
				String val = command.substring(24); 
				adminModifyCharacter(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{	//Case of empty character name
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Error while modifying character.");
				activeChar.sendPacket(sm);
				listCharacters(client, 0);
			}			
		}
		else if (command.startsWith("admin_teleport_character"))
		{
			try
			{
				String val = command.substring(25); 
				teleportCharacter(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				//Case of empty co-ordinates
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Wrong or no Co-ordinates given.");
				activeChar.sendPacket(sm);
				
				showTeleportCharWindow(client); //back to character teleport
			}
		} 		
		else if (command.startsWith("admin_spawn_monster"))
		{
			try
			{
				String val = command.substring(20);
				spawnMenu(client, val);
			}
			catch  (StringIndexOutOfBoundsException e)
			{
				//Case of wrong monster data
			}
		}
		else if (command.startsWith("admin_spawn_confirm"))
		{
			try
			{
				String val = command.substring(20);
				StringTokenizer st = new StringTokenizer(val);
				if (st.countTokens()!= 2)
				{
					String id = st.nextToken();
					spawnMenu(client,id);
				}
				else
				{
					String id = st.nextToken();
					String targetName = st.nextToken();
					spawnMonster(client,id,targetName);
				}
			}
			catch  (StringIndexOutOfBoundsException e)
			{
				//Case of wrong monster data
			}
		}
		else if (command.startsWith("admin_server_shutdown"))
		{	
			try
			{
				int val = Integer.parseInt(command.substring(22)); 
				serverShutdown(client, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				SystemMessage sm = new SystemMessage(614);
				sm.addString("You didnt enter the seconds untill server shutdowns.");
				activeChar.sendPacket(sm);
				
				serverShutdown(client, 0);
			}
		}
		else if (command.equals("admin_gm_shops"))
		{
			showHelpPage(client, "gmshops.htm");
		}
		else if (command.equals("admin_itemcreate"))
		{
			showHelpPage(client, "itemcreation.htm");
		}		
		else if (command.startsWith("admin_create_item"))
		{
			try
			{
				String val = command.substring(17);
				StringTokenizer st = new StringTokenizer(val);
				if (st.countTokens()== 2)
				{
					String id = st.nextToken();
					int idval = Integer.parseInt(id);
					String num = st.nextToken();
					int numval = Integer.parseInt(num);
					createItem(client,idval,numval);
				}
				else if (st.countTokens()== 1)
				{
					String id = st.nextToken();
					int idval = Integer.parseInt(id);
					createItem(client,idval,1);
				}
				else
				{
					showHelpPage(client, "itemcreation.htm");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				SystemMessage sm = new SystemMessage(614);
				sm.addString("Error while creating item.");
				activeChar.sendPacket(sm);
			}
		}
		
	}
	
	/**
	 * @param client
	 */
	private void handleBuyRequest(ClientThread client, String command)
	{
		L2PcInstance player = client.getActiveChar();
		
		int val = -1;
		
		try
		{
			val = Integer.parseInt(command);
		}
		catch (Exception e)
		{
			_log.warning("admin buylist failed:"+command);
		}
		
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

	public void showMainPage(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Server Status</title>");
		replyMSG.append("<body>");
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Players Online: " + players.length + "</td></tr>");				
		replyMSG.append("<tr><td>Server Software: This Server is running L2J version 0.4 created by L2Chef and the L2J team.</td></tr>");
		replyMSG.append("<tr><td>Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		replyMSG.append(" bytes</td></tr>");
		replyMSG.append("</table>");		
		replyMSG.append("<br>");
		
		replyMSG.append("<br>");
		replyMSG.append("<center><button value=\"Help\" action=\"bypass -h admin_help admhelp.htm\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Shutdown Server\" action=\"bypass -h admin_server_shutdown 0\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");	
		replyMSG.append("<center><button value=\"List Characters\" action=\"bypass -h admin_show_characters 0\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Move to Location\" action=\"bypass -h admin_show_moves\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Spawn a Monster\" action=\"bypass -h admin_show_spawns\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Item Creation\" action=\"bypass -h admin_itemcreate\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"GM Shops\" action=\"bypass -h admin_gm_shops\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Announcements\" action=\"bypass -h admin_list_announcements\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br><br>");
		replyMSG.append("<right><button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></right>"); //This does nothing
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);	
	}
	
	
	private void teleportTo(ClientThread client, String Cords)
	{
		L2PcInstance activeChar = client.getActiveChar();
		
		StringTokenizer st = new StringTokenizer(Cords);
		String x1 = st.nextToken();
		int x = Integer.parseInt(x1);
		String y1 = st.nextToken();
		int y = Integer.parseInt(y1);
		String z1 = st.nextToken();
		int z = Integer.parseInt(z1);
				
		TeleportToLocation tele = new TeleportToLocation(activeChar, x,y,z);
		activeChar.sendPacket(tele);
		activeChar.setX(x);
		activeChar.setY(y);
		activeChar.setZ(z);
			
		SystemMessage sm = new SystemMessage(614);
		sm.addString("You have been teleported to " + Cords);
		activeChar.sendPacket(sm);
		
		showMainPage(client); //Back to start
	}

//	private void adminSetContext(ClientThread client)
//	{
//		L2PcInstance activeChar = client.getActiveChar();	
//		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
//		if (player.getName().equals(activeChar.getName()))
//		{
//			SystemMessage sm = new SystemMessage(614);
//			sm.addString("There is no point in doing it on your character...");
//			player.sendPacket(sm);
//			showMainPage(client); //Back to start
//		}
//		else
//		{
//		adminInventory = new Inventory();
//		adminInventory.getItems().addAll(activeChar.getInventory().getItems());
//		activeChar.getInventory().getItems().clear();
//		activeChar.getInventory().getItems().addAll(player.getInventory().getItems());
//		
//		ItemList il = new ItemList(activeChar, true);
//		activeChar.sendPacket(il);
//		
//		SystemMessage sm = new SystemMessage(614);
//		sm.addString("[GM]"+activeChar.getName()+" is viewing your inventory, please wait.");
//		player.sendPacket(sm);
//		
//		player.getInventory().getItems().clear();
//				
//		ItemList il2 = new ItemList(activeChar, true);
//		player.sendPacket(il2);
//		
//		SystemMessage smA = new SystemMessage(614);
//		smA.addString("You are now in the inventory of " +player.getName()+ ".");
//		activeChar.sendPacket(smA);
//		
//		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		
//		StringBuffer replyMSG = new StringBuffer("<html><title>Inventory set to "+player.getName()+"</title>");
//		replyMSG.append("<body>");
//		replyMSG.append("You can now see, add, remove, buy, create items like if it was your own inventory.");
//		replyMSG.append("When finished, reset your inventory and it will save players data.");
//		replyMSG.append("Note: don't forget to reset your inventory, since the players inventory is empty while you work on it :)");
//		replyMSG.append("<center><button value=\"Reset inventory\" action=\"bypass -h admin_reset_context\" width=130 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
//		replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15></center>");
//		replyMSG.append("</body></html>");
//		
//		adminReply.setHtml(replyMSG.toString());
//		activeChar.sendPacket(adminReply);
//		}
//	}
	
//	private void adminResetContext(ClientThread client)
//	{
//		L2PcInstance activeChar = client.getActiveChar();	
//		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
//		if (adminInventory==null)
//		{
//			SystemMessage smA = new SystemMessage(614);
//			smA.addString("You must first get the inventory of a player.");
//			activeChar.sendPacket(smA);
//			showMainPage(client); //Back to start
//		}
//		else
//		{
//			player.getInventory().getItems().clear();
//			player.getInventory().getItems().addAll(activeChar.getInventory().getItems());
//			ItemList il2 = new ItemList(player, true);
//			player.sendPacket(il2);
//			
//			activeChar.getInventory().getItems().clear();
//			activeChar.getInventory().getItems().addAll(adminInventory.getItems());
//			ItemList il = new ItemList(activeChar, true);
//			activeChar.sendPacket(il);
//			
//			SystemMessage sm = new SystemMessage(614);
//			sm.addString("[GM]"+activeChar.getName()+" has modified your inventory.");
//			player.sendPacket(sm);
//			
//			_log.fine("[GM]"+activeChar.getName()+" has modified "+player.getName()+" inventory.");
//			
//			SystemMessage smA = new SystemMessage(614);
//			smA.addString("You are now back to your inventory.");
//			activeChar.sendPacket(smA);
//			adminInventory=null;
//		}
//		showCharacterList(client,_characterToManipulate); //Back to start
//	}
	
	private void createItem(ClientThread client, int id, int num)
	{
		L2PcInstance activeChar = client.getActiveChar();
			
		L2ItemInstance createditem = ItemTable.getInstance().createItem(id);
		for (int i=0;i<num;i++)
		{
			activeChar.getInventory().addItem(createditem);
		}
		 
		ItemList il = new ItemList(activeChar, true);
		activeChar.sendPacket(il);
		
		SystemMessage sm = new SystemMessage(614);
		sm.addString("You have spawned "+num+ " item(s) number " + id + " in your inventory.");
		activeChar.sendPacket(sm);
				
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		
		StringBuffer replyMSG = new StringBuffer("<html><title>Item Creation Complete(I Hope)</title>");
		replyMSG.append("<body>");
		replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void listCharacters(ClientThread client, int page)
	{	
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();

		int MaxCharactersPerPage = 20;
		int MaxPages = players.length / MaxCharactersPerPage;
		int modulus = players.length % MaxCharactersPerPage;
			
		if (modulus!=0)
		{
			MaxPages = MaxPages+1;
		}
		
		//Check if number of users changed
		if (page>MaxPages)
		{
			page=MaxPages;
		}

		int CharactersStart = MaxCharactersPerPage*page;
		int CharactersEnd = players.length-CharactersStart;		
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		

		StringBuffer replyMSG = new StringBuffer("<html><title>Characters List</title>");
		replyMSG.append("<body>");
		
		for (int x=0; x<MaxPages; x++)
		{
			replyMSG.append("<a action=\"bypass -h admin_show_characters " + x + "\">Page" + x+1 + "</a>\t");	
		}			
		replyMSG.append("<br>");

		//List Players in a Table
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Name</td><td>Class</td><td>Level</td></tr>");
		for (int i = CharactersStart; i < CharactersEnd; i++)
		{	//Add player info into new Table row
			replyMSG.append("<tr><td>" + "<a action=\"bypass -h admin_character_list " + players[i].getName() + "\">" + players[i].getName() + "</a>" + "</td><td>" + CharTemplateTable.getInstance().getTemplate(players[i].getClassId()).getClassName() + "</td><td>" + players[i].getLevel() + "</td></tr>");
		}
		replyMSG.append("</table>");
		replyMSG.append("---------<p>");
		replyMSG.append("You can find a character by writing his name<p> and clicking Find bellow:");
		replyMSG.append("<edit var=\"character_name\" width=110>");
		replyMSG.append("<button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);			
	}
	
	private void showCharacterList(ClientThread client, String CharName)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(CharName);		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Character Information</title>");
		replyMSG.append("<body>");
		
		replyMSG.append("<br>");
		replyMSG.append("<center>" + player.getName() + "</center><p>");	
		replyMSG.append("Clan: " + ClanTable.getInstance().getClan(player.getClanId()) + "<p>");		
		replyMSG.append("Lv: " + player.getLevel() + " " + CharTemplateTable.getInstance().getTemplate(player.getClassId()).getClassName() + "<p>");
		replyMSG.append("Exp: " + player.getExp() + "<p>");				

		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>HP</td><td>" + player.getCurrentHp() + " / " + player.getMaxHp() + "</td><td>MP</td><td>" + player.getCurrentMp() + " / " + player.getMaxMp() + "</td></tr>");
		replyMSG.append("<tr><td>SP</td><td>" + player.getSp() + "</td><td>Load</td><td>" + player.getCurrentLoad() + " / "+player.getMaxLoad()+"</td></tr>");		
		replyMSG.append("<tr><td>ClassId</td><td>"+player.getClassId()+"</td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		
		//Combat
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Combat</td></tr>");
		replyMSG.append("</table>");

		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>P.ATK</td><td>" + player.getPhysicalAttack() + "</td><td>M.ATK</td><td>" + player.getMagicalAttack() + "</td></tr>");
		replyMSG.append("<tr><td>P.DEF</td><td>" + player.getPhysicalDefense() + "</td><td>M.DEF</td><td>" + player.getMagicalDefense() + "</td></tr>");
		replyMSG.append("<tr><td>Accuracy</td><td>" + player.getAccuracy() + "</td><td>Evasion</td><td>" + player.getEvasionRate() + "</td></tr>");
		replyMSG.append("<tr><td>Critical</td><td>" + player.getCriticalHit() + "</td><td>Speed</td><td>" + player.getRunSpeed() + "</td></tr>");
		replyMSG.append("<tr><td>ATK Spd</td><td>" + player.getPhysicalSpeed() + "</td><td>Casting Spd</td><td>" + player.getMagicalSpeed() + "</td></tr>");		
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		//Basic
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Basic</td></tr>");
		replyMSG.append("</table>");

		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>STR</td><td>" + player.getStr() + "</td><td>DEX</td><td>" + player.getDex() + "</td><td>CON</td><td>" + player.getCon() + "</td></tr>");
		replyMSG.append("<tr><td>INT</td><td>" + player.getInt() + "</td><td>WIT</td><td>" + player.getWit() + "</td><td>MEN</td><td>" + player.getMen() + "</td></tr>");		
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		//Social
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Social</td></tr>");
		replyMSG.append("</table>");
		
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Karma</td><td>" + player.getKarma() + "</td><td>PvP</td><td>" + player.getPvpFlag() + " / " + player.getPvpKills() +  "</td></tr>");		
		replyMSG.append("</table><p>");				

		replyMSG.append("Character Co-ordinates: " + player.getX() + " " + player.getY() + " " + player.getZ());
		replyMSG.append("<br>");
		replyMSG.append("<center><button value=\"Logout Character\" action=\"bypass -h admin_character_disconnect\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br>");
		replyMSG.append("<center><button value=\"Teleport Character\" action=\"bypass -h admin_show_teleport\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Teleport to Character\" action=\"bypass -h admin_teleport_to_character\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Add Xp-Sp to Character\" action=\"bypass -h admin_add_exp_sp_to_character\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Edit Character\" action=\"bypass -h admin_edit_character\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Manage skills\" action=\"bypass -h admin_show_skills\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Switch to inventory\" action=\"bypass -h admin_set_context\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Reset inventory\" action=\"bypass -h admin_reset_context\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15></center>");
		replyMSG.append("</body></html>");

		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
		
		_characterToManipulate = player.getName();
	}
	
	private void addExpSp(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);		
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		

		StringBuffer replyMSG = new StringBuffer("<html><title>Add Exp-Sp to " + player.getName() + "</title>");
		replyMSG.append("<body>");
		replyMSG.append("<br>");
		replyMSG.append("<center>Lv: " + player.getLevel() + " " + CharTemplateTable.getInstance().getTemplate(player.getClassId()).getClassName() + "<p>");
		replyMSG.append("Exp: " + player.getExp() + "<p>");
		replyMSG.append("Sp: " + player.getSp() + "<p>");
		replyMSG.append("</center><br>");
		replyMSG.append("<center>Caution ! Dont forget that modifying players stats can ruin the game...</center><br>");
		replyMSG.append("Exp: ");
		replyMSG.append("<edit var=\"exp_to_add\" width=110>");
		replyMSG.append("Sp: ");
		replyMSG.append("<edit var=\"sp_to_add\" width=110>");
		replyMSG.append("<button value=\"Add Exp-Sp\" action=\"bypass -h admin_add_exp_sp $exp_to_add $sp_to_add\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void adminAddExpSp(ClientThread client,String ExpSp)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		StringTokenizer st = new StringTokenizer(ExpSp);
		if (st.countTokens()!=2)
		{
			addExpSp(client);
		}
		else
		{
		String exp = st.nextToken();
		String sp = st.nextToken();		
		int expval = Integer.parseInt(exp);
		int spval = Integer.parseInt(sp);
		//Common character information
		SystemMessage sm = new SystemMessage(614);
		sm.addString("Admin is adding you "+expval+" xp and "+spval+" sp.");
		player.sendPacket(sm);
		
		player.addExpAndSp(expval,spval);

		//Admin information	
		SystemMessage smA = new SystemMessage(614);
		smA.addString("Added "+expval+" xp and "+spval+" sp to "+player.getName()+".");
		activeChar.sendPacket(smA);
		_log.fine("[GM]"+activeChar.getName()+" added "+expval+" xp and "+spval+" sp to "+player.getName()+".");
		
		showCharacterList(client,_characterToManipulate); //Back to start
		}
	}
	
	private void adminModifyCharacter(ClientThread client,String modifications)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		StringTokenizer st = new StringTokenizer(modifications);
		if (st.countTokens()!=9)
		{
			editCharacter(client);
		}
		else
		{
		String hp = st.nextToken();
		String hpmax = st.nextToken();
		String mp = st.nextToken();
		String mpmax = st.nextToken();
		String load = st.nextToken();
		String karma = st.nextToken();
		String pvpflag = st.nextToken();
		String pvpkills = st.nextToken();
		String classid = st.nextToken();
		int hpval = Integer.parseInt(hp);
		int hpmaxval = Integer.parseInt(hpmax);
		int mpval = Integer.parseInt(mp);
		int mpmaxval = Integer.parseInt(mpmax);
		int loadval = Integer.parseInt(load);
		int karmaval = Integer.parseInt(karma);
		int pvpflagval = Integer.parseInt(pvpflag);
		int pvpkillsval = Integer.parseInt(pvpkills);
		int classidval = Integer.parseInt(classid);
		
		//Common character information
		SystemMessage sm = new SystemMessage(614);
		sm.addString("Admin has changed your stats." +
				" Hp: "+hpval+" HpMax: "+hpmaxval+" Mp: "+mpval+" MpMax: "+mpmaxval+
				" MaxLoad: "+loadval+" Karma: "+karmaval+" Pvp: "+pvpflagval+" / "+pvpkillsval+" ClassId: "+classidval);
		
		player.sendPacket(sm);
		
		player.setCurrentHp(hpval);
		player.setCurrentMp(mpval);
		player.setMaxHp(hpmaxval);
		player.setMaxMp(mpmaxval);
		player.setMaxLoad(loadval);
		player.setKarma(karmaval);
		player.setPvpFlag(pvpflagval);
		player.setPvpKills(pvpkillsval);
		player.setClassId(classidval);
				
		StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_HP, hpval);
		su.addAttribute(StatusUpdate.MAX_HP, hpmaxval);
		su.addAttribute(StatusUpdate.CUR_MP, mpval);
		su.addAttribute(StatusUpdate.MAX_MP, mpmaxval);
		su.addAttribute(StatusUpdate.MAX_LOAD, loadval);
		su.addAttribute(StatusUpdate.KARMA, karmaval);
		su.addAttribute(StatusUpdate.PVP_FLAG, pvpflagval);
		player.sendPacket(su);
		
		//Admin information	
		SystemMessage smA = new SystemMessage(614);
		smA.addString("Changed stats of "+player.getName()+". " +
				" Hp: "+hpval+" HpMax: "+hpmaxval+" Mp: "+mpval+" MpMax: "+mpmaxval+
				" MaxLoad: "+loadval+" Karma: "+karmaval+" Pvp: "+pvpflagval+" / "+pvpkillsval+" ClassId: "+classidval);
		
		activeChar.sendPacket(smA);
		_log.fine("[GM]"+activeChar.getName()+" changed stats of "+player.getName()+". " +
				" Hp: "+hpval+" HpMax: "+hpmaxval+" Mp: "+mpval+" MpMax: "+mpmaxval+
				" MaxLoad: "+loadval+" Karma: "+karmaval+" Pvp: "+pvpflagval+" / "+pvpkillsval+" ClassId: "+classidval);
		
		showCharacterList(client,_characterToManipulate); //Back to start
		}
	}

	private void editCharacter(ClientThread client)
	{
		///FIXME Made it so that you have to enter all values to 'prevent' abuses...
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);		
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Editing character " + player.getName() + "</title>");
		replyMSG.append("<body>");
		replyMSG.append("<br><center>Caution ! Dont forget that modifying players stats can ruin the game...</center><br>");
		replyMSG.append("Note: you must type all values to confirm modifications.</center><br>");
		replyMSG.append("<br>");
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>HP</td><td>" + player.getCurrentHp() + " / " + player.getMaxHp() + "</td><td>MP</td><td>" + player.getCurrentMp() + " / " + player.getMaxMp() + "</td></tr>");
		replyMSG.append("<tr><td>Load</td><td>" + player.getCurrentLoad() + " / "+player.getMaxLoad()+"</td></tr>");		
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		//Social
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Social</td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Karma</td><td>" + player.getKarma() + "</td><td>PvP</td><td>" + player.getPvpFlag() + " / " + player.getPvpKills() +  "</td></tr>");		
		replyMSG.append("<tr><td>ClassId</td><td>"+player.getClassId()+"</td></tr>");
		replyMSG.append("</table><p>");			
		replyMSG.append("<br>");
		replyMSG.append("<center><table>");
		replyMSG.append("<tr><td>Hp:</td>");
		replyMSG.append("<td><edit var=\"hp\" width=110></td></tr>");
		replyMSG.append("<tr><td>HpMax:</td>");
		replyMSG.append("<td><edit var=\"hpmax\" width=110></td></tr>");
		replyMSG.append("<tr><td>Mp:</td>");
		replyMSG.append("<td><edit var=\"mp\" width=110></td></tr>");
		replyMSG.append("<tr><td>MpMax:</td>");
		replyMSG.append("<td><edit var=\"mpmax\" width=110></td></tr>");
		replyMSG.append("<tr><td>MaxLoad:</td>");
		replyMSG.append("<td><edit var=\"load\" width=110></td></tr>");
		replyMSG.append("<tr><td>Karma:</td>");
		replyMSG.append("<td><edit var=\"karma\" width=110></td></tr>");
		replyMSG.append("<tr><td>PvpFlag:</td>");
		replyMSG.append("<td><edit var=\"pvpflag\" width=110></td></tr>");
		replyMSG.append("<tr><td>PvpKills:</td>");
		replyMSG.append("<td><edit var=\"pvpkills\" width=110></td></tr>");
		replyMSG.append("<tr><td>ClassId:</td>");
		replyMSG.append("<td><edit var=\"classid\" width=110></td></tr>");
		replyMSG.append("</table></center>");
		replyMSG.append("<center><button value=\"Save modifications\" action=\"bypass -h" +
				" admin_save_modifications $hp $hpmax $mp $mpmax $load $karma $pvpflag $pvpkills $classid\" width=140 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15></center>");
		replyMSG.append("</body></html>");

		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void disconnectCharacter(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);		
		
		if (player.getName().equals(activeChar.getName()))
		{		
			SystemMessage sm = new SystemMessage(614);
			sm.addString("You cannot logout your character.");
			activeChar.sendPacket(sm);
		}
		else
		{				
			SystemMessage sm = new SystemMessage(614);
			sm.addString("Character " + player.getName() + " disconnected from server.");
			activeChar.sendPacket(sm);
			
			//Logout Character
			LeaveWorld ql = new LeaveWorld();
			player.sendPacket(ql);
			
			try
			{
				player.getNetConnection().close();
			}
			catch (IOException e)
			{
				// just to make sure we try to kill the connection 
			}
		}
		
		showMainPage(client); //Back to start
	}
	
	private void findCharacter(ClientThread client, String CharacterToFind)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		int CharactersFound = 0;
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Character Search</title>");		
		replyMSG.append("<body>");
		replyMSG.append("<br>");
		
		replyMSG.append("<table>");		
		replyMSG.append("<tr><td>Name</td><td>Class</td><td>Level</td></tr>");
		
		for (int i = 0; i < players.length; i++)
		{	//Add player info into new Table row
			
			if (players[i].getName().startsWith((CharacterToFind)))
			{
				CharactersFound = CharactersFound+1;
				replyMSG.append("<tr><td>" + "<a action=\"bypass -h admin_character_list " + players[i].getName() + "\">" + players[i].getName() + "</a>" + "</td><td>" + CharTemplateTable.getInstance().getTemplate(players[i].getClassId()).getClassName() + "</td><td>" + players[i].getLevel() + "</td></tr>");
			}	
		}
		replyMSG.append("</table>");
		
		if (CharactersFound==0)
		{
			replyMSG.append("<br>Your search did not find any characters. Please try again:");
			replyMSG.append("<edit var=\"character_name\" width=110>");
			replyMSG.append("<button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			replyMSG.append("<br>");
		}
		else
		{
			replyMSG.append("<br>Found " + CharactersFound + " character");
			
			if (CharactersFound==1)
			{
				replyMSG.append(".");
			}
			else 
			{
				if (CharactersFound>1)
				{
					replyMSG.append("s.");
				}
			}

		}
		
		replyMSG.append("<br>");
		replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");		
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);			
	}
	
	private void showTeleportCharWindow(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Teleport Character</title>");
		replyMSG.append("<body>");
		replyMSG.append("The character you will teleport is " + player.getName() + ".");
		replyMSG.append("<br>");
		
		replyMSG.append("Co-ordinate x");
		replyMSG.append("<edit var=\"char_cord_x\" width=110>");
		replyMSG.append("Co-ordinate y");
		replyMSG.append("<edit var=\"char_cord_y\" width=110>");
		replyMSG.append("Co-ordinate z");
		replyMSG.append("<edit var=\"char_cord_z\" width=110>");
		replyMSG.append("<button value=\"Teleport\" action=\"bypass -h admin_teleport_character $char_cord_x $char_cord_y $char_cord_z\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");	
		replyMSG.append("<button value=\"Teleport near you\" action=\"bypass -h admin_teleport_character " + activeChar.getX() + " " + activeChar.getY() + " " + activeChar.getZ() + "\" width=115 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);			
	}
	
	private void teleportCharacter(ClientThread client, String Cords)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		
		if (player.getName().equals(activeChar.getName()))
		{
			SystemMessage sm = new SystemMessage(614);
			sm.addString("You cannot teleport your character.");
			player.sendPacket(sm);
		}
		else
		{
			StringTokenizer st = new StringTokenizer(Cords);
			String x1 = st.nextToken();
			int x = Integer.parseInt(x1);
			String y1 = st.nextToken();
			int y = Integer.parseInt(y1);
			String z1 = st.nextToken();
			int z = Integer.parseInt(z1);
		
			//Common character information
			SystemMessage sm = new SystemMessage(614);
			sm.addString("Admin is teleporting you.");
			player.sendPacket(sm);
			
			TeleportToLocation tele = new TeleportToLocation(player, x,y,z);
			player.sendPacket(tele);
			player.setX(x);
			player.setY(y);
			player.setZ(z);

			//Admin information	
			SystemMessage smA = new SystemMessage(614);
			smA.addString("Character " + player.getName() + " teleported to "+ x + " " + y + " "+z);
			activeChar.sendPacket(smA);
		}
		
		showMainPage(client); //Back to start
	}	
	
	private void teleportToCharacter(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		
		if (player.getName().equals(activeChar.getName()))
		{	
			SystemMessage sm = new SystemMessage(614);
			sm.addString("You cannot self teleport.");
			activeChar.sendPacket(sm);
		}
		else
		{
			int x = player.getX();
			int y = player.getY();
			int z = player.getZ();
		
			TeleportToLocation tele = new TeleportToLocation(activeChar, x,y,z);
			activeChar.sendPacket(tele);
			activeChar.setX(x);
			activeChar.setY(y);
			activeChar.setZ(z);
		
			SystemMessage sm = new SystemMessage(614);
			sm.addString("You have teleported to character " + player.getName() + ".");
			activeChar.sendPacket(sm);
		}
		
		showMainPage(client); //Back to start
	}

	private void spawnMenu(ClientThread client, String monsterId)
	{
		L2PcInstance activeChar = client.getActiveChar();			
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5); 
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Custom Spawn Menu</title>");

		replyMSG.append("<body><br>Enter target player's name below.<br>You may leave the field blank for self-spawn.<br><br>");
		replyMSG.append("<center><edit var=\"targetname\" width=160></center><br><br>");
		replyMSG.append("<center><button value=\"Spawn on self\" action=\"bypass -h admin_spawn_confirm " + monsterId + " " + activeChar.getName() + "\" width=160 height=15></center><br>");
		replyMSG.append("<center><button value=\"Spawn on character\" action=\"bypass -h admin_spawn_confirm " + monsterId + " $targetname\" width=160 height=15></center></body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}	
	
	private void spawnMonster(ClientThread client, String monsterId, String CharName)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance targetPlayer = L2World.getInstance().getPlayer(CharName);
		
		int monsterTemplate = Integer.parseInt(monsterId);
		
		L2Npc template1 = NpcTable.getInstance().getTemplate(monsterTemplate);
		if (template1 == null)
		{
			return;
		}
		
		try
		{
			L2MonsterInstance mob = new L2MonsterInstance(template1);
	
			L2Spawn spawn = new L2Spawn(template1);
			spawn.setLocx(targetPlayer.getX());
			spawn.setLocy(targetPlayer.getY());
			spawn.setLocz(targetPlayer.getZ());
			spawn.setRandomx(0);
			spawn.setRandomy(0);
			spawn.setAmount(1);
			spawn.setHeading(targetPlayer.getHeading());
			spawn.setRespawnDelay(15);
			SpawnTable.getInstance().addNewSpawn(spawn);
			spawn.init();
		
			SystemMessage sm = new SystemMessage(614);
			sm.addString("Created " + template1.getName() + " on " + targetPlayer.getName() + ".");
			activeChar.sendPacket(sm);
		}
		catch (Exception e)
		{
			SystemMessage sm = new SystemMessage(614);
			sm.addString("Target player is offline.");
			activeChar.sendPacket(sm);
		}
		
		showHelpPage(client, "spawns.htm");
	}
	
	private int disconnectAllCharacters()
	{
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();	
		for(int i=0; i<players.length ;i++)
		{
			//Logout Character
			LeaveWorld ql = new LeaveWorld();
			players[i].sendPacket(ql);
			
			try
			{
				players[i].getNetConnection().close();
			}
			catch (IOException e)
			{
				// just to make sure we try to kill the connection 
			}
		}
		return(1);
	}
	private void serverShutdown(ClientThread client, int seconds)
	{
		secondsShut = seconds;
		clientShut = client;
		AdminCommands ShutDownThread = new AdminCommands();
		ShutDownThread.start();
	}
	public void run()
	{
		L2PcInstance activeChar = clientShut.getActiveChar();
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();
		if (secondsShut==0)
		{	
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
			StringBuffer replyMSG = new StringBuffer("<html><title>Shutdown Server</title>");
			replyMSG.append("<body><br>");
		
			replyMSG.append("Enter in seconds the time till the server<p> shutdowns bellow:");
			replyMSG.append("<edit var=\"shutdown_time\" width=110>");
			replyMSG.append("<button value=\"Shutdown\" action=\"bypass -h admin_server_shutdown $shutdown_time\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");			
			
			replyMSG.append("<br>");
			replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15></center>");		
			replyMSG.append("</body></html>");
			
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);				
		}
		else
		{
			_log.fine("[GM]"+activeChar.getName()+" resquested server shutdown.");
			try 
			{
				int i=secondsShut;
				broadcastToAll("The server will shutdown in "+(secondsShut-secondsShut%60)/60+" minutes and "+secondsShut%60+" seconds.", activeChar.getName());
				while(i>0)
				{
					if (i!=secondsShut)
						{
						switch (i)
							{
							case 240:broadcastToAll("The server will shutdown in 4 minutes.", activeChar.getName());break;
							case 180:broadcastToAll("The server will shutdown in 3 minutes.", activeChar.getName());break;
							case 120:broadcastToAll("The server will shutdown in 2 minutes.", activeChar.getName());break;
							case 60:broadcastToAll("The server will shutdown in 1 minutes.", activeChar.getName());break;
							case 30:broadcastToAll("The server will shutdown in 30 seconds.", activeChar.getName());break;
							case 5:broadcastToAll("The server will shutdown in 5 seconds, please delog NOW !", activeChar.getName());break;
							}
						}
					int delay = 1000; //milliseconds	
					Thread.sleep(delay);
					i--;
				}
				if(disconnectAllCharacters()==1)
					{
						_log.fine("All players disconnected, shutting down.");
						System.exit(0);
					}
				else
					{
						_log.warning("Error, aborting shutdown.");
					}
			}
			catch (InterruptedException e)
			{
				// this will never happen
			}
		}
	}
		
	private void showHelpPage(ClientThread client, String filename)
	{
		File file = new File("data/html/admin/" + filename);
		FileInputStream fis = null;
		
		try
		{
			fis = new FileInputStream(file);
			byte[] raw = new byte[fis.available()];
			fis.read(raw);
			
			String content = new String(raw, "UTF-8");
			
			L2PcInstance activeChar = client.getActiveChar();			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

			adminReply.setHtml(content);
			activeChar.sendPacket(adminReply);
		}
		catch (Exception e)
		{
			// problem with adminhelp is ignored
		}		
		finally
		{
			try
			{
				fis.close();
			}
			catch (Exception e1)
			{
				// problems ignored
			}
		}
	}
	
	private void broadcastToAll(String message, String gmName)
	{
		L2PcInstance[] players = L2World.getInstance().getAllPlayers();
		CreatureSay cs = new CreatureSay(0, 9, "[GM]"+ gmName, message);
		
		for(int x=0;x<players.length;x++)
		{
			players[x].sendPacket(cs);
		}
	}
	
	private void removeSkillsPage(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		
		L2Skill[] skills = player.getAllSkills();
				
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		
		StringBuffer replyMSG = new StringBuffer("<html><title>Remove skills of " + player.getName() + "</title>");
		replyMSG.append("<body>");
		replyMSG.append("<br>");
		replyMSG.append("<center>Lv: " + player.getLevel() + " " + CharTemplateTable.getInstance().getTemplate(player.getClassId()).getClassName() + "<p>");
		replyMSG.append("</center><br>");
		replyMSG.append("<center>Caution ! Dont forget that modifying players skills can ruin the game...</center><br>");
		replyMSG.append("<center>Click on the skill you wish to remove:</center>");
		replyMSG.append("<center><table>");
		replyMSG.append("<tr><td><center>Name:</center></td><td></td><td>Lvl:</td><td></td><td>Id:</td></tr>");
		for (int i=0;i<skills.length;i++)
		{
			replyMSG.append("<tr><td><a action=\"bypass -h admin_remove_skill "+skills[i].getId()+"\">"+skills[i].getName()+"</a></td><td></td><td>"+skills[i].getLevel()+"</td><td></td><td>"+skills[i].getId()+"</td></tr>");
		}
		replyMSG.append("</table></center>");
		replyMSG.append("<br><center><table>");
		replyMSG.append("Remove custom skill:");
		replyMSG.append("<tr><td>Id: </td>");
		replyMSG.append("<td><edit var=\"id_to_remove\" width=110></td></tr>");
		replyMSG.append("</table></center>");		
		replyMSG.append("<center><button value=\"Remove skill\" action=\"bypass -h admin_remove_skill $id_to_remove\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	private void showSkillsPage(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);		
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);		

		StringBuffer replyMSG = new StringBuffer("<html><title>Modify skills of " + player.getName() + "</title>");
		replyMSG.append("<body>");
		replyMSG.append("<br>");
		replyMSG.append("<center>Lv: " + player.getLevel() + " " + CharTemplateTable.getInstance().getTemplate(player.getClassId()).getClassName() + "<p>");
		replyMSG.append("</center><br>");
		replyMSG.append("<center>Caution ! Dont forget that modifying players skills can ruin the game...</center><br>");
		replyMSG.append("<center><table>");
		replyMSG.append("<tr><td><button value=\"Add skills\" action=\"bypass -h admin_skill_list\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Remove skills\" action=\"bypass -h admin_remove_skills\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");			
		replyMSG.append("<tr><td><button value=\"Get skills\" action=\"bypass -h admin_get_skills\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Reset skills\" action=\"bypass -h admin_reset_skills\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("</table></center>");		
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15></center>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void adminGetSkills(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		if (player.getName().equals(activeChar.getName()))
		{
			SystemMessage sm = new SystemMessage(614);
			sm.addString("There is no point in doing it on your character...");
			player.sendPacket(sm);
		}
		else
		{
			L2Skill[] skills = player.getAllSkills();
			adminSkills = activeChar.getAllSkills();
			for (int i=0;i<adminSkills.length;i++)
			{
				activeChar.removeSkill(adminSkills[i]);
			}
			for (int i=0;i<skills.length;i++)
			{
				activeChar.addSkill(skills[i]);
			}
			SystemMessage smA = new SystemMessage(614);
			smA.addString("You now have all the skills of  "+player.getName()+".");
			activeChar.sendPacket(smA);
		}
		showSkillsPage(client);
	}
	
	private void adminResetSkills(ClientThread client)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		if (adminSkills==null)
		{
			SystemMessage smA = new SystemMessage(614);
			smA.addString("You must first get the skills of someone to do this.");
			activeChar.sendPacket(smA);
		}
		else
		{
			L2Skill[] skills = player.getAllSkills();
			for (int i=0;i<skills.length;i++)
			{
				player.removeSkill(skills[i]);
			}
			for (int i=0;i<activeChar.getAllSkills().length;i++)
			{
				player.addSkill(activeChar.getAllSkills()[i]);
			}
			for (int i=0;i<skills.length;i++)
			{
				activeChar.removeSkill(skills[i]);
			}
			for (int i=0;i<adminSkills.length;i++)
			{
				activeChar.addSkill(adminSkills[i]);
			}
			SystemMessage sm = new SystemMessage(614);
			sm.addString("[GM]"+activeChar.getName()+" has updated your skills.");
			player.sendPacket(sm);
			SystemMessage smA = new SystemMessage(614);
			smA.addString("You now have all your skills back.");
			activeChar.sendPacket(smA);
			adminSkills=null;
		}
		showSkillsPage(client);
	}
	
	private void adminAddSkill(ClientThread client, String val)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
		StringTokenizer st = new StringTokenizer(val);
		if (st.countTokens()!=2)
		{
			showSkillsPage(client);
		}
		else
		{
		String id = st.nextToken();
		String level = st.nextToken();		
		int idval = Integer.parseInt(id);
		int levelval = Integer.parseInt(level);
		
		L2Skill skill = SkillTable.getInstance().getInfo(idval,levelval);		
		
		if (skill != null)
		{
			SystemMessage sm = new SystemMessage(614);
			sm.addString("Admin gave you the skill "+skill.getName()+".");
			player.sendPacket(sm);
			
			player.addSkill(skill);
			
			//Admin information	
			SystemMessage smA = new SystemMessage(614);
			smA.addString("You gave the skill "+skill.getName()+" to "+player.getName()+".");
			
			activeChar.sendPacket(smA);
			_log.fine("[GM]"+activeChar.getName()+"gave the skill "+skill.getName()+" to "+player.getName()+".");
		}
		else
		{
			SystemMessage smA = new SystemMessage(614);
			smA.addString("Error: there is no such skill.");
		}		
		showSkillsPage(client); //Back to start
		}
	}
	
	private void adminRemoveSkill(ClientThread client, int idval)
	{
		L2PcInstance activeChar = client.getActiveChar();
		L2PcInstance player = L2World.getInstance().getPlayer(_characterToManipulate);
				
		L2Skill skill = SkillTable.getInstance().getInfo(idval,player.getSkillLevel(idval));
				
		if (skill != null)
		{
		SystemMessage sm = new SystemMessage(614);
		sm.addString("Admin removed the skill "+skill.getName()+".");
		player.sendPacket(sm);
				
		player.removeSkill(skill);
		
		//Admin information	
		SystemMessage smA = new SystemMessage(614);
		smA.addString("You removed the skill "+skill.getName()+" from "+player.getName()+".");
		
		activeChar.sendPacket(smA);
		_log.fine("[GM]"+activeChar.getName()+"removed the skill "+skill.getName()+" from "+player.getName()+".");
		}
		else
		{
			SystemMessage smA = new SystemMessage(614);
			smA.addString("Error: there is no such skill.");
		}
		removeSkillsPage(client); //Back to start	
	}
	public void showSkill(ClientThread client, String val)
	{
		L2PcInstance activeChar = client.getActiveChar();	
		
		int skillid = Integer.parseInt(val);
		L2Skill skill = SkillTable.getInstance().getInfo(skillid, 1);

		if (skill != null)
		{
			if (skill.getTargetType() == 0)
			{
				activeChar.setTarget(activeChar);
		
				MagicSkillUser msk = new MagicSkillUser(activeChar, skillid, 1, skill.getSkillTime() , skill.getReuseDelay());
				activeChar.sendPacket(msk);
				activeChar.broadcastPacket(msk);
		
				ActionFailed af = new ActionFailed();
				_log.fine("showing self skill, id: "+skill.getId()+" named: "+skill.getName());
			}
			else if (skill.getTargetType() == 1)
			{
				ActionFailed af = new ActionFailed();
				_log.fine("showing attack skill, id: "+skill.getId()+" named: "+skill.getName());				
			}
		}
		else
		{
		_log.fine("no such skill id: "+skillid);
		ActionFailed af = new ActionFailed();
		}
	}
}