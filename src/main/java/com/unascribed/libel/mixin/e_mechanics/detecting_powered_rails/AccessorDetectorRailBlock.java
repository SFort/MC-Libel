package com.unascribed.libel.mixin.e_mechanics.detecting_powered_rails;

import com.google.common.base.Predicate;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BlockRailDetector.class)
@EligibleIf(configAvailable="*.detecting_powered_rails")
public interface AccessorDetectorRailBlock {

	@Invoker("findMinecarts")
	<T extends EntityMinecart> List<T> fabrication$findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity>... filter);

}
