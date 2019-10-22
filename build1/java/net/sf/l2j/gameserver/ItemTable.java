/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/ItemTable.java,v 1.7 2004/09/28 20:30:55 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/28 20:30:55 $
 * $Revision: 1.7 $
 * $Log: ItemTable.java,v $
 * Revision 1.7  2004/09/28 20:30:55  l2chef
 * full armor slot handling fixed
 *
 * Revision 1.6  2004/09/24 20:39:28  jeichhorn
 * added simple init check
 *
 * Revision 1.5  2004/07/13 23:14:31  l2chef
 * broken item lines do not abort parsing
 *
 * Revision 1.4  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.3  2004/06/27 20:50:02  l2chef
 * better error message when data file is missing. skipping of comment lines
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
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2EtcItem;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * This class ...
 * 
 * @version $Revision: 1.7 $ $Date: 2004/09/28 20:30:55 $
 */
public class ItemTable
{
	private static Logger _log = Logger.getLogger(ItemTable.class.getName());
		
	private static final int TYPE_ETC_ITEM = 0;
	private static final int TYPE_ARMOR = 1;
	private static final int TYPE_WEAPON = 2;
	
	private static final HashMap _materials = new HashMap();
	private static final HashMap _crystalTypes = new HashMap();
	private static final HashMap _weaponTypes = new HashMap();
	private static final HashMap _armorTypes = new HashMap();
	private static final HashMap _slots = new HashMap();
	
	private L2Item[] _allTemplates;
	private HashMap _etcItems;
	private HashMap _armors;
	private HashMap _weapons;
	
	private boolean _initialized = true;
	
	static
	{
		_materials.put("paper", new Integer(L2Item.MATERIAL_PAPER));
		_materials.put("wood", new Integer(L2Item.MATERIAL_WOOD));
		_materials.put("liquid", new Integer(L2Item.MATERIAL_LIQUID));
		_materials.put("cloth", new Integer(L2Item.MATERIAL_CLOTH));
		_materials.put("leather", new Integer(L2Item.MATERIAL_LEATHER));
		_materials.put("horn", new Integer(L2Item.MATERIAL_HORN));
		_materials.put("bone", new Integer(L2Item.MATERIAL_BONE));
		_materials.put("bronze", new Integer(L2Item.MATERIAL_BRONZE));
		_materials.put("fine_steel", new Integer(L2Item.MATERIAL_FINE_STEEL));
		_materials.put("cotton", new Integer(L2Item.MATERIAL_FINE_STEEL));
		_materials.put("mithril", new Integer(L2Item.MATERIAL_MITHRIL));
		_materials.put("silver", new Integer(L2Item.MATERIAL_SILVER));
		_materials.put("gold", new Integer(L2Item.MATERIAL_GOLD));
		_materials.put("adamantaite", new Integer(L2Item.MATERIAL_ADAMANTAITE));
		_materials.put("steel", new Integer(L2Item.MATERIAL_STEEL));
		_materials.put("oriharukon", new Integer(L2Item.MATERIAL_ORIHARUKON));
		_materials.put("blood_steel", new Integer(L2Item.MATERIAL_BLOOD_STEEL));
		_materials.put("crystal", new Integer(L2Item.MATERIAL_CRYSTAL));
		_materials.put("damascus", new Integer(L2Item.MATERIAL_DAMASCUS));
		_materials.put("chrysolite", new Integer(L2Item.MATERIAL_CHRYSOLITE));
		_materials.put("scale_of_dragon", new Integer(L2Item.MATERIAL_SCALE_OF_DRAGON));
		_materials.put("dyestuff", new Integer(L2Item.MATERIAL_DYESTUFF));
		_materials.put("cobweb", new Integer(L2Item.MATERIAL_COBWEB));
		
		_crystalTypes.put("s", new Integer(L2Item.CRYSTAL_S));
		_crystalTypes.put("a", new Integer(L2Item.CRYSTAL_A));
		_crystalTypes.put("b", new Integer(L2Item.CRYSTAL_B));
		_crystalTypes.put("c", new Integer(L2Item.CRYSTAL_C));
		_crystalTypes.put("d", new Integer(L2Item.CRYSTAL_D));
		_crystalTypes.put("none", new Integer(L2Item.CRYSTAL_NONE));

		_weaponTypes.put("blunt", new Integer(L2Weapon.WEAPON_TYPE_BLUNT));
		_weaponTypes.put("bow", new Integer(L2Weapon.WEAPON_TYPE_BOW));
		_weaponTypes.put("dagger", new Integer(L2Weapon.WEAPON_TYPE_DAGGER));
		_weaponTypes.put("dual", new Integer(L2Weapon.WEAPON_TYPE_DUAL));
		_weaponTypes.put("dualfist", new Integer(L2Weapon.WEAPON_TYPE_DUALFIST));
		_weaponTypes.put("etc", new Integer(L2Weapon.WEAPON_TYPE_ETC));
		_weaponTypes.put("fist", new Integer(L2Weapon.WEAPON_TYPE_FIST));
		_weaponTypes.put("none", new Integer(L2Weapon.WEAPON_TYPE_NONE)); // these are shields !
		_weaponTypes.put("pole", new Integer(L2Weapon.WEAPON_TYPE_POLE));
		_weaponTypes.put("sword", new Integer(L2Weapon.WEAPON_TYPE_SWORD));
		
		_armorTypes.put("none", new Integer(L2Armor.ARMORTYPE_NONE));
		_armorTypes.put("light", new Integer(L2Armor.ARMORTYPE_LIGHT));
		_armorTypes.put("heavy", new Integer(L2Armor.ARMORTYPE_HEAVY));
		_armorTypes.put("magic", new Integer(L2Armor.ARMORTYPE_MAGIC));
		
		_slots.put("chest", new Integer(L2Item.SLOT_CHEST));
		_slots.put("CHEST", new Integer(L2Item.SLOT_FULL_ARMOR)); 
		_slots.put("head", new Integer(L2Item.SLOT_HEAD));
		_slots.put("underwear", new Integer(L2Item.SLOT_UNDERWEAR));
		_slots.put("back", new Integer(L2Item.SLOT_BACK));
		_slots.put("neck", new Integer(L2Item.SLOT_NECK));
		_slots.put("legs", new Integer(L2Item.SLOT_LEGS));
		_slots.put("feet", new Integer(L2Item.SLOT_FEET));
		_slots.put("gloves", new Integer(L2Item.SLOT_GLOVES));
		_slots.put("chest,legs", new Integer(L2Item.SLOT_CHEST | L2Item.SLOT_LEGS));
		_slots.put("rhand", new Integer(L2Item.SLOT_R_HAND));
		_slots.put("lhand", new Integer(L2Item.SLOT_L_HAND));
		_slots.put("lrhand", new Integer(L2Item.SLOT_LR_HAND));
		_slots.put("rear,lear", new Integer(L2Item.SLOT_L_EAR | L2Item.SLOT_R_EAR));
		_slots.put("rfinger,lfinger", new Integer(L2Item.SLOT_L_FINGER | L2Item.SLOT_R_FINGER));
		_slots.put("none", new Integer(L2Item.SLOT_NONE));
	}

	
	private static ItemTable _instance;
	
	public static ItemTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new ItemTable();
		}
		return _instance;
	}
	
	public ItemTable()
	{
	    File etcItemFile = new File("data/etcitem.csv");
	    if (!etcItemFile.isFile() && !etcItemFile.canRead())
	    {
	        _initialized = false;
	    }
	    
	    File armorFile = new File("data/armor.csv");
	    if (!armorFile.isFile() && !armorFile.canRead())
	    {
	        _initialized = false;
	    }
	    
	    File weaponFile = new File("data/weapon.csv");
	    if (!weaponFile.isFile() && !weaponFile.canRead())
	    {
	        _initialized = false;
	    }
	    
		parseEtcItems(etcItemFile);
		parseArmors(armorFile);
		parseWeapons(weaponFile);
		
		buildFastLookupTable();
	}
	
	public boolean isInitialized()
	{
	    return _initialized;
	}
	
	private void parseEtcItems(File data)
	{
		_etcItems = parseFile(data, TYPE_ETC_ITEM);
		_log.config("parsed " + _etcItems.size() + " etc items");
		fixEtcItems(_etcItems);
	}
	
	/**
	 * @param items
	 */
	private void fixEtcItems(HashMap items)
	{
		for (Iterator iter = items.keySet().iterator(); iter.hasNext();)
		{
			Integer key = (Integer) iter.next();
			L2EtcItem item = (L2EtcItem) items.get(key);
	
			if (item.getWeight() == 0 && item.getEtcItemType() != L2EtcItem.TYPE_MONEY && 
					!item.getName().startsWith("world_map") &&
					!item.getName().startsWith("crystal_"))
			{
				item.setType2(L2Item.TYPE2_QUEST);
				item.setEtcItemType(L2EtcItem.TYPE_QUEST);
			}
			else if (item.getName().startsWith("sb_"))
			{
				item.setType2(L2Item.TYPE2_OTHER);
				item.setEtcItemType(L2EtcItem.TYPE_SPELLBOOK);
			}
			else if (item.getName().startsWith("rp_"))
			{
				item.setType2(L2Item.TYPE2_OTHER);
				item.setEtcItemType(L2EtcItem.TYPE_RECEIPE);
			}
			else if (item.getName().startsWith("q_"))
			{
				item.setType2(L2Item.TYPE2_QUEST);
				item.setEtcItemType(L2EtcItem.TYPE_QUEST);
			}
		}
	}

	private HashMap parseFile(File dataFile, int type)
	{
		HashMap result = new HashMap();
		LineNumberReader lnr = null;
		L2Item temp = null;

		try 
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(dataFile)));

			String line = null;
			while ( (line=lnr.readLine()) != null)
			{
				try
				{
					// skip all empty lines and all comment lines
					if (line.trim().length() == 0 || line.startsWith("#"))
					{
						continue;
					}
					
					switch (type)
					{
						case TYPE_ETC_ITEM:
							temp = parseEtcLine(line);
							break;
						case TYPE_ARMOR:
							temp = parseArmorLine(line);
							break;
						case TYPE_WEAPON:
							temp = parseWeaponLine(line);
							break;
					}
	
					result.put(new Integer(temp.getItemId()), temp);
				}
				catch (Exception e)
				{
					_log.warning("error while parsing item:"+line + " "+ e);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("error while parsing items:"+e);
		}

		return result;
		
	}
	
	private L2EtcItem parseEtcLine(String line)
	{
		L2EtcItem result = new L2EtcItem();
		try
		{
			StringTokenizer	st = new StringTokenizer(line, ";");
			result.setItemId( Integer.parseInt(st.nextToken()) );
			result.setName(st.nextToken());
			result.setCrystallizable(Boolean.valueOf(st.nextToken()).booleanValue());
			String itemType = st.nextToken();
			result.setType1(L2Item.TYPE1_ITEM_QUESTITEM_ADENA);
			if (itemType.equals("none"))
			{
				// only for default
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_OTHER);
			}
			else if (itemType.equals("arrow"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_ARROW);
				result.setBodyPart(L2Item.SLOT_L_HAND);				
			}
			else if (itemType.equals("castle_guard"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_SCROLL); // dummy
			}
			else if (itemType.equals("material"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_MATERIAL); 
			}
			else if (itemType.equals("pet_collar"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_PET_COLLAR); 
			}
			else if (itemType.equals("potion"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_POTION); 
			}
			else if (itemType.equals("recipe"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_RECEIPE); 
			}
			else if (itemType.equals("scroll"))
			{
				result.setType2(L2Item.TYPE2_OTHER);
				result.setEtcItemType(L2EtcItem.TYPE_SCROLL); 
			}
			else
			{
				_log.warning("unknown etcitem type:" + itemType);
			}

			result.setWeight( Integer.parseInt(st.nextToken()) );
			
			String consume = st.nextToken();
			if (consume.equals("asset"))
			{
				result.setStackable(true);
				result.setEtcItemType(L2EtcItem.TYPE_MONEY);
				result.setType2(L2Item.TYPE2_MONEY);
			}
			else if ( consume.equals("stackable"))
			{
				result.setStackable(true);
			}
			
			Integer material = (Integer)_materials.get(st.nextToken());
			result.setMaterialType(material.intValue());		

			Integer crystal = (Integer)_crystalTypes.get(st.nextToken());
			result.setCrystalType(crystal.intValue());
			
			result.setDurability( Integer.parseInt(st.nextToken()));
		}
		catch (Exception e)
		{
			_log.warning("data error on etc item:" + result + " "+e );
		}
		
		return result;
	}

	
	private L2Armor parseArmorLine(String line)
	{
		L2Armor result = new L2Armor();
		try
		{
			StringTokenizer	st = new StringTokenizer(line, ";");
			result.setItemId( Integer.parseInt(st.nextToken()) );
			result.setName(st.nextToken());
			
			Integer bodyPart = (Integer)_slots.get(st.nextToken());
			result.setBodyPart(bodyPart.intValue());
			
			result.setCrystallizable(Boolean.valueOf(st.nextToken()).booleanValue());
			
			Integer armor = (Integer)_armorTypes.get(st.nextToken());
			result.setArmorType(armor.intValue());
			int slot = result.getBodyPart();
			if (slot == L2Item.SLOT_NECK ||
				(slot & L2Item.SLOT_L_EAR) != 0 || 
				(slot & L2Item.SLOT_L_FINGER) != 0)
			{
				result.setType1(L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
				result.setType2(L2Item.TYPE2_ACCESSORY);
			}
			else
			{
				result.setType1(L2Item.TYPE1_SHIELD_ARMOR);
				result.setType2(L2Item.TYPE2_SHIELD_ARMOR);
			}
			
			result.setWeight( Integer.parseInt(st.nextToken()) );
			
			Integer material = (Integer)_materials.get(st.nextToken());
			result.setMaterialType(material.intValue());

			Integer crystal = (Integer)_crystalTypes.get(st.nextToken());
			result.setCrystalType(crystal.intValue());
			
			result.setAvoidModifier( Integer.parseInt(st.nextToken()) );
			
			result.setDurability( Integer.parseInt(st.nextToken()) );
			
			result.setPDef( Integer.parseInt(st.nextToken()) );

			result.setMDef( Integer.parseInt(st.nextToken()) );

			result.setMpBonus( Integer.parseInt(st.nextToken()) );
		}
		catch (Exception e)
		{
			_log.warning("data error on armor:" + result + " line: "+line );
			e.printStackTrace();
		}
		
		return result;
	}	

	private L2Weapon parseWeaponLine(String line)
	{
		L2Weapon result = new L2Weapon();
		
		try
		{
			StringTokenizer	st = new StringTokenizer(line, ";");
			result.setItemId( Integer.parseInt(st.nextToken()) );
			result.setName(st.nextToken());
			
			result.setType1(L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
			result.setType2(L2Item.TYPE2_WEAPON);
			
			Integer bodyPart = (Integer)_slots.get(st.nextToken());
			result.setBodyPart(bodyPart.intValue());
			
			result.setCrystallizable(Boolean.valueOf(st.nextToken()).booleanValue());
			
			result.setWeight( Integer.parseInt(st.nextToken()) );
			
			result.setSoulShotCount( Integer.parseInt(st.nextToken()) );

			result.setSpiritShotCount( Integer.parseInt(st.nextToken()) );

			Integer material = (Integer)_materials.get(st.nextToken());
			result.setMaterialType(material.intValue());

			Integer crystal = (Integer)_crystalTypes.get(st.nextToken());
			result.setCrystalType(crystal.intValue());
			
			result.setPDamage( Integer.parseInt(st.nextToken()) );
			
			result.setRandomDamage( Integer.parseInt(st.nextToken()) );
			
			Integer weapon = (Integer)_weaponTypes.get(st.nextToken());
			result.setWeaponType(weapon.intValue());
			
			// lets see if this is a shield
			if (weapon.intValue() == L2Weapon.WEAPON_TYPE_NONE)
			{
				result.setType1(L2Item.TYPE1_SHIELD_ARMOR);
				result.setType2(L2Item.TYPE2_SHIELD_ARMOR);
			}
			
			result.setCritical( Integer.parseInt(st.nextToken()) );
			
			result.setHitModifier( Double.parseDouble(st.nextToken()) );

			result.setAvoidModifier( Integer.parseInt(st.nextToken()) );

			result.setShieldDef( Integer.parseInt(st.nextToken()) );

			result.setShieldDefRate( Integer.parseInt(st.nextToken()) );

			result.setAttackSpeed( Integer.parseInt(st.nextToken()) );
			
			result.setMpConsume( Integer.parseInt(st.nextToken()) );
			
			result.setMDamage( Integer.parseInt(st.nextToken()) );
			
			result.setDurability( Integer.parseInt(st.nextToken()) );
		}
		catch (Exception e)
		{
			_log.warning("data error on weapon:" + result + " line: "+line );
			e.printStackTrace();
		}

		return result;
	}	
/**
	 * @return
	 */
	private void parseArmors(File data)
	{
		_armors = parseFile(data, TYPE_ARMOR);
		_log.config("parsed " + _armors.size() + " armors");
//		fixEtcItems(items);
	}

	/**
	 * @return
	 */
	private void parseWeapons(File data)
	{
		_weapons = parseFile(data, TYPE_WEAPON);
		_log.config("parsed " + _weapons.size() + " weapons");
//		fixEtcItems(items);
	}
	
	
	private void buildFastLookupTable()
	{
		int highestId = 0;		
		
		for (Iterator iter = _armors.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2Armor item = (L2Armor) _armors.get(id);
			if (item.getItemId() > highestId)
			{
				highestId = item.getItemId();
			}
		}

		for (Iterator iter = _weapons.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2Weapon item = (L2Weapon) _weapons.get(id);
			if (item.getItemId() > highestId)
			{
				highestId = item.getItemId();
			}
		}
		
		for (Iterator iter = _etcItems.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2EtcItem item = (L2EtcItem) _etcItems.get(id);
			if (item.getItemId() > highestId)
			{
				highestId = item.getItemId();
			}
		}
		
		_log.fine("highest item id used:" + highestId);
		_allTemplates = new L2Item[highestId +1];
		
		for (Iterator iter = _armors.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2Armor item = (L2Armor) _armors.get(id);
			_allTemplates[id.intValue()] = item;
		}

		for (Iterator iter = _weapons.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2Weapon item = (L2Weapon) _weapons.get(id);
			_allTemplates[id.intValue()] = item;
		}
		
		for (Iterator iter = _etcItems.keySet().iterator(); iter.hasNext();)
		{
			Integer id = (Integer) iter.next();
			L2EtcItem item = (L2EtcItem) _etcItems.get(id);
			_allTemplates[id.intValue()] = item;
		}
	}

	public L2Item getTemplate(int id)
	{
		return _allTemplates[id];
	}

	public L2ItemInstance createItem(int itemId)
	{
		L2ItemInstance temp = new L2ItemInstance();
		temp.setObjectId(IdFactory.getInstance().getNextId());
		temp.setItem(getTemplate(itemId));
		
		_log.fine("item created  oid:" + temp.getObjectId()+ " itemid:" + itemId);
		L2World.getInstance().storeObject(temp);
		return temp; 	
	}

	public L2ItemInstance createDummyItem(int itemId)
	{
		L2ItemInstance temp = new L2ItemInstance();
		temp.setObjectId(0);
		L2Item item = null;
		try
		{
			item = getTemplate(itemId);
		} 
		catch (ArrayIndexOutOfBoundsException e)
		{
			// this can happen if the item templates were not initialized
		}
		
		if (item == null)
		{
			_log.warning("item template missing. id:"+ itemId);
		}
		else
		{
			temp.setItem(item);
		}
		
		return temp; 	
	}

	
}
