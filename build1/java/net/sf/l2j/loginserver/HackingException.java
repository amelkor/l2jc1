/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/HackingException.java,v 1.1 2004/09/26 00:17:58 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/26 00:17:58 $
 * $Revision: 1.1 $
 * $Log: HackingException.java,v $
 * Revision 1.1  2004/09/26 00:17:58  l2chef
 * hacking protection added. IP banning added
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/09/26 00:17:58 $
 */

public class HackingException extends Exception
{
	String _ip;
	
	public HackingException(String ip)
	{
		_ip = ip;
	}

	/**
	 * @return
	 */
	public String getIP()
	{
		return _ip;
	}

}
