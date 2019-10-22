/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/CharNameTable.java,v 1.2 2004/07/25 19:22:11 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/25 19:22:11 $
 * $Revision: 1.2 $
 * $Log: CharNameTable.java,v $
 * Revision 1.2  2004/07/25 19:22:11  l2chef
 * prevented npe if no accounts folder exists
 *
 * Revision 1.1  2004/07/25 00:35:56  l2chef
 * charnames are now checked for duplicates when creating char
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
package net.sf.l2j.gameserver;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/25 19:22:11 $
 */
public class CharNameTable
{
	private static Logger _log = Logger.getLogger(CharNameTable.class.getName());
	
	private static CharNameTable _instance;
	
	private ArrayList _charNames;
	
	public static CharNameTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new CharNameTable();
		}
		return _instance;
	}
	
	private CharNameTable()
	{
		int loaded = 0;
		File _accountsFolder = new File("data/accounts");
		_accountsFolder.mkdirs();
		_charNames = new ArrayList();
				
		File[] accounts = _accountsFolder.listFiles();

		for (int i = 0; i < accounts.length; i++) 
		{
			try
			{
				File _charFolder = new File("data/accounts/" + accounts[i].getName());
				
				File[] chars = _charFolder.listFiles(new FilenameFilter()
						{
							public boolean accept(File dir, String name)
							{
								return (name.endsWith("_char.csv"));
							}
						});
				
				for (int j = 0; j < chars.length; j++)
				{
					_charNames.add(chars[j].getName().replaceAll("_char.csv","").toLowerCase());
				}
			}
			catch (NullPointerException e)
			{
				// nothing
			}
		}
		_log.fine("loaded " + _charNames.size() + " charnames to the memory.");
	}
	
	public void addCharName(String name)
	{
		_log.fine("added charname: " + name + " to the memory.");
		_charNames.add(name.toLowerCase());
	}
	
	public void deleteCharName(String name)
	{
		_log.fine("deleted charname: " + name + " from the memory.");
		_charNames.remove(name.toLowerCase());
	}
	
	public boolean doesCharNameExist(String name)
	{
		return _charNames.contains(name.toLowerCase());
	}
}
