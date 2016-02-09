package theandrey.bukkit.event.api;

import org.bukkit.entity.Entity;

/**
 * Реализуется эвентами, которые вызываются механизмами, представляющими собой Entity
 */
public interface IEntityMachineEvent extends IMachineEvent {

	/**
	 * Получить блок механизма
	 * @return
	 */
	public Entity getMachineEntity();

}
