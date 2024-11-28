package me.theandrey.bukkit.event;

import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ITreeType;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a tree grows out of a sapling
 */
public class TreeGrowEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Block block;
	private final Collection<Block> affectedBlocks;
	private final ITreeType type;
	protected boolean cancelled = false;

	public TreeGrowEvent(@Nullable Block block, Collection<Block> affectedBlocks, ITreeType type) {
		this.block = block;
		this.affectedBlocks = Objects.requireNonNull(affectedBlocks, "affectedBlocks");
		this.type = Objects.requireNonNull(type, "type");
	}

	/**
	 * Returns tree sapling block.
	 * @return Returns NULL if tree was generated without a sapling
	 */
	@Nullable
	public Block getBlock() {
		return block;
	}

	/**
	 * Returns the list of blocks that would have been changed from tree grow.
	 * @return All changed blocks
	 */
	public Collection<Block> getAffectedBlocks() {
		return affectedBlocks;
	}

	public ITreeType getTreeType() {
		return type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
