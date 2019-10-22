/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/MagicSkillUser.java,v 1.4 2004/09/15 23:55:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/15 23:55:52 $
 * $Revision: 1.4 $
 * $Log: MagicSkillUser.java,v $
 * Revision 1.4  2004/09/15 23:55:52  l2chef
 * spell target can be independent of player target (Deth)
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

import net.sf.l2j.gameserver.model.L2Character;

/**
 * 
 * sample
 * 
 * 0000: 5a  d8 a8 10 48  d8 a8 10 48  10 04 00 00  01 00 00    Z...H...H.......
 * 0010: 00  f0 1a 00 00  68 28 00 00                         .....h(..
 *
 * format   dddddd dddh (h)
 * 
 * @version $Revision: 1.4 $ $Date: 2004/09/15 23:55:52 $
 */
public class MagicSkillUser extends ServerBasePacket
{
	private static final String _S__5A_MAGICSKILLUSER = "[S] 5A MagicSkillUser";
	private L2Character _cha;
	private int _targetId;
	private int _skillId;
	private int _skillLevel;
	private int _hitTime;
	private int _reuseDelay;
	
	public MagicSkillUser(L2Character cha, L2Character target, int skillId, int skillLevel, int hitTime, int reuseDelay)
	{
		_cha = cha;
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = reuseDelay; 
	}
	
	public MagicSkillUser(L2Character cha, int skillId, int skillLevel, int hitTime, int reuseDelay)
	{
		_cha = cha;
		_targetId = cha.getTargetId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = reuseDelay; 
	}
	
	public byte[] getContent()
	{
		_bao.write(0x5a);
		writeD(_cha.getObjectId());
		writeD(_targetId);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_hitTime);
		writeD(_reuseDelay);
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());
		writeH(0x00);
		
		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__5A_MAGICSKILLUSER;
	}

}

