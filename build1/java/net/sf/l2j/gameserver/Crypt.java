/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/Crypt.java,v 1.3 2004/07/13 23:13:31 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:13:31 $
 * $Revision: 1.3 $
 * $Log: Crypt.java,v $
 * Revision 1.3  2004/07/13 23:13:31  l2chef
 * removed empty constructor
 *
 * Revision 1.2  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.gameserver;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/13 23:13:31 $
 */
public class Crypt
{
	byte[] _key;
	
	public void setKey(byte[] key)
	{
		_key = new byte[key.length];
		System.arraycopy(key,0, _key,0,key.length);
	}

	public long decrypt(byte[] raw)
	{
		if (_key==null)
		{
			return 0;
		}
		
		int temp = 0;
		int j = 0;
		for (int i=0; i<raw.length; i++)
		{
			int temp2 = raw[i] &0xff;
			raw[i] = (byte)(temp2 ^ (_key[j++] &0xff) ^ temp);
			temp = temp2;
			if (j > 7)
			{
				j=0;
			}
		}
		
		long old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;
		
		old += raw.length;
		
		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
		
		return old;
	}
	
	public long encrypt(byte[] raw)
	{
		if (_key==null)
		{
			return 0;
		}
		
		int temp = 0;
		int j = 0;
		for (int i=0; i<raw.length; i++)
		{
			int temp2 = raw[i] &0xff;
			raw[i] = (byte)(temp2 ^ (_key[j++] &0xff) ^ temp);
			temp = raw[i];
			if (j > 7)
			{
				j=0;
			}
		}
		
		long old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;
		
		old += raw.length;
		
		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
		
		return old;
	}
}
