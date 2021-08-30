package theandrey.bukkit.event.util;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Взрыв, созданный игроком. Как правило, с помощью предметов.
 */
public class PlayerExplosionCause implements ExplosionCause {

	private final Player player;

	public PlayerExplosionCause(@Nonnull Player player) {
		this.player = Objects.requireNonNull(player, "player");
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	@Override
	public UUID getOwnerId() {
		return player.getUniqueId();
	}

	@Nonnull
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
		return getClass().getSimpleName() + "{player=" + player + '}';
	}
}
