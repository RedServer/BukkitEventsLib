package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам CraftBukkit
 * @author TheAndrey
 */
public interface CraftBukkitAccessor {

	org.bukkit.World getBukkitWorld(net.minecraft.world.World world);

	org.bukkit.entity.Entity getBukkitEntity(net.minecraft.entity.Entity entity);

	org.bukkit.inventory.ItemStack asCraftMirror(net.minecraft.item.ItemStack original);

	org.bukkit.block.BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z);

}
