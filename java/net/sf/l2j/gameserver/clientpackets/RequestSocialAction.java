/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestSocialAction.java,v 1.6 2004/09/28 01:53:56 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:53:56 $
 * $Revision: 1.6 $
 * $Log: RequestSocialAction.java,v $
 * Revision 1.6  2004/09/28 01:53:56  nuocnam
 * Added javadoc header.
 *
 * Revision 1.5  2004/09/18 01:41:34  whatev66
 * added private store buy/sell
 *
 * Revision 1.4  2004/07/12 20:52:02  l2chef
 * social actions are broadcasted
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
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SocialAction;

/**
 * This class ...
 * 
 * @version $Revision: 1.6 $ $Date: 2004/09/28 01:53:56 $
 */
public class RequestSocialAction extends ClientBasePacket
{
	private static final String _C__1B_REQUESTSOCIALACTION = "[C] 1B RequestSocialAction";
	private static Logger _log = Logger.getLogger(RequestSocialAction.class.getName());
	
	// format  cd
	
	/**
	 * packet type id 0x1b
	 * format:		cd
	 * @param decrypt
	 */
	public RequestSocialAction(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int actionId  = readD();
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar.getPrivateStoreType()==0 && activeChar.getTransactionRequester()==null && activeChar.getCurrentState()==0)
		{
			_log.fine("Social Action:" + actionId);
			
			
			SocialAction atk = new SocialAction(client.getActiveChar().getObjectId(), actionId);
			
			activeChar.sendPacket(atk);
			activeChar.broadcastPacket(atk);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__1B_REQUESTSOCIALACTION;
	}
}
