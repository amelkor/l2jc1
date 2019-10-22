/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/handler/SkillHandler.java,v 1.1 2004/08/17 00:46:41 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/08/17 00:46:41 $
 * $Revision: 1.1 $
 * $Log: SkillHandler.java,v $
 * Revision 1.1  2004/08/17 00:46:41  l2chef
 * new skill handlers contributed by Angel Kira.
 * some adjustments done.
 *
 * Revision 1.1  2004/08/07 14:11:56  l2chef
 * new skill handlers
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
package net.sf.l2j.gameserver.handler;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * This class ...
 *
 * @version $Revision: 1.1 $ $Date: 2004/08/17 00:46:41 $
 */
public class SkillHandler
{
	private static Logger _log = Logger.getLogger(SkillHandler.class.getName());
	
	private static SkillHandler _instance;
	
	private Map _datatable;
	
	public static SkillHandler getInstance()
	{
		if (_instance == null)
		{
			_instance = new SkillHandler();
		}
		return _instance;
	}
	
	private SkillHandler()
	{
		_datatable = new TreeMap();
	}
	
	public void registerSkillHandler(ISkillHandler handler)
	{
		int[] ids = handler.getSkillIds();
		for (int i = 0; i < ids.length; i++)
		{
			_datatable.put(new Integer(ids[i]), handler);
		}
	}
	
	public ISkillHandler getSkillHandler(int skillId)
	{
		return (ISkillHandler) _datatable.get(new Integer(skillId));
	}
}
