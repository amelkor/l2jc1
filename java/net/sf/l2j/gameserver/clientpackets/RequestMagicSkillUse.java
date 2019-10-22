/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/RequestMagicSkillUse.java,v 1.7 2004/09/15 23:54:25 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/15 23:54:25 $
 * $Revision: 1.7 $
 * $Log: RequestMagicSkillUse.java,v $
 * Revision 1.7  2004/09/15 23:54:25  l2chef
 * magic use control is moved to pcinstance (Deth)
 *
 * Revision 1.6  2004/08/18 00:54:57  l2chef
 * mana and hp requirement is checked on skill use
 *
 * Revision 1.5  2004/08/17 00:48:18  l2chef
 * new skill handlers contributed by Angel Kira.
 * some adjustments done.
 *
 * Revision 1.4  2004/07/13 23:02:11  l2chef
 * empty blocks commented
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

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/15 23:54:25 $
 */
public class RequestMagicSkillUse extends ClientBasePacket
{
	private static final String _C__2F_REQUESTMAGICSKILLUSE = "[C] 2F RequestMagicSkillUse";
	private static Logger _log = Logger.getLogger(RequestMagicSkillUse.class.getName());

	/**
	 * packet type id 0x2f
	 * format:		cddc
	 * @param rawPacket
	 */
	public RequestMagicSkillUse(byte[] rawPacket, ClientThread client) throws IOException
	{
		super(rawPacket);
		int magicId  = readD();
		int data2  = readD();
		int data3 =  readC();
		
		L2PcInstance activeChar = client.getActiveChar();
		int level = activeChar.getSkillLevel(magicId);
		
		L2Skill skill = SkillTable.getInstance().getInfo(magicId, level);
		if (skill != null)
		{
			//_log.fine("	skill:"+skill.getName() + " level:"+skill.getLevel() + " passive:"+skill.isPassive());
			//_log.fine("	range:"+skill.getCastRange()+" targettype:"+skill.getTargetType()+" optype:"+skill.getOperateType()+" power:"+skill.getPower());
			//_log.fine("	reusedelay:"+skill.getReuseDelay()+" hittime:"+skill.getHitTime());
			//_log.fine("	currentState:"+activeChar.getCurrentState());	//for debug
			
			activeChar.stopMove();
			activeChar.useMagic(skill);
		}
		else
		{
			_log.warning("No skill found!!");
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__2F_REQUESTMAGICSKILLUSE;
	}
}
