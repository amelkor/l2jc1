/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/loginserver/Logins.java,v 1.10 2004/09/26 00:17:58 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/09/26 00:17:58 $
 * $Revision: 1.10 $
 * $Log: Logins.java,v $
 * Revision 1.10  2004/09/26 00:17:58  l2chef
 * hacking protection added. IP banning added
 *
 * Revision 1.9  2004/07/19 23:13:38  l2chef
 * account names are no longer casesensitive (NuocNam)
 *
 * Revision 1.8  2004/07/13 23:06:30  l2chef
 * empty blocks commented
 *
 * Revision 1.7  2004/07/05 23:02:00  l2chef
 * access levels are stored for logins
 *
 * Revision 1.6  2004/07/04 11:34:39  l2chef
 * passwords are no longer stored in plaintext. instead the Hash is stored encoded. that should drastically increase security
 *
 * Revision 1.5  2004/06/30 21:51:34  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.4  2004/06/27 22:16:51  l2chef
 * fixed storing of accout file
 *
 * Revision 1.3  2004/06/27 20:57:14  l2chef
 * autoAccount creation can be set in config file
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
package net.sf.l2j.loginserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.Base64;

/**
 * This class ...
 * 
 * @version $Revision: 1.10 $ $Date: 2004/09/26 00:17:58 $
 */
public class Logins 
{
	private static final String SHA = "SHA";
	private static final String UTF_8 = "UTF-8";
	private static Logger _log = Logger.getLogger(Logins.class.getName());
			
	private HashMap _logPass;
	private HashMap _accessLevels;
	private HashMap _hackProtection;
	private boolean _autoCreate;
	
	public Logins(boolean autoCreate)
	{
		_logPass = new HashMap();
		_accessLevels = new HashMap();
		_hackProtection = new HashMap();
		_autoCreate = autoCreate;
		_log.config("Automatically creating new accounts: " + _autoCreate);
		File loginFile = new File("data/logins.txt");
		if (loginFile.exists())
		{
			readFromDisk(loginFile);
		}
	}

	public int getAccessLevel(String user)
	{
		return ((Integer)_accessLevels.get(user)).intValue();
	}
	
	/**
	 * user name is not case sensitive any more
	 * @param user
	 * @param password
	 * @param address
	 * @return
	 */
	public boolean loginValid(String user, String password, InetAddress address) throws HackingException
	{
		boolean ok = false;
		
		Integer failedConnects  = (Integer) _hackProtection.get(address.getHostAddress());
		if (failedConnects != null && failedConnects.intValue() > 2)
		{
			_log.warning("hacking detected from ip:"+address.getHostAddress()+" .. adding IP to banlist");
			
			throw new HackingException(address.getHostAddress());
		}

		try
		{
			MessageDigest md = MessageDigest.getInstance(SHA);
			byte[] raw = password.getBytes("UTF-8");
			byte[] hash = md.digest(raw);
	
			byte[] expected = (byte[]) _logPass.get(user);
	
			if (expected == null)
			{
				if (_autoCreate)
				{
					_logPass.put(user, hash);
					_accessLevels.put(user, new Integer(0));
					_log.info("created new account for "+ user);
					saveToDisk();
					return true;
				}
				else
				{
					_log.warning("account missing for user "+user);
					return false;
				}
			}
			
			ok = true;
			for (int i=0;i<expected.length;i++)
			{
				if (hash[i] != expected[i])
				{
					ok = false;
					break;
				}
			}
		}
		catch (Exception e)
		{
			// digest algo not found ??
			// out of bounds should not be possible
			_log.warning("could not check password:"+e);
			ok = false;
		}
		
		if (!ok)
		{
			// add 1 to the failed counter for this IP 
			int failedCount = 1;
			if (failedConnects != null)
			{
				failedCount = failedConnects.intValue() + 1;
			}
			
			_hackProtection.put(address.getHostAddress(), new Integer(failedCount));
		}
		else
		{
			// for long running servers, this should prevent blocking 
			// of users that mistype their passwords once every day :)
			_hackProtection.remove(address.getHostAddress());
		}
		
		return ok;
	}
							
	/**
	 * @param loginFile
	 */
	private void readFromDisk(File loginFile)
	{
		LineNumberReader lnr = null;
		_logPass.clear();
		try
		{
			int i=0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(loginFile));
			while ( (line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line,"\t\n\r");
				if (st.hasMoreTokens())
				{	
					// toLowerCase is here for backward compatibility
					String name = st.nextToken().toLowerCase();
					String password = st.nextToken();
					_logPass.put(name, Base64.decode(password));
					
					// this is just for compatibility to 0.2
					if (st.hasMoreTokens())
					{
						String access = st.nextToken();
						Integer level = new Integer(access);
						_accessLevels.put(name, level);
					}
					else
					{
						_accessLevels.put(name, new Integer(0));
					}
					
					i++;
				}
			}
			
			_log.config("found " + i + " logins on disk");
		}
		catch (FileNotFoundException e)
		{
			// cannot happen
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
				// ignore problems
			}
		}
	}
	
	public void saveToDisk()
	{
		File loginFile = new File("data/logins.txt");
		FileWriter save = null; 

		try
		{
			save = new FileWriter(loginFile);
			for (Iterator iter = _logPass.keySet().iterator(); iter.hasNext();)
			{
				String name = (String) iter.next();
				byte[] pass = (byte[]) _logPass.get(name);
				save.write(name);
				save.write("\t");
				save.write(Base64.encodeBytes(pass));
				save.write("\t");
				save.write("" + _accessLevels.get(name));
				save.write("\r\n");
			}
		}
		catch (IOException e)
		{
			_log.warning("could not store accouts file."+e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				save.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}
}
