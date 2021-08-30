package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	public ItemUseEvent(@Nullable ItemStack item, @Nonnull ClickType click, @Nonnull BlockFace blockFace, @Nullable Block targetBlock, @Nullable Entity targetEntity) {
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
	@Nullable
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Тип клика (ЛКМ/ПКМ)
	 */
	@Nonnull
	public ClickType getClick() {
		return click;
	}

	/**
	 * Сторона блока по которой будет совершён клик
	 */
	@Nonnull
	public BlockFace getBlockFace() {
		return blockFace;
	}

	/**
	 * Блок по которому будет совершён клик
	 * @return null при отсутствии
	 */
	@Nullable
	public Block getTargetBlock() {
		return targetBlock;
	}

	/**
	 * Существо по которому будет совершён клик
	 * @return null при отсутствии
	 */
	@Nullable
	public Entity getTargetEntity() {
		return targetEntity;
	}

	public enum ClickType {
		LEFT, RIGHT
	}

}
