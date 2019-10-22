/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AskJoinPledge.java,v 1.2 2004/09/28 03:01:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 03:01:08 $
 * $Revision: 1.2 $
 * $Log: AskJoinPledge.java,v $
 * Revision 1.2  2004/09/28 03:01:08  nuocnam
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
package net.sf.l2j.gameserver.serverpackets;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 03:01:08 $
 */
public class AskJoinPledge extends ServerBasePacket
{
	private static final String _S__44_ASKJOINPLEDGE = "[S] 44 AskJoinPledge";

	private int _requestorId;
	private String _pledgeName;

	public AskJoinPledge(int requestorId, String pledgeName)
	{
		_requestorId = requestorId;
		_pledgeName = pledgeName;
	}


	public byte[] getContent()
	{
		_bao.write(0x44);
		writeD(_requestorId);
		writeS(_pledgeName);

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__44_ASKJOINPLEDGE;
	}

}
