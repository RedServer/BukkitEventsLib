package theandrey.bukkit.event.api;

import org.bukkit.block.Block;
import theandrey.bukkit.event.MachineAction;

/**
 * Реализуется эвентами, которые вызываются механизмами
 */
public interface IMachineEvent {

	/**
	 * Действие, которое совершает механизм
	 * @return
	 */
	public MachineAction getAction();

	/**
	 * Получить блок механизма
	 * @return
	 */
	public Block getMachineBlock();

	/**
	 * Блок, который изменяет механизм
	 * @return
	 */
	public Block getBlock();

}
