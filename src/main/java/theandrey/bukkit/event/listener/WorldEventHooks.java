package theandrey.bukkit.event.listener;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class WorldEventHooks {

	/**
	 * Управляет работой блокировки спавна дропа. Обычно используется при перемещении блоков.
	 */
	public static boolean blockItemSpawn = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public final void handleEntitySpawn(EntityJoinWorldEvent event) {
		if (blockItemSpawn && event.entity instanceof EntityItem) {
			event.setCanceled(true);
			event.setResult(Event.Result.DENY);
		}
	}
}
