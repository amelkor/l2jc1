/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/MagicEffectIcons.java,v 1.3 2004/07/04 11:14:52 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:52 $
 * $Revision: 1.3 $
 * $Log: MagicEffectIcons.java,v $
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

import java.util.Vector;

/**
 * 
 * sample
 * 
 * 0000: 97 02 00 10 04 00 00 01 00 4b 02 00 00 2c 04 00    .........K...,..
 * 0010: 00 01 00 58 02 00 00                               ...X...
 *  
 *
 * format   h (dhd)
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:52 $
 */
public class MagicEffectIcons extends ServerBasePacket
{
	private static final String _S__97_MAGICEFFECTICONS = "[S] 97 MagicEffectIcons";
	private Vector _effects;
	
	class Effect
	{
		int skillId;
		int dat;
		int duration;
		
		public Effect(int skillId, int dat, int duration)
		{
			this.skillId = skillId;
			this.dat = dat;
			this.duration = duration;	
		}
	}
	
	public MagicEffectIcons()
	{
		_effects = new Vector();
	}
	
	public void addEffect(int skillId, int dat, int duration)
	{
		_effects.add(new Effect(skillId, dat, duration));		
	}
	
	public byte[] getContent()
	{
		_bao.write(0x97);
		
		writeH(_effects.size());
	
		for (int i = 0; i < _effects.size(); i++)
		{
			Effect temp = (Effect) _effects.get(i);

			writeD(temp.skillId);
			writeH(temp.dat);
			writeD(temp.duration/1000);
		}		

		return _bao.toByteArray();
	}
	
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__97_MAGICEFFECTICONS;
	}

}
