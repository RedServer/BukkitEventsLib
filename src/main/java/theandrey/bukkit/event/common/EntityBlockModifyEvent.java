package theandrey.bukkit.event.common;

import java.util.Objects;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.MachineAction;
import theandrey.bukkit.event.api.IEntityMachineEvent;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Ивент изменения блока существом
 * @author TheAndrey
 */
public class EntityBlockModifyEvent extends BlockModifyEvent implements IEntityMachineEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Entity entity;

	/**
	 * @param entity Существо
	 * @param block Изменяемый блок
	 * @param ownerName Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public EntityBlockModifyEvent(Entity entity, Block block, String ownerName, MachineAction action, BlockStateData placed, IMachineType machineType) {
		super(block, ownerName, action, placed, machineType);
		this.entity = Objects.requireNonNull(entity, "entity is null");
	}

	@Override
	public Entity getMachineEntity() {
		return entity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
