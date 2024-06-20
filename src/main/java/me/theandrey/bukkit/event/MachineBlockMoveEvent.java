package me.theandrey.bukkit.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IMachineType;
import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

/**
 * Перемещение блока механизмом
 */
public class MachineBlockMoveEvent extends BlockMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final UUID owner;

	public MachineBlockMoveEvent(Action action, @Nullable Block source, @Nullable Block destination, @Nullable BlockStateData placedData, Block machine, IMachineType machineType, @Nullable UUID owner) {
		super(action, source, destination, placedData);
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(machineType, "machineType");
		this.owner = owner;
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
	@Nullable
	public UUID getOwner() {
		return owner;
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
