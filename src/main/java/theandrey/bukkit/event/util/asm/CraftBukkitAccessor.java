package theandrey.bukkit.event.util.asm;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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

	/**
	 * Bukkit -> Vanilla World
	 */
	World getWorldHandle(org.bukkit.World world);

	/**
	 * Bukkit -> Vanilla Entity
	 */
	Entity getEntityHandle(org.bukkit.entity.Entity entity);

	/**
	 * Задаёт сущность, которая будет использоваться в контексте
	 * {@link EntityDamageByEntityEvent}
	 */
	void setEntityDamage(@Nullable Entity entity);

	/**
	 * Задаёт блок, который будет использоваться в контексте
	 * {@link EntityDamageByBlockEvent}
	 */
	void setBlockDamage(@Nullable org.bukkit.block.Block block);
}
