package theandrey.bukkit.event.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

/**
 * Альтернатива GameProfile для Bukkit. Чисто для того чтобы не подключать authlib к проектам :)
 * @author TheAndrey
 */
public final class BukkitGameProfile {

	private final UUID id;
	private final String name;

	/**
	 * Конструктор
	 * @param id UUID игрока
	 * @param name Никнейм игрока
	 */
	public BukkitGameProfile(UUID id, String name) {
		if(id == null && (name == null || name.isEmpty())) throw new IllegalArgumentException("Missing name and UUID");
		this.id = id;
		this.name = name;
	}

	/**
	 * Получить ID игрока
	 * @return
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Получить никнейм игрока
	 * @return Никнейм игрока
	 */
	public String getName() {
		return name;
	}

	/**
	 * Создать объект из Mojang GameProfile
	 * @param profile Ванильный профиль
	 * @return Вернёт null, если в качестве параметра получит null
	 */
	public static BukkitGameProfile create(GameProfile profile) {
		if(profile == null) return null;
		return new BukkitGameProfile(profile.getId(), profile.getName());
	}

}
