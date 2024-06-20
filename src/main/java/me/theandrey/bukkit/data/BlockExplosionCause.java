package me.theandrey.bukkit.data;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.api.IMachineType;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Взрыв блока (механизма)
 */
public class BlockExplosionCause implements ExplosionCause {

	private final Block block;
	private final UUID owner;
	private final IMachineType machineType;

	public BlockExplosionCause(Block block, @Nullable UUID owner, @Nullable IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block");
		this.owner = owner;
		this.machineType = machineType;
	}

	public BlockExplosionCause(Block block, @Nullable UUID owner) {
		this(block, owner, null);
	}

	public Block getBlock() {
		return block;
	}

	@Nullable
	@Override
	public UUID getOwner() {
		return owner;
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
		return MoreObjects.toStringHelper(this)
			.add("block", block)
			.add("owner", owner)
			.add("machineType", machineType)
			.toString();
	}
}
