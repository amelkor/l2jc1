/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/serverpackets/Attack.java,v 1.3 2004/07/04 11:14:53 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/07/04 11:14:53 $
 * $Revision: 1.3 $
 * $Log: Attack.java,v $
 * Revision 1.3  2004/07/04 11:14:53  l2chef
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

/**
 * sample
 * 06 8f19904b 2522d04b 00000000 80 950c0000 4af50000 08f2ffff 0000    - 0 damage (missed 0x80)
 * 06 85071048 bc0e504b 32000000 10 fc41ffff fd240200 a6f5ffff 0100 bc0e504b 33000000 10                                     3....
  
 * format
 * dddc dddh (ddc)
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/04 11:14:53 $
 */
public class Attack extends ServerBasePacket
{
	// dh
	
	private static final String _S__06_ATTACK = "[S] 06 Attack";
	private int _attackerId;
	private int _defenderId;
	private int _damage;
	private boolean _miss;
	private boolean _critical;
	private boolean _soulshot;
	private int _x;
	private int _y;
	private int _z;

	/**
	 */
	public Attack(int attackerId, int defenderId, int damage, boolean miss, boolean critical, boolean soulshot, int x, int y, int z)
	{
		_attackerId = attackerId;
		_defenderId = defenderId;
		_damage = damage;
		_miss = miss;
		_critical = critical;
		_soulshot = soulshot;
		_x = x;
		_y = y;
		_z = z;
	}


	public byte[] getContent()
	{
		writeC(0x06);

		writeD(_attackerId);
		writeD(_defenderId);
		writeD(_damage);
		int flags = 0;
		if (_soulshot)
		{
			flags |= 0x10;
		}
		
		if (_critical)
		{
			flags |= 0x20;
		}

		if (_miss)
		{
			flags |= 0x80;
		}

		
		writeC(flags);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeH(00);	// if this is 01 then 3 more values are transmitted. but it does not seem to have any effect
//		..... optional values
//		writeD(_defenderId);
//		writeD(_damage???);
//		writeC(0x0);
		
		return getBytes();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__06_ATTACK;
	}
}
