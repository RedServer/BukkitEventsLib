package me.theandrey.bukkit.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IMachineType;
import me.theandrey.bukkit.api.ITileMachineEvent;
import me.theandrey.bukkit.api.MachineAction;
import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

/**
 * Ивент изменения блока механизмом
 * @author TheAndrey
 */
public class MachineBlockModifyEvent extends BlockModifyEvent implements ITileMachineEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;

	/**
	 * @param machine Блок механизма
	 * @param block Изменяемый блок
	 * @param owner Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public MachineBlockModifyEvent(Block machine, Block block, @Nullable UUID owner, MachineAction action, @Nullable BlockStateData placed, IMachineType machineType) {
		super(block, owner, action, placed, machineType);
		this.machine = Objects.requireNonNull(machine, "machine is null");
	}

	@Override
	public Block getMachineBlock() {
		return machine;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
