package theandrey.bukkit.event.helper;

import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nonnull;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;

public final class ServerHelper {

	private static Field floatingTickCount;

	private ServerHelper() {
	}

	/**
	 * Обнуляет счётчик полёта, чтобы не срабатывал анти-чит на игрока
	 */
	public static void resetPlayerInAirTime(@Nonnull EntityPlayerMP player) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (player.capabilities.allowFlying) return; // Фикс не требуется

		try {
			if (floatingTickCount == null) {
				floatingTickCount = ReflectionHelper.findField(NetHandlerPlayServer.class, "field_147365_f", "floatingTickCount");
			}

			floatingTickCount.setInt(player.playerNetServerHandler, 0);
		} catch (ReflectiveOperationException | ReflectionHelper.UnableToFindFieldException ex) {
			throw new RuntimeException("Failed to reset fly counter", ex);
		}
	}

	/**
	 * Получить список игроков, находящихся на сервере
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public static List<EntityPlayerMP> getOnlinePlayers() {
		return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
	}
}
