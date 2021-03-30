package theandrey.bukkit.event.util;

import java.util.Objects;
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

	public EntityExplosionCause(Entity entity, String owner, IMachineType machineType) {
		this.entity = Objects.requireNonNull(entity, "entity");
		this.owner = owner;
		this.machineType = machineType;
	}

	public EntityExplosionCause(Entity entity, String owner) {
		this(entity, owner, null);
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public String getOwnerName() {
		return owner;
	}

	@Override
	public Location getLocation() {
		return entity.getLocation();
	}

	@Override
	public IMachineType getMachineType() {
		return machineType;
	}
}
