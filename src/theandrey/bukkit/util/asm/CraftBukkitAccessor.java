package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам CraftBukkit
 * @author TheAndrey
 */
public interface CraftBukkitAccessor {

	public org.bukkit.World getBukkitWorld(net.minecraft.world.World world);

	public org.bukkit.entity.Entity getBukkitEntity(net.minecraft.entity.Entity entity);

	public org.bukkit.inventory.ItemStack asCraftMirror(net.minecraft.item.ItemStack original);

}
