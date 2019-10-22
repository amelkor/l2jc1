/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/SystemMessage.java,v 1.15 2004/09/27 08:38:53 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/27 08:38:53 $
 * $Revision: 1.15 $
 * $Log: SystemMessage.java,v $
 * Revision 1.15  2004/09/27 08:38:53  nuocnam
 * Added party some party messages. Corrected non final constants so they are final now.
 *
 * Revision 1.14  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.13  2004/09/15 23:56:10  l2chef
 * arrow messages added (Deth)
 *
 * Revision 1.12  2004/08/18 23:26:11  l2chef
 * some new messages
 *
 * Revision 1.11  2004/08/17 00:49:47  l2chef
 * new contants added
 *
 * Revision 1.10  2004/08/15 22:29:04  l2chef
 * 'added target not online' id
 *
 * Revision 1.9  2004/08/10 20:38:53  l2chef
 * pending transactions are cancled on disconnect (whatev)
 *
 * Revision 1.8  2004/08/10 00:51:36  l2chef
 * several new clan messages
 *
 * Revision 1.7  2004/08/09 00:08:48  l2chef
 * clan related updated (NuocNam)
 *
 * Revision 1.6  2004/08/08 23:01:10  l2chef
 * some new trade and party messages
 *
 * Revision 1.5  2004/07/17 11:26:28  l2chef
 * some refactoring
 *
 * Revision 1.4  2004/07/04 17:38:25  l2chef
 * level up message added
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

import java.io.IOException;
import java.util.Vector;

/**
 * This class ...
 * 
 * @version $Revision: 1.15 $ $Date: 2004/09/27 08:38:53 $
 */
public class SystemMessage extends ServerBasePacket
{
	// d d (d S/d d/d dd)
	//      |--------------> 0 - String  1-number 2-textref npcname (1000000-1002655)  3-textref itemname 4-textref skills 5-??	
	private static final int TYPE_SKILL_NAME = 4;
	private static final int TYPE_ITEM_NAME = 3;
	private static final int TYPE_NPC_NAME = 2;
	private static final int TYPE_NUMBER = 1;
	private static final int TYPE_TEXT = 0;
	private static final String _S__7A_SYSTEMMESSAGE = "[S] 7A SystemMessage";
	private int _messageId;
	private Vector _types = new Vector();
	private Vector _values = new Vector();
	
	// magic related
	public static final int NOT_ENOUGH_HP = 23;
	public static final int NOT_ENOUGH_MP = 24;
	public static final int USE_S1 = 46;
	public static final int YOU_FEEL_S1_EFFECT = 110;
	public static final int REJUVENATING_HP = 25;
	public static final int CANNOT_USE_ITEM_WHILE_USING_MAGIC = 104;
	public static final int CASTING_INTERRUPTED = 27;
	
	
	
	public static final int YOU_DID_S1_DMG = 0x23;
	public static final int S1_GAVE_YOU_S2_DMG = 0x24;
	public static final int MISSED_TARGET = 0x2b;
	public static final int CRITICAL_HIT = 0x2c;
	public static final int EFFECT_S1_DISAPPEARED = 0x5c;
	public static final int YOU_EARNED_S1_EXP_AND_S2_SP = 0x5f;
	public static final int YOU_INCREASED_YOUR_LEVEL = 0x60;
	
	public static final int NOTHING_HAPPENED = 61;
	public static final int ITEM_MISSING_TO_LEARN_SKILL = 0x114;
	public static final int LEARNED_SKILL_S1 = 0x115;
	public static final int NOT_ENOUGH_SP_TO_LEARN_SKILL = 0x116;
	
	public static final int FALL_DAMAGE_S1 = 0x128;
	public static final int DROWN_DAMAGE_S1 = 0x129;

	public static final int YOU_DROPPED_S1 = 0x12a;
	public static final int TARGET_IS_NOT_FOUND_IN_THE_GAME = 0x91;

	public static final int SOULSHOTS_GRADE_MISMATCH = 0x151;
	public static final int NOT_ENOUGH_SOULSHOTS = 0x152;
	public static final int CANNOT_USE_SOULSHOTS = 0x153;
	
	public static final int S1_IS_NOT_ONLINE = 3;
	
	public static final int GETTING_READY_TO_SHOOT_AN_ARROW = 41;
	
	// party related 
	public static final int WAITING_FOR_REPLY = 164;
	
	public static final int S1_INVITED_YOU_TO_PARTY_FINDER_KEEPER = 572;
	public static final int S1_INVITED_YOU_TO_PARTY_RANDOM = 573;
	
	public static final int S1_INVITED_YOU_TO_PARTY = 66;
	public static final int YOU_INVITED_S1_TO_PARTY = 105;
	
	public static final int OTHER_PARTY_IS_DROZEN = 692;
	
	public static final int YOU_JOINED_S1_PARTY = 106;
	public static final int S1_JOINED_PARTY = 107;
	public static final int S1_LEFT_PARTY = 108;
	public static final int YOU_LEFT_PARTY = 200;
	public static final int ONLY_LEADER_CAN_INVITE = 154;

	public static final int S1_DID_NOT_REPLY = 135;
	public static final int YOU_DID_NOT_REPLY = 136;
	
	public static final int PLAYER_DECLINED = 305;
	
	public static final int PARTY_FULL = 155;
	public static final int S1_IS_ALREADY_IN_PARTY = 160;
	public static final int INVITED_USER_NOT_ONLINE = 161;

	public static final int PARTY_DISPERSED = 203;

	//weight & inventory
	public static final int WEIGHT_LIMIT_EXCEEDED = 422;
	public static final int NOT_ENOUGH_ARROWS = 112;

	// trade 
	public static final int S1_IS_BUSY_TRY_LATER = 153;
	public static final int TARGET_IS_INCORRECT = 144;
	public static final int ALREADY_TRADING = 142;
	public static final int REQUEST_S1_FOR_TRADE = 118;
	public static final int S1_DENIED_TRADE_REQUEST = 119;
	public static final int BEGIN_TRADE_WITH_S1 = 120;
	public static final int S1_CONFIRMED_TRADE = 121;
	public static final int TRADE_SUCCESSFUL = 123;
	public static final int S1_CANCELED_TRADE = 124;
	
	//private store & store
	public static final int S1_PURCHASED_S2 = 378;
	public static final int S1_PURCHASED_S2_S3 = 379;
	public static final int S1_PURCHASED_S3_S2_s = 380;
	public static final int PURCHASED_S2_FROM_S1 = 559;
	public static final int PURCHASED_S2_S3_FROM_S1 =560;
	public static final int	PURCHASED_S3_S2_s_FROM_S1 =561;
	public static final int	THE_PURCHASE_IS_COMPLETE =700;
	public static final int	THE_PURCHASE_PRICE_IS_HIGHER_THAN_MONEY = 720;
	public static final int YOU_NOT_ENOUGH_ADENA = 279;
	
	//Clan stuff
	public static final int FAILED_TO_CREATE_CLAN = 190;
	public static final int CLAN_NAME_INCORRECT = 261;
	public static final int CLAN_NAME_TOO_LONG = 262;
	
	public static final int CLAN_CREATED = 189;
	public static final int CLAN_LVL_3_NEEDED_TO_ENDOVE_TITLE = 271;
	public static final int CANNOT_INVITE_YOURSELF = 4;
	public static final int S1_WORKING_WITH_ANOTHER_CLAN = 10;
	public static final int ENTERED_THE_CLAN = 195;
	public static final int S1_REFUSED_TO_JOIN_CLAN = 196;
	public static final int S1_HAS_JOINED_CLAN = 222;
	public static final int CLAN_MEMBER_S1_LOGGED_IN = 304;
	
	public static final int CLAN_MEMBER_S1_EXPELLED = 191;
	public static final int CLAN_MEMBERSHIP_TERMINATED = 199;
	public static final int S1_S2 = 614;
	
	//pickup items
	public static final int YOU_PICKED_UP_S1_ADENA = 28;
	public static final int YOU_PICKED_UP_S1_S2 = 29;
	public static final int YOU_PICKED_UP_S1 = 30;	
	public static final int S1_PICKED_UP_S2_S3 = 299;
	public static final int S1_PICKED_UP_S2 = 300;
	

	/**
	 * @param _characters
	 */
	public SystemMessage(int messageId)
	{
		_messageId = messageId;
	}
	
	public void addString(String text)
	{
		_types.add(new Integer(TYPE_TEXT));
		_values.add(text);
	}

	public void addNumber(int number)
	{
		_types.add(new Integer(TYPE_NUMBER));
		_values.add(new Integer(number));
	}
	
	public void addNpcName(int id)
	{
		_types.add(new Integer(TYPE_NPC_NAME));
		_values.add(new Integer(1000000+id));
	}

	public void addItemName(int id)
	{
		_types.add(new Integer(TYPE_ITEM_NAME));
		_values.add(new Integer(id));
	}

	public void addSkillName(int id)
	{
		_types.add(new Integer(TYPE_SKILL_NAME));
		_values.add(new Integer(id));
	}

	public byte[] getContent() throws IOException
	{
		writeC(0x7a);

		writeD(_messageId);
		writeD(_types.size());

		for (int i=0; i<_types.size();i++)
		{
			int t = ((Integer)_types.get(i)).intValue();


			writeD(t);

			switch (t)
			{
				case TYPE_TEXT:
				{
					writeS( (String)_values.get(i));
					break;
				}
				case TYPE_NUMBER:
				case TYPE_NPC_NAME:
				case TYPE_ITEM_NAME:
				{
					int t1 = ((Integer)_values.get(i)).intValue();
					writeD(t1);	
					
					break;
				}
				case TYPE_SKILL_NAME:
				{
					int t1 = ((Integer)_values.get(i)).intValue();
					writeD(t1);	
					writeD(1);  //??
				}
			}
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__7A_SYSTEMMESSAGE;
	}
}
	
