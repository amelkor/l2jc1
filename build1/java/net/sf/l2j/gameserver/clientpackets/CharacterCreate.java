/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/CharacterCreate.java,v 1.7 2004/08/14 22:30:41 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/14 22:30:41 $
 * $Revision: 1.7 $
 * $Log: CharacterCreate.java,v $
 * Revision 1.7  2004/08/14 22:30:41  l2chef
 * unknown methods renamed
 *
 * Revision 1.6  2004/07/25 18:25:09  l2chef
 * fixed npe (Deth)
 *
 * Revision 1.5  2004/07/25 00:37:19  l2chef
 * charnames are now checked for duplicates when creating char
 *
 * Revision 1.4  2004/07/11 23:37:38  l2chef
 * chars are always reloaded from disk  (whatev)
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.CharNameTable;
import net.sf.l2j.gameserver.CharTemplateTable;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.IdFactory;
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.SkillTable;
import net.sf.l2j.gameserver.SkillTreeTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.L2World;
//import net.sf.l2j.gameserver.serverpackets.CharCreateFail;
import net.sf.l2j.gameserver.serverpackets.CharCreateFail;
import net.sf.l2j.gameserver.serverpackets.CharCreateOk;
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;
import net.sf.l2j.gameserver.templates.L2CharTemplate;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/08/14 22:30:41 $
 */
public class CharacterCreate extends ClientBasePacket
{
	private static final String _C__0B_CHARACTERCREATE = "[C] 0B CharacterCreate";
	private static Logger _log = Logger.getLogger(CharacterCreate.class.getName());
	
	// cSdddddddddddd
	
	/**
	 * @param decrypt
	 */
	public CharacterCreate(byte[] decrypt, ClientThread client)	throws IOException
	{
		super(decrypt);
		
		L2PcInstance newChar = new L2PcInstance();
		newChar.setName(readS());
		newChar.setRace(readD());
		newChar.setSex(readD());
		newChar.setClassId(readD());
		newChar.setInt(readD());
		newChar.setStr(readD());
		newChar.setCon(readD());
		newChar.setMen(readD());
		newChar.setDex(readD());
		newChar.setWit(readD());
		newChar.setHairStyle(readD());
		newChar.setHairColor(readD());
		newChar.setFace(readD());
		
		//TODO checked if there is already maximum number of chars. send error in that case
		if (CharNameTable.getInstance().doesCharNameExist(newChar.getName()))
		{
			_log.fine("charname: "+ newChar.getName() + " already exists. creation failed.");
			CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_NAME_ALREADY_EXISTS);
			client.getConnection().sendPacket(ccf);
		}
		else
		{
			if ((newChar.getName().length() <= 16) && (isAlphaNumeric(newChar.getName())))
			{
				_log.fine("charname: " + newChar.getName() + " classId: " + newChar.getClassId());
				
				// send acknowledgement
				
				CharCreateOk cco = new CharCreateOk();
				client.getConnection().sendPacket(cco);
				
				initNewChar(client, newChar);
				CharNameTable.getInstance().addCharName(newChar.getName());
			}
			else
			{
				_log.fine("charname: " + newChar.getName() + " is invalid. creation failed.");
				CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_16_ENG_CHARS);
				client.getConnection().sendPacket(ccf);
			}
		}
	}
	
	private boolean isAlphaNumeric(String text)
	{
		boolean result = true;
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			if (!Character.isLetterOrDigit(chars[i]))
			{
				result = false;
				break;
			}
		}
		return result;
	}
	
	private void initNewChar(ClientThread client, L2PcInstance newChar)	throws FileNotFoundException, IOException
	{   
		_log.fine("Character init start");
		newChar.setObjectId(IdFactory.getInstance().getNextId());
		L2World.getInstance().storeObject(newChar);
		
		L2CharTemplate template = CharTemplateTable.getInstance().getTemplate(newChar.getClassId());
		
		// preinit new char
		newChar.setAccuracy(template.getAcc());
		newChar.setCon(template.getCon());
		newChar.setCriticalHit(template.getCrit());
		newChar.setMaxHp(template.getHp());
		newChar.setCurrentHp(template.getHp());
		newChar.setMaxLoad(template.getLoad());
		newChar.setMaxMp(template.getMp());
		newChar.setCurrentMp(template.getMp());
		newChar.setDex(template.getDex());
		newChar.setEvasionRate(template.getEvas());
		newChar.setExp(0);
		newChar.setInt(template.getInt());
		newChar.setKarma(0);
		newChar.setLevel(1);
		newChar.setMagicalAttack(template.getMatk());
		newChar.setMagicalDefense(template.getMdef());
		newChar.setMagicalSpeed(template.getMspd());
		newChar.setPhysicalAttack(template.getPatk());
		newChar.setPhysicalDefense(template.getPdef());
		newChar.setPhysicalSpeed(template.getPspd());
		newChar.setMen(template.getMen());
		newChar.setPvpKills(0);
		newChar.setPkKills(0);
		newChar.setSp(0);
		newChar.setStr(template.getStr());
		newChar.setRunSpeed(template.getMoveSpd());
		
		newChar.setWalkSpeed((int)(template.getMoveSpd() * 0.7));
		newChar.setWit(template.getWit());
		newChar.setPvpFlag(0);
		newChar.addAdena(5000);
		newChar.setCanCraft(template.getCanCraft());
		
		newChar.setX(template.getX());
		newChar.setY(template.getY());
		newChar.setZ(template.getZ());
		
		if (newChar.isMale())
		{
			newChar.setMovementMultiplier(template.getMUnk1());
			newChar.setAttackSpeedMultiplier(template.getMUnk2());
			newChar.setCollisionRadius(template.getMColR());
			newChar.setCollisionHeight(template.getMColH());
		}
		else
		{
			newChar.setMovementMultiplier(template.getFUnk1());
			newChar.setAttackSpeedMultiplier(template.getFUnk2());
			newChar.setCollisionRadius(template.getFColR());
			newChar.setCollisionHeight(template.getFColH());
		}
		
		ItemTable itemTable = ItemTable.getInstance();
		Integer[] items = template.getItems();
		for (int i = 0; i < items.length; i++)
		{
			L2ItemInstance item = itemTable.createItem(items[i].intValue());
			newChar.getInventory().addItem(item);
		}
		
		newChar.setTitle("");
		newChar.setClanId(0);
		
		L2SkillLearn[] startSkills = SkillTreeTable.getInstance().getAvailableSkills(newChar);
		for (int i = 0; i < startSkills.length; i++)
		{
			newChar.addSkill(SkillTable.getInstance().getInfo(startSkills[i].getId(), startSkills[i].getLevel()));
			_log.fine("adding starter skill:" + startSkills[i].getId()+ " / "+ startSkills[i].getLevel());
		}
		
		client.saveCharToDisk(newChar);
		
		// send char list
		
		CharSelectInfo cl =	new CharSelectInfo(client.getLoginName(),client.getSessionId());
		client.getConnection().sendPacket(cl);
		_log.fine("Character init end");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__0B_CHARACTERCREATE;
	}
}
