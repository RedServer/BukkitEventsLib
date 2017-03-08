package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам CraftItemStack
 * @author TheAndrey
 */
public interface CraftItemStackAccessor {

	public org.bukkit.inventory.ItemStack asCraftMirror(net.minecraft.item.ItemStack original);

}
