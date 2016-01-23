package theandrey.bukkit.event;

import org.bukkit.Material;

/**
 * Просто хранить информацию о блоке
 */
public final class BlockStateData {

	private final int id;
	private final int data;
	private final Material type;

	/**
	 * @param id ID блока
	 * @param data Meta блока
	 */
	public BlockStateData(int id, int data) {
		this.id = id;
		this.data = data;
		type = Material.getMaterial(id);
	}

	/**
	 * @param id ID блока
	 */
	public BlockStateData(int id) {
		this(id, 0);
	}

	/**
	 * ID блока
	 * @return
	 */
	public int getTypeId() {
		return id;
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