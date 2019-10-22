/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/serverpackets/LoginFail.java,v 1.3 2004/07/19 23:13:59 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/19 23:13:59 $
 * $Revision: 1.3 $
 * $Log: LoginFail.java,v $
 * Revision 1.3  2004/07/19 23:13:59  l2chef
 * accounts can be banned if accesslevel is set to negative value (Deth)
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
package net.sf.l2j.loginserver.serverpackets;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/19 23:13:59 $
 */
public class LoginFail extends ServerBasePacket
{
	public static int REASON_ACCOUNT_BANNED = 0x09;
	public static int REASON_ACCOUNT_IN_USE = 0x07;
	public static int REASON_ACCESS_FAILED = 0x04;
	public static int REASON_USER_OR_PASS_WRONG = 0x03;
	public static int REASON_PASS_WRONG = 0x02;
	public static int REASON_SYSTEM_ERROR = 0x01;
	
	public LoginFail(int reason) 
	{
		writeC(0x01);
		writeD(reason);	
	}
	
	public byte[] getContent()
	{
		return getBytes();
	}
}
