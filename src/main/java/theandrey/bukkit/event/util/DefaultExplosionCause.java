package theandrey.bukkit.event.util;

import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Location;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Причина взрыва по умолчанию, если неизвестно чем был вызван.
 * Разрушения допускаются только на свободной территории.
 */
public final class DefaultExplosionCause implements ExplosionCause {

	@Nullable
	@Override
	public UUID getOwnerId() {
		return null;
	}

	@Nullable
	@Override
	public Location getLocation() {
		return null;
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return null;
	}
}
