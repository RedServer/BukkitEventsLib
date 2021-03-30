package theandrey.bukkit.event.util.asm;

import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Предоставляет доступ к методам CraftBukkit
 * @author TheAndrey
 */
public interface CraftBukkitAccessor {

	org.bukkit.World getBukkitWorld(net.minecraft.world.World world);

	org.bukkit.entity.Entity getBukkitEntity(net.minecraft.entity.Entity entity);

	org.bukkit.inventory.ItemStack asCraftMirror(net.minecraft.item.ItemStack original);

	org.bukkit.block.BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z);

	boolean spawnEntityInWorld(net.minecraft.world.World world, net.minecraft.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

	/**
	 * Bukkit -> Vanilla World
	 */
	net.minecraft.world.World getWorldHandle(org.bukkit.World world);

	/**
	 * Bukkit -> Vanilla Entity
	 */
	net.minecraft.entity.Entity getEntityHandle(org.bukkit.entity.Entity entity);
}
