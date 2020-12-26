package theandrey.bukkit.event.common;

import java.util.Objects;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.MachineAction;
import theandrey.bukkit.event.api.IMachineType;
import theandrey.bukkit.event.api.ITileMachineEvent;

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
	 * @param ownerName Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public MachineBlockModifyEvent(Block machine, Block block, String ownerName, MachineAction action, BlockStateData placed, IMachineType machineType) {
		super(block, ownerName, action, placed, machineType);
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
