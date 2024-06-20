package me.theandrey.bukkit.data;

import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.api.IMachineType;
import org.bukkit.Location;

/**
 * Причина взрыва по умолчанию, если неизвестно чем был вызван.
 * Разрушения допускаются только на свободной территории.
 */
public final class DefaultExplosionCause implements ExplosionCause {

	@Nullable
	@Override
	public UUID getOwner() {
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
