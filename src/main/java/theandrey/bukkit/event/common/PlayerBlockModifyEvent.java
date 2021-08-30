package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import theandrey.bukkit.event.api.IPlayerEvent;

/**
 * Ивент изменения блока игроком без разрушения или замены (например: покраска, поворот и т.д.).
 * Используется для проверки наличия доступа у игрока к региону.
 */
public class PlayerBlockModifyEvent extends CancellableEvent implements IPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Block block;

	public PlayerBlockModifyEvent(@Nonnull Block block, @Nonnull Player player) {
		this.player = Objects.requireNonNull(player, "player");
		this.block = Objects.requireNonNull(block, "block");
	}

	/**
	 * Игрок, который совершает действие
	 */
	@Override
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Изменяемый игроком блок
	 */
	@Nonnull
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
}
