package me.theandrey.bukkit.proxy;

import me.theandrey.bukkit.BukkitEventsMod;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import me.theandrey.bukkit.internal.WorldEventHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ServerProxy implements ISidedProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new WorldEventHooks());

		try {
			// Work test
			CraftBukkitAccessor.get();
		} catch (Exception e) {
			BukkitEventsMod.logger.error("Unable to initialize CraftBukkitAccessor!", e);
			BukkitEventsMod.logger.error("Is this a Bukkit compatible server?");
		}
	}
}
