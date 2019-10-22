/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/NpcInfo.java,v 1.7 2004/08/15 03:39:29 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/15 03:39:29 $
 * $Revision: 1.7 $
 * $Log: NpcInfo.java,v $
 * Revision 1.7  2004/08/15 03:39:29  l2chef
 * swimspeed set
 *
 * Revision 1.6  2004/08/14 22:32:12  l2chef
 * unknown methods renamed
 *
 * Revision 1.5  2004/08/13 00:00:26  l2chef
 * owner lookup removed (L2Chef)
 *
 * Revision 1.4  2004/07/25 22:57:21  l2chef
 * pet system started (whatev)
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

import net.sf.l2j.gameserver.model.L2NpcInstance;
import net.sf.l2j.gameserver.model.L2PetInstance;
/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/08/15 03:39:29 $
 */
public class NpcInfo extends ServerBasePacket
{
	//   ddddddddddddddddddffffdddcccccSSddd

	private static final String _S__22_NPCINFO = "[S] 22 NpcInfo";
	private L2NpcInstance _cha;
	private L2PetInstance _chaPet;
	/**
	 * @param _characters
	 */
	public NpcInfo(L2NpcInstance cha)
	{
		_cha = cha;
	}
	public NpcInfo(L2PetInstance cha)
	{
		_chaPet = cha;
	}
	
	public byte[] getContent()
	{
		if (_chaPet == null)
		{
			writeC(0x22);
			writeD(_cha.getObjectId());
			writeD(_cha.getNpcTemplate().getNpcId()+1000000);  // npctype id
			if (_cha.isAttackable())
			{
				writeD(1); // can be attacked
			}
			else
			{
				writeD(0); 
			}
			
			writeD(_cha.getX());
			writeD(_cha.getY());
			writeD(_cha.getZ());
			writeD(_cha.getHeading());
			writeD(0x00);
			writeD(_cha.getMagicalSpeed());//_cha.getMagicalSpeed());	
			writeD(_cha.getPhysicalSpeed());//0x02);
			writeD(_cha.getRunSpeed());	
			writeD(_cha.getWalkSpeed());
			writeD(50);	//swimspeed
			writeD(50);	//swimspeed
			writeD(_cha.getFloatingRunSpeed());
			writeD(_cha.getFloatingWalkSpeed());
			writeD(_cha.getFlyingRunSpeed());
			writeD(_cha.getFlyingWalkSpeed());
			
			writeF(_cha.getMovementMultiplier());
			writeF(_cha.getAttackSpeedMultiplier());
			writeF(_cha.getCollisionRadius());
			writeF(_cha.getCollisionHeight());
			writeD(_cha.getRightHandItem()); // right hand weapon
			writeD(0);
			writeD(_cha.getLeftHandItem()); // left hand weapon
			writeC(1);	// name above char 1=true ... ??
			if (_cha.isRunning())
			{
				writeC(1);	// running=1  
			}
			else
			{
				writeC(0);	// walking=0 
			}
			
			if (_cha.isInCombat())
			{
				writeC(1);	// attacking 1=true
			}
			else
			{
				writeC(0);
			}
			
			if (_cha.isDead())
			{
				writeC(1);  // dead 1=true
			}
			else
			{
				writeC(0);  // dead 1=true
			}
			
			writeC(0);  // invisible ?? 0=false  1=true   2=summoned (only works if model has a summon animation)
			
			writeS(_cha.getName());
			writeS(_cha.getTitle());
			writeD(0);
			writeD(0);
			writeD(0000);  // hmm karma ??
		}
		else
		{
			writeC(0x22);
			writeD(_chaPet.getObjectId());
			writeD(_chaPet.getNpcId()+1000000);  // npctype id
			if (_chaPet.getKarma() > 0)
			{
				writeD(1); // can be attacked
			}
			else
			{
				writeD(0); 
			}
			
			writeD(_chaPet.getX());
			writeD(_chaPet.getY());
			writeD(_chaPet.getZ());
			writeD(_chaPet.getHeading());
			writeD(0x00);
			writeD(_chaPet.getMagicalSpeed());//_cha.getMagicalSpeed());	
			writeD(_chaPet.getPhysicalSpeed());//0x02);
			writeD(_chaPet.getRunSpeed());	
			writeD(_chaPet.getWalkSpeed());
			writeD(0x00);
			writeD(50);
			writeD(_chaPet.getFloatingRunSpeed());
			writeD(_chaPet.getFloatingWalkSpeed());
			writeD(_chaPet.getFlyingRunSpeed());
			writeD(_chaPet.getFlyingWalkSpeed());
			
			writeF(_chaPet.getMovementMultiplier());
			writeF(_chaPet.getAttackSpeedMultiplier());
			writeF(_chaPet.getCollisionRadius());
			writeF(_chaPet.getCollisionHeight());
			writeD(0x00); // right hand weapon
			writeD(0);
			writeD(0x00); // left hand weapon
			writeC(1);	// name above char 1=true ... ??
			if (_chaPet.isRunning())
			{
				writeC(1);	// running=1  
			}
			else
			{
				writeC(0);	// walking=0 
			}
			
			if (_chaPet.isInCombat())
			{
				writeC(1);	// attacking 1=true
			}
			else
			{
				writeC(0);
			}
			
			if (_chaPet.isDead())
			{
				writeC(1);  // dead 1=true
			}
			else
			{
				writeC(0);  // dead 1=true
			}
			
			if(_chaPet.getOwner() == null)
			{
				writeC(2);  // invisible ?? 0=false  1=true   2=summoned (only works if model has a summon animation)
			}
			else
			{
				writeC(1);
			}
			writeS(_chaPet.getName());
			writeS(_chaPet.getTitle());
			writeD(0);
			writeD(0);
			writeD(0000);  // hmm karma ??
		}
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__22_NPCINFO;
	}
}
