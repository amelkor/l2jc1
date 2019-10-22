/*
* $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/model/L2Potion.java,v 1.1 2004/08/08 03:02:18 l2chef Exp $
*
* $Author: l2chef $
* $Date: 2004/08/08 03:02:18 $
* $Revision: 1.1 $
* $Log: L2Potion.java,v $
* Revision 1.1  2004/08/08 03:02:18  l2chef
* potion handler added (imwookie)
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
package net.sf.l2j.gameserver.model;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


/**
 * 
 * This class ...
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/08 03:02:18 $
 */
public class L2Potion extends L2Object
{
	private static final Logger _log = Logger.getLogger(L2Character.class.getName());


	L2Character _target;
	
	private static Timer _regenTimer = new Timer(true);
	private PotionHpHealing _potionhpRegTask = new PotionHpHealing(_target);
	private boolean _potionhpRegenActive;
	private PotionMpHealing _potionmpRegTask = new PotionMpHealing(_target);
	private boolean _potionmpRegenActive;
	private int _seconds;
	private double _effect;
	private int _duration;
	private int _potion;
	private Object _mpLock = new Object();
	private Object _hpLock = new Object();
	
	
	class PotionHpHealing extends TimerTask
	{
		L2Character _instance;
				
		public PotionHpHealing(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				synchronized(_hpLock)
				{
					double nowHp = _instance.getCurrentHp();
					if(_duration == 0)
					{
						stopPotionHpRegeneration();
					}
					if (_duration != 0)
					{
						nowHp+=_effect;
						_instance.setCurrentHp(nowHp);
						_duration=(_duration-(_seconds/1000));
						setCurrentHpPotion2(_instance);
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Error in hp potion task:"+e);
			}
		}
	}
	
	private void startPotionHpRegeneration(L2Character activeChar)
	{
		_potionhpRegTask = new PotionHpHealing(activeChar);
		_regenTimer.schedule(_potionhpRegTask, 1000, _seconds);
		_potionhpRegenActive = true;
		_log.fine("Potion HP regen Started");
	}

	public void stopPotionHpRegeneration()
	{
		if (_potionhpRegTask != null)
		{
			_potionhpRegTask.cancel();
		}
		_potionhpRegTask = null;
		_potionhpRegenActive = false;
		_log.fine("Potion HP regen stop");
	}

	public void setCurrentHpPotion2(L2Character activeChar)
	{
		if (_duration == 0)
		{
			stopPotionHpRegeneration();
		}
	}
	public void setCurrentHpPotion1(L2Character activeChar, int item)
	{
		_potion = item;
		_target = activeChar;

		switch (_potion)
		{
			case (65):
				_seconds = 3000;
				_duration = 15;
				_effect = 2;
				startPotionHpRegeneration(activeChar);
				break;		
			case (725):
				_seconds = 1000;
				_duration = 20;
				_effect = 1.5;
				startPotionHpRegeneration(activeChar);
				break;
			case (727):
				_seconds = 1000;
				_duration = 20;
				_effect = 1.5;
				startPotionHpRegeneration(activeChar);
				break;
			case (1060):
				_seconds = 3000;
				_duration = 15;
				_effect = 4;
				startPotionHpRegeneration(activeChar);
				break;
			case (1061):
				_seconds = 3000;
				_duration = 15;
				_effect = 14;
				startPotionHpRegeneration(activeChar);
				break;
			case (1073):
				_seconds = 3000;
				_duration = 15;
				_effect = 2;
				startPotionHpRegeneration(activeChar);
				break;
			case (1539):
				_seconds = 3000;
				_duration = 15;
				_effect = 32;
				startPotionHpRegeneration(activeChar);
				break;
			case (1540):
				double nowHp = activeChar.getCurrentHp();
				nowHp+=435;
				if (nowHp>= activeChar.getMaxHp())
				{
					nowHp = activeChar.getMaxHp();
				}
				activeChar.setCurrentHp(nowHp);
				break;
		}
	}
	
	class PotionMpHealing extends TimerTask
	{
		L2Character _instance;
		
		public PotionMpHealing(L2Character instance)
		{
			_instance = instance;
		}
		
		public void run()
		{
			try
			{
				synchronized(_mpLock)
				{
					double nowMp = _instance.getCurrentMp();
					if(_duration == 0)
					{
						stopPotionMpRegeneration();
					}
					if (_duration != 0)
					{
						nowMp+=_effect;
						_instance.setCurrentMp(nowMp);
						_duration=(_duration-(_seconds/1000));
						setCurrentMpPotion2(_instance);
						
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("error in mp potion task:"+e);
			}
		}
	}
	
	private void startPotionMpRegeneration(L2Character activeChar)
	{
		
		_potionmpRegTask = new PotionMpHealing(activeChar);
		_regenTimer.schedule(_potionmpRegTask, 1000, _seconds);
		_potionmpRegenActive = true;
		_log.fine("Potion MP regen Started");
	}

	public void stopPotionMpRegeneration()
	{
		if (_potionmpRegTask != null)
		{
			_potionmpRegTask.cancel();
		}

		_potionmpRegTask = null;
		_potionmpRegenActive = false;
		_log.fine("Potion MP regen stop");
	}

	public void setCurrentMpPotion2(L2Character activeChar)
	{
		if (_duration == 0)
		{
			stopPotionMpRegeneration();
		}
	}
	
	public void setCurrentMpPotion1(L2Character activeChar, int item)
	{
		_potion = item;
		_target = activeChar;

		switch (_potion)
		{
			case (726):
				_seconds = 1000;
				_duration = 20;
				_effect = 1.5;
				startPotionMpRegeneration(activeChar);
				break;		
			case (728):
				double nowMp = activeChar.getMaxMp();
				nowMp+=435;
				if (nowMp>= activeChar.getMaxMp())
				{
					nowMp = activeChar.getMaxMp();
				}
				activeChar.setCurrentMp(nowMp);
				break;
		}
	}
}