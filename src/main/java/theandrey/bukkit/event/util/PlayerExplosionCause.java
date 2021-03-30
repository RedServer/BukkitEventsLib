package theandrey.bukkit.event.util;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Взрыв, созданный игроком. Как правило, с помощью предметов.
 */
public class PlayerExplosionCause implements ExplosionCause {

	private final Player player;

	public PlayerExplosionCause(Player player) {
		this.player = Objects.requireNonNull(player, "player");
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String getOwnerName() {
		return player.getName();
	}

	@Override
	public Location getLocation() {
		return player.getLocation();
	}

	@Override
	public IMachineType getMachineType() {
		return null;
	}
}
