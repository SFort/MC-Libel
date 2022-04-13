package com.unascribed.mirage.mixin.b_utility.chat_markdown;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.util.Markdown;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NetHandlerPlayServer.class)
@EligibleIf(configAvailable="*.chat_markdown")
public class MixinServerPlayNetworkHandler {

	@ModifyArg(at=@At(value="INVOKE", target="Lorg/apache/commons/lang3/StringUtils;normalizeSpace(Ljava/lang/String;)Ljava/lang/String;"), method="processChatMessage(Lnet/minecraft/network/play/client/CPacketChatMessage;)V")
	public String consume(String in) {
		if (!FabConf.isEnabled("*.chat_markdown")) return in;
		return Markdown.convert(in);
	}

}
