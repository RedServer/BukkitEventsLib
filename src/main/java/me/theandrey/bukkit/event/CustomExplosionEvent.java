package me.theandrey.bukkit.event;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomExplosionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Entity exploder;
	private final Location location;
	private final ExplosionCause cause;
	private final List<Block> blocks;
	private final List<Entity> entities;
	private boolean isFlaming;
	private float yield;
	protected boolean cancelled = false;

	public CustomExplosionEvent(@Nullable Entity exploder, Location location, ExplosionCause cause, List<Block> blocks, List<Entity> entities, boolean isFlaming, float yield) {
		this.exploder = exploder;
		this.location = Objects.requireNonNull(location, "location");
		this.cause = Objects.requireNonNull(cause, "cause");
		this.blocks = Objects.requireNonNull(blocks, "blocks");
		this.entities = Objects.requireNonNull(entities, "entities");
		this.isFlaming = isFlaming;
		this.yield = yield;
	}

	/**
	 * Возвращает существо, которое вызвало взрыв
	 * @return Может возвращать null (например, если это был блок)
	 */
	@Nullable
	public Entity getExploder() {
		return exploder;
	}

	/**
	 * Точка эпицентра взрыва
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Возвращает объект с описанием причины/источника взрыва
	 */
	public ExplosionCause getCause() {
		return cause;
	}

	/**
	 * Возвращает список затронутых блоков. Удаление элемента из этого списка предотвращает разрушение блока.
	 */
	public List<Block> getAffectedBlocks() {
		return blocks;
	}

	/**
	 * Возвращает список существ получающих урон. Удаление элемента из этого списка предотвращает получение урона существом.
	 */
	public List<Entity> getAffectedEntities() {
		return entities;
	}

	/**
	 * Возвращает процент вероятности выпадения блоков
	 */
	public float getYield() {
		return yield;
	}

	/**
	 * Задаёт процент вероятности выпадения блоков, разрушенных взрывом
	 */
	public void setYield(float yield) {
		this.yield = yield;
	}

	/**
	 * Будет ли поджигать блоки
	 */
	public boolean isFlaming() {
		return isFlaming;
	}

	/**
	 * Задаёт, будет ли взрыв поджигать блоки
	 */
	public void setFlaming(boolean flaming) {
		isFlaming = flaming;
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
