/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AuthLoginFail.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: AuthLoginFail.java,v $
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

/**
 * format  d   rev 417
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class AuthLoginFail extends ServerBasePacket
{
	private static final String _S__12_AUTHLOGINFAIL = "[S] 12 AuthLoginFail";
	public static int NO_TEXT = 0;
	public static int SYSTEM_ERROR_LOGIN_LATER = 1;
	public static int PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT = 2;
	public static int PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT2 = 3;
	public static int ACCESS_FAILED_TRY_LATER = 4;
	public static int INCORRECT_ACCOUNT_INFO_CONTACT_CUSTOMER_SUPPORT = 5;
	public static int ACCESS_FAILED_TRY_LATER2 = 6;
	public static int ACOUNT_ALREADY_IN_USE = 7;
	public static int ACCESS_FAILED_TRY_LATER3 = 8;
	public static int ACCESS_FAILED_TRY_LATER4 = 9;
	public static int ACCESS_FAILED_TRY_LATER5 = 10;
	
	private int _reason;

	/**
	 * @param _characters
	 */
	public AuthLoginFail(int reason)
	{
		_reason = reason;
	}


	public byte[] getContent()
	{
		_bao.write(0x12);
		
		writeD(_reason); 

		return _bao.toByteArray();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__12_AUTHLOGINFAIL;
	}
}
