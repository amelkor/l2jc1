/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/CommunityBoard.java,v 1.3 2004/10/01 09:28:47 dethx Exp $
 *
 * $Author: dethx $
 * $Date: 2004/10/01 09:28:47 $
 * $Revision: 1.3 $
 * $Log: CommunityBoard.java,v $
 * Revision 1.3  2004/10/01 09:28:47  dethx
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/25 16:25:52  dethx
 * community board players list fixed
 *
 *
 * Revision 1.0  2004/08/05 00:30:00  dethx
 * first release
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.RatesController;
import net.sf.l2j.gameserver.clientpackets.Say2;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.ShowBoard;

public class CommunityBoard
{
	private static Logger _log = Logger.getLogger(CommunityBoard.class.getName());
	private static CommunityBoard _instance;
	
	public static CommunityBoard getInstance()
	{
		if (_instance == null)
		{
			_instance = new CommunityBoard();
		}
		
		return _instance;
	}
	
	private static String _characterToManipulate;
	
	public void handleCommands(ClientThread client, String command)
	{
		L2PcInstance activeChar = client.getActiveChar();
		
		if (command.startsWith("bbs_"))
		{
			StringBuffer htmlCode = new StringBuffer("<html imgsrc=\"sek.cbui353\"><body><br><table border=0><tr><td FIXWIDTH=15></td><td align=center>L2J Community Board<img src=\"sek.cbui355\" width=610 height=1></td></tr><tr><td FIXWIDTH=15></td><td>");
			
			if (command.equals("bbs_default"))
			{
				L2PcInstance[] players = L2World.getInstance().getAllPlayers();
				htmlCode.append("<table border=0>");
				
				int t = GameTimeController.getInstance().getGameTime();
				int h = t/60;
				int m = t%60;
				SimpleDateFormat format = new SimpleDateFormat("h:mm a");
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, h);
				cal.set(Calendar.MINUTE, m);
				
				htmlCode.append("<tr><td>Game Time: "+format.format(cal.getTime())+"</td></tr>");
				htmlCode.append("<tr><td>XP Rate: "+RatesController.getInstance().getExpRate()+"</td></tr>");
				htmlCode.append("<tr><td>SP Rate: "+RatesController.getInstance().getSpRate()+"</td></tr>");
				htmlCode.append("<tr><td>Adena Rate: "+RatesController.getInstance().getAdenaRate()+"</td></tr>");
				htmlCode.append("<tr><td>Drop Rate: "+RatesController.getInstance().getDropRate()+"</td></tr>");
				htmlCode.append("<tr><td><img src=\"sek.cbui355\" width=610 height=1><br></td></tr>");
				
				htmlCode.append("<tr><td>"+players.length+" Player(s) Online:</td></tr><tr><td><table border=0><tr>");
				int n = 1;
				for (int i = 0; i < players.length; i++)
				{
					htmlCode.append("<td><a action=\"bypass bbs_player_info "+players[i].getName()+"\">"+players[i].getName()+"</a></td><td FIXWIDTH=15></td>");
					if (n == 5)
					{
						htmlCode.append("</tr><tr>");
						n = 0;
					}
					n++;
				}
				htmlCode.append("</tr></table></td></tr></table>");
			}
			else if (command.equals("bbs_top"))
			{
				htmlCode.append("<center>"+command+"</center>");
			}
			else if (command.equals("bbs_up"))
			{
				htmlCode.append("<center>"+command+"</center>");
			}
			else if (command.equals("bbs_favorate"))
			{
				htmlCode.append("<center>"+command+"</center>");
			}
			else if (command.equals("bbs_add_fav"))
			{
				htmlCode.append("<center>"+command+"</center>");
			}
			else if (command.equals("bbs_region"))
			{
				htmlCode.append("<center>"+command+"</center>");
			}
			else if (command.equals("bbs_clan"))
			{
				L2Clan clan = activeChar.getClan();
				htmlCode.append("<table border=0><tr><td>"+clan.getName()+" (Level "+clan.getLevel()+"):</td></tr><tr><td><table border=0>");
				String title = "";
				if (!clan.getClanMember(clan.getLeaderName()).getTitle().equals(""))
				{
					title = "<td>["+clan.getClanMember(clan.getLeaderName()).getTitle()+"]</td><td FIXWIDTH=5></td>";
				}
				String name = clan.getLeaderName();
				if (clan.getClanMember(clan.getLeaderName()).isOnline())
				{
					name = "<a action=\"bypass bbs_player_info "+clan.getLeaderName()+"\">"+clan.getLeaderName()+"</a>";
				}
				htmlCode.append("<tr>"+title+"<td>"+name+"</td></tr>");
				htmlCode.append("<tr><td><br></td></tr>");
				
				L2ClanMember[] members = clan.getMembers();
				for (int i = 0; i < members.length; i++)
				{
					if (members[i].getName() != clan.getLeaderName())
					{
						title = "";
						if (!members[i].getTitle().equals(""))
						{
							title = "<td>["+members[i].getTitle()+"]</td><td FIXWIDTH=5></td>";
						}
						name = members[i].getName();
						if (members[i].isOnline())
						{
							name = "<a action=\"bypass bbs_player_info "+members[i].getName()+"\">"+members[i].getName()+"</a>";
						}
						htmlCode.append("<tr>"+title+"<td>"+name+"</td></tr>");
					}
				}
				htmlCode.append("</table></td></tr></table>");
			}
			
			//EXTRAS
			else if (command.startsWith("bbs_player_info"))
			{
				String name = command.substring(16);
				L2PcInstance player = L2World.getInstance().getPlayer(name);
				
				String sex = "Male";
				if (player.getSex() == 1)
				{
					sex = "Female";
				}
				htmlCode.append("<table border=0><tr><td>"+player.getName()+" ("+sex+" "+CharTemplateTable.getInstance().getTemplate(player.getClassId()).getClassName()+"):</td></tr>");
				htmlCode.append("<tr><td>Level: "+player.getLevel()+"</td></tr>");
				htmlCode.append("<tr><td><br></td></tr>");
				
				int nextLevelExp = 0;
				int nextLevelExpNeeded = 0;
				if (player.getLevel() < 60)
				{
					nextLevelExp = ExperienceTable.getInstance().getExp(player.getLevel() + 1);
					nextLevelExpNeeded = ExperienceTable.getInstance().getExp(player.getLevel()+1)-player.getExp();
				}
				
				htmlCode.append("<tr><td>Experience: "+player.getExp()+"/"+nextLevelExp+"</td></tr>");
				htmlCode.append("<tr><td>Experience needed for level up: "+nextLevelExpNeeded+"</td></tr>");
				htmlCode.append("<tr><td><br></td></tr>");
				
				int uptime = (int)player.getUptime()/1000;
				int h = uptime/3600;
				int m = (uptime-(h*3600))/60;
				int s = ((uptime-(h*3600))-(m*60));
				
				htmlCode.append("<tr><td>Uptime: "+h+"h "+m+"m "+s+"s</td></tr>");
				htmlCode.append("<tr><td><br></td></tr>");
				
				if (player.getClan() != null)
				{
					htmlCode.append("<tr><td>Clan: "+player.getClan().getName()+"</td></tr>");
					htmlCode.append("<tr><td><br></td></tr>");
				}
				
				htmlCode.append("<tr><td><multiedit var=\"pm\" width=240 height=40><button value=\"Send PM\" action=\"bypass bbs_player_pm "+player.getName()+" $pm\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr><tr><td><br><button value=\"Back\" action=\"bypass bbs_default\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>");
			}
			
			else if (command.startsWith("bbs_player_pm"))
			{
				try
				{
					String val = command.substring(14);
					StringTokenizer st = new StringTokenizer(val);
					String name = st.nextToken();
					String message = val.substring(name.length()+1);
					L2PcInstance reciever = L2World.getInstance().getPlayer(name);
					CreatureSay cs = new CreatureSay(activeChar.getObjectId(), Say2.TELL, activeChar.getName(), message);
					reciever.sendPacket(cs);
					activeChar.sendPacket(cs);
					htmlCode.append("Message Sent<br><button value=\"Back\" action=\"bypass bbs_player_info "+reciever.getName()+"\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
				}
				catch (StringIndexOutOfBoundsException e)
				{
					// ignore
				}
			}
			
			htmlCode.append("</td></tr></table></body></html>");
			
			ShowBoard sb = new ShowBoard(activeChar,htmlCode.toString());
			activeChar.sendPacket(sb);
		}
	}
}
