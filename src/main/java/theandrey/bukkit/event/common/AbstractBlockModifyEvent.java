package theandrey.bukkit.event.common;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.block.Block;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.MachineAction;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Базовый ивент изменения блока
 * @author TheAndrey
 */
public abstract class AbstractBlockModifyEvent extends CancellableEvent {

	protected final Block block;
	protected final UUID ownerId;
	protected final MachineAction action;
	protected final BlockStateData placed;
	protected final IMachineType machineType;

	/**
	 * @param block Изменяемый блок
	 * @param ownerId Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public AbstractBlockModifyEvent(Block block, UUID ownerId, MachineAction action, BlockStateData placed, IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block is null");
		this.ownerId = ownerId;
		this.action = Objects.requireNonNull(action, "action is null");
		this.placed = placed;
		this.machineType = Objects.requireNonNull(machineType, "machineType is null");
	}

	/**
	 * Изменяемый блок
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * ID владельца. Может быть null если неизвестен
	 */
	public UUID getOwnerId() {
		return ownerId;
	}

	/**
	 * Действие с блоком, совершаемое механизмом
	 */
	public MachineAction getAction() {
		return action;
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	public IMachineType getMachineType() {
		return machineType;
	}

	/**
	 * Тип нового блока. Может быть null
	 */
	public BlockStateData getBlockPlaced() {
		return placed;
	}

}
