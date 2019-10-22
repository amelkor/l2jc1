/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/MyTargetSelected.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: MyTargetSelected.java,v $
 * Revision 1.3  2004/07/04 11:14:53  l2chef
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

/**
 * 
 * <p>
 * sample  bf 73 5d 30 49 01 00 
 * <p>
 * format dh	(objectid, color)
 * <p>
 * color 	-xx -> -9 	red<p>
 * 			-8  -> -6	light-red<p>
 * 			-5	-> -3	yellow<p>
 * 			-2	-> 2    white<p>
 * 			 3	-> 5	green<p>
 * 			 6	-> 8	light-blue<p>
 * 			 9	-> xx	blue<p>	
 * <p>
 * usually the color equals the level difference to the selected target 			
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class MyTargetSelected extends ServerBasePacket
{
	private static final String _S__BF_MYTARGETSELECTED = "[S] BF MyTargetSelected";
	private int _objectId;
	private int _color;

	/**
	 * @param int objectId of the target
	 * @param int level difference to the target. name color is calculated from that
	 */
	public MyTargetSelected(int objectId, int color)
	{
		_objectId = objectId;
		_color = color;
	}


	public byte[] getContent()
	{
		_bao.write(0xbf);
		writeD(_objectId);
		writeH(_color);

		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__BF_MYTARGETSELECTED;
	}

}
