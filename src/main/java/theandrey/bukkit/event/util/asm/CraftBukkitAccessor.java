package theandrey.bukkit.event.util.asm;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Предоставляет доступ к методам CraftBukkit
 * @author TheAndrey
 */
public interface CraftBukkitAccessor {

	org.bukkit.World getBukkitWorld(World world);

	org.bukkit.entity.Entity getBukkitEntity(Entity entity);

	org.bukkit.inventory.ItemStack asCraftMirror(ItemStack original);

	BlockState getBlockState(World world, int x, int y, int z);

	boolean spawnEntityInWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason);
}
