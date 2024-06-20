package me.theandrey.bukkit.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IMachineType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Ивент использования предмета существом
 * @author TheAndrey
 */
public class EntityItemUseEvent extends ItemUseEvent {

	private final UUID owner;
	private final Entity entity;
	private final IMachineType machineType;

	/**
	 * @param entity Существо
	 * @param machineType Тип механизма
	 * @param owner ID владельца механизма. (Допускается использование null)
	 * @param item Используемый предмет. (Допускается использование null)
	 * @param click Тип клика (ЛКМ/ПКМ)
	 * @param blockFace Сторона блока по которой совершён клик
	 * @param targetBlock Блок по которому совершён клик. (Допускается использование null)
	 * @param targetEntity Существо по которому совершён клик. (Допускается использование null)
	 */
	public EntityItemUseEvent(Entity entity, IMachineType machineType, @Nullable UUID owner, ItemStack item, ClickType click, BlockFace blockFace, Block targetBlock, Entity targetEntity) {
		super(item, click, blockFace, targetBlock, targetEntity);
		this.entity = Objects.requireNonNull(entity, "entity");
		this.machineType = Objects.requireNonNull(machineType, "machineType");
		this.owner = owner;
	}

	/**
	 * Возвращает ID владельца механизма
	 * @return null если неизвестен
	 */
	public UUID getOwner() {
		return owner;
	}

	/**
	 * Возвращает существо. Используется для определения местоположения.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	public IMachineType getMachineType() {
		return machineType;
	}

	// ---- BUKKIT ----
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
