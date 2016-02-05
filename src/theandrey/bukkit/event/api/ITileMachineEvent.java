package theandrey.bukkit.event.api;

import org.bukkit.block.Block;

/**
 * Реализуется эвентами, которые вызываются классическими механизмами (TileEntity)
 */
public interface ITileMachineEvent extends IMachineEvent {

	/**
	 * Получить блок механизма
	 * @return
	 */
	public Block getMachineBlock();

}
