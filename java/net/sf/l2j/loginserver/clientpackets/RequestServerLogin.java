/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/clientpackets/RequestServerLogin.java,v 1.2 2004/06/27 08:51:44 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:44 $
 * $Revision: 1.2 $
 * $Log: RequestServerLogin.java,v $
 * Revision 1.2  2004/06/27 08:51:44  jeichhorn
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
package net.sf.l2j.loginserver.clientpackets;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:51:44 $
 */
public class RequestServerLogin
{
	private long _data1;
	private long _data2;
	private int _data3;
	
	/**
	 * @return
	 */
	public long getData1()
	{
		return _data1;
	}

	/**
	 * @return
	 */
	public long getData2()
	{
		return _data2;
	}

	/**
	 * @return
	 */
	public int getData3()
	{
		return _data3;
	}

	public RequestServerLogin(byte[] rawPacket)
	{
		_data1  = rawPacket[1] &0xff;
		_data1 |= rawPacket[2] << 8 &0xff00;
		_data1 |= rawPacket[3] << 0x10 &0xff0000;
		_data1 |= rawPacket[4] << 0x18 &0xff000000;

		_data2  = rawPacket[5] &0xff;
		_data2 |= rawPacket[6] << 8 &0xff00;
		_data2 |= rawPacket[7] << 0x10 &0xff0000;
		_data2 |= rawPacket[8] << 0x18 &0xff000000;
		
		_data3 =  rawPacket[9] &0xff;
	}
}
