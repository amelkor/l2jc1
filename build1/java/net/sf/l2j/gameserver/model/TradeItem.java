/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/TradeItem.java,v 1.2 2004/09/24 19:42:19 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/09/24 19:42:19 $
 * $Revision: 1.2 $
 * $Log: TradeItem.java,v $
 * Revision 1.2  2004/09/24 19:42:19  jeichhorn
 * fixed file comments
 *
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

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/24 19:42:19 $
 */
public class TradeItem
{
    private int _ObjectId;
    private int _ItemId;
    private int _Price;
    private int _storePrice;
    private int _count;

    public void setObjectId(int id)
    {
        _ObjectId = id;
    }

    public int getObjectId()
    {
        return _ObjectId;
    }

    public void setItemId(int id)
    {
        _ItemId = id;
    }

    public int getItemId()
    {
        return _ItemId;
    }

    public void setOwnersPrice(int price)
    {
        _Price = price;
    }

    public int getOwnersPrice()
    {
        return _Price;
    }

    public void setstorePrice(int price)
    {
        _storePrice = price;
    }

    public int getStorePrice()
    {
        return _storePrice;
    }

    public void setCount(int count)
    {
        _count = count;
    }

    public int getCount()
    {
        return _count;
    }

}