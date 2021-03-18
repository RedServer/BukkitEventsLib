package theandrey.bukkit.event.common;

import java.util.Objects;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * Базовый ивент использования предмета
 * @author TheAndrey
 */
public abstract class ItemUseEvent extends CancellableEvent {

	protected final ItemStack item;
	protected final ClickType click;
	protected final BlockFace blockFace;
	protected final Block targetBlock;
	protected final Entity targetEntity;

	public ItemUseEvent(ItemStack item, ClickType click, BlockFace blockFace, Block targetBlock, Entity targetEntity) {
		this.item = item;
		this.click = Objects.requireNonNull(click, "click");
		this.blockFace = Objects.requireNonNull(blockFace, "blockFace");
		this.targetBlock = targetBlock;
		this.targetEntity = targetEntity;
	}

	/**
	 * Возвращает используемый предмет
	 * @return Может возвращать null, если предмет отсутствует (при взаимодействии с блоками)
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Тип клика (ЛКМ/ПКМ)
	 */
	public ClickType getClick() {
		return click;
	}

	/**
	 * Сторона блока по которой будет совершён клик
	 */
	public BlockFace getBlockFace() {
		return blockFace;
	}

	/**
	 * Блок по которому будет совершён клик
	 * @return null при отсутствии
	 */
	public Block getTargetBlock() {
		return targetBlock;
	}

	/**
	 * Существо по которому будет совершён клик
	 * @return null при отсутствии
	 */
	public Entity getTargetEntity() {
		return targetEntity;
	}

	public enum ClickType {
		LEFT, RIGHT
	}

}
