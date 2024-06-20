package me.theandrey.bukkit.util;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.theandrey.bukkit.internal.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public final class PlayerHelper {

	private static Field floatingTickCount;

	private PlayerHelper() {
	}

	/**
	 * Обнуляет счётчик полёта, чтобы не срабатывал анти-чит на игрока
	 */
	public static void resetPlayerInAirTime(EntityPlayerMP player) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (player.capabilities.allowFlying) return; // Фикс не требуется

		try {
			if (floatingTickCount == null) {
				floatingTickCount = ReflectionHelper.findField(NetHandlerPlayServer.class, "field_147365_f", "floatingTickCount");
			}

			floatingTickCount.setInt(player.connection, 0);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Failed to reset fly counter", ex);
		}
	}

	/**
	 * Получить список игроков, находящихся на сервере
	 */
	public static List<EntityPlayerMP> getOnlinePlayers() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
	}

	/**
	 * Получить профиль игрока по UUID
	 * @return Вернёт профиль даже если не удалось определить никнейм
	 */
	public static GameProfile getPlayerProfile(UUID uuid) {
		if (uuid == null) throw new IllegalArgumentException("uuid is null");

		// Используем профиль онлайн-игрока
		for (EntityPlayerMP player : getOnlinePlayers()) {
			if (player.getGameProfile().getId().equals(uuid)) {
				return player.getGameProfile();
			}
		}

		// ИЛИ ищем имя в кеше
		String name = Optional.ofNullable(UsernameCache.getLastKnownUsername(uuid)).orElse("uuid:" + uuid);
		return new GameProfile(uuid, name);
	}
}
