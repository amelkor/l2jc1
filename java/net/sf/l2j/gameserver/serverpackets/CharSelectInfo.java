/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/CharSelectInfo.java,v 1.6 2004/07/13 22:59:58 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 22:59:58 $
 * $Revision: 1.6 $
 * $Log: CharSelectInfo.java,v $
 * Revision 1.6  2004/07/13 22:59:58  l2chef
 * removed empty constructor
 *
 * Revision 1.5  2004/07/12 20:57:40  l2chef
 * char data is now stored in subfolder data/accounts
 *
 * Revision 1.4  2004/07/11 23:39:01  l2chef
 * chars are always reloaded from disk  (whatev)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.model.CharSelectInfoPackage;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.model.Inventory;
/**
 * This class ...
 * 
 * @version $Revision: 1.6 $ $Date: 2004/07/13 22:59:58 $
 */
public class CharSelectInfo extends ServerBasePacket
{
	// d SdSddddddddddffddddddddddddddddddddddddddddddddddddddddddddddffd
	private static final String _S__1F_CHARSELECTINFO = "[S] 1F CharSelectInfo";
	private static Logger _log = Logger.getLogger(CharSelectInfo.class.getName());
	
	private String _loginName;
	private int _sessionId;
	private CharSelectInfoPackage _charInfopackage;
	private CharSelectInfoPackage [] _characterPackage;
	private String [] _charNameList;
	/**
	 * @param _characters
	 */
	public CharSelectInfo(String loginName, int sessionId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		_characterPackage = loadCharacterSelectInfoFromDisk();
	}


	public byte[] getContent()
	{
		int size = (_characterPackage.length);
		
		_bao.write(0x1f);
		writeD(size);
		
		long count = 0x01e1eb;
		for (int i=0; i < size;i++)
		{
							
			CharSelectInfoPackage charInfoPackage = _characterPackage[i];			
					
			writeS(charInfoPackage.getName());
			writeD(charInfoPackage.getCharId()); //?
			writeS(_loginName);
			writeD(_sessionId);
			writeD(charInfoPackage.getClanId());
			writeD(0x00); //??
			
			writeD(charInfoPackage.getSex());
			writeD(charInfoPackage.getRace());
			writeD(charInfoPackage.getClassId());
			writeD(0x01); // active ??

			writeD(0x00); // noeffect ?
			writeD(0x00); // noeffect ?
			writeD(0x00); // noeffect ?

			writeF(charInfoPackage.getCurrentHp()); // hp cur 
			writeF(charInfoPackage.getCurrentMp()); // mp cur 

			writeD(charInfoPackage.getSp());  
			writeD(charInfoPackage.getExp());	 
			writeD(charInfoPackage.getLevel()); 

			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			writeD(0x00); 
			
			writeD(0x00); 
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR)); 
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR)); 
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK)); 
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));

			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
			writeD(charInfoPackage.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));

			writeD(0x00); 
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));  
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR)); 
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK)); 
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));

			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(charInfoPackage.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));


			writeD(charInfoPackage.getHairStyle()); 
			writeD(charInfoPackage.getHairColor()); 
			writeD(charInfoPackage.getFace());
			
			writeF(charInfoPackage.getMaxHp()); // hp max 
			writeF(charInfoPackage.getMaxMp()); // mp max
			
			writeD(charInfoPackage.getDeleteTimer());  // days left before delete .. if != 0 then char is inactive
		}

		return _bao.toByteArray();
	}

	
	public CharSelectInfoPackage [] loadCharacterSelectInfoFromDisk()
	{
		File _charFolder = new File("data/accounts",_loginName);
		_charFolder.mkdirs();
		CharSelectInfoPackage [] characters;
		
		
		
		File[] chars = _charFolder.listFiles(new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						return (name.endsWith("_char.csv"));
					}
				});
		
		_log.fine("found " + chars.length + " characters on disk.");
		
		_charNameList = new String [chars.length];
		characters = new CharSelectInfoPackage [chars.length];
		for (int i = 0; i < chars.length; i++)
		{
			_charInfopackage = new CharSelectInfoPackage();
		    restoreChar(chars[i]);
		    
		    if (_charInfopackage != null)
			{
				restoreInventory( new File(_charFolder, _charInfopackage.getName() + "_items.csv"));
				characters[i] = _charInfopackage;
				_charNameList[i] = _charInfopackage.getName();
			}
			else
			{
				_log.warning("could not restore "+ chars[i]);
			}
		}
		
		return characters;
	}
	
	private void restoreInventory(File inventory)
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
				
				_charInfopackage.getInventory().addItem(item);
				if (item.isEquipped())
				{
					 _charInfopackage.getInventory().equipItem(item);
				}
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
	
	
	private void restoreChar(File charFile)
	{
		LineNumberReader lnr = null;
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(charFile)));
			lnr.readLine();	// skip header
			String line = lnr.readLine();
			StringTokenizer st = new StringTokenizer(line, ";");
			st.nextToken();
			_charInfopackage.setName( st.nextToken() );
			_charInfopackage.setLevel( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setMaxHp( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setCurrentHp( Double.parseDouble(st.nextToken()) );
			_charInfopackage.setMaxMp( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setCurrentMp( Double.parseDouble(st.nextToken()) );
			
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();

			_charInfopackage.setFace( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setHairStyle( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setHairColor( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setSex( Integer.parseInt(st.nextToken()) );
			
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();

			_charInfopackage.setExp( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setSp( Integer.parseInt(st.nextToken()) );
			st.nextToken();
			st.nextToken();
			st.nextToken();
			_charInfopackage.setClanId( Integer.parseInt(st.nextToken()) );
			st.nextToken();
			_charInfopackage.setRace( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setClassId( Integer.parseInt(st.nextToken()) );
			_charInfopackage.setDeleteTimer( Integer.parseInt(st.nextToken()) );
			st.nextToken();

			st.nextToken().trim();   // this is a workaround for the inability of stringtokennizer to handle empty columns
			//CharInfopackage.setCrestId(Integer.parseInt(st.nextToken()));
			st.nextToken();
		}
		catch (Exception e)
		{
			_log.warning("error while loading charfile:" + charFile + " "+e.toString());
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

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String [] getCharacterlist()
	{
				
		return _charNameList;
	}
	
	public String getType()
	{
		return _S__1F_CHARSELECTINFO;
	}

}
