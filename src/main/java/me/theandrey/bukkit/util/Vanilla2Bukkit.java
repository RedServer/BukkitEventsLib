package me.theandrey.bukkit.util;

import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Методы преобразования объектов Vanilla → Bukkit
 * @author TheAndrey
 */
public final class Vanilla2Bukkit {

	@Nullable
	private static CraftBukkitAccessor accessorInstance;

	private Vanilla2Bukkit() {
	}

	/**
	 * Получает Material блока
	 */
	@Nullable
	@SuppressWarnings("deprecation")
	public static Material getMaterial(@Nullable Block block) {
		return Material.getMaterial(Block.getIdFromBlock(block));
	}

	/**
	 * Получает Material предмета
	 */
	@Nullable
	@SuppressWarnings("deprecation")
	public static Material getMaterial(@Nullable Item item) {
		return Material.getMaterial(Item.getIdFromItem(item));
	}

	public static BlockFace getBlockFace(EnumFacing facing) {
		switch (facing) {
			case UP:
				return BlockFace.UP;
			case DOWN:
				return BlockFace.DOWN;
			case WEST:
				return BlockFace.WEST;
			case EAST:
				return BlockFace.EAST;
			case NORTH:
				return BlockFace.NORTH;
			case SOUTH:
				return BlockFace.SOUTH;
		}
		return BlockFace.SELF;
	}

	/**
	 * Получить BlockFace по номеру стороны
	 * @param side Номер стороны
	 * @return BlockFace. Если определить не удалось, вернёт SELF
	 */
	public static BlockFace getBlockFace(int side) {
		switch (side) {
			case 0:
				return BlockFace.DOWN;
			case 1:
				return BlockFace.UP;
			case 2:
				return BlockFace.NORTH;
			case 3:
				return BlockFace.SOUTH;
			case 4:
				return BlockFace.WEST;
			case 5:
				return BlockFace.EAST;
		}
		return BlockFace.SELF;
	}

	/**
	 * Vanilla world → Bukkit world
	 */
	public static org.bukkit.World getWorld(World world) {
		return getAccessor().getBukkitWorld(Objects.requireNonNull(world, "world"));
	}

	/**
	 * Получить блок по данным {@link BlockPos}
	 */
	public static org.bukkit.block.Block getBlock(World world, BlockPos pos) {
		return getBlock(world, pos.getX(), pos.getY(), pos.getZ());
	}

	public static org.bukkit.block.Block getBlock(World world, int x, int y, int z) {
		return getWorld(world).getBlockAt(x, y, z);
	}

	/**
	 * Получить блок по координатам {@link TileEntity}
	 */
	public static org.bukkit.block.Block getBlock(TileEntity tile) {
		return getBlock(tile.getWorld(), tile.getPos());
	}

	/**
	 * Возвращает Bukkit Player. Возвращает null на null.
	 */
	@Nullable
	public static Player getPlayer(@Nullable EntityPlayerMP player) {
		return player != null ? (Player)getAccessor().getBukkitEntity(player) : null;
	}

	/**
	 * Получает Bukkit Entity. Возвращает null на null.
	 */
	@Nullable
	public static org.bukkit.entity.Entity getBukkitEntity(@Nullable Entity entity) {
		return entity != null ? getAccessor().getBukkitEntity(entity) : null;
	}

	/**
	 * Возвращает ассоциированный Bukkit ItemStack. Вернёт null, если в параметре был передан null
	 */
	@Nullable
	public static org.bukkit.inventory.ItemStack getItemStack(@Nullable ItemStack stack) {
		return stack != null ? getAccessor().asCraftMirror(stack) : null;
	}

	/**
	 * Получить снимок блока
	 */
	public static org.bukkit.block.BlockState getBukkitBlockState(World world, BlockPos pos) {
		Objects.requireNonNull(world, "world");
		Objects.requireNonNull(pos, "pos");
		return getAccessor().getBlockState(world, pos.getX(), pos.getY(), pos.getZ());
	}

	private static CraftBukkitAccessor getAccessor() {
		if (accessorInstance == null) {
			accessorInstance = CraftBukkitAccessor.get();
		}
		return accessorInstance;
	}
}
