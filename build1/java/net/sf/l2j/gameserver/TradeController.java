/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/TradeController.java,v 1.5 2004/08/06 00:21:55 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/06 00:21:55 $
 * $Revision: 1.5 $
 * $Log: TradeController.java,v $
 * Revision 1.5  2004/08/06 00:21:55  l2chef
 * player trading added (whatev)
 *
 * Revision 1.4  2004/07/04 11:08:08  l2chef
 * logging is used instead of system.out
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
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2TradeList;
import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/08/06 00:21:55 $
 */
public class TradeController
{
	private static Logger _log = Logger.getLogger(TradeController.class.getName());
	private static TradeController _instance;
	
	private int _nextListId;
	private HashMap _lists;
	
	public static TradeController getInstance()
	{
		if (_instance == null)
		{
			_instance = new TradeController();
		}
		return _instance;
	}
	
	private TradeController()
	{
		_lists = new HashMap();
		
		String line = null;
		LineNumberReader lnr = null;
		int dummyItemCount = 0;
		
		try
		{
			File buylistData = new File("data/buylists.csv");
			lnr = new LineNumberReader(new BufferedReader( new FileReader(buylistData)));
			
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}
				
				dummyItemCount += parseList(line);
			}

			_log.fine("created " + dummyItemCount + " Dummy-Items for buylists");
			_log.config("created " + _lists.size() + " buylists");
		}
		catch (Exception e)
		{
			if (lnr != null)
			{
				_log.warning("error while creating trade controller in linenr: " +lnr.getLineNumber() );
				e.printStackTrace();
			}
			else
			{
				_log.warning("No buylists were found in data folder");
			}
		}
	}
	
	private int parseList(String line)
	{
		int itemCreated = 0;
		StringTokenizer st = new StringTokenizer(line,";");

		int listId = Integer.parseInt(st.nextToken()); 
		L2TradeList buy1 = new L2TradeList(listId);
		while (st.hasMoreTokens())
		{
			int itemId = Integer.parseInt(st.nextToken());
			int price = Integer.parseInt(st.nextToken());
			L2ItemInstance item = ItemTable.getInstance().createDummyItem(itemId);
			item.setPrice(price);
			buy1.addItem(item);
			itemCreated++;
		}
		
		_lists.put(new Integer(buy1.getListId()), buy1);
		return itemCreated;
	}

	public L2TradeList getBuyList(int listId)
	{
		return (L2TradeList) _lists.get(new Integer(listId));
	}


	/**
	 * @return
	 */
	private synchronized int getNextId()
	{
		return _nextListId++;
	}
}
