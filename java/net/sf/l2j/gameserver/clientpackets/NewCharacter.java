/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/NewCharacter.java,v 1.3 2004/07/04 11:12:33 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:12:33 $
 * $Revision: 1.3 $
 * $Log: NewCharacter.java,v $
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

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.CharTemplateTable;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.ClassId;
import net.sf.l2j.gameserver.serverpackets.CharTemplates;
import net.sf.l2j.gameserver.templates.L2CharTemplate;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:12:33 $
 */
public class NewCharacter extends ClientBasePacket
{
	private static final String _C__0E_NEWCHARACTER = "[C] 0E NewCharacter";
	private static Logger _log = Logger.getLogger(NewCharacter.class.getName());

	/**
	 * packet type id 0x0e
	 * format:		c
	 * @param rawPacket
	 */
	public NewCharacter(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		
		_log.fine("CreateNewChar");
		// packet contains no data so just create answer

		
		CharTemplates ct = new CharTemplates();
		
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(0);
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.fighter);	// human fighter
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.mage);	// human mage
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.elvenFighter);	// elf fighter
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.elvenMage);	// elf mage
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.darkFighter);	// dark elf fighter
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.darkMage);	// dark elf mage
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.orcFighter);	// orc fighter
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.orcMage);	// orc mage
		ct.addChar(template);

		template = CharTemplateTable.getInstance().getTemplate(ClassId.dwarvenFighter);	// dwarf fighter
		ct.addChar(template);

		client.getConnection().sendPacket(ct);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__0E_NEWCHARACTER;
	}
}
