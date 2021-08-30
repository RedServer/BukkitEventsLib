package theandrey.bukkit.event.helper;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public final class ServerHelper {

	private static Field floatingTickCount;

	private ServerHelper() {
	}

	/**
	 * Обнуляет счётчик полёта, чтобы не срабатывал анти-чит на игрока
	 */
	public static void resetPlayerInAirTime(@Nonnull EntityPlayerMP player) {
		if(player == null) throw new IllegalArgumentException("player is null");
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
