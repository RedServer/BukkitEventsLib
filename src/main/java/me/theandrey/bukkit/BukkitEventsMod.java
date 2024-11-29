package me.theandrey.bukkit;

import me.theandrey.bukkit.proxy.ISidedProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Главный класс мода
 * @author TheAndrey
 */
@Mod(modid = BukkitEventsMod.MOD_ID, useMetadata = true, acceptableRemoteVersions = "*")
public final class BukkitEventsMod {

	public static final String MOD_ID = "bukkitevents";
	public static final Logger logger = LogManager.getLogger("BukkitEvents");

	@SidedProxy(clientSide = "me.theandrey.bukkit.proxy.ClientProxy", serverSide = "me.theandrey.bukkit.proxy.ServerProxy")
	public static ISidedProxy proxy;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
}
