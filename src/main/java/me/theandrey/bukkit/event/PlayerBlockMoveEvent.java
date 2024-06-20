package me.theandrey.bukkit.event;

import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.IPlayerEvent;
import me.theandrey.bukkit.data.BlockStateData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Ивент перемещения блока игроком с помощью инструмента
 */
public class PlayerBlockMoveEvent extends BlockMoveEvent implements IPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;

	public PlayerBlockMoveEvent(Player player, Action action, @Nullable Block source, @Nullable Block destination, @Nullable BlockStateData placedData) {
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
