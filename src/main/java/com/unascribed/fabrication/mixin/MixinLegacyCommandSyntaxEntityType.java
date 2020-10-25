package com.unascribed.fabrication.mixin;

import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.unascribed.fabrication.support.EligibleIf;
import com.unascribed.fabrication.support.MixinConfigPlugin.RuntimeChecks;

import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.util.Identifier;

@Mixin(EntitySummonArgumentType.class)
@EligibleIf(configEnabled="*.legacy_command_syntax")
public class MixinLegacyCommandSyntaxEntityType {
	
	@Redirect(at=@At(value="INVOKE", target="net/minecraft/util/Identifier.fromCommandInput(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/util/Identifier;"),
			method="parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/util/Identifier;")
	public Identifier fromCommandInput(StringReader sr) throws CommandSyntaxException {
		if (!RuntimeChecks.check("*.legacy_command_syntax")) return Identifier.fromCommandInput(sr);
		char peek = sr.peek();
		if (peek >= 'A' && peek <= 'Z') {
			int start = sr.getCursor();
			while (sr.canRead() && isCharValid(sr.peek())) {
				sr.skip();
			}
			return new Identifier("minecraft", sr.getString().substring(start, sr.getCursor())
						.replaceAll("([a-z])([A-Z])", "$1_$2")
						.toLowerCase(Locale.ROOT));
		}
		return Identifier.fromCommandInput(sr);
	}
	
	@Unique
	private static boolean isCharValid(char c) {
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
	
}