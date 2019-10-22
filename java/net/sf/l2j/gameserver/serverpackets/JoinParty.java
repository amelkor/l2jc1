/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/JoinParty.java,v 1.1 2004/07/04 11:13:58 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:13:58 $
 * $Revision: 1.1 $
 * $Log: JoinParty.java,v $
 * Revision 1.1  2004/07/04 11:13:58  l2chef
 * new party related handers
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
package net.sf.l2j.gameserver.serverpackets;

import java.util.logging.Logger;


/**
 *
 * sample
 * <p>
 * 4c
 * 01 00 00 00
 * <p>
 * 
 * format
 * cd
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/04 11:13:58 $
 */
public class JoinParty extends ServerBasePacket
{
	private static final String _S__4C_JOINPARTY = "[S] 4C JoinParty";
	private static Logger _log = Logger.getLogger(JoinParty.class.getName());

	private int _response;

	/**
	 * @param int 
	 */
	public JoinParty(int response)
	{
		_response = response;
	}


	public byte[] getContent()
	{
		_bao.write(0x4c);
		writeD(_response);

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__4C_JOINPARTY;
	}

}
