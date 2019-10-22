/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CharTemplates.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: CharTemplates.java,v $
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

import java.util.Vector;

import net.sf.l2j.gameserver.templates.L2CharTemplate;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class CharTemplates extends ServerBasePacket
{
	// dddddddddddddddddddd
		

	
	private static final String _S__23_CHARTEMPLATES = "[S] 23 CharTemplates";
	private Vector _chars = new Vector();
	
	public void addChar(L2CharTemplate template)
	{
		_chars.add(template);
	}	

	public byte[] getContent()
	{
		_bao.write(0x23);
		writeD(_chars.size());

		for (int i=0; i< _chars.size();i++)
		{
			L2CharTemplate temp = (L2CharTemplate) _chars.get(i);
			
			writeD(temp.getRaceId());
			writeD(temp.getClassId());
			writeD(0x46);
			writeD(temp.getStr());
			writeD(0x0a);
			writeD(0x46);
			writeD(temp.getDex());
			writeD(0x0a);
			writeD(0x46);
			writeD(temp.getCon());
			writeD(0x0a);
			writeD(0x46);
			writeD(temp.getInt());
			writeD(0x0a);
			writeD(0x46);
			writeD(temp.getWit());
			writeD(0x0a);
			writeD(0x46);
			writeD(temp.getMen());
			writeD(0x0a);
		}

		return _bao.toByteArray();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__23_CHARTEMPLATES;
	}
}
