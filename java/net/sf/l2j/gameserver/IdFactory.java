/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/IdFactory.java,v 1.3 2004/07/11 22:11:46 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/11 22:11:46 $
 * $Revision: 1.3 $
 * $Log: IdFactory.java,v $
 * Revision 1.3  2004/07/11 22:11:46  l2chef
 * stacktrace converted to log
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/11 22:11:46 $
 */
public class IdFactory
{
	private static Logger _log = Logger.getLogger(IdFactory.class.getName());

	private int _curOID;
	private Stack _oldOIDs;
	
	private static int FIRST_OID = 0x10000000;  
	
	private static IdFactory _instance;

	private IdFactory()
	{
		FileInputStream fis;
		try
		{
			fis = new FileInputStream("data/idstate.dat");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream oos = new ObjectInputStream(bis);
			_curOID = ((Integer) oos.readObject()).intValue();
			_oldOIDs = (Stack) oos.readObject();
			oos.close();
		}
		catch (Exception e)
		{
			_curOID = FIRST_OID;
			_oldOIDs = new Stack();
		}

	}
	
	public static IdFactory getInstance()
	{
		if (_instance == null)
		{
			_instance = new IdFactory();
		}
		return _instance;
	}


	public synchronized int getNextId()
	{
		if (_oldOIDs.isEmpty())
		{
			return _curOID++;
		}
		else
		{
			return ((Integer) _oldOIDs.pop()).intValue();
		}
	}
	
	/**
	 * return a used Object ID back to the pool
	 * @param object ID
	 */
	public void releaseId(int id)
	{
		_oldOIDs.push(new Integer(id));
	}
	
	/**
	 * this is used to make the id value somewhat persistant... not a good solution yet. 
	 * @param id
	 */
	public void saveCurrentState()
	{
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream("data/idstate.dat");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(new Integer(_curOID));
			oos.writeObject(_oldOIDs);
			oos.close();
		}
		catch (IOException e)
		{
			_log.warning("idstate couldnt be saved."+e.getMessage());
		}
	}
}
