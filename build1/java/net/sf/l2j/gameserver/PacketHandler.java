/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/PacketHandler.java,v 1.17 2004/10/22 23:40:41 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/22 23:40:41 $
 * $Revision: 1.17 $
 * $Log: PacketHandler.java,v $
 * Revision 1.17  2004/10/22 23:40:41  l2chef
 * alt-g packets added
 *
 * Revision 1.16  2004/10/11 17:12:37  nuocnam
 * Added SendBypassBuildCmd and RequestGMList packets
 *
 * Revision 1.15  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.14  2004/09/15 23:43:28  l2chef
 * /sit /run commands are handled,  community board added (Deth)
 *
 * Revision 1.13  2004/08/10 00:47:33  l2chef
 * some new packets
 *
 * Revision 1.12  2004/08/09 00:07:25  l2chef
 * new clan related packet handlers added (NuocNam)
 *
 * Revision 1.11  2004/08/08 16:32:17  l2chef
 * withdrawal from party uses l2pcinstance as parameter
 *
 * Revision 1.10  2004/08/06 00:24:46  l2chef
 * cursor movement + player trading  added (Deth/whatev)
 *
 * Revision 1.9  2004/07/28 23:53:09  l2chef
 * Selling items implemented (Deth)
 *
 * Revision 1.8  2004/07/25 23:00:38  l2chef
 * pet system started (whatev)
 *
 * Revision 1.7  2004/07/19 02:01:44  l2chef
 * party code completed (whatev)
 *
 * Revision 1.6  2004/07/14 22:09:27  l2chef
 * hander for appear packet added
 *
 * Revision 1.5  2004/07/12 20:51:38  l2chef
 * warehouses added (nuocnan)
 *
 * Revision 1.4  2004/07/11 23:39:23  l2chef
 * handling for revive  (MetalRabbit)
 *
 * Revision 1.3  2004/07/04 11:08:08  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.2  2004/06/27 08:51:43  jeichhorn
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
package net.sf.l2j.gameserver;

import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.clientpackets.*;


/**
 * This class ...
 * 
 * @version $Revision: 1.17 $ $Date: 2004/10/22 23:40:41 $
 */
public class PacketHandler
{
	private static Logger _log = Logger.getLogger(PacketHandler.class.getName());

	private ClientThread _client;
	public PacketHandler(ClientThread client)
	{
		_client = client;
	}
	
	
	public void handlePacket(byte[] data) throws IOException
	{
		int id = data[0] & 0xff;
		switch(id)
		{
			case 0x00:
				new ProtocolVersion(data, _client);
				break;
			case 0x01:
				new MoveBackwardToLocation(data, _client); 
				break;
//			case 0x02:
//				// Say  ... not used any more ??
//				break;
			case 0x03:
				new EnterWorld(data, _client);
				break;
			case 0x04:
				new Action(data, _client);
				break;
				
			case 0x08:
				new AuthLogin(data, _client);
				break;
			case 0x09:
				new Logout(data, _client);
				break;
			case 0x0a:
				new AttackRequest(data, _client);
				break;
			case 0x0b:
				new CharacterCreate(data, _client);	
				break;
			case 0x0c:
				new CharacterDelete(data, _client);	
				break;
			case 0x0d:
				new CharacterSelected(data, _client);
				break;
			case 0x0e:
				new NewCharacter(data, _client);
				break;
			case 0x0f:
				new RequestItemList(data, _client);
				break;
//			case 0x10:
//				// RequestEquipItem ... not used any more, instead "useItem"  
//				break;
			case 0x11:
				new RequestUnEquipItem(data, _client);
				break;
			case 0x12:
				new RequestDropItem(data, _client);
				break;
				
			case 0x14:
				new UseItem(data, _client);
				break;
			case 0x15:
				new TradeRequest(data, _client);
				break;
			case 0x16:
				 new AddTradeItem(data, _client);
				break;
			case 0x17:
				 new TradeDone(data, _client);
				break;
			
//			case 0x1a:
//				// RequestTeleport
//				break;
			case 0x1b:
				new RequestSocialAction(data, _client);
				break;
			case 0x1c:
				new ChangeMoveType2(data, _client);
				break;
			case 0x1d:
				new ChangeWaitType2(data, _client);
				break;
			case 0x1e:
				new RequestSellItem(data, _client);
				break;
			case 0x1f:
				new RequestBuyItem(data, _client);
				break;
//			case 0x20:
//				// RequestLinkHtml
//				break;
			case 0x21:
				new RequestBypassToServer(data, _client);
				break;
//			case 0x22:
//				// RequestBBSwrite
//				break;
//			case 0x23:
//				// RequestCreatePledge   ... seems to be deprecated
//				break;
			case 0x24:
				new RequestJoinPledge(data, _client);
				break;
			case 0x25:
				new RequestAnswerJoinPledge(data, _client);
				break;
			case 0x26:
				new RequestWithdrawalPledge(data, _client);  
				break;
			case 0x27:
				new RequestOustPledgeMember(data, _client);  
				break;
//			case 0x28:
//				// RequestDismissPledge  
//				break;
			case 0x29:
				new RequestJoinParty(data, _client);    
				break;
			case 0x2a:
				new RequestAnswerJoinParty(data, _client);     
				break;
			case 0x2b:
				new RequestWithDrawalParty(data, _client.getActiveChar());     
				break;
			case 0x2c:
			    new RequestOustPartyMember(data, _client);
				break;
//			case 0x2d:
//				// RequestDismissParty  
//				break;
//			case 0x2e:
//				// RequestMagicSkillList  .. deprecated .. RequestSkillList is used instead   
//				break;
			case 0x2f:
				new RequestMagicSkillUse(data, _client);
				break;
			case 0x30:
				new Appearing(data, _client);  //  (after death)
				break;
			case 0x31:
				new SendWareHouseDepositList(data, _client);  
				break;
			case 0x32:
				new SendWareHouseWithDrawList(data, _client);  
				break;
			case 0x33:
				new RequestShortCutReg(data, _client);    
				break;
//			case 0x34:
//				// RequestShortCutUse    
//				break;
			case 0x35:
				new RequestShortCutDel(data, _client);    
				break;

			case 0x36:
				new StopMove(data, _client);    
				break;
				
			case 0x37:
				new RequestTargetCanceld(data, _client);
				break;
			case 0x38:
				new Say2(data, _client);
				break;
				
			case 0x3c:
				new RequestPledgeMemberList(data, _client);
				break;
				
//			case 0x3e:
//				// RequestMagicList  .. deprecated .. RequestSkillList is used instead
//				break;
			case 0x3f:
				new RequestSkillList(data, _client);
				break;

//			case 0x41:
//				// MoveWithDelta    ... unused ?? or only on ship ??
//				break;
//			case 0x42:
//				// GetOnVehicle    
//				break;
//			case 0x43:
//				// GetOffVehicle   
//				break;
			case 0x44:
				new AnswerTradeRequest(data, _client);
				break;
			case 0x45:
				new RequestActionUse(data, _client);
				break;
			case 0x46:
				new RequestRestart(data, _client);
				break;
//			case 0x47:
//				// RequestSiegeInfo   
//				break;
			case 0x48:
				new ValidatePosition(data, _client);
				break;
//			case 0x49:
//				// RequestSEKCustom   
//				break;
			case 0x4a:
				new StartRotating(data, _client);  
				break;
			case 0x4b:
				new FinishRotating(data, _client); 
				break;

//			case 0x4d:
//				// RequestStartPledgeWar    
//				break;
//			case 0x4e:
//				// RequestReplyStartPledgeWar      
//				break;
//			case 0x4f:
//				// RequestStopPledgeWar      
//				break;
//			case 0x50:
//				// RequestReplyStopPledgeWar         
//				break;
//			case 0x51:
//				// RequestSurrenderPledgeWar         
//				break;
//			case 0x52:
//				// RequestReplySurrenderPledgeWar            
//				break;
			case 0x53:
				new RequestSetPledgeCrest(data, _client);           
				break;

			case 0x55:
				new RequestGiveNickName(data, _client);             
				break;
//
			case 0x57:
				new RequestShowBoard(data, _client);                
				break;
//			case 0x58:
//				// RequestEnchantItem               
//				break;
			case 0x59:
				new RequestDestroyItem(data, _client);
				break;

			case 0x5b:
				new SendBypassBuildCmd(data, _client);   
				break;
			
//			case 0x5e:
//				// RequestFriendInvite	cS
//				break;
//			case 0x5f:
//				// RequestFriendAddReply	cd
//				break;
//			case 0x60:
//				// RequestFriendList	c
//				break;
//			case 0x61:
//				// RequestFriendDel	cS
//				break;
			case 0x62:
				new CharacterRestore(data, _client);
				break;
			case 0x63:
				new RequestQuestList(data, _client);
				break;
//			case 0x64:
//				// RequestDestroyQuest   
//				break;
				
			case 0x66:
				new RequestPledgeInfo(data, _client);
				break;
//			case 0x67:
//				// RequestPledgeExtendedInfo   
//				break;
			case 0x68:
				new RequestPledgeCrest(data, _client);
				break;
//			case 0x69:
//				// RequestSurrenderPersonally  
//				break;
//			case 0x6a:
//				// Ride  
//				break;
			case 0x6b: // send when talking to trainer npc, to show list of available skills
				new RequestAquireSkillInfo(data, _client);//  --> [s] 0xa4;
				break;
			case 0x6c: // send when a skill to be learned is selected
				new RequestAquireSkill(data, _client);    
				break;
			case 0x6d:
				new RequestRestartPoint(data, _client); 
				break;
			case 0x6e: 
				new RequestGMCommand(data, _client);  
				break;
			case 0x6f:
				 new RequestPartyMatchConfig(data, _client);   
				break;
			case 0x70:
				new RequestPartyMatchList(data, _client);      
				break;
			case 0x71:
				new RequestPartyMatchDetail(data, _client);     
				break;
//			case 0x72:
//				// RequestCrystallizeItem     
//				break;
			case 0x73:
				new RequestPrivateStoreManage(data, _client);        
				break;
			case 0x74:
				new SetPrivateStoreListSell(data, _client);        
				break;
//			case 0x75:
//				new RequestPrivateStoreManageCancel(data, _client);        
//				break;
			case 0x76:
				new RequestPrivateStoreQuitSell(data, _client);          
				break;
			case 0x77:
				new SetPrivateStoreMsgSell(data, _client);           
				break;
//			case 0x78:
//				// RequestPrivateStoreList         
//				break;
			case 0x79:
				new SendPrivateStoreBuyList(data, _client);             
				break;
//			case 0x7a:
//				// ReviveReply            
//				break;
//			case 0x7b:
//				// RequestTutorialLinkHtml           
//				break;
//			case 0x7c:
//				// RequestTutorialPassCmdToServer              
//				break;
//			case 0x7d:
//				// RequestTutorialQuestionMark             
//				break;
//			case 0x7e:
//				// RequestTutorialClientEvent             
//				break;
//			case 0x7f:
//				// RequestPetition               
//				break;
//			case 0x80:
//				// RequestPetitionCancel               
//				break;
			case 0x81:
				new RequestGmList(data, _client);
				break;
//			case 0x82:
//				// RequestJoinAlly 
//				break;
//			case 0x83:
//				// RequestAnswerJoinAlly
//				break;
//			case 0x84:
//				// RequestWithdrawAlly
//				break;
//			case 0x85:
//				// RequestOustAlly
//				break;
//			case 0x86:
//				// RequestDismissAlly
//				break;
//			case 0x87:
//				// RequestSetAllyCrest
//				break;
			case 0x88:
				new RequestAllyCrest(data, _client);
				break;
			case 0x89:
				 new RequestChangePetName(data, _client);
				break;
//			case 0x8a:
//				// RequestPetUseItem
//				break;
			case 0x8b:
				 new RequestGiveItemToPet(data, _client);
				break;
			case 0x8c:
				 new RequestGetItemFromPet(data, _client);
				break;
				
//			case 0x8e:
//				// RequestAllyInfo
//				break;
			case 0x8f:
				 new RequestPetGetItem(data, _client);
				break;
			case 0x90:
				new RequestPrivateStoreBuyManage(data, _client);
				break;
			case 0x91:
				new SetPrivateStoreListBuy (data, _client);
				break;
//			case 0x92:
//				// RequestPrivateStoreBuyManageCancel
//				break;
			case 0x93:
				new RequestPrivateStoreQuitBuy (data, _client);
				break;
			case 0x94:
				new SetPrivateStoreMsgBuy(data, _client);
				break;
//			case 0x95:
//				// RequestPrivateStoreBuyList
//				break;
			case 0x96:
				new SendPrivateStoreBuyBuyList(data, _client);
				break;
//			case 0x97:
//				// SendTimeCheckPacket
//				break;
//			case 0x98:
//				// RequestStartAllianceWar
//				break;
//			case 0x99:
//				// ReplyStartAllianceWar
//				break;
//			case 0x9a:
//				// RequestStopAllianceWar
//				break;
//			case 0x9b:
// 				// ReplyStopAllianceWar
//				break;
//			case 0x9c:
//				// RequestSurrenderAllianceWar
//				break;
			case 0x9d:
				// RequestSkillCoolTime
				_log.warning("Request Skill Cool Time .. ignored");
				break;
//			case 0x9e:
//				// RequestPackageSendableItemList
//				break;
//			case 0x9f:
//				// RequestPackageSend
//				break;
//			case 0xa0:
//				// RequestBlock
//				break;
//			case 0xa1:
//				// RequestCastleSiegeInfo
//				break;
//			case 0xa2:
//				// RequestCastleSiegeAttackerList
//				break;
//			case 0xa3:
//				// RequestCastleSiegeInfo
//				break;
//			case 0xa4:
//				// RequestJoinCastleSiege
//				break;
//			case 0xa5:
//				// RequestConfirmCastleSiegeWaitingList
//				break;
//			case 0xa6:
//				// RequestSetCastleSiegeTime
//				break;
//			case 0xa7:
//				// RequestMultiSellChoose
//				break;
//			case 0xa8:
//				// NetPing
//				break;
			default:
				_log.warning("Unknown Packet:" + Integer.toHexString(id));
				_log.warning(printData(data, data.length));
		}
	}
	
	private String printData(byte[] data, int len)
	{ 
		StringBuffer result = new StringBuffer();
		
		int counter = 0;
		
		for (int i=0;i< len;i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i,4)+": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i-15;
				for (int a=0; a<16;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0 )
		{
			for (int i=0; i<17-rest;i++ )
			{
				result.append("   ");
			}

			int charpoint = data.length-rest;
			for (int a=0; a<rest;a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char)t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}

		
		return result.toString();
	}
	
	private String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i=number.length(); i< digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
}
