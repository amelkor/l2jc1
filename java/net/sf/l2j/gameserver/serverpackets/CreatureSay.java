/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CreatureSay.java,v 1.4 2004/08/15 22:18:23 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/15 22:18:23 $
 * $Revision: 1.4 $
 * $Log: CreatureSay.java,v $
 * Revision 1.4  2004/08/15 22:18:23  l2chef
 * chat text is now displayed above char
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
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/08/15 22:18:23 $
 */
public class CreatureSay extends ServerBasePacket
{
	// ddSS
	private static final String _S__5D_CREATURESAY = "[S] 5D CreatureSay";
	private int _objectId;
	private int _textType;
	private String _charName;
	private String _text;
	

	/**
	 * @param _characters
	 */
	public CreatureSay(int objectId, int messageType, String charName, String text)
	{
		_objectId = objectId;
		_textType = messageType;
		_charName = charName;
		_text = text;
	}


	public byte[] getContent()
	{
		_bao.write(0x5d);
		writeD(_objectId);
		writeD(_textType);

		writeS(_charName);
		writeS(_text);
		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__5D_CREATURESAY;
	}
	
}
