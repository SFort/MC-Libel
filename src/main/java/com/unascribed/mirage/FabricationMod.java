package com.unascribed.mirage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.unascribed.mirage.support.ConfigLoader;
import com.unascribed.mirage.support.ConfigValue;
import com.unascribed.mirage.support.Feature;
import com.unascribed.mirage.support.MixinConfigPlugin;
import com.unascribed.mirage.support.OptionalFScript;
import com.unascribed.mirage.support.ResolvedConfigValue;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Mod(modid = "mirage", name = "Mirage", version = "3.0")
public class FabricationMod {

	private static final Map<String, Feature> features = Maps.newHashMap();
	private static final List<Feature> unconfigurableFeatures = Lists.newArrayList();
	private static final Set<String> enabledFeatures = Sets.newHashSet();
	public static File confPath = new File("config");
	public static boolean isClient;

	public static final long LAUNCH_ID = ThreadLocalRandom.current().nextLong();

	public static SoundEvent LEVELUP_LONG;
	public static SoundEvent OOF;
	public static SoundEvent ABSORPTION_HURT;
	static {
		try {
			Class.forName("net.minecraft.client.renderer.texture.SimpleTexture", false, FabricationMod.class.getClassLoader());
			isClient = true;
		} catch (Exception ignore) {}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FabLog.log = event.getModLog();
	}
	@Mod.EventHandler
	public void init(FMLPostInitializationEvent event) {
		if (isClient) {
			LEVELUP_LONG = new SoundEvent(new ResourceLocation("mirage", "levelup_long"));
			OOF = new SoundEvent(new ResourceLocation("mirage", "oof"));
			ABSORPTION_HURT = new SoundEvent(new ResourceLocation("mirage", "absorption_hurt"));
		}
		for (String str : MixinConfigPlugin.discoverClassesInPackage("com.unascribed.mirage.loaders", false)) {
			try {
				FabConf.introduce((ConfigLoader)Class.forName(str).newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (Loader.isModLoaded("fscript")) OptionalFScript.reload();
		for (String s : MixinConfigPlugin.discoverClassesInPackage("com.unascribed.mirage.features", false)) {
			try {
				Feature r = (Feature)Class.forName(s).newInstance();
				String key = FabConf.remap(r.getConfigKey());
				if (key == null || FabConf.isEnabled(key)) {
					try {
						r.apply();
						if (key != null) {
							enabledFeatures.add(key);
						}
					} catch (Throwable t) {
						featureError(r, t);
						continue;
					}
				}
				if (key != null) {
					features.put(key, r);
				} else {
					unconfigurableFeatures.add(r);
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to initialize feature "+s, e);
			}
		}
	}

	public static void featureError(Feature f, Throwable t) {
		featureError(f.getClass(), f.getConfigKey(), t);
	}

	public static void featureError(Class<?> clazz, String configKey, Throwable t) {
		FabLog.debug("Original feature error", t);
		if (configKey == null) {
			FabLog.warn("Feature "+clazz.getName()+" failed to apply!");
		} else {
			FabLog.warn("Feature "+clazz.getName()+" failed to apply! Force-disabling "+configKey);
		}
		FabConf.addFailure(configKey);
	}

	public static ResourceLocation createIdWithCustomDefault(String namespace, String pathOrId) {
		if (pathOrId.contains(":")) {
			return new ResourceLocation(pathOrId);
		}
		return new ResourceLocation(namespace, pathOrId);
	}

	public static boolean isAvailableFeature(String configKey) {
		return features.containsKey(FabConf.remap(configKey));
	}

	public static boolean updateFeature(String configKey) {
		configKey = FabConf.remap(configKey);
		boolean enabled = FabConf.isEnabled(configKey);
		if (enabledFeatures.contains(configKey) == enabled) return true;
		if (enabled) {
			features.get(configKey).apply();
			enabledFeatures.add(configKey);
			return true;
		} else {
			boolean b = features.get(configKey).undo();
			if (b) {
				enabledFeatures.remove(configKey);
			}
			return b;
		}
	}

	private static final ResourceLocation CONFIG = new ResourceLocation("mirage", "config");

	public static void sendConfigUpdate(MinecraftServer server, String key, EntityPlayerMP spe) {
		if ("general.profile".equals(key)) key = null;
		PacketBuffer data = new PacketBuffer(Unpooled.buffer());
		if (key == null) {
			Map<String, ResolvedConfigValue> trileans = Maps.newHashMap();
			Map<String, String> strings = Maps.newHashMap();
			for (String k : FabConf.getAllKeys()) {
				if (FabConf.isStandardValue(k)) {
					trileans.put(k, FabConf.getResolvedValue(k));
				} else {
					strings.put(k, FabConf.getRawValue(k));
				}
			}
			data.writeVarInt(trileans.size());
			trileans.entrySet().forEach(en -> data.writeString(en.getKey()).writeByte(en.getValue().ordinal()));
			data.writeVarInt(strings.size());
			strings.entrySet().forEach(en -> data.writeString(en.getKey()).writeString(en.getValue()));
			data.writeLong(LAUNCH_ID);
		} else {
			if (FabConf.isStandardValue(key)) {
				data.writeVarInt(1);
				data.writeString(key);
				data.writeByte(FabConf.getResolvedValue(key).ordinal());
				data.writeVarInt(0);
				data.writeLong(LAUNCH_ID);
			} else {
				data.writeVarInt(0);
				data.writeVarInt(1);
				data.writeString(key);
				data.writeString(FabConf.getRawValue(key));
				data.writeLong(LAUNCH_ID);
			}
		}
		data.writeString(Agnos.getModVersion());
		data.writeVarInt(FabConf.getAllFailures().size());
		for (String k : FabConf.getAllFailures()) {
			data.writeString(k);
		}
		data.writeVarInt(FabConf.getAllBanned().size());
		for (String k : FabConf.getAllBanned()) {
			data.writeString(k);
		}
		SPacketCustomPayload pkt = new SPacketCustomPayload(CONFIG.toString(), data);
		spe.connection.sendPacket(pkt);
	}

	private static final BlockPos.MutableBlockPos scratchpos1 = new BlockPos.MutableBlockPos();
	private static final BlockPos.MutableBlockPos scratchpos2 = new BlockPos.MutableBlockPos();
	private static final BlockPos.MutableBlockPos scratchpos3 = new BlockPos.MutableBlockPos();
	private static final BlockPos.MutableBlockPos scratchpos4 = new BlockPos.MutableBlockPos();

	public interface BlockScanCallback {
		boolean invoke(World w, BlockPos.MutableBlockPos bp, BlockPos.MutableBlockPos scratch, EnumFacing dir);
	}

	public static void forAllAdjacentBlocks(Entity entity, BlockScanCallback callback) {
		World w = entity.world;
		AxisAlignedBB box = entity.getCollisionBoundingBox();
		if (!scanBlocks(w, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, EnumFacing.DOWN, callback)) return;
		if (!scanBlocks(w, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, EnumFacing.UP, callback)) return;

		if (!scanBlocks(w, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.maxZ, EnumFacing.WEST, callback)) return;
		if (!scanBlocks(w, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, EnumFacing.EAST, callback)) return;

		if (!scanBlocks(w, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, EnumFacing.NORTH, callback)) return;
		if (!scanBlocks(w, box.minX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, EnumFacing.SOUTH, callback)) return;
	}

	private static boolean scanBlocks(World w, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, EnumFacing dir,
			BlockScanCallback callback) {
		BlockPos min = scratchpos1.setPos(minX+dir.getFrontOffsetX(), minY+dir.getFrontOffsetY(), minZ+dir.getFrontOffsetZ());
		BlockPos max = scratchpos2.setPos(maxX+dir.getFrontOffsetX(), maxY+dir.getFrontOffsetY(), maxZ+dir.getFrontOffsetZ());
		BlockPos.MutableBlockPos mut = scratchpos3;
		if (w.isAreaLoaded(min, max)) {
			for (int x = min.getX(); x <= max.getX(); x++) {
				for (int y = min.getY(); y <= max.getY(); y++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						mut.setPos(x, y, z);
						scratchpos4.setPos(mut);
						if (!callback.invoke(w, mut, scratchpos4, dir)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
