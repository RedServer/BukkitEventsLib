package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам World
 * @author TheAndrey
 */
public interface WorldAccessor {

	public org.bukkit.World getBukkitWorld(net.minecraft.world.World world);

}
