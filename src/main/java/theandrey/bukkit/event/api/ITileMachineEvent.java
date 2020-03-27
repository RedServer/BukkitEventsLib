package theandrey.bukkit.event.api;

import org.bukkit.block.Block;

/**
 * Реализуется ивентами, которые вызываются классическими механизмами (TileEntity)
 */
public interface ITileMachineEvent extends IMachineEvent {

	/**
	 * Получить блок механизма
	 */
	Block getMachineBlock();

}
