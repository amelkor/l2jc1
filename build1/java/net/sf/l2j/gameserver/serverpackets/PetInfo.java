/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/PetInfo.java,v 1.5 2004/08/15 22:34:08 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/15 22:34:08 $
 * $Revision: 1.5 $
 * $Log: PetInfo.java,v $
 * Revision 1.5  2004/08/15 22:34:08  l2chef
 * using effective speed
 *
 * Revision 1.4  2004/08/15 03:39:39  l2chef
 * swimspeed set
 *
 * Revision 1.3  2004/08/14 22:40:13  l2chef
 * unknown methods renamed
 *
 * Revision 1.2  2004/08/13 00:01:04  l2chef
 * owner lookup removed (L2Chef)
 *
 * Revision 1.1  2004/07/25 22:57:21  l2chef
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

import java.util.logging.Logger;


import net.sf.l2j.gameserver.model.L2PetInstance;
/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/15 22:34:08 $
 */
public class PetInfo extends ServerBasePacket
{
	
	
	private static Logger _log = Logger.getLogger(PetInfo.class.getName());
	
	private static final String _S__CA_PETINFO = "[S] CA PetInfo";
	private L2PetInstance _cha;

	/**
	 * @param _characters
	 */
	public PetInfo(L2PetInstance cha)
	{
		_cha = cha;
		
	}


	public byte[] getContent()
	{
		writeC(0xca);
		writeD(_cha.getPetId()); 
		writeD(_cha.getObjectId());
		writeD(_cha.getNpcId()+1000000);  
		writeD(0); 
		
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());
		writeD(_cha.getHeading());
		writeD(0x00);
		writeD(_cha.getMagicalSpeed());	
		writeD(_cha.getPhysicalSpeed());
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
		writeD(0); // right hand weapon
		writeD(0);
		writeD(0); // left hand weapon
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
		
		if(_cha.getOwner()== null)
		{
			writeC(2);  // invisible ?? 0=false  1=true   2=summoned (only works if model has a summon animation)
		}
		else
		{
			writeC(1);
		}
		writeS(_cha.getName());
		writeS(_cha.getTitle());
		writeD(1);
		writeD(0);	//0 = white,2= purpleblink, if its greater then karma = purple 
		writeD(_cha.getKarma());  // hmm karma ??
		writeD(_cha.getCurrentFed()); // how fed it is
		writeD(_cha.getMaxFed()); //max fed it can be
		writeD((int)_cha.getCurrentHp());//current hp
		writeD((int)_cha.getMaxHp());// max hp
		writeD((int)_cha.getCurrentMp());//current mp
		writeD((int)_cha.getMaxMp());//max mp
		writeD(_cha.getSp()); //sp
		writeD(_cha.getLevel());// lvl 
		writeD(_cha.getExp()); 
		writeD(0);// 0%  absolute value	
		writeD(200000);// 100% absoulte value
		writeD(_cha.getInventory().getTotalWeight());//wieght
		writeD(200000);//max wieght it can carry
		writeD(_cha.getPhysicalAttack());//patk
		writeD(_cha.getPhysicalDefense());//pdef
		writeD(_cha.getMagicalAttack());//matk
		writeD(_cha.getMagicalDefense());//mdef
		writeD(_cha.getAccuracy());//accuracy
		writeD(_cha.getEvasionRate());//evasion
		writeD(_cha.getCriticalHit());//critical
		writeD((int)_cha.getEffectiveSpeed());//speed
		writeD(80);//atkspeed
		writeD(_cha.getMagicalSpeed());//casting speed
		
		return getBytes();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__CA_PETINFO;
	}

}
