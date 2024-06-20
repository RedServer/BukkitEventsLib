package me.theandrey.bukkit.api;

import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;

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
