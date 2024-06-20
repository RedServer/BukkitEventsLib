package me.theandrey.bukkit.event;

import java.util.Objects;
import me.theandrey.bukkit.api.IPlayerEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Ивент изменения блока игроком без разрушения или замены (например: покраска, поворот и т.д.).
 * Используется для проверки наличия доступа у игрока к региону.
 */
public class PlayerBlockModifyEvent extends Event implements IPlayerEvent, Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Block block;
	protected boolean cancelled = false;

	public PlayerBlockModifyEvent(Block block, Player player) {
		this.player = Objects.requireNonNull(player, "player");
		this.block = Objects.requireNonNull(block, "block");
	}

	/**
	 * Игрок, который совершает действие
	 */
	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Изменяемый игроком блок
	 */
	public Block getBlock() {
		return block;
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
