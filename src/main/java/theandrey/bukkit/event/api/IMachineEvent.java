package theandrey.bukkit.event.api;

import org.bukkit.block.Block;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.MachineAction;

/**
 * Реализуется ивентами, которые вызываются механизмами
 */
public interface IMachineEvent {

	/**
	 * Действие, которое совершает механизм
	 */
	MachineAction getAction();

	/**
	 * Блок, который изменяет механизм
	 */
	Block getBlock();

	/**
	 * Блок, который устанавливает механизм
	 */
	BlockStateData getBlockPlaced();

}
