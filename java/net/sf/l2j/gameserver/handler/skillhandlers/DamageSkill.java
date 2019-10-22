/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/skillhandlers/DamageSkill.java,v 1.4 2004/10/19 23:49:20 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/19 23:49:20 $
 * $Revision: 1.4 $
 * $Log: DamageSkill.java,v $
 * Revision 1.4  2004/10/19 23:49:20  nuocnam
 * Target is now passed to skill handlers. No skill handler should use activeChar.getTarget() ever
 *
 * Revision 1.3  2004/09/28 02:17:03  nuocnam
 * corrected header
 *
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
package net.sf.l2j.gameserver.handler.skillhandlers;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/19 23:49:20 $
 */
public class DamageSkill implements ISkillHandler
{
	public static final int POWER_STRIKE = 3;
	public static final int WIND_STRIKE = 1177;
	public static final int FLAME_STRIKE = 1181;
	public static final int MORTAL_BLOW = 16;
	public static final int POWER_SHOT = 56;
	public static final int IRON_PUNCH = 29;
	
	// all the items ids that this handler knowns
	private static Logger _log = Logger.getLogger(DamageSkill.class.getName());
	private static int[] _skillIds = {POWER_STRIKE, WIND_STRIKE, FLAME_STRIKE, MORTAL_BLOW, POWER_SHOT, IRON_PUNCH};

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public void useSkill(L2PcInstance activeChar, L2Skill skill, L2Object target) throws IOException
	{
	    if (target instanceof L2Character)
		{
			L2Character targetChar = (L2Character)target;
			int mdef = targetChar.getMagicalDefense();
			if (mdef == 0)
			{
				mdef = 350;
			}
			
			int dmg = (int)(91*skill.getPower() * Math.sqrt((double)activeChar.getMagicalAttack())/mdef);
			SystemMessage sm = new SystemMessage(SystemMessage.YOU_DID_S1_DMG);
			sm.addNumber(dmg);
			activeChar.sendPacket(sm);
			
			targetChar.reduceCurrentHp(dmg, activeChar);
			if (targetChar.getCurrentHp() > targetChar.getMaxHp())
			{
				targetChar.setCurrentHp(targetChar.getMaxHp());
			}
		}
	}
	
	public int[] getSkillIds()
	{
		return _skillIds;
	}
}
