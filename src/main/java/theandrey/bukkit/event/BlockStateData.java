package theandrey.bukkit.event;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.bukkit.Material;

/**
 * Просто хранить информацию о блоке
 */
public final class BlockStateData {

	private final Material type;
	private final int meta;

	/**
	 * @param type Material блока
	 * @param meta Meta блока
	 */
	public BlockStateData(@Nonnull Material type, int meta) {
		this.type = Objects.requireNonNull(type, "type");
		this.meta = meta;
	}

	/**
	 * @param type ID блока
	 */
	public BlockStateData(@Nonnull Material type) {
		this(type, 0);
	}

	public static BlockStateData create(net.minecraft.block.Block block, int meta) {
		return new BlockStateData(BukkitEventUtils.getMaterial(block), meta);
	}

	public static BlockStateData create(net.minecraft.block.Block block) {
		return create(block, 0);
	}

	/**
	 * Material блока
	 */
	@Nonnull
	public Material getType() {
		return type;
	}

	/**
	 * Meta блока
	 */
	public int getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{type=" + type + ", meta=" + meta + "}";
	}
}
