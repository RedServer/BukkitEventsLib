package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
	public MachineBlockModifyEvent(@Nonnull Block machine, @Nonnull Block block, @Nullable String ownerName, @Nonnull MachineAction action, @Nullable BlockStateData placed, @Nonnull IMachineType machineType) {
		super(block, ownerName, action, placed, machineType);
		this.machine = Objects.requireNonNull(machine, "machine is null");
	}

	@Nonnull
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
