package theandrey.bukkit.event;

import org.bukkit.Material;

/**
 * Просто хранить информацию о блоке
 */
public final class BlockStateData {

	private final Material type;
	private final int data;

	/**
	 * @param type Material блока
	 * @param data Meta блока
	 */
	public BlockStateData(Material type, int data) {
		this.type = type;
		this.data = data;
	}

	/**
	 * @param type ID блока
	 */
	public BlockStateData(Material type) {
		this(type, 0);
	}

	public static BlockStateData create(net.minecraft.block.Block block, int metadata) {
		return new BlockStateData(BukkitEventUtils.getMaterial(block), metadata);
	}

	public static BlockStateData create(net.minecraft.block.Block block) {
		return create(block, 0);
	}

	/**
	 * Material блока
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Meta блока
	 */
	public int getData() {
		return data;
	}

}
