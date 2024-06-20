package me.theandrey.bukkit.event;

import java.util.Objects;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * Базовый ивент использования предмета
 * @author TheAndrey
 */
public abstract class ItemUseEvent extends Event implements Cancellable {

	protected final ItemStack item;
	protected final ClickType click;
	protected final BlockFace blockFace;
	protected final Block targetBlock;
	protected final Entity targetEntity;
	protected boolean cancelled = false;

	public ItemUseEvent(@Nullable ItemStack item, ClickType click, BlockFace blockFace, @Nullable Block targetBlock, @Nullable Entity targetEntity) {
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

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public enum ClickType {
		LEFT, RIGHT
	}

}
