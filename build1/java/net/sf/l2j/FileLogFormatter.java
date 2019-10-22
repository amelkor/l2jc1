/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/FileLogFormatter.java,v 1.1 2004/06/30 21:51:33 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/06/30 21:51:33 $
 * $Revision: 1.1 $
 * $Log: FileLogFormatter.java,v $
 * Revision 1.1  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
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
package net.sf.l2j;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/06/30 21:51:33 $
 */

public class FileLogFormatter extends Formatter
{

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	private static final String CRLF = "\r\n";
	private static final String _ = "\t";
	public String format(LogRecord record)
	{
		StringBuffer output = new StringBuffer();
		output.append(record.getMillis());
		output.append(_);
		output.append(record.getLevel().getName());
		output.append(_);
		output.append(record.getThreadID());
		output.append(_);
		output.append(record.getLoggerName());
		output.append(_);
		output.append(record.getMessage());
		output.append(CRLF);
		return output.toString();
	}
}
