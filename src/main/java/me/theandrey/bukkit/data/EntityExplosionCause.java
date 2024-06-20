package me.theandrey.bukkit.data;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.api.IMachineType;
import me.theandrey.bukkit.event.CustomExplosionEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Взрыв вызванный существом. Обычно является источником взрыва {@link CustomExplosionEvent#getExploder()}
 */
public class EntityExplosionCause implements ExplosionCause {

	private final Entity entity;
	private final UUID owner;
	private final IMachineType machineType;

	public EntityExplosionCause(Entity entity, @Nullable UUID owner, @Nullable IMachineType machineType) {
		this.entity = Objects.requireNonNull(entity, "entity");
		this.owner = owner;
		this.machineType = machineType;
	}

	public EntityExplosionCause(Entity entity, @Nullable UUID owner) {
		this(entity, owner, null);
	}

	public Entity getEntity() {
		return entity;
	}

	@Nullable
	@Override
	public UUID getOwner() {
		return owner;
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
		return MoreObjects.toStringHelper(this)
			.add("entity", entity)
			.add("owner", owner)
			.add("machineType", machineType)
			.toString();
	}
}
