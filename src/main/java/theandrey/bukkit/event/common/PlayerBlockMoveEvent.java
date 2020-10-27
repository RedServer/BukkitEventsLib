package theandrey.bukkit.event.common;

import java.util.Objects;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import theandrey.bukkit.event.BlockStateData;
import theandrey.bukkit.event.api.IPlayerEvent;

/**
 * Ивент перемещения блока игроком с помощью инструмента
 */
public class PlayerBlockMoveEvent extends BlockMoveEvent implements IPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;

	public PlayerBlockMoveEvent(Player player, Action action, Block source, Block destination, BlockStateData placedData) {
		super(action, source, destination, placedData);
		this.player = Objects.requireNonNull(player, "player is null");
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
