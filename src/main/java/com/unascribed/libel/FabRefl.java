package com.unascribed.libel;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.unascribed.libel.support.MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.throwables.MixinError;
import org.spongepowered.asm.mixin.throwables.MixinException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class FabRefl {

	// "muh performance"
	// invokeExact becomes a standard INVOKE* insn after the JIT gets its hands on it. The entire
	// purpose of MethodHandles is to be basically free. the catch is there can be *no* abstraction
	// on an invokeExact or the calltime signature will be wrong and the JVM will get confused.

	private static final MethodHandle ie_pickupDelay = unreflectGetter("ItemEntity", () -> EntityItem.class, "pickupDelay")
			.requiredBy("*.instant_pickup").get();
	public static int getPickupDelay(EntityItem subject) {
		try {
			return (int)checkHandle(ie_pickupDelay).invokeExact(subject);
		} catch (Throwable t) {
			throw rethrow(t);
		}
	}

	private static MethodHandle checkHandle(MethodHandle handle) {
		if (handle == null) throw new IllegalStateException("Attempt to use an unresolved method handle");
		return handle;
	}

	private static final class UnreflResult {
		private final String desc;
		private final MethodHandle val;
		private final Throwable err;

		private final Set<String> requiredBy = Sets.newHashSet();

		private UnreflResult(String desc, MethodHandle val, Throwable err) {
			this.desc = desc;
			this.val = val;
			this.err = err;
		}

		public UnreflResult requiredBy(String... features) {
			for (String f : features) requiredBy.add(f);
			return this;
		}

		public MethodHandle get() {
			if (err != null) {
				if (requiredBy.isEmpty()) throw rethrow(err);
				FabLog.warn("Failed to retrieve "+desc+" - force-disabling "+Joiner.on(", ").join(requiredBy));
				for (String s : requiredBy) {
					FabConf.addFailure(s);
				}
				return null;
			}
			return val;
		}

		public static UnreflResult success(String desc, MethodHandle handle) {
			return new UnreflResult(desc, handle, null);
		}
		public static UnreflResult failure(String desc, Throwable err) {
			return new UnreflResult(desc, null, err);
		}
	}

	private static UnreflResult unreflectGetter(String className, Supplier<Class<?>> clazz, String name) {
		String desc = "field "+className+"#"+name;
		try {
			Field f = clazz.get().getDeclaredField(name);
			f.setAccessible(true);
			return UnreflResult.success(desc, MethodHandles.lookup().unreflectGetter(f));
		} catch (Throwable t) {
			return UnreflResult.failure(desc, t);
		}
	}

	private static UnreflResult unreflectSetter(String className, Supplier<Class<?>> clazz, String name) {
		String desc = "field "+className+"#"+name;
		try {
			Field f = clazz.get().getDeclaredField(name);
			f.setAccessible(true);
			return UnreflResult.success(desc, MethodHandles.lookup().unreflectSetter(f));
		} catch (Throwable t) {
			return UnreflResult.failure(desc, t);
		}
	}

	private static UnreflResult unreflectMethod(String className, Supplier<Class<?>> clazz, String name, Class<?> returnType, Class<?>... args) {
		String desc = "method "+className+"."+name+signatureToString(args);
		try {
			Method m = clazz.get().getDeclaredMethod(name, args);
			if (m.getReturnType() != returnType) {
				throw new NoSuchMethodException("Method "+name+" does not have return type "+returnType+" - it has "+m.getReturnType());
			}
			m.setAccessible(true);
			return UnreflResult.success(desc, MethodHandles.lookup().unreflect(m));
		} catch (Throwable t) {
			return UnreflResult.failure(desc, t);
		}
	}

	private static UnreflResult unreflectConstructor(String className, Supplier<Class<?>> clazz, Class<?>... args) {
		String desc = "constructor "+className+signatureToString(args);
		try {
			Constructor<?> c = clazz.get().getDeclaredConstructor(args);
			c.setAccessible(true);
			return UnreflResult.success(desc, MethodHandles.lookup().unreflectConstructor(c));
		} catch (Throwable t) {
			return UnreflResult.failure(desc, t);
		}
	}

	private static String signatureToString(Class<?>[] args) {
		return "("+Joiner.on(", ").join(Collections2.transform(Arrays.asList(args), Class::getSimpleName))+")";
	}

	private static RuntimeException rethrow(Throwable t) {
		if (!MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR.actuallyItWasUs && (t instanceof MixinError || t instanceof MixinException)) {
			throw new RuntimeException("DO NOT REPORT THIS ERROR TO FABRICATION.\n"
					+ "This is caused by ANOTHER MOD'S MIXIN FAILURE that was initiated by Fabrication initializing reflection.\n"
					+ "Errors like these show up attributed to whoever was the first person to load the class with the broken mixin.\n"
					+ "!!!!! DO NOT REPORT THIS ERROR TO FABRICATION !!!!!");
		}
		if (t instanceof RuntimeException) {
			throw (RuntimeException)t;
		} else if (t instanceof Error) {
			throw (Error)t;
		} else {
			throw new RuntimeException(t);
		}
	}

	private static <T extends Throwable> RuntimeException rethrow(Throwable t, Class<T> passthru) throws T {
		if (passthru.isInstance(t)) {
			throw (T)t;
		} else {
			throw rethrow(t);
		}
	}

}
