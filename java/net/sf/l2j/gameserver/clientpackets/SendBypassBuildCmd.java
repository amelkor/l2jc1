/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/SendBypassBuildCmd.java,v 1.1 2004/10/11 17:32:45 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:32:45 $
 * $Revision: 1.1 $
 * $Log: SendBypassBuildCmd.java,v $
 * Revision 1.1  2004/10/11 17:32:45  nuocnam
 * New class that handles all GM commands triggered by //command
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

import net.sf.l2j.gameserver.AdminCommands;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.GmListTable;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class handles all GM commands triggered by //command
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/11 17:32:45 $
 */
public class SendBypassBuildCmd extends ClientBasePacket
{
	private static final String _C__5B_SENDBYPASSBUILDCMD = "[C] 5b SendBypassBuildCmd";
	public final static int GM_MESSAGE = 9;
	public final static int ANNOUNCEMENT = 10;

	public SendBypassBuildCmd(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		L2PcInstance activeChar = client.getActiveChar();
		String command = readS();
		
		if (client.getAccessLevel() >= 100)
		{
			if (command.equals("admin"))
			{
			  AdminCommands.getInstance().showMainPage(client);
			  
			} else if (command.startsWith("announce"))
			{
				try
				{
					String text = command.substring(9);
					CreatureSay cs = new CreatureSay(0, ANNOUNCEMENT, activeChar.getName(), text);
					L2PcInstance[] players = L2World.getInstance().getAllPlayers();
					for (int i = 0; i < players.length; i++)
					{
						players[i].sendPacket(cs);
					}
				}
				catch (StringIndexOutOfBoundsException e)
				{
					// empty message.. ignore
				}
				
			} else if (command.startsWith("gmchat"))
			{
				try
				{
					String text = command.substring(7);
					CreatureSay cs = new CreatureSay(0, GM_MESSAGE, activeChar.getName(), text);
					GmListTable.getInstance().broadcastToGMs(cs);
				}
				catch (StringIndexOutOfBoundsException e)
				{
					// empty message.. ignore
				}
				
			} else if (command.startsWith("invul"))
			{
				if (activeChar.isInvul())
				{
	            	activeChar.setIsInvul(false);
	            	String text = "Your status is set back to mortal.";
	            	SystemMessage sm = new SystemMessage(614);
	            	sm.addString(text);
	            	activeChar.sendPacket(sm);                  
				} else
				{
					activeChar.setIsInvul(true);
					String text = "You are now Invulnerable";
					SystemMessage sm = new SystemMessage(614);
					sm.addString(text);
					activeChar.sendPacket(sm);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__5B_SENDBYPASSBUILDCMD;
	}
}