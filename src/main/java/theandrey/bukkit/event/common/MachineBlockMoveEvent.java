package theandrey.bukkit.event.common;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Перемещение блока механизмом
 */
public class MachineBlockMoveEvent extends BlockMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final UUID ownerId;

	public MachineBlockMoveEvent(Action action, Block source, Block destination, BlockStateData placedData, Block machine, IMachineType machineType, UUID ownerId) {
		super(action, source, destination, placedData);
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(machineType, "machineType");
		this.ownerId = ownerId;
	}

	/**
	 * Возвращает блок самого механизма, который совершает действие
	 */
	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * ID владельца. Может быть null если неизвестен
	 */
	public UUID getOwnerId() {
		return ownerId;
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	public IMachineType getMachineType() {
		return machineType;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
