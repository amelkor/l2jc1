/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CharInfo.java,v 1.7 2004/08/15 22:33:45 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/15 22:33:45 $
 * $Revision: 1.7 $
 * $Log: CharInfo.java,v $
 * Revision 1.7  2004/08/15 22:33:45  l2chef
 * some more flags identified
 *
 * Revision 1.6  2004/08/15 03:39:10  l2chef
 * correct movement speed values are send for players
 *
 * Revision 1.5  2004/08/14 22:32:02  l2chef
 * unknown methods renamed
 *
 * Revision 1.4  2004/07/19 23:15:06  l2chef
 * 2handed weapons are now shown correct to other players (Deth)
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

import net.sf.l2j.gameserver.model.Inventory;
import net.sf.l2j.gameserver.model.L2PcInstance;

/**
 * 0000: 03 32 15 00 00 44 fe 00 00 80 f1 ff ff 00 00 00    .2...D..........<p>
 * 0010: 00 6b b4 c0 4a 45 00 6c 00 6c 00 61 00 6d 00 69    .k..JE.l.l.a.m.i<p>
 * 0020: 00 00 00 01 00 00 00 01 00 00 00 12 00 00 00 00    ................<p>
 * 0030: 00 00 00 2a 00 00 00 42 00 00 00 71 02 00 00 31    ...*...B...q...1<p>
 * 0040: 00 00 00 18 00 00 00 1f 00 00 00 25 00 00 00 00    ...........%....<p>
 * 0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 f9    ................<p>
 * 0060: 00 00 00 b3 01 00 00 00 00 00 00 00 00 00 00 7d    ...............}<p>
 * 0070: 00 00 00 5a 00 00 00 32 00 00 00 32 00 00 00 00    ...Z...2...2....<p>
 * 0080: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 67    ...............g<p>
 * 0090: 66 66 66 66 66 f2 3f 5f 63 97 a8 de 1a f9 3f 00    fffff.?_c.....?.<p>
 * 00a0: 00 00 00 00 00 1e 40 00 00 00 00 00 00 37 40 01    .............7..<p>
 * 00b0: 00 00 00 01 00 00 00 01 00 00 00 00 00 c1 0c 00    ................<p>
 * 00c0: 00 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00    ................<p>
 * 00d0: 00 00<p>
 * <p>
 *  dddddSdddddddddddddddddddddddddddffffdddSdddccccccc (h)<p>
 *  dddddSdddddddddddddddddddddddddddffffdddSdddddccccccch
 * 
 * @version $Revision: 1.7 $ $Date: 2004/08/15 22:33:45 $
 */
public class CharInfo extends ServerBasePacket
{

	private static final String _S__03_CHARINFO = "[S] 03 CharInfo";
	private L2PcInstance _cha;

	/**
	 * @param _characters
	 */
	public CharInfo(L2PcInstance cha)
	{
		_cha = cha;
	}


	public byte[] getContent()
	{
		_bao.write(0x03);
		writeD(_cha.getX());
		writeD(_cha.getY());
		writeD(_cha.getZ());
		writeD(_cha.getHeading());
		writeD(_cha.getObjectId());
		writeS(_cha.getName());
		writeD(_cha.getRace());
		writeD(_cha.getSex());
		writeD(_cha.getClassId());
		writeD(0x00);  // unknown usually 00
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
		writeD(_cha.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_UNDER));

		writeD(0x00);  // unknown
		writeD(_cha.getMagicalSpeed());
		writeD(_cha.getPhysicalSpeed());
		
		writeD(_cha.getPvpFlag());
		writeD(_cha.getKarma());

		writeD(_cha.getRunSpeed());
		writeD(_cha.getWalkSpeed());
		writeD(0x32);  // swimspeed
		writeD(0x32);  // swimspeed
		writeD(_cha.getFloatingRunSpeed());
		writeD(_cha.getFloatingWalkSpeed());
		writeD(_cha.getFlyingRunSpeed());
		writeD(_cha.getFlyingWalkSpeed());
		writeF(_cha.getMovementMultiplier());
		writeF(_cha.getAttackSpeedMultiplier());
		writeF(_cha.getCollisionRadius());
		writeF(_cha.getCollisionHeight());

		writeD(_cha.getHairStyle());
		writeD(_cha.getHairColor());
		writeD(_cha.getFace());

		writeS(_cha.getTitle());
		writeD(_cha.getClanId());
		writeD(_cha.getClanId()); // crest id
		writeD(0x10);
		
		writeD(_cha.getAllyId());	// new in rev 417
		writeD(0);	// new in rev 417   siege-flags
		
		writeC(_cha.getWaitType());	// standing = 1  sitting = 0
		writeC(_cha.getMoveType());	// running = 1   walking = 0
		if (_cha.isInCombat())
		{
			writeC(0x01);   //  combat stance
		}
		else
		{
			writeC(0x00);
		}
		
		if (_cha.isDead())
		{
			writeC(0x01);	// dead =1  alive = 0
		}
		else
		{
			writeC(0x00);
		}
		
		writeC(0x00);	// invisible = 1  visible =0
		writeC(0x00);	// 1 on strider   2 on wyfern   0 no mount
		writeC(0x00);   //  1 - sellshop
		
		writeH(0x00);  // cubic count
//		writeH(0x00);  // cubic 
		writeC(0x00);	// find party members
		
		return _bao.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__03_CHARINFO;
	}
	
}
