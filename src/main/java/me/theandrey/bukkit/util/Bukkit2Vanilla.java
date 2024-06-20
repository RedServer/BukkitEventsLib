package me.theandrey.bukkit.util;

import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Методы преобразования объектов Bukkit → Vanilla
 * @author TheAndrey
 */
public final class Bukkit2Vanilla {

	@Nullable
	private static CraftBukkitAccessor accessorInstance;

	private Bukkit2Vanilla() {
	}

	/**
	 * Возвращает {@link BlockPos} по координатам {@link org.bukkit.block.Block}
	 */
	public static BlockPos toBlockPos(org.bukkit.block.Block block) {
		return new BlockPos(block.getX(), block.getY(), block.getZ());
	}

	/**
	 * Возвращает {@link IBlockState} по координатам {@link org.bukkit.block.Block}
	 */
	public static IBlockState getBlockState(org.bukkit.block.Block block) {
		return getWorld(block.getWorld()).getBlockState(toBlockPos(block));
	}

	/**
	 * Bukkit world → Vanilla world
	 */
	public static World getWorld(org.bukkit.World world) {
		return getAccessor().getWorldHandle(Objects.requireNonNull(world, "world"));
	}

	/**
	 * Bukkit entity → Vanilla entity
	 */
	@Nullable
	public static Entity getEntity(@Nullable org.bukkit.entity.Entity entity) {
		return entity != null ? getAccessor().getEntityHandle(entity) : null;
	}

	private static CraftBukkitAccessor getAccessor() {
		if (accessorInstance == null) {
			accessorInstance = CraftBukkitAccessor.get();
		}
		return accessorInstance;
	}
}
