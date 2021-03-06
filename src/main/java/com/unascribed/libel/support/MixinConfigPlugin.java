package com.unascribed.libel.support;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.unascribed.libel.Agnos;
import com.unascribed.libel.FabConf;
import com.unascribed.libel.FabLog;
import com.unascribed.libel.FabricationMod;
import com.unascribed.libel.support.injection.FailsoftCallbackInjectionInfo;
import com.unascribed.libel.support.injection.FailsoftModifyArgInjectionInfo;
import com.unascribed.libel.support.injection.FailsoftModifyArgsInjectionInfo;
import com.unascribed.libel.support.injection.FailsoftModifyConstantInjectionInfo;
import com.unascribed.libel.support.injection.FailsoftModifyVariableInjectionInfo;
import com.unascribed.libel.support.injection.FailsoftRedirectInjectionInfo;
import com.unascribed.libel.support.injection.FabInjector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {

	private static final SetMultimap<String, String> configKeysForDiscoveredClasses = HashMultimap.create();

	@Override
	public void onLoad(String mixinPackage) {
		FabConf.reload();
		Mixins.registerErrorHandlerClass("com.unascribed.libel.support.MixinErrorHandler_THIS_ERROR_HANDLER_IS_FOR_SOFT_FAILURE_IN_FABRICATION_ITSELF_AND_DOES_NOT_IMPLY_FABRICATION_IS_RESPONSIBLE_FOR_THE_BELOW_ERROR");
		FabLog.warn("Libel is about to inject into Mixin to add support for failsoft mixins.");
		FabLog.warn("THE FOLLOWING WARNINGS ARE NOT AN ERROR AND DO NOT IMPLY LIBEL IS RESPONSIBLE FOR A CRASH.");
		InjectionInfo.register(FailsoftCallbackInjectionInfo.class);
		InjectionInfo.register(FailsoftModifyArgInjectionInfo.class);
		InjectionInfo.register(FailsoftModifyArgsInjectionInfo.class);
		InjectionInfo.register(FailsoftRedirectInjectionInfo.class);
		InjectionInfo.register(FailsoftModifyVariableInjectionInfo.class);
		InjectionInfo.register(FailsoftModifyConstantInjectionInfo.class);
		FabLog.warn("Injection complete.");
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		FabLog.debug("??? Profile: "+FabConf.getProfileName().toLowerCase(Locale.ROOT));
		return discoverClassesInPackage("com.unascribed.libel.mixin", true);
	}

	@SuppressWarnings("unchecked")
	public static List<String> discoverClassesInPackage(String pkg, boolean truncate) {
		FabLog.debug("Starting discovery pass...");
		try {
			int count = 0;
			int enabled = 0;
			int skipped = 0;
			List<String> rtrn = Lists.newArrayList();
			for (ClassInfo ci : getClassesInPackage(pkg)) {
				// we want nothing to do with inner classes and the like
				if (ci.getName().contains("$")) continue;
				count++;
				String truncName = ci.getName().substring(pkg.length()+1);
				FabLog.debug("--");
				FabLog.debug((Math.random() < 0.01 ? "????" : "????")+" Considering "+truncName);
				ClassReader cr = new ClassReader(ci.asByteSource().read());
				ClassNode cn = new ClassNode();
				cr.accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
				boolean eligible = true;
				List<String> eligibilityFailures = Lists.newArrayList();
				List<String> eligibilityNotes = Lists.newArrayList();
				List<String> eligibilitySuccesses = Lists.newArrayList();
				boolean anyRestrictions = false;
				if (cn.visibleAnnotations != null) {
					for (AnnotationNode an : cn.visibleAnnotations) {
						if (an.desc.equals("Lcom/unascribed/libel/support/EligibleIf;")) {
							if (an.values == null) continue;
							for (int i = 0; i < an.values.size(); i += 2) {
								anyRestrictions = true;
								String k = (String)an.values.get(i);
								Object v = an.values.get(i+1);
								if (k.equals("configAvailable")) {
									if (!FabConf.defaultContains((String)v)) {
										FabLog.debug("???? Dev error! Exploding.");
										throw FabConf.devError(cn.name.substring(pkg.length()+1).replace('/', '.')+" references an unknown config key "+v+"\n\nDid you forget to add it to features.txt and run build-features.sh?");
									}
									if (FabConf.limitRuntimeConfigs() && !FabConf.isEnabled((String) v))
										eligible = false;
									if (FabConf.isBanned((String)v)) {
										eligibilityFailures.add("Required config setting "+ FabConf.remap((String)v)+" is banned");
										eligible = false;
									} else {
										eligibilitySuccesses.add("Required config key "+ FabConf.remap((String)v)+" is not banned");
									}
									configKeysForDiscoveredClasses.put(ci.getName(), (String)v);
								} else if (k.equals("envMatches")) {
									String[] arr = (String[])v;
									if (arr[0].equals("Lcom/unascribed/libel/support/Env;")) {
										Env e = Env.valueOf(arr[1]);
										if (!e.matches(FabricationMod.isClient)) {
											eligibilityFailures.add("Environment is incorrect (want "+e.name().toLowerCase(Locale.ROOT)+", isClient: "+FabricationMod.isClient+")");
											eligible = false;
										} else {
											eligibilitySuccesses.add("Environment is correct ("+e.name().toLowerCase(Locale.ROOT)+")");
										}
									}
								} else if (k.equals("modLoaded")) {
									for (String s : (List<String>)v) {
										if (!Agnos.isModLoaded(s)) {
											eligibilityFailures.add("Required mod "+s+" is not loaded");
											eligible = false;
										} else {
											eligibilitySuccesses.add("Required mod "+s+" is loaded");
										}
									}
								} else if (k.equals("modNotLoaded")) {
									for (String s : (List<String>)v) {
										if (Agnos.isModLoaded(s)) {
											eligibilityFailures.add("Conflicting mod "+s+" is loaded");
											eligible = false;
										} else {
											eligibilitySuccesses.add("Conflicting mod "+s+" is not loaded");
										}
									}
								} else if (k.equals("classPresent")) {
									for (String s : (List<String>)v) {
										try {
											Class.forName(s, false, MixinConfigPlugin.class.getClassLoader());
											eligibilitySuccesses.add("Required class "+s+" is present");
										} catch (ClassNotFoundException e) {
											eligibilityFailures.add("Required class "+s+" is not present");
											eligible = false;
										}
									}
								} else if (k.equals("classNotPresent")) {
									for (String s : (List<String>)v) {
										try {
											Class.forName(s, false, MixinConfigPlugin.class.getClassLoader());
											eligibilityFailures.add("Conflicting class "+s+" is present");
											eligible = false;
										} catch (ClassNotFoundException e) {
											eligibilitySuccesses.add("Conflicting class "+s+" is not present");
										}
									}
								} else if (k.equals("anyConfigAvailable")) {
									boolean allBanned = true;
									boolean runtimeCheck = FabConf.limitRuntimeConfigs();
									for (String s : (List<String>)v) {
										s = FabConf.remap(s);
										if (runtimeCheck && FabConf.isEnabled(s))
											runtimeCheck = false;
										if (FabConf.isBanned(s)) {
											eligibilityNotes.add("Relevant config setting "+s+" is banned");
										} else {
											allBanned = false;
											eligibilitySuccesses.add("Relevant config setting "+s+" is not banned");
										}
										configKeysForDiscoveredClasses.put(ci.getName(), s);
									}
									if (runtimeCheck)
										eligible = false;
									if (allBanned) {
										eligibilityFailures.add("All of the relevant config settings are banned");
										eligible = false;
									}
								} else if (k.equals("specialConditions")) {
									List<String[]> li = (List<String[]>)v;
									if (li.isEmpty()) {
										eligibilityNotes.add("Special conditions is present but empty - ignoring");
									} else {
										for (String[] e : li) {
											if (!"Lcom/unascribed/libel/support/SpecialEligibility;".equals(e[0])) {
												eligibilityNotes.add("Unknown special condition type "+e[0]+" - ignoring");
											} else {
												try {
													SpecialEligibility se = SpecialEligibility.valueOf(e[1]);
													if (FabConf.isMet(se)) {
														eligibilitySuccesses.add("Special condition "+se+" is met");
													} else {
														eligibilityFailures.add("Special condition "+se+" is not met");
														eligible = false;
													}
												} catch (IllegalArgumentException ex) {
													eligibilityFailures.add("Unknown special condition "+e[1]);
													eligible = false;
												}
											}
										}
									}
								} else {
									FabLog.warn("Unknown annotation setting "+k);
								}
							}
						}
					}
				}
				if (!anyRestrictions) {
					eligibilityNotes.add("No restrictions on eligibility");
				}
				for (String s : eligibilityNotes) {
					FabLog.debug("  ?????? "+s);
				}
				for (String s : eligibilitySuccesses) {
					FabLog.debug("  ??? "+s);
				}
				for (String s : eligibilityFailures) {
					FabLog.debug("  ??? "+s);
				}
				if (eligible) {
					enabled++;
					FabLog.debug("???? Eligibility requirements met. Applying "+truncName);
					rtrn.add(truncate ? truncName : ci.getName());
				} else {
					skipped++;
					FabLog.debug("??? Eligibility requirements not met. Skipping "+truncName);
				}
			}
			FabLog.debug("Discovery pass complete. Found "+count+" candidates, enabled "+enabled+", skipped "+skipped+".");
			return rtrn;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Iterable<ClassInfo> getClassesInPackage(String pkg) {
		try (InputStream is = MixinConfigPlugin.class.getClassLoader().getResourceAsStream("classes.txt")) {
			if (is != null) {
				Constructor<ClassInfo> cons = ClassInfo.class.getDeclaredConstructor(String.class, ClassLoader.class);
				cons.setAccessible(true);
				List<ClassInfo> rtrn = Lists.newArrayList();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
				String prefix = pkg.replace('.', '/')+"/";
				while (true) {
					String line = br.readLine();
					if (line == null) break;
					if (line.startsWith(prefix)) {
						rtrn.add(cons.newInstance(line, MixinConfigPlugin.class.getClassLoader()));
					}
				}
				return rtrn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return ClassPath.from(MixinConfigPlugin.class.getClassLoader()).getTopLevelClassesRecursive(pkg);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (FabConf.limitRuntimeConfigs()) finalizeIsEnabled(targetClass);
		FabInjector.apply(targetClass);
	}

	public static void finalizeIsEnabled(ClassNode targetClass){
		targetClass.methods.forEach(methodNode -> {
			for (ListIterator<AbstractInsnNode> it = methodNode.instructions.iterator(); it.hasNext();) {
				AbstractInsnNode insnNode = it.next();
				if (insnNode instanceof MethodInsnNode) {
					MethodInsnNode insn = (MethodInsnNode) insnNode;
					if (insn.getOpcode() == Opcodes.INVOKESTATIC && "com/unascribed/libel/FabConf".equals(insn.owner) && "(Ljava/lang/String;)Z".equals(insn.desc)) {
						AbstractInsnNode prevInsn = insn.getPrevious();
						if (prevInsn.getOpcode() == Opcodes.LDC){
							Object key = ((LdcInsnNode)prevInsn).cst;
							if ("isEnabled".equals(insn.name)){
								methodNode.instructions.insertBefore(prevInsn, new InsnNode(FabConf.isEnabled((String)key)? Opcodes.ICONST_1 : Opcodes.ICONST_0));
							}else if ("isAnyEnabled".equals(insn.name)){
								methodNode.instructions.insertBefore(prevInsn, new InsnNode(FabConf.isAnyEnabled((String)key)? Opcodes.ICONST_1 : Opcodes.ICONST_0));
							}else continue;
							methodNode.instructions.remove(prevInsn);
							methodNode.instructions.remove(insn);
							FabLog.debug("Removed IsEnabled Check from : "+targetClass.name+";"+methodNode.name+methodNode.desc);
						}
					}
				}
			}
		});
	}

}
