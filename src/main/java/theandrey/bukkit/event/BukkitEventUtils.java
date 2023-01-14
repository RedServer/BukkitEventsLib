package theandrey.bukkit.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
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
	@Nonnull
	public static org.bukkit.World getWorld(@Nonnull World world) {
		if (world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getBukkitWorld(world);
	}

	/**
	 * Получение Bukkit Block по ChunkCoordinates
	 * @param world Vanilla World
	 * @param coord ChunkCoordinates
	 * @return Bukkit Block
	 */
	@Nonnull
	public static org.bukkit.block.Block getBlock(@Nonnull World world, @Nonnull ChunkCoordinates coord) {
		if (coord == null) return null;
		return getBlock(world, coord.posX, coord.posY, coord.posZ);
	}

	/**
	 * Получение Bukkit Block по ChunkPosition
	 * @param world Vanilla World
	 * @param pos ChunkPosition
	 * @return Bukkit Block
	 */
	@Nonnull
	public static org.bukkit.block.Block getBlock(@Nonnull World world, @Nonnull ChunkPosition pos) {
		if (pos == null) return null;
		return getBlock(world, pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
	}

	/**
	 * Получение Bukkit Block
	 * @param world Vanilla World
	 * @param x X блока
	 * @param y Y блока
	 * @param z Z блока
	 * @return Bukkit Block
	 */
	@Nonnull
	public static org.bukkit.block.Block getBlock(@Nonnull World world, int x, int y, int z) {
		return getWorld(world).getBlockAt(x, y, z);
	}

	/**
	 * Возвращает блок механизма
	 * @return Bukkit Block
	 */
	@Nonnull
	public static org.bukkit.block.Block getBlock(@Nonnull TileEntity tile) {
		if (tile == null) throw new IllegalArgumentException("tile is null");
		return getBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
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
	@Nonnull
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

	public static BlockFace getBlockFace(ForgeDirection direction) {
		switch (direction) {
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
	@Nonnull
	public static BlockState getBlockState(@Nonnull World world, int x, int y, int z) {
		if (world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getBlockState(world, x, y, z);
	}

	/**
	 * Спавнит существо в мире с указанием причины для CreatureSpawnEvent
	 * @return Успешный спавн
	 */
	public static boolean spawnEntityInWorld(@Nonnull World world, @Nonnull Entity entity, @Nonnull CreatureSpawnEvent.SpawnReason reason) {
		if (world == null) throw new IllegalArgumentException("world is null!");
		if (entity == null) throw new IllegalArgumentException("entity is null!");
		if (reason == null) throw new IllegalArgumentException("reason is null");

		return craftBukkitAccessor.spawnEntityInWorld(world, entity, reason);
	}

	/**
	 * Преобразует Bukkit World обратно
	 * @return Vanilla World
	 */
	@Nonnull
	public static World toVanillaWorld(@Nonnull org.bukkit.World world) {
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
