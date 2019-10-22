/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/NewCrypt.java,v 1.3 2004/06/27 08:51:43 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:51:43 $
 * $Revision: 1.3 $
 * $Log: NewCrypt.java,v $
 * Revision 1.3  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.loginserver;

import java.io.IOException;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/06/27 08:51:43 $
 */
public class NewCrypt
{
	BlowfishEngine _crypt;
	BlowfishEngine _decrypt;
	
	public NewCrypt(String key)
	{
		byte[] keybytes = key.getBytes();
		_crypt = new BlowfishEngine();
		_crypt.init(true, keybytes);
		_decrypt = new BlowfishEngine();
		_decrypt.init(false, keybytes);
	}
	
	public boolean checksum(byte[] raw)
	{
		long chksum = 0;
		int count = raw.length-8;
		int i =0;
		for (i=0; i<count; i+=4)
		{
			long ecx = raw[i] &0xff;
			ecx |= raw[i+1] << 8 &0xff00;
			ecx |= raw[i+2] << 0x10 &0xff0000;
			ecx |= raw[i+3] << 0x18 &0xff000000;
			
			chksum ^= ecx;
		}

		long ecx = raw[i] &0xff;
		ecx |= raw[i+1] << 8 &0xff00;
		ecx |= raw[i+2] << 0x10 &0xff0000;
		ecx |= raw[i+3] << 0x18 &0xff000000;

		raw[i] = (byte) (chksum &0xff);
		raw[i+1] = (byte) (chksum >>0x08 &0xff);
		raw[i+2] = (byte) (chksum >>0x10 &0xff);
		raw[i+3] = (byte) (chksum >>0x18 &0xff);

		return ecx == chksum;	
	}
	

	public byte[] decrypt(byte[] raw) throws IOException
	{
		byte[] result = new byte[raw.length];
		int count = raw.length /8;

		for (int i=0; i<count;i++)
		{
			_decrypt.processBlock(raw,i*8,result,i*8);
		}

		return result;
	}
	
	public byte[] crypt(byte[] raw) throws IOException
	{
		int count = raw.length /8;
		byte[] result = new byte[raw.length];

		for (int i=0; i<count;i++)
		{
			_crypt.processBlock(raw,i*8,result,i*8);
		}
		
		return result;
	}
}
