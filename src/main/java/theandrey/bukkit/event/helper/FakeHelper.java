package theandrey.bukkit.event.helper;

import java.util.UUID;
import javax.annotation.Nonnull;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Утилиты для работы с {@link FakePlayer}
 */
public final class FakeHelper {

	private FakeHelper() {
	}

	/**
	 * Получить профиль игрока по UUID
	 * @return Вернёт профиль даже если не удалось определить никнейм
	 */
	@Nonnull
	public static GameProfile getPlayerProfile(@Nonnull UUID uuid) {
		if (uuid == null) throw new IllegalArgumentException("uuid is null");

		// Использует профиль онлайн-игрока
		for (EntityPlayerMP player : ServerHelper.getOnlinePlayers()) {
			if (player.getGameProfile().getId().equals(uuid)) {
				return player.getGameProfile();
			}
		}

		// ИЛИ ищем в кеше
		GameProfile cached = MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid);
		if (cached != null) return cached;

		// Профиль без ника
		return new GameProfile(uuid, "uuid:" + uuid);
	}
}
