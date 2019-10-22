/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestPetGetItem.java,v 1.2 2004/09/28 01:50:14 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 01:50:14 $
 * $Revision: 1.2 $
 * $Log: RequestPetGetItem.java,v $
 * Revision 1.2  2004/09/28 01:50:14  nuocnam
 * Added header and copyright notice.
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

import net.sf.l2j.gameserver.model.L2World;
import java.io.IOException;
import java.util.logging.Logger;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.ClientThread;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 01:50:14 $
 */
public class RequestPetGetItem extends ClientBasePacket
{

	private static Logger _log = Logger.getLogger(RequestPetGetItem.class.getName());
	private static final String _C__8f_REQUESTPETGETITEM= "[C] 8F RequestPetGetItem";
	
	public RequestPetGetItem(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int objectId = readD();
		L2World world = L2World.getInstance();
		L2ItemInstance item = (L2ItemInstance)world.findObject(objectId);
		client.getActiveChar().getPet().setTarget(item);
		client.getActiveChar().getPet().setCurrentState(L2Character.STATE_PICKUP_ITEM);
		client.getActiveChar().getPet().moveTo(item.getX(),item.getY(),item.getZ(),0);
		
		
	
	}

	public String getType()
	{
		return _C__8f_REQUESTPETGETITEM;
	}

}
