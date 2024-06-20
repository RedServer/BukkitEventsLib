package me.theandrey.bukkit.api;

import java.util.UUID;
import me.theandrey.bukkit.CustomExplosion;
import org.bukkit.Location;

/**
 * Причина взрыва. Используется в {@link CustomExplosion}
 */
public interface ExplosionCause {

	/**
	 * Возвращает ID игрока-владельца взорвавшегося механизма или существа
	 * @return Возвращает null если данные отсутствуют
	 */
	UUID getOwner();

	/**
	 * Возвращает местоположение объекта который вызвал взрыв.
	 * @return Может возвращать null, если не поддерживает реализацией
	 */
	Location getLocation();

	/**
	 * Тип механизма который вызывал взрыв
	 * @return Может возвращать null, если реализация не содержит такой информации
	 */
	IMachineType getMachineType();
}
