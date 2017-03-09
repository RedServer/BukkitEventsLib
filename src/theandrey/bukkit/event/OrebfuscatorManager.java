package theandrey.bukkit.event;

import net.minecraft.world.World;

public final class OrebfuscatorManager {

	private OrebfuscatorManager() {
	}

	/**
	 * Отправляет клиентам обновления ближайших блоков
	 * @param world Мир
	 * @param x Координата
	 * @param y Координата
	 * @param z Координата
	 */
	public static void updateNearbyBlocks(World world, int x, int y, int z) {
		BukkitEventUtils.craftBukkitAccessor.ofc_updateNearbyBlocks(world, x, y, z);
	}

}
