/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/clientpackets/CharacterSelected.java,v 1.5 2004/07/13 23:02:11 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/13 23:02:11 $
 * $Revision: 1.5 $
 * $Log: CharacterSelected.java,v $
 * Revision 1.5  2004/07/13 23:02:11  l2chef
 * empty blocks commented
 *
 * Revision 1.4  2004/07/11 23:37:38  l2chef
 * chars are always reloaded from disk  (whatev)
 *
 * Revision 1.3  2004/07/04 11:12:33  l2chef
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
package net.sf.l2j.gameserver.clientpackets;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.Connection;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.CharSelected;

/**
 * This class ...
 * 
 * @version $Revision: 1.5 $ $Date: 2004/07/13 23:02:11 $
 */
public class CharacterSelected extends ClientBasePacket
{
	private static final String _C__0D_CHARACTERSELECTED = "[C] 0D CharacterSelected";
	private static Logger _log = Logger.getLogger(CharacterSelected.class.getName());

	// cd

	/**
	 * @param decrypt
	 */
	public CharacterSelected(byte[] decrypt, ClientThread client) throws IOException
	{
		super(decrypt);
		int charSlot = readD();
		
		// if there is a playback.dat file in the current directory, it will
		// be sent to the client instead of any regular packets
		// to make this work, the first packet in the playback.dat has to
		// be a  [S]0x21 packet
		// after playback is done, the client will not work correct and need to exit
		playLogFile(client.getConnection()); // try to play log file
		

		// HAVE TO CREATE THE L2PCINSTANCE HERE TO SET AS ACTIVE
		_log.fine("selected slot:" + charSlot);
		
		//loadup character from disk
		L2PcInstance cha = client.loadCharFromDisk(charSlot);

		// preinit some values for each login
		cha.setMoveType(1);		// running is default
		cha.setWaitType(1);		// standing is default
		
		
		CharSelected cs = new CharSelected(cha, client.getSessionId());
		client.getConnection().sendPacket(cs);
		
		client.setActiveChar(cha);
	}
	
	/**
	 * 
	 */
	private void playLogFile(Connection connection)
	{
		long diff = 0;
		long first = -1;

		try
		{
			LineNumberReader lnr =
			new LineNumberReader(new FileReader("playback.dat"));

			String line = null;
			while ((line = lnr.readLine()) != null)
			{
				if (line.length() > 0 && line.substring(0, 1).equals("1"))
				{
					String timestamp = line.substring(0, 13);
					long time = Long.parseLong(timestamp);
					if (first == -1)
					{
						long start = System.currentTimeMillis();
						first = time;
						diff = start - first;
					}
					
					String cs = line.substring(14, 15);
					// read packet definition
					ByteArrayOutputStream bais = new ByteArrayOutputStream();

					while (true)
					{
						String temp = lnr.readLine();
						if (temp.length() < 53)
						{
							break;
						}
						
						String bytes = temp.substring(6, 53);
						StringTokenizer st = new StringTokenizer(bytes);
						while (st.hasMoreTokens())
						{
							String b = st.nextToken();
							int number = Integer.parseInt(b, 16);
							bais.write(number);
						}
					}

					if (cs.equals("S"))
					{
						//wait for timestamp and send packet
						int wait =
						(int) (time + diff - System.currentTimeMillis());
						if (wait > 0)
						{
							_log.fine("waiting"+ wait);
							Thread.sleep(wait);
						}
						_log.fine("sending:"+ time);
						byte[] data = bais.toByteArray();
						if (data.length != 0)
						{
							connection.sendPacket(data);	
						}
						else
						{
							_log.fine("skipping broken data");
						}

					}
					else
					{
						// skip packet
					}
				}

			}
		}
		catch (FileNotFoundException f)
		{
			// should not happen
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	public String getType()
	{
		return _C__0D_CHARACTERSELECTED;
	}	
}
