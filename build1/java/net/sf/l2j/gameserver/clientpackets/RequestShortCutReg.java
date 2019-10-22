/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestShortCutReg.java,v 1.3 2004/07/04 11:12:33 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:12:33 $
 * $Revision: 1.3 $
 * $Log: RequestShortCutReg.java,v $
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

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.serverpackets.ShortCutRegister;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:12:33 $
 */
public class RequestShortCutReg extends ClientBasePacket
{
	private static final String _C__33_REQUESTSHORTCUTREG = "[C] 33 RequestShortCutReg";
	/**
	 * packet type id 0x33
	 * format:		cdddd
	 * @param rawPacket
	 */
	public RequestShortCutReg(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);

		L2PcInstance activeChar = client.getActiveChar();
		Connection con = client.getConnection();

		int type = readD();
		int slot = readD();
		int id = readD();
		int unk = readD();
		
		switch (type)
		{
			case 0x01:	// item
			case 0x03:	// action
			{
				
				con.sendPacket(new ShortCutRegister(slot,type, id, unk));
				activeChar.registerShortCut(new L2ShortCut(slot, type, id, -1, unk));
				break;
			}
			case 0x02:	// skill
			{
				int level = activeChar.getSkillLevel( id );
				if (level > 0)
				{
					con.sendPacket(new ShortCutRegister(slot,type, id, level, unk));
					activeChar.registerShortCut(new L2ShortCut(slot, type, id, level, unk));
				}
				break;
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__33_REQUESTSHORTCUTREG;
	}
}
