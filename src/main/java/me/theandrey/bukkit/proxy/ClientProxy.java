package me.theandrey.bukkit.proxy;

import me.theandrey.bukkit.BukkitEventsMod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy implements ISidedProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		BukkitEventsMod.logger.warn("=============================================================");
		BukkitEventsMod.logger.warn("This mod is intended for server-side use only!");
		BukkitEventsMod.logger.warn("Mod doesn't have any useful functionality on the client side.");
		BukkitEventsMod.logger.warn("=============================================================");
	}
}
