package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.MachineAction;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Базовый ивент изменения блока
 * @author TheAndrey
 */
public abstract class BlockModifyEvent extends CancellableEvent {

	protected final Block block;
	protected final String ownerName;
	protected final MachineAction action;
	protected final BlockStateData placed;
	protected final IMachineType machineType;

	/**
	 * @param block Изменяемый блок
	 * @param ownerName Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public BlockModifyEvent(@Nonnull Block block, @Nullable String ownerName, @Nonnull MachineAction action, @Nullable BlockStateData placed, @Nonnull IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block is null");
		this.ownerName = ownerName;
		this.action = Objects.requireNonNull(action, "action is null");
		this.placed = placed;
		this.machineType = Objects.requireNonNull(machineType, "machineType is null");
	}

	/**
	 * Изменяемый блок
	 */
	@Nonnull
	public Block getBlock() {
		return block;
	}

	/**
	 * Никнейм владельца. Может быть null если неизвестен
	 */
	@Nullable
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * Действие с блоком, совершаемое механизмом
	 */
	@Nonnull
	public MachineAction getAction() {
		return action;
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	@Nonnull
	public IMachineType getMachineType() {
		return machineType;
	}

	/**
	 * Тип нового блока
	 * @return null, если событие не связано с установкой блока
	 */
	@Nullable
	public BlockStateData getBlockPlaced() {
		return placed;
	}

}
