/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestRestartPoint.java,v 1.5 2004/08/04 21:13:39 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/04 21:13:39 $
 * $Revision: 1.5 $
 * $Log: RequestRestartPoint.java,v $
 * Revision 1.5  2004/08/04 21:13:39  l2chef
 * closest town calculation moved (NuocNam)
 *
 * Revision 1.4  2004/07/23 01:44:59  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 * player position was not updated in the server
 *
 * Revision 1.3  2004/07/14 22:08:54  l2chef
 * position of revived char fixed
 *
 * Revision 1.2  2004/07/13 23:02:12  l2chef
 * empty blocks commented
 *
 * Revision 1.1  2004/07/11 23:36:22  l2chef
 * added by MetalRabbit
 *
 * Revision 1.4  2004/07/04 11:12:33  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/30 21:51:27  l2chef
 * using jdk logger instead of println
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
import net.sf.l2j.gameserver.Connection;
import java.util.logging.Logger;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.gameserver.serverpackets.StopMove;
import net.sf.l2j.gameserver.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.serverpackets.Revive;
import net.sf.l2j.gameserver.MapRegionTable;


/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/04 21:13:39 $
 */
public class RequestRestartPoint extends ClientBasePacket
{
	private static final String _C__6d_REQUESTRESTARTPOINT = "[C] 6d RequestRestartPoint";
	private static Logger _log = Logger.getLogger(RequestRestartPoint.class.getName());	
			
	/**
	 * packet type id 0x6d
	 * format:		c
	 * @param decrypt
	 */
	public RequestRestartPoint(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		Connection con = client.getConnection();
		L2PcInstance activeChar = client.getActiveChar();
		String townCordsString = MapRegionTable.getInstance().getClosestTownCords(activeChar);
		String[] temp = null;
		temp = townCordsString.split("!");
	
		int townX = Integer.parseInt(temp[0]);
		int townY = Integer.parseInt(temp[1]);
		int townZ = Integer.parseInt(temp[2]);
		
			
	
		//Packets and teleport
		StopMove stopMove = new StopMove(activeChar);
		con.sendPacket(stopMove);
		ActionFailed actionFailed = new ActionFailed();
		con.sendPacket(actionFailed);
		activeChar.broadcastPacket(stopMove);
		TeleportToLocation teleport = new TeleportToLocation(activeChar,townX,townY,townZ);
		activeChar.sendPacket(teleport);
		L2World.getInstance().removeVisibleObject(activeChar);
		activeChar.removeAllKnownObjects();

		activeChar.setX(townX);
		activeChar.setY(townY);
		activeChar.setZ(townZ);

		activeChar.setCurrentHp(0.6 * activeChar.getMaxHp());
		activeChar.setCurrentMp(0.6 * activeChar.getMaxMp());
		Revive revive = new Revive(activeChar);
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			// ignore interruptions
		}
		
		L2World.getInstance().addVisibleObject(activeChar);
		
		SocialAction sa = new SocialAction(activeChar.getObjectId(), 15);
		activeChar.broadcastPacket(sa);
		activeChar.sendPacket(sa);
		activeChar.sendPacket(revive);
		activeChar.broadcastPacket(revive);
		activeChar.setTarget(activeChar);
		//MagicSkillUser msk = new MagicSkillUser(activeChar, 1016, 1,20000,0);
		//con.sendPacket(msk);
		//activeChar.broadcastPacket(msk);
	}



	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__6d_REQUESTRESTARTPOINT;
	}
}
