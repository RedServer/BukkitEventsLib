package theandrey.bukkit.event.helper;

import java.lang.reflect.Field;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public final class ServerHelper {

	private static Field floatingTickCount;

	private ServerHelper() {
	}

	/**
	 * Обнуляет счётчик полёта, чтобы не срабатывал античит на игрока
	 */
	public static void resetPlayerInAirTime(EntityPlayerMP player) {
		if(player.capabilities.allowFlying) return; // Фикс не требуется
		try {
			if(floatingTickCount == null) {
				floatingTickCount = NetHandlerPlayServer.class.getDeclaredField("field_147365_f"); // floatingTickCount
				floatingTickCount.setAccessible(true);
			}

			floatingTickCount.setInt(player.playerNetServerHandler, 0);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Failed to reset fly counter", ex);
		}
	}
}
