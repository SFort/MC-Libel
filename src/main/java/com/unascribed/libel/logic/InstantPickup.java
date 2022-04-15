package com.unascribed.libel.logic;

import com.unascribed.libel.FabRefl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class InstantPickup {

	public static void slurp(World world, AxisAlignedBB box, EntityPlayer breaker) {
		for (EntityItem ie : world.getEntitiesWithinAABB(EntityItem.class, box, e -> e.ticksExisted == 0)) {
			if (ie.isDead) continue;
			int oldPickupDelay = FabRefl.getPickupDelay(ie);
			ie.setPickupDelay(0);
			ie.onCollideWithPlayer(breaker);
			if (!ie.isDead) {
				ie.setPickupDelay(oldPickupDelay);
			}
		}
	}

}
