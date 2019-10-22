/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/PriceList.java,v 1.2 2004/09/28 02:13:08 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/28 02:13:08 $
 * $Revision: 1.2 $
 * $Log: PriceList.java,v $
 * Revision 1.2  2004/09/28 02:13:08  nuocnam
 * Added javadoc header.
 *
 * Revision 1.1  2004/08/04 21:54:40  l2chef
 * reference prices added (Deth)
 *
 * Revision 1.0  2004/08/05 00:30:00  deth
 * first release
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.lang.Integer;

import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.ItemTable;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/09/28 02:13:08 $
 */
public class PriceList
{
	private static Logger _log = Logger.getLogger(PriceList.class.getName());
	
	private static PriceList _instance;

	public PriceList()
	{
		loadPriceList();
	}
	
	public static PriceList getInstance()
	{
		if (_instance == null)
		{
			_instance = new PriceList();
		}
		
		return _instance;
	}
	
	
	public void loadPriceList()
	{
		File file = new File("data/pricelist.csv");
		if (file.exists())
		{
			readFromDisk(file);
		}
		else
		{
			_log.config("data/pricelist.csv is missing!");
		}
	}
	
	private void readFromDisk(File file)
	{
		LineNumberReader lnr = null;
		try
		{
			int i=0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			while ( (line = lnr.readLine()) != null)
			{
				if (!line.startsWith("#"))
				{
					StringTokenizer st = new StringTokenizer(line,";");
					int itemId = Integer.parseInt(st.nextToken().toString());
					int price = Integer.parseInt(st.nextToken().toString());
					L2Item temp = ItemTable.getInstance().getTemplate(itemId);
					temp.setItemId(itemId);
					temp.setReferencePrice(price);
					
					i++;
				}
			}
			
			_log.config(i + " prices loaded");
		}
		catch (FileNotFoundException e)
		{
			// nothing
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e2)
			{
				// nothing
			}
		}
	}
}