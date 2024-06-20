package me.theandrey.bukkit.data;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.api.IMachineType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	public UUID getOwner() {
		return player.getUniqueId();
	}

	@Override
	public Location getLocation() {
		return player.getLocation();
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("player", player)
			.toString();
	}
}
