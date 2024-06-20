package me.theandrey.bukkit.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IEntityMachineEvent;
import me.theandrey.bukkit.api.IMachineType;
import me.theandrey.bukkit.api.MachineAction;
import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

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
	 * @param owner Владелец (может быть null)
	 * @param action Совершаемое действие
	 * @param placed Размещаемый блок (может быть null)
	 * @param machineType Тип механизма из мода
	 */
	public EntityBlockModifyEvent(Entity entity, Block block, @Nullable UUID owner, MachineAction action, @Nullable BlockStateData placed, IMachineType machineType) {
		super(block, owner, action, placed, machineType);
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
