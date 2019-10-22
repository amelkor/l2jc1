/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2TradeList.java,v 1.4 2004/10/17 06:46:22 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/10/17 06:46:22 $
 * $Revision: 1.4 $
 * $Log: L2TradeList.java,v $
 * Revision 1.4  2004/10/17 06:46:22  l2chef
 * no more direct access to Item collection to avoid wrong usage
 *
 * Revision 1.3  2004/09/18 01:41:39  whatev66
 * added private store buy/sell
 *
 * Revision 1.2  2004/08/08 16:29:46  l2chef
 * methodname fixed
 *
 * Revision 1.1  2004/08/06 00:22:12  l2chef
 * player trading added (whatev)
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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.ItemTable;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.4 $ $Date: 2004/10/17 06:46:22 $
 */
public class L2TradeList
{
	private ArrayList _items;
	private int _listId;
	private boolean _confirmed;
	private String _Buystorename,_Sellstorename;
	
	public L2TradeList(int listId)
	{
		_items = new ArrayList();
		_listId = listId;
		_confirmed = false;
	}
	
	
	public void addItem(L2ItemInstance item)
	{
		_items.add(item);
	}
	
	/**
	 * @return Returns the listId.
	 */
	public int getListId()
	{
		return _listId;
	}
	public void setSellStoreName(String name)
	{
		_Sellstorename = name;
	}
	public String getSellStoreName()
	{
		return _Sellstorename;
	}
	public void setBuyStoreName(String name)
	{
		_Buystorename = name;
	}
	public String getBuyStoreName()
	{
		return _Buystorename;
	}
	
	/**
	 * @return Returns the items.
	 */
	public ArrayList getItems()
	{
		return _items;
	}
	
	public int getPriceForItemId(int itemId)
	{
		for (int i = 0; i < _items.size(); i++)
		{
			L2ItemInstance item = (L2ItemInstance) _items.get(i);
			if (item.getItemId() == itemId)
			{
				return item.getPrice();
			}
		}
		return -1;
	}
	public L2ItemInstance getItem(int ObjectId)
	{
		for (int i = 0; i < _items.size(); i++)
		{
			L2ItemInstance item = (L2ItemInstance) _items.get(i);
			if (item.getObjectId() == ObjectId)
			{
				return item;
			}
		}
		return null;
	}
	public void setConfirmedTrade(boolean x)
	{
		_confirmed = x;
	}
	public boolean hasConfirmed()
	{
		return _confirmed;
	}
	public void removeItem(int objId,int count)
	{
		boolean bool = false;
		L2ItemInstance temp;
		for(int y = 0 ; y < _items.size(); y++)
		{
			temp = (L2ItemInstance) _items.get(y);
			if (temp.getObjectId()  == objId)
			{
				if (count == temp.getCount())
				{
					_items.remove(temp);
				}
				
				break;
			}
		}
		
		
	}
	
	public boolean contains(int objId)
	{
		boolean bool = false;
		L2ItemInstance temp;
		for(int y = 0 ; y < _items.size(); y++)
		{
			temp = (L2ItemInstance) _items.get(y);
			if (temp.getObjectId()  == objId)
			{
				bool = true;
				break;
			}
		}
		
		return bool;
	}
	public void tradeItems(L2PcInstance player,L2PcInstance reciever)
	{
		Inventory playersInv = player.getInventory();
		Inventory recieverInv = reciever.getInventory();
		L2ItemInstance playerItem,recieverItem,temp,newitem;
		InventoryUpdate update = new InventoryUpdate();
		ItemTable itemTable = ItemTable.getInstance();
		
		for(int y = 0 ; y < _items.size(); y++)
		{
			temp = (L2ItemInstance) _items.get(y);
			playerItem = playersInv.getItem(temp.getObjectId());
			newitem = itemTable.createItem(playerItem.getItemId());
			newitem.setCount(temp.getCount());
			
			playerItem = playersInv.destroyItem(playerItem.getObjectId(),temp.getCount());
			recieverItem = recieverInv.addItem(newitem);
			
			if (playerItem.getLastChange() == L2ItemInstance.MODIFIED)
			{
				update.addModifiedItem(playerItem);
			}
			else
			{
				L2World world = L2World.getInstance();
				world.removeObject(playerItem);
				update.addRemovedItem(playerItem);
				
			}
			
			player.sendPacket(update);
			
			update = new InventoryUpdate();
			if (recieverItem.getLastChange() == L2ItemInstance.MODIFIED)
			{
				update.addModifiedItem(recieverItem);
			}
			else
			{
				update.addNewItem(recieverItem);
			}
			
			reciever.sendPacket(update);
		}
	}
	public void updateBuyList(L2PcInstance player,ArrayList list)
	{
		
		TradeItem temp;
		int count;
		Inventory playersInv = player.getInventory();
		L2ItemInstance temp2;
		count =0;
		
		while(count!= list.size())
		{
			temp = (TradeItem)list.get(count);
			temp2 =playersInv.findItemByItemId(temp.getItemId());
			if (temp2 == null) 
			{
				list.remove(count);
				count = count-1;
			}
			else
			{
				if ( temp.getCount() ==0)
				{
					list.remove(count);
					count =count-1;
				}
			}
			count++;
		}
		
	}
	public void updateSellList(L2PcInstance player,ArrayList list)
	{
		Inventory playersInv = player.getInventory();
		TradeItem temp;
		L2ItemInstance temp2;
		int count =0;
		while(count != list.size())
		{
			temp = (TradeItem)list.get(count);
			temp2 =playersInv.getItem(temp.getObjectId());
			if (temp2 == null) 
			{
				list.remove(count);
				count = count-1;
			}
			else
			{
				if (temp2.getCount() < temp.getCount())
				{
					temp.setCount(temp2.getCount());
				}
				
			}
			count++;
		} 
		
	}
	
	public synchronized void BuySellItems(L2PcInstance buyer,ArrayList buyerslist,L2PcInstance seller,ArrayList sellerslist)
	{
		Inventory sellerInv = seller.getInventory();
		Inventory buyerInv = buyer.getInventory();
		TradeItem buyerItem,temp2=null;
		L2ItemInstance sellerItem =null,temp,newitem = null,adena;
		InventoryUpdate buyerupdate = new InventoryUpdate(),sellerupdate = new InventoryUpdate();
		ItemTable itemTable = ItemTable.getInstance();
		int cost = 0,amount,size,x,y=0;
		ArrayList sysmsgs = new ArrayList();
		SystemMessage msg;
		
		for( ; y <  buyerslist.size(); y++)
		{
			buyerItem = (TradeItem)buyerslist.get(y);
			
				for(x=0 ; x < sellerslist.size(); x++)//find in sellerslist
				{
					temp2 = (TradeItem) sellerslist.get(x);
					if (temp2.getItemId() == buyerItem.getItemId())
					{
						sellerItem = sellerInv.findItemByItemId(buyerItem.getItemId());
						break;
					}
				}
				
				if (sellerItem !=null)
				{ 
				//if (sellerslist.contains(temp2))
				//{
					if (buyerItem.getCount()> temp2.getCount())
					{
						amount = temp2.getCount();
					}
					else
					{
						amount = buyerItem.getCount();
					}
					sellerItem = sellerInv.destroyItem(sellerItem.getObjectId(),amount);
					cost =  buyerItem.getCount() * buyerItem.getOwnersPrice();
					seller.addAdena(cost);
					newitem = itemTable.createItem(sellerItem.getItemId());
					newitem.setCount(amount);
					temp = buyerInv.addItem(newitem);
					if (amount == 1)//system msg stuff
					{
						msg = new SystemMessage(SystemMessage.S1_PURCHASED_S2);
						msg.addString(buyer.getName());
						msg.addItemName(sellerItem.getItemId());
						sysmsgs.add(msg);
						msg = new SystemMessage(SystemMessage.S1_PURCHASED_S2);
						msg.addString("You");
						msg.addItemName(sellerItem.getItemId());
						sysmsgs.add(msg);
					}
					else
					{
						msg = new SystemMessage(SystemMessage.S1_PURCHASED_S3_S2_s);
						msg.addString(buyer.getName());
						msg.addItemName(sellerItem.getItemId());
						msg.addNumber(amount);
						sysmsgs.add(msg);
						msg = new SystemMessage(SystemMessage.S1_PURCHASED_S3_S2_s);
						msg.addString("You");
						msg.addItemName(sellerItem.getItemId());
						msg.addNumber(amount);
						sysmsgs.add(msg);
					}
					buyer.reduceAdena(cost);
					if(temp2.getCount() == buyerItem.getCount())
					{
						sellerslist.remove(x);
						buyerItem.setCount(0);
					}
					else
					{
						if (buyerItem.getCount()< temp2.getCount())
						{	
							temp2.setCount(temp2.getCount()-buyerItem.getCount());
						}
						else
						{
							buyerItem.setCount(buyerItem.getCount()-temp2.getCount());
						}
					}
					
					
					if (sellerItem .getLastChange() == L2ItemInstance.MODIFIED)
					{
						sellerupdate.addModifiedItem(sellerItem);
						
					}
					else
					{
						L2World world = L2World.getInstance();
						world.removeObject(sellerItem );
						sellerupdate.addRemovedItem(sellerItem );
						
					}
					
					
					
					if (temp.getLastChange() == L2ItemInstance.MODIFIED)
					{
						buyerupdate.addModifiedItem(temp);
					}
					else
					{
						buyerupdate.addNewItem(temp);
					}
					
					
				//}
				
				sellerItem =  null;
			}
		}
		if (newitem != null)
		{
			//updateSellList(seller,sellerslist);
			adena = seller.getInventory().getAdenaInstance();
			adena.setLastChange(L2ItemInstance.MODIFIED);
			sellerupdate.addModifiedItem(adena);
			adena = buyer.getInventory().getAdenaInstance();
			adena.setLastChange(L2ItemInstance.MODIFIED);
			buyerupdate.addModifiedItem(adena);
			
			seller.sendPacket(sellerupdate);
			buyer.sendPacket(buyerupdate );
			y=0;
			
			for (x=0;x < sysmsgs.size();x++)
			{
				
				if (y == 0)
				{
					seller.sendPacket((SystemMessage) sysmsgs.get(x));
					y=1;
				}
				else
				{
					buyer.sendPacket((SystemMessage) sysmsgs.get(x));
					y=0;
				}
			}
		}
	}
		
	
	
}

