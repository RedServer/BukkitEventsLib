package theandrey.bukkit.util.asm;

/**
 * Предоставляет доступ к методам Entity
 * @author TheAndrey
 */
public interface EntityAccessor {

	public org.bukkit.entity.Entity getBukkitEntity(net.minecraft.entity.Entity entity);

}
