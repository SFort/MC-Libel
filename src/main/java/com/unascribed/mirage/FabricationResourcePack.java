package com.unascribed.mirage;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

public class FabricationResourcePack implements IResourcePack {
	private final String path;

	private final JsonObject meta;

	public FabricationResourcePack(String path) {
		this.path = path;
		JsonObject meta;
		try {
			meta = new Gson().fromJson(Resources.toString(url("pack.mcmeta"), Charsets.UTF_8), JsonObject.class);
		} catch (Throwable t) {
			FabLog.warn("Failed to load meta for internal resource pack "+path);
			meta = new JsonObject();
		}
		this.meta = meta;
	}

	private URL url(ResourceLocation id) {
		return url("assets/"+id.getResourceDomain()+"/"+id.getResourcePath());
	}

	private URL url(String path) {
		return getClass().getClassLoader().getResource("packs/"+this.path+"/"+path);
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		URL u = url(location);
		if (u == null) throw new FileNotFoundException(location.toString());
		return u.openStream();
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return url(location) != null;
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.copyOf(Iterables.transform(meta.getAsJsonObject("mirage").getAsJsonArray("namespaces"), JsonElement::getAsString));
	}

	@Nullable @Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
		if (!meta.has(metadataSectionName)) return null;
		return metadataSerializer.parseMetadataSection(metadataSectionName, meta.getAsJsonObject(metadataSectionName));
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return "Mirage";
	}
}
