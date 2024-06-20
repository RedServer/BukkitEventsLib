package theandrey.bukkit.event.util;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Взрыв блока (механизма)
 */
public class BlockExplosionCause implements ExplosionCause {

	private final Block block;
	private final UUID ownerId;
	private final IMachineType machineType;

	public BlockExplosionCause(Block block, @Nullable UUID ownerId, @Nullable IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block");
		this.ownerId = ownerId;
		this.machineType = machineType;
	}

	public BlockExplosionCause(Block block, @Nullable UUID ownerId) {
		this(block, ownerId, null);
	}

	public Block getBlock() {
		return block;
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return ownerId;
	}

	@Override
	public Location getLocation() {
		return block.getLocation();
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return machineType;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{block=" + block + ", ownerId=" + ownerId + ", machineType=" + machineType + '}';
	}
}
