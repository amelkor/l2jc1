/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/ValidatePosition.java,v 1.13 2004/10/05 20:02:47 whatev66 Exp $
 *
 * $Author: whatev66 $
 * $Date: 2004/10/05 20:02:47 $
 * $Revision: 1.13 $
 * $Log: ValidatePosition.java,v $
 * Revision 1.13  2004/10/05 20:02:47  whatev66
 * got an null exception from validate when i restart because my player had been set to null so i added that condition.
 *
 * Revision 1.12  2004/10/05 19:38:26  whatev66
 * players only do delete add visible/known every timer count
 *
 * Revision 1.11  2004/10/03 16:58:03  whatev66
 * no more position listener
 *
 * Revision 1.10  2004/08/02 22:32:56  l2chef
 * other objects did not add player to their known list
 *
 * Revision 1.9  2004/08/02 00:19:16  l2chef
 * removed unneccessary spawn code
 *
 * Revision 1.8  2004/07/30 10:04:32  l2chef
 * z coord is now updated again
 *
 * Revision 1.7  2004/07/25 22:57:48  l2chef
 * pet system started (whatev)
 *
 * Revision 1.6  2004/07/23 01:44:25  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.5  2004/07/19 02:04:50  l2chef
 * mob following improved
 *
 * Revision 1.4  2004/07/17 23:09:08  l2chef
 * client is not trusted any more on updating the position. view radius was increased to 3000
 *
 * Revision 1.3  2004/07/04 11:11:33  l2chef
 * pickup item was handled here and in PcCharacter, that caused duping... one pickup is enough
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2MonsterInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2World;
/**
 * This class ...
 * 
 * @version $Revision: 1.13 $ $Date: 2004/10/05 20:02:47 $
 */
public class ValidatePosition extends ClientBasePacket
{
	private static Logger _log = Logger.getLogger(ValidatePosition.class.getName());
	private static final String _C__48_VALIDATEPOSITION = "[C] 48 ValidatePosition";
	/**
	 * packet type id 0x48
	 * format:		cddddd
	 * @param decrypt
	 */
	public ValidatePosition(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int x  = readD();
		int y  = readD();
		int z  = readD();
		int heading  = readD();
		int data  = readD();
		
		
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar != null)
		{
			Connection con = client.getConnection();
			
			int realX = activeChar.getX();		
			int realY = activeChar.getY();		
			int realZ = activeChar.getZ();		
			int realHeading = activeChar.getHeading();
			
			//TODO we have to trust the client on the z-coord right now, so just copy the value we got back
			activeChar.setZ(z);
			
			//		_log.fine("client pos: "+ x + " "+y+ " "+z +" head "+heading);
			//		_log.fine("server pos: "+ realX + " "+realY+ " "+realZ +" head "+realHeading);
			
			// check for objects that are now out of range
			activeChar.updateKnownCounter += 1;
			if (activeChar.updateKnownCounter >3)
			{
				int delete = 0;
				List known = activeChar.getKnownObjects();
				ArrayList toBeDeleted = new ArrayList();
				
				for (int i = 0; i < known.size(); i++)
				{
					L2Object obj = (L2Object) known.get(i);
					if (distance(activeChar, obj) > 4000*4000)
					{
						toBeDeleted.add(obj);
						delete++;
					}
				}
				
				if (delete >0)
				{
					for (int i = 0; i < toBeDeleted.size(); i++)
					{
						activeChar.removeKnownObject((L2Object) toBeDeleted.get(i));
						if (toBeDeleted.get(i) instanceof L2MonsterInstance)
						{
							((L2MonsterInstance)toBeDeleted.get(i)).removeKnownObject(activeChar);
						}
						else
						{
							((L2Object)toBeDeleted.get(i)).removeKnownObject(activeChar);
						}
						
					}
					_log.fine("deleted " +delete+" objects");
				}
				
				
				// check for new objects that are now in range
				int newObjects = 0;
				L2Object[] visible = L2World.getInstance().getVisibleObjects(activeChar, 3000);
				for (int i = 0; i < visible.length; i++)
				{
					if (! activeChar.knownsObject(visible[i]))
					{
						activeChar.addKnownObject(visible[i]);
						visible[i].addKnownObject(activeChar);
						newObjects++;
					}
				}
				
				if (newObjects >0)
				{
					_log.fine("added " + newObjects + " new objects");
				}
				activeChar.updateKnownCounter = 0;	
			}
		}
	}
	
	
	private long distance(L2Object a, L2Object b)
	{
		long dX = a.getX() - b.getX();
		long dY = a.getY() - b.getY();
		return ( dX*dX + dY*dY );
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__48_VALIDATEPOSITION;
	}
	
}
