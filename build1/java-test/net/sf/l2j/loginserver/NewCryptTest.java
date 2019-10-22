/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java-test/net/sf/l2j/loginserver/NewCryptTest.java,v 1.1 2004/06/28 22:17:49 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/06/28 22:17:49 $
 * $Revision: 1.1 $
 * $Log: NewCryptTest.java,v $
 * Revision 1.1  2004/06/28 22:17:49  l2chef
 * init
 *
 * Revision 1.3  2004/06/27 20:50:02  l2chef
 * better error message when data file is missing. skipping of comment lines
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
package net.sf.l2j.loginserver;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/06/28 22:17:49 $
 */
public class NewCryptTest extends TestCase
{

	/*
	 * Class under test for byte[] decrypt(byte[])
	 */
	public void testDecryptbyteArray() throws IOException
	{
		byte[] in = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b,0x0c,0x0d,0x0e,0x0f, 0x10 };
		NewCrypt nc = new NewCrypt("[;'.]94-31==-%&@!^+]\000");
		byte[] result = nc.decrypt(in);
		byte[] in2 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b,0x0c,0x0d,0x0e,0x0f, 0x10 };
		byte[] result2 = nc.decrypt(in2);
	}

	/*
	 * Class under test for byte[] crypt(byte[])
	 */
	public void testCryptbyteArray() throws IOException
	{
		NewCrypt nc = new NewCrypt("[;'.]94-31==-%&@!^+]\000");
		byte[] in = { 
				(byte)0x03,(byte)0x55,(byte)0x55,(byte)0x55,(byte)0x55,(byte)0x44,(byte)0x44,(byte)0x44,(byte)0x44,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00
				,(byte)0x00,(byte)0xea ,(byte)0x03 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x02 ,(byte)0x00 ,(byte)0x00
				,(byte)0x12 ,(byte)0xf9 ,(byte)0x12 ,(byte)0x11 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00
				 };
		byte[] result = nc.crypt(in);
	}

}
