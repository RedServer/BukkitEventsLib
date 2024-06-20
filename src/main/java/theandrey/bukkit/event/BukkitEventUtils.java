package theandrey.bukkit.event;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import theandrey.bukkit.event.util.asm.CraftBukkitAccessor;

public final class BukkitEventUtils {

	static CraftBukkitAccessor craftBukkitAccessor;

	private BukkitEventUtils() {
	}

	/**
	 * Получает Material блока
	 * @param block Блок
	 * @return Bukkit Material
	 */
	@SuppressWarnings("deprecation")
	@Nullable
	public static org.bukkit.Material getMaterial(@Nullable Block block) {
		return org.bukkit.Material.getMaterial(Block.getIdFromBlock(block));
	}

	/**
	 * Получает Material предмета
	 * @param item Предмет
	 * @return Bukkit Material
	 */
	@SuppressWarnings("deprecation")
	@Nullable
	public static org.bukkit.Material getMaterial(@Nullable Item item) {
		return org.bukkit.Material.getMaterial(Item.getIdFromItem(item));
	}

	/**
	 * Получает Bukkit World
	 * @param world Vanilla World
	 * @return Bukkit World
	 */
	public static org.bukkit.World getWorld(World world) {
		if (world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getBukkitWorld(world);
	}

	/**
	 * Получение Bukkit Block по ChunkPosition
	 * @param world Vanilla World
	 * @param pos ChunkPosition
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(World world, BlockPos pos) {
		return getBlock(world, pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Получение Bukkit Block
	 * @param world Vanilla World
	 * @param x X блока
	 * @param y Y блока
	 * @param z Z блока
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(World world, int x, int y, int z) {
		return getWorld(world).getBlockAt(x, y, z);
	}

	/**
	 * Возвращает блок механизма
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(TileEntity tile) {
		return getBlock(tile.getWorld(), tile.getPos());
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
		return toVanillaWorld(block.getWorld()).getBlockState(toBlockPos(block));
	}

	/**
	 * Возвращает Bukkit Player. Возвращает null на null.
	 * @param player Vanilla Player
	 * @return Bukkit Player
	 */
	@Nullable
	public static Player getPlayer(@Nullable EntityPlayer player) {
		if (player == null) return null;
		return (Player)craftBukkitAccessor.getBukkitEntity(player);
	}

	/**
	 * Получает Bukkit Entity. Возвращает null на null.
	 * @param entity Vanilla Entity
	 * @return Bukkit Entity
	 */
	@Nullable
	public static org.bukkit.entity.Entity getBukkitEntity(@Nullable Entity entity) {
		if (entity == null) return null;
		return craftBukkitAccessor.getBukkitEntity(entity);
	}

	/**
	 * Получить BlockFace по номеру стороны
	 * @param side Номер стороны
	 * @return BlockFace. Если определить не удалось, вернёт SELF
	 */
	@Deprecated
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
	 * Конвертирует Vanilla ItemStack в Bukkit ItemStack
	 * @param stack Vanilla ItemStack
	 * @return Bukkit ItemStack. Вернёт null, если в параметре был передан null
	 */
	@Nullable
	public static org.bukkit.inventory.ItemStack getItemStack(@Nullable ItemStack stack) {
		if (stack == null) return null;
		return craftBukkitAccessor.asCraftMirror(stack);
	}

	/**
	 * Получить снимок блока
	 */
	public static org.bukkit.block.BlockState getBukkitBlockState(World world, int x, int y, int z) {
		if (world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getBlockState(world, x, y, z);
	}

	/**
	 * Спавнит существо в мире с указанием причины для CreatureSpawnEvent
	 * @return Успешный спавн
	 */
	public static boolean spawnEntityInWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		if (world == null) throw new IllegalArgumentException("world is null!");
		if (entity == null) throw new IllegalArgumentException("entity is null!");
		if (reason == null) throw new IllegalArgumentException("reason is null");

		return craftBukkitAccessor.spawnEntityInWorld(world, entity, reason);
	}

	/**
	 * Преобразует Bukkit World обратно
	 * @return Vanilla World
	 */
	public static World toVanillaWorld(org.bukkit.World world) {
		if (world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getWorldHandle(world);
	}

	/**
	 * Преобразует Bukkit Entity обратно
	 * @return Vanilla Entity
	 */
	@Nullable
	public static Entity toVanillaEntity(@Nullable org.bukkit.entity.Entity entity) {
		if (entity == null) return null;
		return craftBukkitAccessor.getEntityHandle(entity);
	}

	/**
	 * Задаёт сущность, которая будет использоваться в контексте
	 * {@link EntityDamageByEntityEvent}
	 */
	public static void setEntityDamage(@Nullable Entity entity) {
		craftBukkitAccessor.setEntityDamage(entity);
	}

	/**
	 * Задаёт блок, который будет использоваться в контексте
	 * {@link EntityDamageByBlockEvent}
	 */
	public static void setBlockDamage(@Nullable org.bukkit.block.Block block) {
		craftBukkitAccessor.setBlockDamage(block);
	}

}
