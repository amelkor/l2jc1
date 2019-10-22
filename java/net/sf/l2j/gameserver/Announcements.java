/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/Announcements.java,v 1.4 2004/09/28 02:13:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:13:08 $
 * $Revision: 1.4 $
 * $Log: Announcements.java,v $
 * Revision 1.4  2004/09/28 02:13:08  nuocnam
 * Added javadoc header.
 *
 * Revision 1.3  2004/08/15 22:30:13  l2chef
 * uses constant from Say2
 *
 * Revision 1.2  2004/08/04 21:54:17  l2chef
 * multiline edit for announcements (Deth)
 *
 * Revision 1.1  2004/08/05 00:30:00  deth
 * changed "edit" to "multiedit"
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.clientpackets.Say2;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/09/28 02:13:08 $
 */
public class Announcements
{
	private static Logger _log = Logger.getLogger(Announcements.class.getName());
	
	private static Announcements _instance;
	private ArrayList _announcements = new ArrayList();

	public Announcements()
	{
		loadAnnouncements();
	}
	
	public static Announcements getInstance()
	{
		if (_instance == null)
		{
			_instance = new Announcements();
		}
		
		return _instance;
	}
	
	
	public void loadAnnouncements()
	{
		_announcements.clear();
		File file = new File("data/announcements.txt");
		if (file.exists())
		{
			readFromDisk(file);
		}
		else
		{
			_log.config("data/announcements.txt doesn't exist");
		}
	}
	
	public void showAnnouncements(L2PcInstance activeChar)
	{
		for (int i = 0; i < _announcements.size(); i++)
		{
			CreatureSay cs = new CreatureSay(0, Say2.ANNOUNCEMENT, activeChar.getName(), _announcements.get(i).toString());
			activeChar.sendPacket(cs);
		}
	}
	
	public void listAnnouncements(L2PcInstance activeChar)
	{		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
		StringBuffer replyMSG = new StringBuffer("<html><title>Announcements:</title>");
		replyMSG.append("<body>");
		for (int i = 0; i < _announcements.size(); i++)
		{
			replyMSG.append(_announcements.get(i).toString());
			replyMSG.append("<center><button value=\"Delete\" action=\"bypass -h admin_del_announcement " + i + "\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		}
		replyMSG.append("<br>");
		replyMSG.append("Add a new announcement:");
		replyMSG.append("<center><multiedit var=\"add_announcement\" width=240 height=40></center>");
		replyMSG.append("<center><button value=\"Add\" action=\"bypass -h admin_add_announcement $add_announcement\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Reload\" action=\"bypass -h admin_reload_announcements\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<center><button value=\"Announce\" action=\"bypass -h admin_announce_announcements\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
		replyMSG.append("<br><br>");
		replyMSG.append("<right><button value=\"Back\" action=\"bypass -h admin_show\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></right>");
		replyMSG.append("</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	public void addAnnouncement(String text)
	{
		_announcements.add(text);
		saveToDisk();
	}
	
	public void delAnnouncement(int line)
	{
		_announcements.remove(line);
		saveToDisk();
	}
	
	private void readFromDisk(File file)
	{
		LineNumberReader lnr = null;
		try
		{
			int i=0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line,"\n\r");
				if (st.hasMoreTokens())
				{	
					String announcement = st.nextToken();
					_announcements.add(announcement);
					
					i++;
				}
			}
			
			_log.config(i + " announcements added");
		}
		catch (FileNotFoundException e)
		{
			// nothing
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e2)
			{
				// nothing
			}
		}
	}
	
	private void saveToDisk()
	{
		File file = new File("data/announcements.txt");
		FileWriter save = null; 

		try
		{
			save = new FileWriter(file);
			for (int i = 0; i < _announcements.size(); i++)
			{
				save.write(_announcements.get(i).toString());
				save.write("\r\n");
			}
		}
		catch (IOException e)
		{
			_log.warning("saving the announcements file has failed: " + e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				save.close();
			}
			catch (Exception e1)
			{
				// nothing
			}
		}
	}
}