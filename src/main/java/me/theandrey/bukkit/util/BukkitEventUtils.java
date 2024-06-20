package me.theandrey.bukkit.util;

import javax.annotation.Nullable;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import net.minecraft.entity.Entity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Набор вспомогательных утилит для работы с ивентами
 */
public final class BukkitEventUtils {

	@Nullable
	private static CraftBukkitAccessor accessorInstance;

	private BukkitEventUtils() {
	}

	/**
	 * Задаёт сущность, которая будет использоваться в контексте
	 * {@link EntityDamageByEntityEvent}
	 */
	public static void setEntityDamage(@Nullable Entity entity) {
		getAccessor().setEntityDamage(entity);
	}

	/**
	 * Задаёт блок, который будет использоваться в контексте
	 * {@link EntityDamageByBlockEvent}
	 */
	public static void setBlockDamage(@Nullable org.bukkit.block.Block block) {
		getAccessor().setBlockDamage(block);
	}

	private static CraftBukkitAccessor getAccessor() {
		if (accessorInstance == null) {
			accessorInstance = CraftBukkitAccessor.get();
		}
		return accessorInstance;
	}
}
