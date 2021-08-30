package theandrey.bukkit.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.bukkit.Material;

/**
 * Просто хранить информацию о блоке
 */
public final class BlockStateData {

	private final int id;
	private final int meta;
	private final Material type;

	/**
	 * @param id ID блока
	 * @param meta Meta блока
	 */
	public BlockStateData(int id, int meta) {
		this.id = id;
		this.meta = meta;
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

	/**
	 * Получает информацию о блоке из ItemStack
	 */
	public static BlockStateData fromItemStack(@Nullable ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ItemBlock)) return null;
		ItemBlock item = (ItemBlock)stack.getItem();
		return new BlockStateData(stack.itemID, item.getHasSubtypes() ? item.getMetadata(stack.getItemDamage()) : 0);
	}

}
