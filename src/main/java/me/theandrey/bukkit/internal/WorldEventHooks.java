package me.theandrey.bukkit.internal;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHooks {

	/**
	 * Управляет работой блокировки спавна дропа. Обычно используется при перемещении блоков.
	 */
	public static boolean blockItemSpawn = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public final void handleEntitySpawn(EntityJoinWorldEvent event) {
		if (blockItemSpawn && event.getEntity() instanceof EntityItem) {
			event.setCanceled(true);
			event.setResult(Event.Result.DENY);
		}
	}
}
