package theandrey.bukkit.event.util;

import java.util.Objects;
import javax.annotation.Nonnull;
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
	private final String owner;
	private final IMachineType machineType;

	public BlockExplosionCause(@Nonnull Block block, @Nullable String owner, @Nullable IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block");
		this.owner = owner;
		this.machineType = machineType;
	}

	public BlockExplosionCause(@Nonnull Block block, @Nullable String owner) {
		this(block, owner, null);
	}

	@Nonnull
	public Block getBlock() {
		return block;
	}

	@Nullable
	@Override
	public String getOwnerName() {
		return owner;
	}

	@Nonnull
	@Override
	public Location getLocation() {
		return block.getLocation();
	}

	@Nullable
	@Override
	public IMachineType getMachineType() {
		return machineType;
	}
}
