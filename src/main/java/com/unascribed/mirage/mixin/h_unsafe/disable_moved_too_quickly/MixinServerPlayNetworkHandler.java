package com.unascribed.mirage.mixin.h_unsafe.disable_moved_too_quickly;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NetHandlerPlayServer.class)
@EligibleIf(configAvailable="*.disable_moved_too_quickly")
public abstract class MixinServerPlayNetworkHandler {

	@ModifyConstant(constant={@Constant(floatValue=300.0F), @Constant(floatValue=100.0F)}, method="processPlayer(Lnet/minecraft/network/play/client/CPacketPlayer;)V")
	private float disableMoveTooQuick(float old) {
		if (!FabConf.isEnabled("*.disable_moved_too_quickly")) return old;
		return Float.MAX_VALUE;
	}

	@ModifyConstant(constant=@Constant(doubleValue=100.0), method="processVehicleMove(Lnet/minecraft/network/play/client/CPacketVehicleMove;)V")
	private double getMaxPlayerVehicleSpeed(double old) {
		if (!FabConf.isEnabled("*.disable_moved_too_quickly")) return old;
		return Double.MAX_VALUE;
	}

}
