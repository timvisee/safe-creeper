package com.timvisee.SafeCreeper.ExplosionRepair;

import java.util.Comparator;

import org.bukkit.block.BlockState;


public class SafeCreeperExplosionRepairComparator implements Comparator<BlockState> {
	public int compare(BlockState b1, BlockState b2) {
		boolean c1 = CreeperHeal.blocks_dependent.contains(b1.getTypeId());
		boolean c2 = CreeperHeal.blocks_dependent.contains(b2.getTypeId());
		
		if(c1 && !c2)
			return 1;
		else if(c2 && !c1)
			return -1;

		int pos1 = b1.getY();	//altitude of block one
		int pos2 = b2.getY();	//altitude of block two

		if(pos1 > pos2)
			return 1;
		else if(pos1<pos2)
			return -1;
		else
			return 0;
	}
}
