/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/templates/L2Item.java,v 1.5 2004/09/28 20:31:43 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/28 20:31:43 $
 * $Revision: 1.5 $
 * $Log: L2Item.java,v $
 * Revision 1.5  2004/09/28 20:31:43  l2chef
 * full armor slot handling fixed
 *
 * Revision 1.4  2004/08/04 21:55:16  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.3  2004/07/19 02:03:52  l2chef
 * soulshot code added (MetalRabbit)
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
package net.sf.l2j.gameserver.templates;

import java.io.Serializable;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/09/28 20:31:43 $
 */
public abstract class L2Item implements Serializable
{
	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	public static final int TYPE1_SHIELD_ARMOR = 1;
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;

	public static final int TYPE2_WEAPON = 0;
	public static final int TYPE2_SHIELD_ARMOR = 1;
	public static final int TYPE2_ACCESSORY = 2;
	public static final int TYPE2_QUEST = 3;
	public static final int TYPE2_MONEY = 4;
	public static final int TYPE2_OTHER = 5;
	
	
	public static final int SLOT_NONE = 0x0000;
	public static final int SLOT_UNDERWEAR = 0x0001;
	public static final int SLOT_R_EAR = 0x0002;
	public static final int SLOT_L_EAR = 0x0004;
	public static final int SLOT_NECK = 0x0008;
	public static final int SLOT_R_FINGER = 0x0010;
	public static final int SLOT_L_FINGER = 0x0020;
	public static final int SLOT_HEAD = 0x0040;
	public static final int SLOT_R_HAND = 0x0080;
	public static final int SLOT_L_HAND = 0x0100;
	public static final int SLOT_GLOVES = 0x0200;
	public static final int SLOT_CHEST = 0x0400;
	public static final int SLOT_LEGS = 0x0800;
	public static final int SLOT_FEET = 0x1000;
	public static final int SLOT_BACK = 0x2000;
	public static final int SLOT_LR_HAND = 0x4000;
	public static final int SLOT_FULL_ARMOR = 0x8000;
	
	public static final int MATERIAL_STEEL = 0x00; // ??
	public static final int MATERIAL_FINE_STEEL = 0x01; // ??
	public static final int MATERIAL_BLOOD_STEEL = 0x02; // ??
	public static final int MATERIAL_BRONZE = 0x03; // ??
	public static final int MATERIAL_SILVER = 0x04; // ??
	public static final int MATERIAL_GOLD = 0x05; // ??
	public static final int MATERIAL_MITHRIL = 0x06; // ??
	public static final int MATERIAL_ORIHARUKON = 0x07; // ??
	public static final int MATERIAL_PAPER = 0x08; // ??
	public static final int MATERIAL_WOOD = 0x09; // ??
	public static final int MATERIAL_CLOTH = 0x0a; // ??
	public static final int MATERIAL_LEATHER = 0x0b; // ??
	public static final int MATERIAL_BONE = 0x0c; // ??
	public static final int MATERIAL_HORN = 0x0d; // ??
	public static final int MATERIAL_DAMASCUS = 0x0e; // ??
	public static final int MATERIAL_ADAMANTAITE = 0x0f; // ??
	public static final int MATERIAL_CHRYSOLITE = 0x10; // ??
	public static final int MATERIAL_CRYSTAL = 0x11; // ??
	public static final int MATERIAL_LIQUID = 0x12; // ??
	public static final int MATERIAL_SCALE_OF_DRAGON = 0x13; // ??
	public static final int MATERIAL_DYESTUFF = 0x14; // ??
	public static final int MATERIAL_COBWEB = 0x15; // ??

	public static final int CRYSTAL_NONE = 0x01; // ??
	public static final int CRYSTAL_D = 0x02; // ??
	public static final int CRYSTAL_C = 0x03; // ??
	public static final int CRYSTAL_B = 0x04; // ??
	public static final int CRYSTAL_A = 0x05; // ??
	public static final int CRYSTAL_S = 0x06; // ??
	
	
	private int _itemId;
	private String _name;
	private int _type1;	// needed for item list (inventory)
	private int _type2;	// different lists for armor, weapon, etc
	private int _weight;
	private boolean _crystallizable;
	private boolean _stackable;
	private int _materialType;
	private int _crystalType = CRYSTAL_NONE; // default to none-grade 
	private int _durability;
	private int _bodyPart;
	private int _referencePrice;
	
	/**
	 * @return
	 */
	public int getDurability()
	{
		return _durability;
	}

	/**
	 * @param durability
	 */
	public void setDurability(int durability)
	{
		this._durability = durability;
	}

	/**
	 * @return
	 */
	public int getItemId()
	{
		return _itemId;
	}

	/**
	 * @param itemId
	 */
	public void setItemId(int itemId)
	{
		this._itemId = itemId;
	}

	/**
	 * @return
	 */
	public int getMaterialType()
	{
		return _materialType;
	}

	/**
	 * @param materialType
	 */
	public void setMaterialType(int materialType)
	{
		this._materialType = materialType;
	}

	/**
	 * @return
	 */
	public int getType2()
	{
		return _type2;
	}

	/**
	 * @param type
	 */
	public void setType2(int type)
	{
		this._type2 = type;
	}

	/**
	 * @return
	 */
	public int getWeight()
	{
		return _weight;
	}

	/**
	 * @param weight
	 */
	public void setWeight(int weight)
	{
		this._weight = weight;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @return
	 */
	public boolean isCrystallizable()
	{
		return _crystallizable;
	}

	/**
	 * @param crystallizable
	 */
	public void setCrystallizable(boolean crystallizable)
	{
		this._crystallizable = crystallizable;
	}

	/**
	 * @return
	 */
	public int getCrystalType()
	{
		return _crystalType;
	}

	/**
	 * @param crystalType
	 */
	public void setCrystalType(int crystalType)
	{
		this._crystalType = crystalType;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @return
	 */
	public int getBodyPart()
	{
		return _bodyPart;
	}

	/**
	 * @param bodyPart
	 */
	public void setBodyPart(int bodyPart)
	{
		_bodyPart = bodyPart;
	}

	/**
	 * @return
	 */
	public int getType1()
	{
		return _type1;
	}

	/**
	 * @param type1
	 */
	public void setType1(int type1)
	{
		_type1 = type1;
	}

	/**
	 * @return Returns the stackable.
	 */
	public boolean isStackable()
	{
		return _stackable;
	}

	/**
	 * @param stackable The stackable to set.
	 */
	public void setStackable(boolean stackable)
	{
		_stackable = stackable;
	}
	
	public void setReferencePrice(int price)
	{
		_referencePrice = price;
	}

	public int getReferencePrice()
	{
		return _referencePrice;
	}
	
}
