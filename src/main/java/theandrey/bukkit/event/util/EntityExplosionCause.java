package theandrey.bukkit.event.util;

import java.util.Objects;
import java.util.UUID;
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
	private final UUID ownerId;
	private final IMachineType machineType;

	public EntityExplosionCause(Entity entity, @Nullable UUID ownerId, @Nullable IMachineType machineType) {
		this.entity = Objects.requireNonNull(entity, "entity");
		this.ownerId = ownerId;
		this.machineType = machineType;
	}

	public EntityExplosionCause(Entity entity, @Nullable UUID ownerId) {
		this(entity, ownerId, null);
	}

	public Entity getEntity() {
		return entity;
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return ownerId;
	}

	@Override
	public Location getLocation() {
		return entity.getLocation();
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return machineType;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{entity=" + entity + ", ownerId=" + ownerId + ", machineType=" + machineType + '}';
	}
}
