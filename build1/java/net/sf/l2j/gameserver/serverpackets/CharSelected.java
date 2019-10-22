/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CharSelected.java,v 1.4 2004/07/25 02:18:29 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/25 02:18:29 $
 * $Revision: 1.4 $
 * $Log: CharSelected.java,v $
 * Revision 1.4  2004/07/25 02:18:29  l2chef
 * correct ingame time is managed (Deth/L2Chef)
 *
 * Revision 1.3  2004/07/04 11:14:52  l2chef
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
package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/07/25 02:18:29 $
 */
public class CharSelected extends ServerBasePacket
{
	//   SdSddddddddddffddddddddddddddddddddddddddddddddddddddddd d
	private static final String _S__21_CHARSELECTED = "[S] 21 CharSelected";
	private L2PcInstance _cha;
	private int _sessionId;

	/**
	 * @param _characters
	 */
	public CharSelected(L2PcInstance cha, int sessionId)
	{
		_cha = cha;
		_sessionId = sessionId;
	}


	public byte[] getContent()
	{
		_bao.write(0x21);
		
		writeS(_cha.getName());
		writeD(_cha.getCharId()); // ??
		writeS(_cha.getTitle());
		writeD(_sessionId);
		writeD(_cha.getClanId());
		writeD(0x00);  //??
		writeD(_cha.getSex());
		writeD(_cha.getRace());
		writeD(_cha.getClassId());
		writeD(0x01); // active ??
		writeD(_cha.getX());	
		writeD(_cha.getY());	
		writeD(_cha.getZ());	

		writeF(_cha.getCurrentHp());  
		writeF(_cha.getCurrentMp());  
		writeD(_cha.getSp());
		writeD(_cha.getExp());
		writeD(_cha.getLevel());
		writeD(0x0);	//?
		writeD(0x0);	//?
		writeD(_cha.getInt()); 
		writeD(_cha.getStr()); 
		writeD(_cha.getCon()); 
		writeD(_cha.getMen()); 
		writeD(_cha.getDex()); 
		writeD(_cha.getWit()); 
		for (int i=0; i<30; i++)
		{
			writeD(0x00);
		}

		// extra info
		writeD(GameTimeController.getInstance().getGameTime());	// in-game time 

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__21_CHARSELECTED;
	}

}
