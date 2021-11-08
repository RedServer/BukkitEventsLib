package theandrey.bukkit.event.helper;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
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

		// Используем профиль онлайн-игрока
		for (EntityPlayerMP player : ServerHelper.getOnlinePlayers()) {
			if (player.getGameProfile().getId().equals(uuid)) {
				return player.getGameProfile();
			}
		}

		// ИЛИ ищем имя в кеше
		String name = Optional.ofNullable(UsernameCache.getLastKnownUsername(uuid)).orElse("uuid:" + uuid);
		return new GameProfile(uuid, name);
	}
}
