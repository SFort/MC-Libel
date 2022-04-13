package com.unascribed.mirage.mixin._general.config;

import com.unascribed.mirage.FabConf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Inject(method="loadAllWorlds(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/WorldType;Ljava/lang/String;)V", at=@At("HEAD"))
	public void getPath(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions, CallbackInfo ci) {
		FabConf.setWorldPath(new File(saveName).toPath(), true);
	}

}
