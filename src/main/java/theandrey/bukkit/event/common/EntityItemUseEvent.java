package theandrey.bukkit.event.common;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Ивент использования предмета существом
 * @author TheAndrey
 */
public class EntityItemUseEvent extends ItemUseEvent {

	private final UUID ownerId;
	private final Entity entity;
	private final IMachineType machineType;

	/**
	 * @param entity Существо
	 * @param machineType Тип механизма
	 * @param ownerId ID владельца механизма. (Допускается использование null)
	 * @param item Используемый предмет. (Допускается использование null)
	 * @param click Тип клика (ЛКМ/ПКМ)
	 * @param blockFace Сторона блока по которой совершён клик
	 * @param targetBlock Блок по которому совершён клик. (Допускается использование null)
	 * @param targetEntity Существо по которому совершён клик. (Допускается использование null)
	 */
	public EntityItemUseEvent(Entity entity, IMachineType machineType, @Nullable UUID ownerId, ItemStack item, ClickType click, BlockFace blockFace, Block targetBlock, Entity targetEntity) {
		super(item, click, blockFace, targetBlock, targetEntity);
		this.entity = Objects.requireNonNull(entity, "entity");
		this.machineType = Objects.requireNonNull(machineType, "machineType");
		this.ownerId = ownerId;
	}

	/**
	 * Возвращает ID владельца механизма
	 * @return null если неизвестен
	 */
	public UUID getOwnerId() {
		return ownerId;
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

	@Override
	public String toString() {
		return new StringJoiner(", ", EntityItemUseEvent.class.getSimpleName() + "{", "}")
				.add("entity=" + entity)
				.add("machineType=" + machineType)
				.add("item=" + item)
				.add("click=" + click)
				.add("blockFace=" + blockFace)
				.add("ownerId=" + ownerId)
				.add("targetBlock=" + targetBlock)
				.add("targetEntity=" + targetEntity)
				.add("cancelled=" + cancelled)
				.toString();
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
