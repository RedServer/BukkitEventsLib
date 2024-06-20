package me.theandrey.bukkit.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IMachineType;
import me.theandrey.bukkit.api.MachineAction;
import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Базовый ивент изменения блока
 * @author TheAndrey
 */
public abstract class BlockModifyEvent extends Event implements Cancellable {

	protected final Block block;
	protected final UUID owner;
	protected final MachineAction action;
	protected final BlockStateData blockPlaced;
	protected final IMachineType machineType;
	protected boolean cancelled = false;

	/**
	 * @param block Изменяемый блок
	 * @param owner Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param blockPlaced Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public BlockModifyEvent(Block block, @Nullable UUID owner, MachineAction action, @Nullable BlockStateData blockPlaced, IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block is null");
		this.owner = owner;
		this.action = Objects.requireNonNull(action, "action is null");
		this.blockPlaced = blockPlaced;
		this.machineType = Objects.requireNonNull(machineType, "machineType is null");
	}

	/**
	 * Изменяемый блок
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * ID владельца
	 */
	@Nullable
	public UUID getOwner() {
		return owner;
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
	 * Тип нового блока
	 * @return null, если событие не связано с установкой блока
	 */
	@Nullable
	public BlockStateData getBlockPlaced() {
		return blockPlaced;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
