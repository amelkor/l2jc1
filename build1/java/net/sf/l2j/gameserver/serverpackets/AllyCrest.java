/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/AllyCrest.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: AllyCrest.java,v $
 * Revision 1.3  2004/07/04 11:14:52  l2chef
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
package net.sf.l2j.gameserver.serverpackets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * <code>
 * sample
 * 0000: c7 6d 06 00 00 36 05 00 00 42 4d 36 05 00 00 00    .m...6...BM6....
 * 0010: 00 00 00 36 04 00 00 28 00 00 00 10 00 00 00 10    ...6...(........
 * 0020: 00 00 00 01 00 08 00 00 00 00 00 00 01 00 00 c4    ................
 * 0030: ...
 * 0530: 10 91 00 00 00 60 9b d1 01 e4 6e ee 52 97 dd       .....`....n.R..
 * </code>
 * 
 *  
 * format   dd x...x 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class AllyCrest extends ServerBasePacket
{
	private static final String _S__C7_ALLYCREST = "[S] C7 AllyCrest";
	private File _crestFile;
	private int _crestId;
	
	public AllyCrest(int crestId, File crestFile)
	{
		_crestFile = crestFile;
		_crestId = crestId;
	}	
	
	public byte[] getContent()
	{
		writeC(0xc7);
		writeD(_crestId);
		try
		{
			FileInputStream fis = new FileInputStream(_crestFile);
			BufferedInputStream bfis = new BufferedInputStream(fis);
			int crestSize = fis.available();

			writeD(crestSize);
			int temp = -1;
			while ( (temp = fis.read()) != -1)
			{
				writeC(temp);
			}
			fis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__C7_ALLYCREST;
	}
}
