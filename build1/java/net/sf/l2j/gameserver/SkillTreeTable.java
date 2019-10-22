/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/gameserver/SkillTreeTable.java,v 1.12 2004/09/30 01:48:43 nuocnam Exp $
 *
 * $Author: nuocnam $
 * $Date: 2004/09/30 01:48:43 $
 * $Revision: 1.12 $
 * $Log: SkillTreeTable.java,v $
 * Revision 1.12  2004/09/30 01:48:43  nuocnam
 * corrected warlord
 *
 * Revision 1.11  2004/09/19 02:55:33  nuocnam
 * Corrected Tyrant & Destroyer class (sh1ny)
 *
 * Revision 1.10  2004/08/05 08:55:29  l2chef
 * treasurehunter skill file is now correct
 *
 * Revision 1.9  2004/08/01 12:18:04  l2chef
 * dwarf skilltree fixed (NuocNam)
 *
 * Revision 1.8  2004/07/30 22:28:49  l2chef
 * level 40+ classes are used (NuocNam)
 *
 * Revision 1.7  2004/07/13 23:15:58  l2chef
 * empty blocks commented
 *
 * Revision 1.6  2004/07/07 23:23:57  l2chef
 * errors in skilltree files get a better error message now
 *
 * Revision 1.5  2004/07/04 17:36:42  l2chef
 * rogue id fixed
 *
 * Revision 1.4  2004/07/04 11:08:08  l2chef
 * logging is used instead of system.out
 *
 * Revision 1.3  2004/06/27 20:50:02  l2chef
 * better error message when data file is missing. skipping of comment lines
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.ClassId;
import net.sf.l2j.gameserver.model.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2SkillLearn;

/**
 * This class ...
 * 
 * @version $Revision: 1.12 $ $Date: 2004/09/30 01:48:43 $
 */
public class SkillTreeTable
{
	private static Logger _log = Logger.getLogger(SkillTreeTable.class.getName());
	private static SkillTreeTable _instance;
	
	private HashMap _skillTrees;
	
	public static SkillTreeTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SkillTreeTable();
		}
		return _instance;
	}
	
	private SkillTreeTable()
	{
		_skillTrees = new HashMap();
		
		// starter jobs
		File skillData = new File("data/skilltree_D_Fighter.csv");
		readFile(skillData, ClassId.dwarvenFighter, -1);

		skillData = new File("data/skilltree_DE_Fighter.csv");
		readFile(skillData, ClassId.darkFighter, -1);

		skillData = new File("data/skilltree_DE_Mage.csv");
		readFile(skillData, ClassId.darkMage, -1);

		skillData = new File("data/skilltree_E_Fighter.csv");
		readFile(skillData, ClassId.elvenFighter, -1);

		skillData = new File("data/skilltree_E_Mage.csv");
		readFile(skillData, ClassId.elvenMage, -1);

		skillData = new File("data/skilltree_H_Fighter.csv");
		readFile(skillData, ClassId.fighter, -1);

		skillData = new File("data/skilltree_H_Mage.csv");
		readFile(skillData, ClassId.mage, -1);

		skillData = new File("data/skilltree_O_Fighter.csv");
		readFile(skillData, ClassId.orcFighter, -1);

		skillData = new File("data/skilltree_O_Mage.csv");
		readFile(skillData, ClassId.orcMage, -1);

		// first level

		skillData = new File("data/skilltree_H_Knight.csv");
		readFile(skillData, ClassId.knight, ClassId.fighter);

		skillData = new File("data/skilltree_H_Warrior.csv");
		readFile(skillData, ClassId.warrior, ClassId.fighter);

		skillData = new File("data/skilltree_H_Rogue.csv");
		readFile(skillData, ClassId.rogue, ClassId.fighter);

		
		skillData = new File("data/skilltree_H_Cleric.csv");
		readFile(skillData, ClassId.cleric, ClassId.mage);

		skillData = new File("data/skilltree_H_Wizard.csv");
		readFile(skillData, ClassId.wizard, ClassId.mage);

		
		skillData = new File("data/skilltree_E_Knight.csv");
		readFile(skillData, ClassId.elvenKnight, ClassId.elvenFighter);

		skillData = new File("data/skilltree_E_Scout.csv");
		readFile(skillData, ClassId.elvenScout, ClassId.elvenFighter);

		
		skillData = new File("data/skilltree_E_Wizard.csv");
		readFile(skillData, ClassId.elvenWizard, ClassId.elvenMage);

		skillData = new File("data/skilltree_E_Oracle.csv");
		readFile(skillData, ClassId.oracle, ClassId.elvenMage);
		

		skillData = new File("data/skilltree_DE_PaulusKnight.csv");
		readFile(skillData, ClassId.palusKnight, ClassId.darkFighter);

		skillData = new File("data/skilltree_DE_Assassin.csv");
		readFile(skillData, ClassId.assassin, ClassId.darkFighter);
		

		skillData = new File("data/skilltree_DE_DarkWizard.csv");
		readFile(skillData, ClassId.darkWizard, ClassId.darkMage);

		skillData = new File("data/skilltree_DE_ShillienOracle.csv");
		readFile(skillData, ClassId.shillienOracle, ClassId.darkMage);


		skillData = new File("data/skilltree_O_Monk.csv");
		readFile(skillData, ClassId.orcMonk, ClassId.orcFighter);

		skillData = new File("data/skilltree_O_Raider.csv");
		readFile(skillData, ClassId.orcRaider, ClassId.orcFighter);


		skillData = new File("data/skilltree_O_Shaman.csv");
		readFile(skillData, ClassId.orcShaman, ClassId.orcMage);

		
		skillData = new File("data/skilltree_D_Artisan.csv");
		readFile(skillData, ClassId.artisan, ClassId.dwarvenFighter);

		skillData = new File("data/skilltree_D_Scavenger.csv");
		readFile(skillData, ClassId.scavenger, ClassId.dwarvenFighter);
		
		//40+

		skillData = new File("data/skilltree_H_DarkAvenger.csv");
		readFile(skillData, ClassId.darkAvenger ,ClassId.knight);

		skillData = new File("data/skilltree_H_Paladin.csv");
		readFile(skillData, ClassId.paladin ,ClassId.knight);
				
		skillData = new File("data/skilltree_H_TreasureHunter.csv");
		readFile(skillData, ClassId.treasureHunter ,ClassId.rogue);
		
		skillData = new File("data/skilltree_H_Hawkeye.csv");
		readFile(skillData, ClassId.hawkeye ,ClassId.rogue);
		
		skillData = new File("data/skilltree_H_Gladiator.csv");
		readFile(skillData, ClassId.gladiator ,ClassId.warrior);

		skillData = new File("data/skilltree_H_Warlord.csv");
		readFile(skillData, ClassId.warlord ,ClassId.warrior);
		
		skillData = new File("data/skilltree_H_Sorceror.csv");
		readFile(skillData, ClassId.sorceror ,ClassId.wizard);
		
		skillData = new File("data/skilltree_H_Necromancer.csv");
		readFile(skillData, ClassId.necromancer ,ClassId.wizard);
		
		skillData = new File("data/skilltree_H_Warlock.csv");
		readFile(skillData, ClassId.warlock ,ClassId.wizard);
		
		skillData = new File("data/skilltree_H_Bishop.csv");
		readFile(skillData, ClassId.bishop ,ClassId.cleric);
		
		skillData = new File("data/skilltree_H_Prophet.csv");
		readFile(skillData, ClassId.prophet ,ClassId.cleric);
				

		skillData = new File("data/skilltree_E_TempleKnight.csv");
		readFile(skillData, ClassId.templeKnight ,ClassId.elvenKnight);
		
		skillData = new File("data/skilltree_E_SwordSinger.csv");
		readFile(skillData, ClassId.swordSinger ,ClassId.elvenKnight);
		
		skillData = new File("data/skilltree_E_SilverRanger.csv");
		readFile(skillData, ClassId.silverRanger ,ClassId.elvenScout);
		
		skillData = new File("data/skilltree_E_PlainsWalker.csv");
		readFile(skillData, ClassId.plainsWalker ,ClassId.elvenScout);
		
		skillData = new File("data/skilltree_E_ElementalSummoner.csv");
		readFile(skillData, ClassId.elementalSummoner ,ClassId.elvenWizard);
		
		skillData = new File("data/skilltree_E_SpellSinger.csv");
		readFile(skillData, ClassId.spellsinger ,ClassId.elvenWizard);
		
		skillData = new File("data/skilltree_E_Elder.csv");
		readFile(skillData, ClassId.elder ,ClassId.oracle);
		
		
		skillData = new File("data/skilltree_DE_ShillienKnight.csv");
		readFile(skillData, ClassId.shillienKnight , ClassId.palusKnight);

		skillData = new File("data/skilltree_DE_BladeDancer.csv");
		readFile(skillData, ClassId.bladedancer ,ClassId.palusKnight);

		skillData = new File("data/skilltree_DE_AbyssWalker.csv");
		readFile(skillData, ClassId.abyssWalker ,ClassId.assassin);

		skillData = new File("data/skilltree_DE_PhantomRanger.csv");
		readFile(skillData, ClassId.phantomRanger ,ClassId.assassin);

		skillData = new File("data/skilltree_DE_ShillienElder.csv");
		readFile(skillData, ClassId.shillenElder ,ClassId.shillienOracle);

		skillData = new File("data/skilltree_DE_PhantomSummoner.csv");
		readFile(skillData, ClassId.phantomSummoner ,ClassId.darkWizard);

		skillData = new File("data/skilltree_DE_Spellhowler.csv");
		readFile(skillData, ClassId.spellhowler ,ClassId.darkWizard);

		
		skillData = new File("data/skilltree_D_Warsmith.csv");
		readFile(skillData, ClassId.warsmith ,ClassId.artisan);

		skillData = new File("data/skilltree_D_BountyHunter.csv");
		readFile(skillData, ClassId.bountyHunter ,ClassId.scavenger);
		
		skillData = new File("data/skilltree_O_Destroyer.csv");
		readFile(skillData, ClassId.destroyer ,ClassId.orcRaider);
		
		skillData = new File("data/skilltree_O_Tyrant.csv");
		readFile(skillData, ClassId.tyrant ,ClassId.orcMonk);
		
		skillData = new File("data/skilltree_O_Overlord.csv");
		readFile(skillData, ClassId.overlord ,ClassId.orcShaman);
		
		skillData = new File("data/skilltree_O_Warcryer.csv");
		readFile(skillData, ClassId.warcryer ,ClassId.orcShaman);
				
	}
	
	
	private void readFile(File skillData, int classId, int parentClassId)
	{
		LineNumberReader lnr = null;
		String line = null;
		
		try
		{
			lnr = new LineNumberReader(new BufferedReader( new FileReader(skillData)));
			ArrayList list = new ArrayList();
			if (parentClassId != -1)
			{
				ArrayList parentList = (ArrayList) _skillTrees.get(new Integer(parentClassId));
				list.addAll(parentList);
			}
			
			while ( (line=lnr.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}

				L2SkillLearn skill = parseList(line);
				list.add(skill);
			}

			_skillTrees.put(new Integer(classId), list);
			_log.config("skill tree for class " + classId + " has " + list.size() + " skills");
		}
		catch (FileNotFoundException e)
		{
			_log.warning("SkillTree file for classId "+ classId + " is missing in data folder");
		}
		catch (Exception e)
		{
			_log.warning("error while creating skill tree for classId "+classId + "  " +line + " "+e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				lnr.close();
			}
			catch (Exception e1)
			{
				// ignore problems
			}
		}
	}

	private L2SkillLearn parseList(String line)
	{
		StringTokenizer st = new StringTokenizer(line,";");

		L2SkillLearn skill = new L2SkillLearn();
		skill.setId( Integer.parseInt(st.nextToken()) );
		skill.setName( st.nextToken() );
		skill.setLevel( Integer.parseInt(st.nextToken()) );
		skill.setSpCost( Integer.parseInt(st.nextToken()) );
		skill.setMinLevel( Integer.parseInt(st.nextToken()) );
		
		return skill;
	}

	public L2SkillLearn[] getAvailableSkills(L2PcInstance cha)
	{
		ArrayList result = new ArrayList();
		ArrayList skills = (ArrayList) _skillTrees.get(new Integer(cha.getClassId()));
		if (skills == null)
		{
			// the skilltree for this class is undefined, so we give an empty list
			_log.warning("Skilltree for class " + cha.getClassId() + " is not defined !");
			return new L2SkillLearn[0];
		}

		L2Skill[] oldSkills = cha.getAllSkills();
		
		for (int i = 0; i < skills.size(); i++)
		{
			L2SkillLearn temp = (L2SkillLearn) skills.get(i);
			if (temp.getMinLevel() <= cha.getLevel())
			{
				boolean knownSkill = false;
				
				for (int j = 0; j < oldSkills.length && !knownSkill; j++)
				{
					if (oldSkills[j].getId() == temp.getId() )
					{
						knownSkill = true;

						if ( oldSkills[j].getLevel() == temp.getLevel()-1)
						{
							// this is the next level of a skill that we know
							result.add(temp);
						}
					}
				}
				
				if (!knownSkill && temp.getLevel() == 1)
				{
					// this is a new skill
					result.add(temp);
				}
			}
		}
		
		return (L2SkillLearn[]) result.toArray(new L2SkillLearn[result.size()]);
	}
}
