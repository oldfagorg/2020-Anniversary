package org.oldfag;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public final class OldfagAnniversary extends JavaPlugin implements Listener {
	@Override
	public void onLoad() {
		super.onLoad();
		
		try {
			URLClassLoader sysCl = (URLClassLoader) ClassLoader.getSystemClassLoader();
			
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			m.setAccessible(true);
			m.invoke(sysCl, this.getClass().getProtectionDomain().getCodeSource().getLocation());
			
			Class<?> clazz = Class.forName("org.apache.openjpa.enhance.InstrumentationFactory", true, ClassLoader.getSystemClassLoader());
			Instrumentation instrumentation = (Instrumentation) clazz.getDeclaredMethod("getInstrumentation")
				.invoke(null);
			if (instrumentation == null) {
				new IllegalStateException("No Instrumentation").printStackTrace();
			}
			else {
				List<String> names = Arrays.asList(
					"net.minecraft.server.v1_12_R1.WorldMap",
					"net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn",
					"net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo",
					"net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving",
					"net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect"
				);
				instrumentation.addTransformer(
					(loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
						
						String name = className.replace('/', '.');
						if (names.contains(name)) {
							ClassNode classNode = new ClassNode();
							new ClassReader(classfileBuffer).accept(classNode, 0);
							
							boolean transformed = false;
							switch (name) {
								case "net.minecraft.server.v1_12_R1.WorldMap":
									for (MethodNode method : classNode.methods) {
										if (method.name.equals("<init>")) {
											for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext(); ) {
												AbstractInsnNode insn = it.next();
												
												if (insn.getOpcode() == Opcodes.RETURN) {
													InsnList list = new InsnList();
													list.add(new VarInsnNode(Opcodes.ALOAD, 0));
													list.add(new MethodInsnNode(
														INVOKESTATIC,
														"org/oldfag/MapManager",
														"onMapCreate",
														"(Lnet/minecraft/server/v1_12_R1/WorldMap;)V",
														false
													));
													method.instructions.insertBefore(insn, list);
													transformed = true;
												}
											}
										}
									}
									break;
								case "net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn":
									for (MethodNode method : classNode.methods) {
										if (method.name.equals("b") && method.desc.equals("(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V")) {
											ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
											while (iterator.hasNext()) {
												AbstractInsnNode insn = iterator.next();
												if (insn instanceof MethodInsnNode) {
													MethodInsnNode methodInsnNode = (MethodInsnNode)insn;
													if (methodInsnNode.name.equals("a") && methodInsnNode.desc.equals("(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V")) {
														methodInsnNode.setOpcode(INVOKESTATIC);
														methodInsnNode.owner = "org/oldfag/OldfagAnniversary";
														methodInsnNode.name = "writeDataWatcher";
														methodInsnNode.desc = "(Lnet/minecraft/server/v1_12_R1/DataWatcher;Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V";
														transformed = true;
													}
												}
											}
										}
									}
									break;
								case "net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo":
									for (MethodNode method : classNode.methods) {
										if (method.name.equals("<init>")) {
											for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext(); ) {
												AbstractInsnNode insn = it.next();
												
												if (insn.getOpcode() == Opcodes.RETURN) {
													InsnList list = new InsnList();
													list.add(new VarInsnNode(Opcodes.ALOAD, 0));
													list.add(new MethodInsnNode(
														INVOKESTATIC,
														"org/oldfag/OldfagAnniversary",
														"onPlayerInfo",
														"(Lnet/minecraft/server/v1_12_R1/PacketPlayOutPlayerInfo;)V",
														false
													));
													method.instructions.insertBefore(insn, list);
													transformed = true;
												}
											}
										}
									}
									break;
								case "net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving":
									for (MethodNode method : classNode.methods) {
										if (method.name.equals("b") && method.desc.equals("(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V")) {
											ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
											while (iterator.hasNext()) {
												AbstractInsnNode insn = iterator.next();
												if (insn instanceof MethodInsnNode) {
													MethodInsnNode methodInsnNode = (MethodInsnNode)insn;
													if (methodInsnNode.name.equals("a") && methodInsnNode.desc.equals("(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V")) {
														methodInsnNode.setOpcode(INVOKESTATIC);
														methodInsnNode.owner = "org/oldfag/OldfagAnniversary";
														methodInsnNode.name = "writeDataWatcher";
														methodInsnNode.desc = "(Lnet/minecraft/server/v1_12_R1/DataWatcher;Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V";
														transformed = true;
													}
												}
											}
										}
									}
									break;
								case "net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect":
									for (MethodNode method : classNode.methods) {
										if (method.name.equals("<init>") && method.desc.equals("(Lnet/minecraft/server/v1_12_R1/SoundEffect;Lnet/minecraft/server/v1_12_R1/SoundCategory;DDDFF)V")) {
											for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext(); ) {
												AbstractInsnNode insn = it.next();
												
												if (insn.getOpcode() == Opcodes.RETURN) {
													InsnList list = new InsnList();
													list.add(new VarInsnNode(Opcodes.ALOAD, 0));
													list.add(new MethodInsnNode(
														INVOKESTATIC,
														"org/oldfag/SoundManager",
														"onSoundEffect",
														"(Lnet/minecraft/server/v1_12_R1/PacketPlayOutNamedSoundEffect;)V",
														false
													));
													method.instructions.insertBefore(insn, list);
													transformed = true;
												}
											}
										}
									}
									break;
							}
							
							if (!transformed) {
								new IllegalStateException("ERROR: Failed to transform [" + name +"]").printStackTrace();
								System.exit(-1);
							} else {
								System.out.println("OldfagAnniversary transformed: [" + name + "]");
							}
							
							ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
							classNode.accept(writer);
							return writer.toByteArray();
						}
						
						
						return classfileBuffer;
					}
					, true
				);
				Class<?>[] clazzes = names.stream().map((name) -> {
					try {
						return Class.forName(name);
					}
					catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}).toArray(Class[]::new);
				instrumentation.retransformClasses(clazzes);
			}
		} catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		//Bukkit.getServer().getPluginManager().registerEvents(new ResourcePackManager(), this);
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	static Field playerInfoB;
	static Field playerInfoDatad;
	static final String popbob = "0f75a81d70e543c5b892f33c524284f2";
	static UUID popbobUUID = null;
	static UUID getPopbobUUID() {
		if (popbobUUID != null) return popbobUUID;
		popbobUUID = Bukkit.getOfflinePlayer("popbob").getUniqueId();
		return popbobUUID;
	}
	
	public static void onPlayerInfo(PacketPlayOutPlayerInfo packet) {
		try {
			if (playerInfoB == null) {
				playerInfoB = PacketPlayOutPlayerInfo.class.getDeclaredField("b");
				playerInfoB.setAccessible(true);
			}
			Class<?> infoData = Class.forName("net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo$PlayerInfoData");
			if (playerInfoDatad == null) {
				playerInfoDatad = infoData.getDeclaredField("d");
				playerInfoDatad.setAccessible(true);
			}
			if (playerInfoB != null) {
				List b = (List) playerInfoB.get(packet);
				for (Object data : b) {
					GameProfile gameProfile = (GameProfile)playerInfoDatad.get(data);
					if (gameProfile != null) {
						gameProfile = new BackedUpsideDownProfile(gameProfile);
						SkinManager.updateSkin(gameProfile, popbob);
						playerInfoDatad.set(data, gameProfile);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private static DataWatcherObject<String> customNameData;
	private static DataWatcherObject<Boolean> customNameVisibleData;
	
	static {
		try {
			Field aB = net.minecraft.server.v1_12_R1.Entity.class.getDeclaredField("aB");
			aB.setAccessible(true);
			customNameData = (DataWatcherObject<String>) aB.get(null);
			Field aC = net.minecraft.server.v1_12_R1.Entity.class.getDeclaredField("aC");
			aC.setAccessible(true);
			customNameVisibleData = (DataWatcherObject<Boolean>) aC.get(null);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	public static void writeDataWatcher(DataWatcher dataWatcher, PacketDataSerializer packetDataSerializer) {
		try {
			if (true) {
				dataWatcher.a(packetDataSerializer);
				return;
			};
			
			// Set custom name
			String customName = dataWatcher.get(customNameData);
			boolean customNameVisible = dataWatcher.get(customNameVisibleData);
			
			dataWatcher.set(customNameData, "Dinnerbone");
			dataWatcher.set(customNameVisibleData, true);
			
			dataWatcher.a(packetDataSerializer);
			
			dataWatcher.set(customNameData, customName);
			dataWatcher.set(customNameVisibleData, customNameVisible);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	// :( Disabled at johns request
	/*@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player != null) {
			player.setPlayerListName("John200410");
		}
	}*/
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		PlayerProfile playerProfile = event.getPlayer().getPlayerProfile();
		if (playerProfile != null && playerProfile.getGameProfile() != null) {
			GameProfile gameProfile = playerProfile.getGameProfile();
			SkinManager.updateSkin(gameProfile, popbob);
		}
	}
}
