package theandrey.bukkit.event.api;

import org.bukkit.entity.Entity;

/**
 * Реализуется ивентами, которые вызываются механизмами, представляющими собой Entity
 */
public interface IEntityMachineEvent extends IMachineEvent {

	/**
	 * Получить блок механизма
	 */
	Entity getMachineEntity();

}
