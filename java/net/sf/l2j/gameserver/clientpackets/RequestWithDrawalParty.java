/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestWithDrawalParty.java,v 1.3 2004/09/27 08:44:51 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/27 08:44:51 $
 * $Revision: 1.3 $
 * $Log: RequestWithDrawalParty.java,v $
 * Revision 1.3  2004/09/27 08:44:51  nuocnam
 * Everything party related moved to L2Party class.
 *
 * Revision 1.2  2004/08/08 16:28:50  l2chef
 * dependency on client thread removed
 * status listener deregistered on disperse
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

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2PcInstance;


/**
 * 
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/27 08:44:51 $
 */
public class RequestWithDrawalParty extends ClientBasePacket
{
	private static final String _C__2B_REQUESTWITHDRAWALPARTY = "[C] 2B RequestWithDrawalParty";
	private static Logger _log = Logger.getLogger(RequestWithDrawalParty.class.getName());
	
	public RequestWithDrawalParty(byte[] decrypt, L2PcInstance player)
	{	
		super(decrypt);
		if (player.isInParty()) player.getParty().oustPartyMember(player);		
	}
	
	public String getType()
	{
		return _C__2B_REQUESTWITHDRAWALPARTY;
	}
}
