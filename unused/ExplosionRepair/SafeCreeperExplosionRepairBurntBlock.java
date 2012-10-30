package com.timvisee.SafeCreeper.ExplosionRepair;

import java.util.Date;

import org.bukkit.block.BlockState;

public class SafeCreeperExplosionRepairBurntBlock {
	private BlockState blockState;
	private Date time;

	public SafeCreeperExplosionRepairBurntBlock(Date now, BlockState state) {
		setBlockState(state);
		setTime(now);
	}

	public void addTime(int delay) {
		time = new Date(time.getTime() + delay);
	}

	public Date getTime() {
		return time;
	   }

	public void setTime(Date time) {
		this.time = time;
	}

	public BlockState getBlockState() {
		return blockState;
	}

	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}
}
