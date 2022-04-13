package com.unascribed.mirage.support;

import java.util.Set;

import com.unascribed.mirage.FabConf;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.unascribed.mirage.FabLog;

import com.google.common.base.Joiner;

public class MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR implements IMixinErrorHandler {

	public static final MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR INST = new MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR();
	public static boolean actuallyItWasUs = false;

	@Override
	public ErrorAction onPrepareError(IMixinConfig config, Throwable th, IMixinInfo mixin, ErrorAction action) {
		return onError(th, mixin, action, "prepare");
	}

	@Override
	public ErrorAction onApplyError(String targetClassName, Throwable th, IMixinInfo mixin, ErrorAction action) {
		return onError(th, mixin, action, "apply");
	}

	public ErrorAction onError(Throwable th, IMixinInfo mixin, ErrorAction action, String verb) {
		if (mixin.getClassName().startsWith("com.unascribed.fabrication.")) {
			if (action == ErrorAction.ERROR) {
				Set<String> keys = FabConf.getConfigKeysForDiscoveredClass(mixin.getClassName());
				if (!keys.isEmpty()) {
					FabLog.debug("Original Mixin error", th);
					FabLog.warn("Mixin "+mixin.getClassName()+" failed to "+verb+"! Force-disabling "+Joiner.on(", ").join(keys));
					for (String opt : keys) {
						FabConf.addFailure(opt);
					}
					return ErrorAction.NONE;
				} else {
					actuallyItWasUs = true;
				}
			}
		}
		return action;
	}

}
