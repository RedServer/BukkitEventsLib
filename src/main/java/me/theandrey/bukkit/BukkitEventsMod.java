package me.theandrey.bukkit;

import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import me.theandrey.bukkit.internal.WorldEventHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
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

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide() != Side.SERVER) {
			throw new RuntimeException("This is server mod!");
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new WorldEventHooks());

		CraftBukkitAccessor.get(); // Work test
	}
}
