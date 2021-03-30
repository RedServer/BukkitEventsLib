package theandrey.bukkit.event.util;

import java.util.Objects;
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

	public BlockExplosionCause(Block block, String owner, IMachineType machineType) {
		this.block = Objects.requireNonNull(block, "block");
		this.owner = owner;
		this.machineType = machineType;
	}

	public BlockExplosionCause(Block block, String owner) {
		this(block, owner, null);
	}

	public Block getBlock() {
		return block;
	}

	@Override
	public String getOwnerName() {
		return owner;
	}

	@Override
	public Location getLocation() {
		return block.getLocation();
	}

	@Override
	public IMachineType getMachineType() {
		return machineType;
	}
}
