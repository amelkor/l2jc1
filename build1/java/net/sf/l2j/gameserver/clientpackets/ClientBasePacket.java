/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/ClientBasePacket.java,v 1.5 2004/10/23 17:26:18 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/23 17:26:18 $
 * $Revision: 1.5 $
 * $Log: ClientBasePacket.java,v $
 * Revision 1.5  2004/10/23 17:26:18  l2chef
 * fallback for incorrect string data
 *
 * Revision 1.4  2004/08/10 00:47:54  l2chef
 * data block getter added
 *
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

import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/10/23 17:26:18 $
 */
public abstract class ClientBasePacket
{
	static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());
	
	private byte[] _decrypt;
	private int _off;
	
	public ClientBasePacket(byte[] decrypt)
	{
		_log.finest(getType());
		_decrypt = decrypt;
		_off = 1;		// skip packet type id
	}
	
	public int readD()
	{
		int result = _decrypt[_off++] &0xff;
		result |= _decrypt[_off++] << 8 &0xff00;
		result |= _decrypt[_off++] << 0x10 &0xff0000;
		result |= _decrypt[_off++] << 0x18 &0xff000000;
		return result;
	}

	public int readC()
	{
		int result = _decrypt[_off++] &0xff;
		return result;
	}

	public int readH()
	{
		int result = _decrypt[_off++] &0xff;
		result |= _decrypt[_off++] << 8 &0xff00;
		return result;
	}

	public double readF()
	{
		long result = _decrypt[_off++] &0xff;
		result |= _decrypt[_off++] << 8 &0xff00;
		result |= _decrypt[_off++] << 0x10 &0xff0000;
		result |= _decrypt[_off++] << 0x18 &0xff000000;
		result |= (long)_decrypt[_off++] << 0x20 &0xff00000000l;
		result |= (long)_decrypt[_off++] << 0x28 &0xff0000000000l;
		result |= (long)_decrypt[_off++] << 0x30 &0xff000000000000l;
		result |= (long)_decrypt[_off++] << 0x38 &0xff00000000000000l;
		return Double.longBitsToDouble(result);
	}

	public String readS()
	{
		String result = null;
		try
		{
			result = new String(_decrypt,_off,_decrypt.length-_off, "UTF-16LE");
			result = result.substring(0, result.indexOf(0x00));
		}
		catch (Exception e)
		{
			result = "";
		}
		
		_off += result.length()*2 + 2;
		return result;
	}
	
	public byte[] readB(int length)
	{
		byte[] result = new byte[length];
		System.arraycopy(_decrypt, _off, result, 0, length);
		_off += length;
		
		return result;
	}
	
	public abstract String getType();
}
