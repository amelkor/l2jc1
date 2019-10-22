/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/ClientThread.java,v 1.18 2004/10/17 06:46:23 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:23 $
 * $Revision: 1.18 $
 * $Log: ClientThread.java,v $
 * Revision 1.18  2004/10/17 06:46:23  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.17  2004/09/16 00:03:04  l2chef
 * fistweapon is set on restore (Deth)
 *
 * Revision 1.16  2004/08/14 22:30:19  l2chef
 * unknown methods renamed
 *
 * Revision 1.15  2004/08/10 00:47:00  l2chef
 * clan reference is set on load, automatic expel from clan added
 *
 * Revision 1.14  2004/08/08 22:57:00  l2chef
 * resetting active char is now possible
 *
 * Revision 1.13  2004/08/08 16:33:34  l2chef
 * deleteme method of L2pcinstance is used to cleanup
 *
 * Revision 1.12  2004/07/25 23:00:38  l2chef
 * pet system started (whatev)
 *
 * Revision 1.11  2004/07/25 00:36:49  l2chef
 * warehouse file is now also deleted when a char is deleted (Deth)
 *
 * Revision 1.10  2004/07/23 01:42:35  l2chef
 * all object spawn and delete is now handeld in L2PcInstance
 *
 * Revision 1.9  2004/07/19 02:02:56  l2chef
 * party code completed (whatev)
 * some exception cases fixed
 *
 * Revision 1.8  2004/07/17 12:15:43  l2chef
 * logging removed (NuocNam)
 *
 * Revision 1.7  2004/07/13 23:17:05  l2chef
 * log message added and empty blocks commented
 *
 * Revision 1.6  2004/07/12 20:53:03  l2chef
 * warehouses added (nuocnam)
 * char data is now stored in subfolder data/accounts
 *
 * Revision 1.5  2004/07/11 23:41:22  l2chef
 * chars are always reloaded from disk and stored every 15mins (whatev)
 *
 * Revision 1.4  2004/07/05 23:03:31  l2chef
 * access levels are stored for logins
 *
 * Revision 1.3  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.loginserver.LoginController;

/**
 * This class ...
 * 
 * @version $Revision: 1.18 $ $Date: 2004/10/17 06:46:23 $
 */
public class ClientThread extends Thread
{
	private static Logger _log = Logger.getLogger(ClientThread.class.getName());
	
	private String _loginName;
	private L2PcInstance _activeChar;
	private int _sessionId;

	private byte[] _filter;
	
	private byte[] _cryptkey = {
		(byte)0x94, (byte)0x35, (byte)0x00, (byte)0x00, 
		(byte)0xa1, (byte)0x6c,	(byte)0x54, (byte)0x87   // these 4 bytes are fixed
	};

	private File _userFolder;
	private File _charFolder;
	private long _autoSaveTime;
	private Connection _connection;
	private PacketHandler _handler;
	private L2World _world;

	// this is used for restricted commands and actions
	private int _accessLevel;

	public ClientThread(Socket client) throws IOException
	{
		_connection = new Connection(client, _cryptkey);
		_sessionId = 0x12345678;
		_handler = new PacketHandler(this);
		_world = L2World.getInstance();
		_autoSaveTime = 60000 * 15;//15 min
		
		start();
	}
	
	public void run()
	{
		_log.fine("thread[C] started");
		long starttime = System.currentTimeMillis();
		boolean checksumOk = false;

		try
		{
			while (true)
			{	
				
				if ((_activeChar != null) && (_autoSaveTime < (System.currentTimeMillis() - starttime)))
				{	
					saveCharToDisk(_activeChar);
					starttime = System.currentTimeMillis();
				}
				
				byte[] decrypt = _connection.getPacket();
				_handler.handlePacket(decrypt);
			}
		}
		catch (IOException io)
		{
			// this happens when the client disconnects
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (_activeChar != null)  // this should only happen on connection loss
				{
					// notify the world about our disconnect
					_activeChar.deleteMe();
					
					try
					{
						saveCharToDisk(_activeChar);
					}
					catch (Exception e2)
					{
						// ignore any problems here
					}
				}

				_connection.close();
			}
			catch (Exception e1)
			{
				_log.warning(e1.toString());
			}
			finally
			{
				// remove the account
				LoginController.getInstance().removeGameServerLogin(getLoginName());
			}
		}

		_log.fine("gameserver thread[C] stopped");
	}
	
	/**
	 * 
	 */
	public void saveCharToDisk(L2PcInstance cha) throws FileNotFoundException, IOException
	{  
		if (_charFolder != null)
		{
			File saveFile = new File(_charFolder, cha.getName()+"_char.csv");
			storeChar(cha, saveFile);
			
			saveFile = new File(_charFolder, cha.getName()+"_items.csv");
			storeInventory(cha, saveFile);
			
			saveFile = new File(_charFolder, cha.getName()+"_skills.csv");
			storeSkills(cha, saveFile);
			
			saveFile = new File(_charFolder, cha.getName()+"_shortcuts.csv");
			storeShortcuts(cha, saveFile);
			
			saveFile = new File(_charFolder, cha.getName()+"_warehouse.csv");
			storeWarehouse(cha, saveFile);
		}
		
		IdFactory.getInstance().saveCurrentState();
	}


	/**
	 * @param cha
	 * @param saveFile
	 */
	private void storeShortcuts(L2PcInstance cha, File saveFile)
	{
		FileWriter out = null;
		try
		{
			L2ShortCut[] scs = cha.getAllShortCuts();

			out = new FileWriter(saveFile);
			out.write("slot;type;id;level;unknown\r\n");
			for (int i = 0; i < scs.length; i++)
			{
				out.write(scs[i].getSlot()+";");
				out.write(scs[i].getType()+";");
				out.write(scs[i].getId()+";");
				out.write(scs[i].getLevel()+";");
				out.write(scs[i].getUnk()+"\r\n");
			}
		}
		catch (Exception e)
		{
			_log.warning("could not store shortcuts:" + e.toString());
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	
	
	
	public void deleteCharFromDisk(int charslot) throws IOException
	{	
		//have to make sure active character must be nulled
		if (getActiveChar() != null)
		{
			saveCharToDisk (getActiveChar());
			_log.fine("active Char saved");
			_activeChar = null;
		}
		
		
		//finds and deletes one file at a time based on which slot was sent back
		File[] chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_char.csv"));
					}
				});
		chars[charslot].delete();
		
		chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_items.csv"));
					}
				});
		
		chars[charslot].delete();
		chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_skills.csv"));
					}
				});
		
		chars[charslot].delete();
		chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_shortcuts.csv"));
					}
				});
		
		chars[charslot].delete();
		chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_warehouse.csv"));
					}
				});
		
		chars[charslot].delete();
		CharNameTable.getInstance().deleteCharName(chars[charslot].getName().replaceAll("_warehouse.csv","").toLowerCase());
	}
	
	public L2PcInstance loadCharFromDisk(int charslot)
	{
		L2PcInstance character = new L2PcInstance();
		
		File[] chars = _charFolder.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return (name.endsWith("_char.csv"));
			}
		});
		character = restoreChar(chars[charslot]);

		if (character != null)
		{
			restoreInventory( new File(_charFolder, character.getName() + "_items.csv"), character);
			restoreSkills( new File(_charFolder, character.getName() + "_skills.csv"), character);
			restoreShortCuts( new File(_charFolder, character.getName() + "_shortcuts.csv"), character);
			restoreWarehouse( new File(_charFolder, character.getName() + "_warehouse.csv"), character);
			if (character.getClanId() != 0)
			{
				L2Clan clan = ClanTable.getInstance().getClan(character.getClanId());
				if (!clan.isMember(character.getName()))
				{
					// char has been kicked from clan
					character.setClanId(0);
					character.setTitle("");
				}
				else
				{
					character.setClan(clan);
					character.setIsClanLeader(clan.getLeaderId() == character.getObjectId());
				}
			}
		}
		else
		{
			_log.warning("could not restore "+ chars[charslot]);
		}
		
		//setCharacter(character);
		return character;
	}

	

	/**
	 * @param file
	 * @param restored
	 */
	private void restoreShortCuts(File file, L2PcInstance restored)
	{
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(file)));
			lnr.readLine();	// skip header

			String line = null;
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, ";");
				
				int slot = Integer.parseInt(st.nextToken());
				int type = Integer.parseInt(st.nextToken());
				int id = Integer.parseInt(st.nextToken());
				int level = Integer.parseInt(st.nextToken());
				int unk = Integer.parseInt(st.nextToken());

				L2ShortCut sc = new L2ShortCut(slot, type, id, level, unk);
				restored.registerShortCut(sc);
			}
		}
		catch (Exception e)
		{
			_log.warning("could not restore shortcuts:"+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	/**
	 * @param cha
	 * @param saveFile
	 */
	private void storeInventory(L2PcInstance cha, File saveFile)
	{
		FileWriter out = null;
		try
		{
			L2ItemInstance[] items = cha.getInventory().getItems();

			out = new FileWriter(saveFile);
			out.write("objectId;itemId;name;count;price;equipSlot;\r\n");
			for (int i = 0; i < items.length; i++)
			{
				L2ItemInstance item = items[i];
				out.write(item.getObjectId()+";");
				out.write(item.getItemId()+";");
				out.write(item.getItem().getName()+";");
				out.write(item.getCount()+";");
				out.write(item.getPrice()+";");
				if (item.getItemId() == 17 || item.getItemId() == 1341 || item.getItemId() == 1342 || item.getItemId() == 1343 || item.getItemId() == 1344 || item.getItemId() == 1345)
				{
					out.write("-1\r\n");
				}
				else
				{
					out.write(item.getEquipSlot()+"\r\n");
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("could not store inventory:"+e);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
		
	}

	/**
	 * @param cha
	 * @param saveFile
	 */
	private void storeSkills(L2PcInstance cha, File saveFile)
	{
		FileWriter out = null;
		try
		{
			L2Skill[] skills = cha.getAllSkills();

			out = new FileWriter(saveFile);
			out.write("skillId;skillLevel;skillName\r\n");
			for (int i = 0; i < skills.length; i++)
			{
				out.write(skills[i].getId()+";");
				out.write(skills[i].getLevel()+";");
				out.write(skills[i].getName()+"\r\n");
			}
		}
		catch (Exception e)
		{
			_log.warning("could not store skills:"+e);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}
	
	
	private void storeChar(L2PcInstance cha, File charFile)
	{
		FileWriter out = null;
		try
		{
			out = new FileWriter(charFile);
			out.write("objId;charName;level;maxHp;curHp;maxMp;curMp;acc;crit;evasion;" +
					"mAtk;mDef;mSpd;pAtk;pDef;pSpd;runSpd;walkSpd;" +
					"str;con;dex;int;men;wit;face;hairStyle;hairColor;sex;" +
					"heading;x;y;z;unk1;unk2;colRad;colHeight;" +
					"exp;sp;karma;pvpkills;pkkills;clanid;maxload;race;classid;deletetime;cancraft;title;allyId\r\n");
			out.write(cha.getObjectId()+";");
			out.write(cha.getName()+";");
			out.write(cha.getLevel()+";");
			out.write(cha.getMaxHp()+";");
			out.write(cha.getCurrentHp()+";");
			out.write(cha.getMaxMp()+";");
			out.write(cha.getCurrentMp()+";");
			out.write(cha.getAccuracy()+";");
			out.write(cha.getCriticalHit()+";");
			out.write(cha.getEvasionRate()+";");
			out.write(cha.getMagicalAttack()+";");
			out.write(cha.getMagicalDefense()+";");
			out.write(cha.getMagicalSpeed()+";");
			out.write(cha.getPhysicalAttack()+";");
			out.write(cha.getPhysicalDefense()+";");
			out.write(cha.getPhysicalSpeed()+";");
			out.write(cha.getRunSpeed()+";");
			out.write(cha.getWalkSpeed()+";");
			out.write(cha.getStr()+";");
			out.write(cha.getCon()+";");
			out.write(cha.getDex()+";");
			out.write(cha.getInt()+";");
			out.write(cha.getMen()+";");
			out.write(cha.getWit()+";");

			out.write(cha.getFace()+";");
			out.write(cha.getHairStyle()+";");
			out.write(cha.getHairColor()+";");
			out.write(cha.getSex()+";");

			out.write(cha.getHeading()+";");
			out.write(cha.getX()+";");
			out.write(cha.getY()+";");
			out.write(cha.getZ()+";");

			out.write(cha.getMovementMultiplier()+";");
			out.write(cha.getAttackSpeedMultiplier()+";");
			out.write(cha.getCollisionRadius()+";");
			out.write(cha.getCollisionHeight()+";");

			out.write(cha.getExp()+";");
			out.write(cha.getSp()+";");
			out.write(cha.getKarma()+";");
			out.write(cha.getPvpKills()+";");
			out.write(cha.getPkKills()+";");
			out.write(cha.getClanId()+";");
			out.write(cha.getMaxLoad()+";");
			out.write(cha.getRace()+";");
			out.write(cha.getClassId()+";");
			out.write(cha.getDeleteTimer()+";");
			out.write(cha.getCanCraft()+";");
			
			out.write(" " + cha.getTitle()+";"); // this is a workaround for the inability of stringtokennizer to handle empty columns
//			out.write(cha.getCrestId()+";");
			out.write(cha.getAllyId()+";");
		}
		catch (IOException e)
		{
			_log.warning("could not store char data:"+e);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	
	private void restoreWarehouse(File wfile, L2PcInstance cha)
	{
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(wfile)));
			lnr.readLine();	// skip header

			String line = null;
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, ";");
				
				L2ItemInstance item = new L2ItemInstance();
				item.setObjectId(Integer.parseInt(st.nextToken()));
				int itemId = Integer.parseInt(st.nextToken());
				L2Item itemTemp = ItemTable.getInstance().getTemplate(itemId);
				item.setItem(itemTemp);
				st.nextToken();	// skip name
				item.setCount(Integer.parseInt(st.nextToken()));
				
				cha.getWarehouse().addItem(item);
				
				// add this item to the world
				_world.storeObject(item);
			}
		}
		catch (Exception e)
		{
			_log.warning("could not restore warehouse:"+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}
	
	private void storeWarehouse(L2PcInstance cha, File saveFile)
	{
		FileWriter out = null;
		try
		{
			ArrayList items = cha.getWarehouse().getItems();

			// \n\r puts extra blank lines that fvck whole saving up :/
			out = new FileWriter(saveFile);
			out.write("#objectId;itemId;name;count;\n");
			for (int i = 0; i < items.size(); i++)
			{
				L2ItemInstance item = (L2ItemInstance) items.get(i);
				out.write(item.getObjectId()+";");
				out.write(item.getItemId()+";");
				out.write(item.getItem().getName()+";");
				out.write(item.getCount()+"\n"); 
			}
		}
		catch (Exception e)
		{
			_log.warning("could not store warehouse:"+e);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}
	
	
	/**
	 * @param string
	 */
	private void restoreInventory(File inventory, L2PcInstance cha)
	{
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(inventory)));
			lnr.readLine();	// skip header

			String line = null;
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, ";");
				
				L2ItemInstance item = new L2ItemInstance();
				item.setObjectId(Integer.parseInt(st.nextToken()));
				int itemId = Integer.parseInt(st.nextToken());
				L2Item itemTemp = ItemTable.getInstance().getTemplate(itemId);
				item.setItem(itemTemp);
				st.nextToken();	// skip name
				item.setCount(Integer.parseInt(st.nextToken()));
				item.setPrice(Integer.parseInt(st.nextToken()));
				// equip slot is the current used slot. -1 == item is not equiped
				item.setEquipSlot(Integer.parseInt(st.nextToken()));
				
				cha.getInventory().addItem(item);
				if (item.isEquipped())
				{
					cha.getInventory().equipItem(item);
				}
				
				// add this item to the world
				_world.storeObject(item);
			}
		}
		catch (Exception e)
		{
			_log.warning("could not restore inventory:"+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	/**
	 * @param string
	 */
	private void restoreSkills(File inventory, L2PcInstance cha)
	{
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(inventory)));
			lnr.readLine();	// skip header

			String line = null;
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, ";");
				
				int id = Integer.parseInt(st.nextToken());
				int level = Integer.parseInt(st.nextToken());
				st.nextToken();	// skip name

				L2Skill skill = SkillTable.getInstance().getInfo(id, level);
				cha.addSkill(skill);
			}
		}
		catch (Exception e)
		{
			_log.warning("could not restore skills:"+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}
	
	
	/**
	 * @param line
	 * @return
	 */
	private L2PcInstance restoreChar(File charFile)
	{
		L2PcInstance oldChar = new L2PcInstance();
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(charFile)));
			
			lnr.readLine();	// skip header
			String line = lnr.readLine();
			StringTokenizer st = new StringTokenizer(line, ";");
			oldChar.setObjectId(Integer.parseInt(st.nextToken()));
			oldChar.setName(st.nextToken());
			oldChar.setLevel(Integer.parseInt(st.nextToken()));
			oldChar.setMaxHp(Integer.parseInt(st.nextToken()));
			oldChar.setCurrentHp(Double.parseDouble(st.nextToken()));
			oldChar.setMaxMp(Integer.parseInt(st.nextToken()));
			oldChar.setCurrentMp(Double.parseDouble(st.nextToken()));
			oldChar.setAccuracy(Integer.parseInt(st.nextToken()));
			oldChar.setCriticalHit(Integer.parseInt(st.nextToken()));
			oldChar.setEvasionRate(Integer.parseInt(st.nextToken()));
			oldChar.setMagicalAttack(Integer.parseInt(st.nextToken()));
			oldChar.setMagicalDefense(Integer.parseInt(st.nextToken()));
			oldChar.setMagicalSpeed(Integer.parseInt(st.nextToken()));
			oldChar.setPhysicalAttack(Integer.parseInt(st.nextToken()));
			oldChar.setPhysicalDefense(Integer.parseInt(st.nextToken()));
			oldChar.setPhysicalSpeed(Integer.parseInt(st.nextToken()));
			oldChar.setRunSpeed(Integer.parseInt(st.nextToken()));
			oldChar.setWalkSpeed(Integer.parseInt(st.nextToken()));
			oldChar.setStr(Integer.parseInt(st.nextToken()));
			oldChar.setCon(Integer.parseInt(st.nextToken()));
			oldChar.setDex(Integer.parseInt(st.nextToken()));
			oldChar.setInt(Integer.parseInt(st.nextToken()));
			oldChar.setMen(Integer.parseInt(st.nextToken()));
			oldChar.setWit(Integer.parseInt(st.nextToken()));

			oldChar.setFace(Integer.parseInt(st.nextToken()));
			oldChar.setHairStyle(Integer.parseInt(st.nextToken()));
			oldChar.setHairColor(Integer.parseInt(st.nextToken()));
			oldChar.setSex(Integer.parseInt(st.nextToken()));
			
			oldChar.setHeading(Integer.parseInt(st.nextToken()));
			oldChar.setX(Integer.parseInt(st.nextToken()));
			oldChar.setY(Integer.parseInt(st.nextToken()));
			oldChar.setZ(Integer.parseInt(st.nextToken()));
			
			oldChar.setMovementMultiplier(Double.parseDouble(st.nextToken()));
			oldChar.setAttackSpeedMultiplier(Double.parseDouble(st.nextToken()));
			oldChar.setCollisionRadius(Double.parseDouble(st.nextToken()));
			oldChar.setCollisionHeight(Double.parseDouble(st.nextToken()));

			oldChar.setExp(Integer.parseInt(st.nextToken()));
			oldChar.setSp(Integer.parseInt(st.nextToken()));
			oldChar.setKarma(Integer.parseInt(st.nextToken()));
			oldChar.setPvpKills(Integer.parseInt(st.nextToken()));
			oldChar.setPkKills(Integer.parseInt(st.nextToken()));
			oldChar.setClanId(Integer.parseInt(st.nextToken()));
			oldChar.setMaxLoad(Integer.parseInt(st.nextToken()));
			oldChar.setRace(Integer.parseInt(st.nextToken()));
			oldChar.setClassId(Integer.parseInt(st.nextToken()));
			oldChar.setFistsWeaponItem(oldChar.findFistsWeaponItem(oldChar.getClassId()));
			oldChar.setDeleteTimer(Integer.parseInt(st.nextToken()));
			oldChar.setCanCraft(Integer.parseInt(st.nextToken()));

			oldChar.setTitle(st.nextToken().trim());   // this is a workaround for the inability of stringtokennizer to handle empty columns
			//oldChar.setCrestId(Integer.parseInt(st.nextToken()));
			oldChar.setAllyId(Integer.parseInt(st.nextToken()));

			L2World.getInstance().storeObject(oldChar);
			
			oldChar.setUptime(System.currentTimeMillis());
		}
		catch (Exception e)
		{
			_log.warning("could not restore char data:"+e);
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				//  ignore problems
			}
		}
		
		return oldChar;
	}

	/**
	 * @return
	 */
	public Connection getConnection()
	{
		return _connection;
	}

	/**
	 * @return
	 */
	public L2PcInstance getActiveChar()
	{
		return _activeChar;
	}
	/**
	 * @return Returns the sessionId.
	 */
	public int getSessionId()
	{
		return _sessionId;
	}

	public String getLoginName()
	{
		return _loginName;
	}
	public void setLoginFolder(String folder)
	{
		_charFolder = new File("data/accounts",_loginName);
		_charFolder.mkdirs();
	}
	
	public void setLoginName(String loginName)
	{
		_loginName = loginName;
	}

	/**
	 * @param cha
	 */
	public void setActiveChar(L2PcInstance cha)
	{
		_activeChar = cha;
		if (cha != null)
		{
			// we store the connection in the player object so that external
			// events can directly send events to the players client
			// might be changed later to use a central event management and distribution system
			_activeChar.setNetConnection(_connection);
			
			// update world data
			_world.storeObject(_activeChar);
		}
	}
	
	/**
	 * @param access
	 */
	public void setAccessLevel(int access)
	{
		_accessLevel = access;
	}
	
	/**
	 * The access level that the account is having. level 0 is a regular user.
	 * @return
	 */
	public int getAccessLevel()
	{
		return _accessLevel;
	}
}
