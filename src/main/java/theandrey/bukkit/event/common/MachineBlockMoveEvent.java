package theandrey.bukkit.event.common;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	public MachineBlockMoveEvent(@Nonnull Action action, @Nullable Block source, @Nullable Block destination, @Nullable BlockStateData placedData, @Nonnull Block machine, @Nonnull IMachineType machineType, @Nullable UUID ownerId) {
		super(action, source, destination, placedData);
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(machineType, "machineType");
		this.ownerId = ownerId;
	}

	/**
	 * Возвращает блок самого механизма, который совершает действие
	 */
	@Nonnull
	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * ID владельца. Может быть null если неизвестен
	 */
	@Nullable
	public UUID getOwnerId() {
		return ownerId;
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	@Nonnull
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
