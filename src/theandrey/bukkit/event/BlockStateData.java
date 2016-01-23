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

	/**
	 * Material блока
	 * @return
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Meta блока
	 * @return
	 */
	public int getData() {
		return data;
	}

}
