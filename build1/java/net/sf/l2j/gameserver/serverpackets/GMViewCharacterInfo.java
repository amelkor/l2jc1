/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/GMViewCharacterInfo.java,v 1.1 2004/10/22 23:41:06 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/22 23:41:06 $
 * $Revision: 1.1 $
 * $Log: GMViewCharacterInfo.java,v $
 * Revision 1.1  2004/10/22 23:41:06  l2chef
 * alt-g packets added
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

import net.sf.l2j.gameserver.model.Inventory;
import net.sf.l2j.gameserver.model.L2PcInstance;
/**
 *
 *
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSddd
 * 
 * @version $Revision: 1.1 $ $Date: 2004/10/22 23:41:06 $
 */
public class GMViewCharacterInfo extends ServerBasePacket
{
	private static final String _S__04_USERINFO = "[S] A8 GMViewCharacterInfo";
	private L2PcInstance _cha;
	private static int _test = 1;

	/**
	 * @param _characters
	 */
	public GMViewCharacterInfo(L2PcInstance cha)
	{
		_cha = cha;
	}


	public byte[] getContent()
	{
		_bao.write(0xa8);
		
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());
		writeD(_cha.getHeading());
		writeD(_cha.getObjectId());
		writeS(_cha.getName());
		writeD(_cha.getRace());
		writeD(_cha.getSex());
		writeD(_cha.getClassId());
		writeD(_cha.getLevel());
		writeD(_cha.getExp());
		writeD(_cha.getStr());
		writeD(_cha.getDex());
		writeD(_cha.getCon());
		writeD(_cha.getInt());
		writeD(_cha.getWit());
		writeD(_cha.getMen());
		writeD(_cha.getMaxHp());
		writeD((int) _cha.getCurrentHp());
		writeD(_cha.getMaxMp());
		writeD((int)_cha.getCurrentMp());
		writeD(_cha.getSp());
		writeD(_cha.getCurrentLoad());
		writeD(_cha.getMaxLoad());

		writeD(0x28);  // unknown
		
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_UNDER)); 
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR)); 
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR)); 
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK)); 
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));

		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
		writeD(_cha.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));

		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_UNDER)); 
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));  
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR)); 
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK)); 
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));

		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));

		writeD(_cha.getPhysicalAttack());
		writeD(_cha.getPhysicalSpeed());
		writeD(_cha.getPhysicalDefense());
		writeD(_cha.getEvasionRate());
		writeD(_cha.getAccuracy());
		writeD(_cha.getCriticalHit());
		writeD(_cha.getMagicalAttack());

		writeD(_cha.getMagicalSpeed());
		writeD(_cha.getPhysicalSpeed());
		
		writeD(_cha.getMagicalDefense());

		writeD(_cha.getPvpFlag()); // 0-non-pvp  1-pvp = violett name
		writeD(_cha.getKarma());

		writeD(_cha.getRunSpeed());
		writeD(_cha.getWalkSpeed());
		writeD(0x32);	// swimspeed
		writeD(0x32);	// swimspeed
		writeD(_cha.getFloatingRunSpeed());
		writeD(_cha.getFloatingWalkSpeed());
		writeD(_cha.getFlyingRunSpeed());
		writeD(_cha.getFlyingWalkSpeed());
		writeF(_cha.getMovementMultiplier()); // move speed 0.7);/
		writeF(_cha.getAttackSpeedMultiplier()); //2.9);//
		writeF(_cha.getCollisionRadius());  // scale
		writeF(_cha.getCollisionHeight()); // y offset ??!? fem dwarf 4033

		writeD(_cha.getHairStyle());
		writeD(_cha.getHairColor());
		writeD(_cha.getFace());		
		if (_cha.isGM()) 
		{
			writeD(0x01);
		} 
		else 
		{
			writeD(0x00);		// builder level			
		}
		
		writeS(_cha.getTitle());
		writeD(_cha.getClanId());		// pledge id
		writeD(_cha.getClanId());		// pledge crest id
		writeD(_cha.getAllyId());		// ally id 
		writeD(_cha.getAllyId());		// ally crest id    new in rev 417
		if (_cha.isClanLeader())
		{
			writeD(0x60);			// siege-flags        0x40 - leader rights  0x20 - ?? 			
		} 
		else 
		{
			writeD(0);			// siege-flags      new in rev 417 						
		}

		return _bao.toByteArray();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__04_USERINFO;
	}
}
