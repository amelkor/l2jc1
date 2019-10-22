/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/SetupGauge.java,v 1.4 2004/08/18 00:47:45 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/18 00:47:45 $
 * $Revision: 1.4 $
 * $Log: SetupGauge.java,v $
 * Revision 1.4  2004/08/18 00:47:45  l2chef
 * added color constants
 *
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
 *
 *	sample
 *	0000: 85 00 00 00 00 f0 1a 00 00
 *
 * @version $Revision: 1.4 $ $Date: 2004/08/18 00:47:45 $
 */
public class SetupGauge extends ServerBasePacket
{
	private static final String _S__85_SETUPGAUGE = "[S] 85 SetupGauge";
	public static final int BLUE = 0; 
	public static final int RED = 1; 
	public static final int CYAN = 2; 

	private int _dat1;
	private int _time;

	public SetupGauge(int dat1, int time)
	{
		_dat1 = dat1;// color  0-blue   1-red  2-cyan  3-
		_time = time;
	}	
	
	public byte[] getContent()
	{
		writeC(0x85);
		writeD(_dat1);
		writeD(_time);
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__85_SETUPGAUGE;
	}
}
