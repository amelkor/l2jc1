/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/Connection.java,v 1.8 2004/07/11 11:32:04 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/11 11:32:04 $
 * $Revision: 1.8 $
 * $Log: Connection.java,v $
 * Revision 1.8  2004/07/11 11:32:04  l2chef
 * no limit on client packet size
 *
 * Revision 1.7  2004/07/05 22:59:47  l2chef
 * small change on the packet sending
 *
 * Revision 1.6  2004/07/05 20:24:51  l2chef
 * better handling of incoming corrupt data
 *
 * Revision 1.5  2004/07/04 17:35:40  l2chef
 * raw packet data can be logged for better debugging
 *
 * Revision 1.4  2004/07/04 11:08:08  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/29 22:54:10  l2chef
 * using buffered stream to behave like org servers. might prevent crashing clients
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.serverpackets.ServerBasePacket;

/**
 * This class ...
 * 
 * @version $Revision: 1.8 $ $Date: 2004/07/11 11:32:04 $
 */
public class Connection
{
	private static Logger _log = Logger.getLogger(Connection.class.getName());

	private Crypt _inCrypt;
	private Crypt _outCrypt;

	private byte[] _cryptkey;

	private Socket _csocket;
	private InputStream _in;
	private OutputStream _out;

	public Connection(Socket client, byte[] cryptKey) throws IOException
	{
		_csocket = client;
		_in = client.getInputStream();
		_out = new BufferedOutputStream(client.getOutputStream());
		_inCrypt = new Crypt();
		_outCrypt = new Crypt();
		_cryptkey = cryptKey;	// this is defined here but it is not used before key packet was send 
	}

	public byte[] getPacket() throws IOException
	{
		int lengthHi = 0;
		int lengthLo = 0;
		int length = 0;
		lengthLo = _in.read();
		lengthHi = _in.read();
		length = lengthHi * 256 + lengthLo;

		if (lengthHi < 0)
		{
			_log.warning("client terminated connection");
			throw new IOException("EOF");
		}

		byte[] incoming = new byte[length];
		incoming[0] = (byte) lengthLo;
		incoming[1] = (byte) lengthHi;

		
		int receivedBytes = 0;
		int newBytes = 0;
		while (newBytes != -1 && receivedBytes<length-2)
		{
			newBytes =  _in.read(incoming, 2, length-2);
			receivedBytes = receivedBytes + newBytes;
		}
		
		if (receivedBytes != length-2)
		{
			_log.warning("Incomplete Packet is sent to the server, closing connection.");
			throw new IOException();
		}
		
		byte[] decrypt = new byte[incoming.length - 2];
		System.arraycopy(incoming, 2, decrypt, 0, decrypt.length);
		// decrypt if we have a key
		_inCrypt.decrypt(decrypt);

		int packetType = decrypt[0] & 0xff;

		return decrypt;
	}

	/**
	 * This method will be called indirectly by several threads, to notify
	 * one client about all parallel events in the world.
	 * it has to be either synchronized like this, or it might be changed to 
	 * stack packets in a outbound queue. 
	 * advantage would be that the calling thread is independent of the amount
	 * of events that the target gets.
	 * if one target receives hundreds of events in parallel, all event sources
	 * will have to wait until the packets are send... 
	 * for now, we use the direct communication
	 * @param data
	 * @throws IOException
	 */
	public void sendPacket(byte[] data) throws IOException
	{
		synchronized(this)
		{
			// this is time consuming.. only enable for debugging
			if (_log.isLoggable(Level.FINEST))
			{
				_log.finest("\n"+printData(data, data.length));
			}
			
			_outCrypt.encrypt(data);
			int length = data.length + 2;
			_out.write(length & 0xff);
			_out.write(length >> 8 &0xff);
			_out.flush(); // behave like org server
			_out.write(data);
			_out.flush();
		}
	}

	public void sendPacket(ServerBasePacket bp) throws IOException
	{
		byte[] data = bp.getContent();
		sendPacket(data);
	}

	public void activateCryptKey()
	{
		_inCrypt.setKey(_cryptkey);
		_outCrypt.setKey(_cryptkey);
	}

	/**
	 * this only gives the correct result if the cryptkey is not yet activated
	 */
	public byte[] getCryptKey()
	{
		return _cryptkey;
	}

	/**
	 * 
	 */
	public void close() throws IOException
	{
		_csocket.close();
	}
	
	private String printData(byte[] data, int len)
	{ 
		StringBuffer result = new StringBuffer();
		
		int counter = 0;
		
		for (int i=0;i< len;i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i,4)+": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i-15;
				for (int a=0; a<16;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0 )
		{
			for (int i=0; i<17-rest;i++ )
			{
				result.append("   ");
			}

			int charpoint = data.length-rest;
			for (int a=0; a<rest;a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char)t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}

		
		return result.toString();
	}
	
	private String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i=number.length(); i< digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
}
