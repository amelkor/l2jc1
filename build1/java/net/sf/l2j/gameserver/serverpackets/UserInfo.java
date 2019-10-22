/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/UserInfo.java,v 1.11 2004/10/11 17:31:41 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/10/11 17:31:41 $
 * $Revision: 1.11 $
 * $Log: UserInfo.java,v $
 * Revision 1.11  2004/10/11 17:31:41  nuocnam
 * Added isGM check
 *
 * Revision 1.10  2004/09/28 20:36:28  l2chef
 * *** empty log message ***
 *
 * Revision 1.9  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.8  2004/08/15 03:40:03  l2chef
 * missing speeds set
 *
 * Revision 1.7  2004/08/14 22:48:32  l2chef
 * unknown methods renamed
 *
 * Revision 1.6  2004/08/10 00:52:06  l2chef
 * clan leader flag used
 *
 * Revision 1.5  2004/08/09 00:08:48  l2chef
 * clan related updated (NuocNam)
 *
 * Revision 1.4  2004/08/08 02:16:51  l2chef
 * clan leader flag found
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
 * 0000: 04 03 15 00 00 77 ff 00 00 80 f1 ff ff 00 00 00    .....w..........
 * 0010: 00 2a 89 00 4c 43 00 61 00 6c 00 61 00 64 00 6f    .*..LC.a.l.a.d.o
 * 0020: 00 6e 00 00 00 01 00 00 00 00 00 00 00 19 00 00    .n..............
 * 0030: 00 0d 00 00 00 ee 81 02 00 15 00 00 00 18 00 00    ................
 * 0040: 00 19 00 00 00 25 00 00 00 17 00 00 00 28 00 00    .....%.......(..
 * 0050: 00 14 01 00 00 14 01 00 00 02 01 00 00 02 01 00    ................
 * 0060: 00 fa 09 00 00 81 06 00 00 26 34 00 00 2e 00 00    .........&4.....
 * 0070: 00 00 00 00 00 db 9f a1 41 93 26 64 41 de c8 31    ........A.&dA..1
 * 0080: 41 ca 73 c0 41 d5 22 d0 41 83 bd 41 41 81 56 10    A.s.A.".A..AA.V.
 * 0090: 41 00 00 00 00 27 7d 30 41 69 aa e0 40 b4 fb d3    A....'}0Ai..@...
 * 00a0: 41 91 f9 63 41 00 00 00 00 81 56 10 41 00 00 00    A..cA.....V.A...
 * 00b0: 00 71 00 00 00 71 00 00 00 76 00 00 00 74 00 00    .q...q...v...t..
 * 00c0: 00 74 00 00 00 2a 00 00 00 e8 02 00 00 00 00 00    .t...*..........
 * 00d0: 00 5f 04 00 00 ac 01 00 00 cf 01 00 00 62 04 00    ._...........b..
 * 00e0: 00 00 00 00 00 e8 02 00 00 0b 00 00 00 52 01 00    .............R..
 * 00f0: 00 4d 00 00 00 2a 00 00 00 2f 00 00 00 29 00 00    .M...*.../...)..
 * 0100: 00 12 00 00 00 82 01 00 00 52 01 00 00 53 00 00    .........R...S..
 * 0110: 00 00 00 00 00 00 00 00 00 7a 00 00 00 55 00 00    .........z...U..
 * 0120: 00 32 00 00 00 32 00 00 00 00 00 00 00 00 00 00    .2...2..........
 * 0130: 00 00 00 00 00 00 00 00 00 a4 70 3d 0a d7 a3 f0    ..........p=....
 * 0140: 3f 64 5d dc 46 03 78 f3 3f 00 00 00 00 00 00 1e    ?d].F.x.?.......
 * 0150: 40 00 00 00 00 00 00 38 40 02 00 00 00 01 00 00    @......8@.......
 * 0160: 00 00 00 00 00 00 00 00 00 00 00 c1 0c 00 00 01    ................
 * 0170: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
 * 0180: 00 00 00 00                                        ....
 *
 *
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddcccdd (h)
 * 
 * @version $Revision: 1.11 $ $Date: 2004/10/11 17:31:41 $
 */
public class UserInfo extends ServerBasePacket
{
	private static final String _S__04_USERINFO = "[S] 04 UserInfo";
	private L2PcInstance _cha;
	private static int _test = 1;

	/**
	 * @param _characters
	 */
	public UserInfo(L2PcInstance cha)
	{
		_cha = cha;
	}


	public byte[] getContent()
	{
		_bao.write(0x04);
		
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
		if (_cha.isGM()) {
			writeD(0x01);
		} else {
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

		writeC(0x00);
		writeC(_cha.getPrivateStoreType());
		writeC(_cha.getCanCraft());

		writeD(_cha.getPkKills());		
		writeD(_cha.getPvpKills());		

		writeH(0x00);		// cubic count
//		writeH(0x01);		// 1-yellow 2-orange 3-yellow star  4-violett 5-blue cube  
//		writeH(0x02);		// 1-yellow 2-orange 3-yellow star  4-violett 5-blue cube  
//		writeH(0x03);		// 1-yellow 2-orange 3-yellow star  4-violett 5-blue cube  
//		writeH(0x04);		// 1-yellow 2-orange 3-yellow star  4-violett 5-blue cube  
//		writeH(0x05);		// 1-yellow 2-orange 3-yellow star  4-violett 5-blue cube  
		writeC(0x00);       //1-find party members

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
