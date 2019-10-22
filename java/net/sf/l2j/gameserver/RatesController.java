/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/RatesController.java,v 1.3 2004/09/30 20:07:48 dethx Exp $
 *
 * $Author: dethx $
 * $Date: 2004/09/30 20:07:48 $
 * $Revision: 1.3 $
 * $Log: RatesController.java,v $
 * Revision 1.3  2004/09/30 20:07:48  dethx
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/28 01:30:26  nuocnam
 * Added header & copyright notice
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * This class ...
 * 
 * @version $Revision: 1.3 $ $Date: 2004/09/30 20:07:48 $
 */
public class RatesController
{
	private static Logger _log = Logger.getLogger(RatesController.class.getName());
	private static RatesController _instance;
	private int _expRate = 1;
	private int _spRate = 1;
	private int _adenaRate = 1;
	private int _dropRate = 1;
	private boolean checked = false;
	
    public static RatesController getInstance()
    {
        if (_instance == null)
        {
            _instance = new RatesController();
        }
        return _instance;
    }
    
    public RatesController()
    {
    	if (!checked)
    	{
	    	String line = null;
	        LineNumberReader lnr = null;
	        
	        try
	        {
	            File ratesData = new File("data/rates.csv");
	            lnr = new LineNumberReader(new BufferedReader( new FileReader(ratesData)));
	            
	            while ((line=lnr.readLine()) != null)
	            {
	                if (line.trim().length() == 0 || line.startsWith("#"))
	                {
	                    continue;
	                }
	                
	                parseList(line);
	                
	            }
	        }
	        catch (Exception e)
	        {
	        	_log.warning("Error loading rate modifiers: "+e.toString());
	        }
    	}
    }
    
    public int getExpRate()
    {
    	return _expRate;
    }
    
    public int getSpRate()
    {
    	return _spRate;
    }
    
    public int getAdenaRate()
    {
    	return _adenaRate;
    }
    
    public int getDropRate()
    {
    	return _dropRate;
    }
    
    private void parseList(String line)
    {
        StringTokenizer st = new StringTokenizer(line,";");
        
        _expRate = Integer.parseInt(st.nextToken());
        _log.config("EXP rate is now: "+_expRate);
        _spRate = Integer.parseInt(st.nextToken());
        _log.config("SP rate is now: "+_spRate);
        _adenaRate = Integer.parseInt(st.nextToken());
        _log.config("ADENA rate is now: "+_adenaRate);
        _dropRate = Integer.parseInt(st.nextToken());
        _log.config("DROP rate is now: "+_dropRate);
        checked = true;
    }
}