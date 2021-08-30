package theandrey.bukkit.event.util;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.api.IMachineType;
import theandrey.bukkit.event.common.CustomExplosionEvent;

/**
 * Взрыв вызванный существом. Обычно является источником взрыва {@link CustomExplosionEvent#getExploder()}
 */
public class EntityExplosionCause implements ExplosionCause {

	private final Entity entity;
	private final String owner;
	private final IMachineType machineType;

	public EntityExplosionCause(@Nonnull Entity entity, @Nullable String owner, @Nullable IMachineType machineType) {
		this.entity = Objects.requireNonNull(entity, "entity");
		this.owner = owner;
		this.machineType = machineType;
	}

	public EntityExplosionCause(@Nonnull Entity entity, @Nullable String owner) {
		this(entity, owner, null);
	}

	@Nonnull
	public Entity getEntity() {
		return entity;
	}

	@Nullable
	@Override
	public String getOwnerName() {
		return owner;
	}

	@Nonnull
	@Override
	public Location getLocation() {
		return entity.getLocation();
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return machineType;
	}
}
