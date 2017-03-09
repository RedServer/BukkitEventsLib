package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам CraftBukkit
 * @author TheAndrey
 */
public interface CraftBukkitAccessor {

	public org.bukkit.World getBukkitWorld(net.minecraft.world.World world);

	public org.bukkit.entity.Entity getBukkitEntity(net.minecraft.entity.Entity entity);

	public org.bukkit.inventory.ItemStack asCraftMirror(net.minecraft.item.ItemStack original);

	public org.bukkit.block.BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z);

	/**
	 * Orebfuscator: Обновить ближайшие блоки
	 * @param world Мир
	 * @param x Координата
	 * @param y Координата
	 * @param z Координата
	 */
	public void ofc_updateNearbyBlocks(net.minecraft.world.World world, int x, int y, int z);

}
