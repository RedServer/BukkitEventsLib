package theandrey.bukkit.event;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
	 */
	public int getTypeId() {
		return id;
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

	/**
	 * Получает информацию о блоке из ItemStack
	 */
	public static BlockStateData fromItemStack(ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ItemBlock)) return null;
		ItemBlock item = (ItemBlock)stack.getItem();
		return new BlockStateData(stack.itemID, item.getHasSubtypes() ? item.getMetadata(stack.getItemDamage()) : 0);
	}

}
