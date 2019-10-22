/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestGMCommand.java,v 1.1 2004/10/22 23:40:54 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/22 23:40:54 $
 * $Revision: 1.1 $
 * $Log: RequestGMCommand.java,v $
 * Revision 1.1  2004/10/22 23:40:54  l2chef
 * alt-g packets added
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
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.GMViewCharacterInfo;
import net.sf.l2j.gameserver.serverpackets.GMViewItemList;
import net.sf.l2j.gameserver.serverpackets.GMViewPledgeInfo;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/22 23:40:54 $
 */
public class RequestGMCommand extends ClientBasePacket
{
	private static final String _C__6E_REQUESTGMCOMMAND = "[C] 6e RequestGMCommand";
	static Logger _log = Logger.getLogger(RequestGMCommand.class.getName());
			
	/**
	 * packet type id 0x00
	 * format:	cd
	 *  
	 * @param rawPacket
	 */
	public RequestGMCommand(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		String targetName = readS();
		int command = readD();
		int unknown = readD();
		L2PcInstance player = L2World.getInstance().getPlayer(targetName);
		if (player == null)
		{
			return;
		}
		
		switch(command)
		{
			case 1: // player status
			{
				client.getActiveChar().sendPacket(new GMViewCharacterInfo(player));
				break;
			}
			case 2: // player clan
			{
				if (player.getClan() != null)
				{
					client.getActiveChar().sendPacket(new GMViewPledgeInfo(player.getClan(),player));
				}
				break;
			}
			case 3: // player skills
			{
				break;
			}
			case 4: // player quests
			{
				break;
			}
			case 5: // player inventory
			{
				client.getActiveChar().sendPacket(new GMViewItemList(player));
				break;
			}
			case 6: // player warehouse
			{
				break;
			}
				
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__6E_REQUESTGMCOMMAND;
	}
}
