package theandrey.bukkit.event;

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
	public static org.bukkit.Material getMaterial(Block block) {
		return org.bukkit.Material.getMaterial(Block.getIdFromBlock(block));
	}

	/**
	 * Получает Material предмета
	 * @param item Предмет
	 * @return Bukkit Material
	 */
	@SuppressWarnings("deprecation")
	public static org.bukkit.Material getMaterial(Item item) {
		return org.bukkit.Material.getMaterial(Item.getIdFromItem(item));
	}

	/**
	 * Получает Bukkit World
	 * @param world Vanilla World
	 * @return Bukkit World
	 */
	public static org.bukkit.World getWorld(World world) {
		return craftBukkitAccessor.getBukkitWorld(world);
	}

	/**
	 * Получение Bukkit Block по ChunkCoordinates
	 * @param world Vanilla World
	 * @param coord ChunkCoordinates
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(World world, ChunkCoordinates coord) {
		if(coord == null) return null;
		return getBlock(world, coord.posX, coord.posY, coord.posZ);
	}

	/**
	 * Получение Bukkit Block по ChunkPosition
	 * @param world Vanilla World
	 * @param pos ChunkPosition
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(World world, ChunkPosition pos) {
		if(pos == null) return null;
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
	public static org.bukkit.block.Block getBlock(World world, int x, int y, int z) {
		org.bukkit.World bWorld = getWorld(world);
		if(bWorld != null) return bWorld.getBlockAt(x, y, z);
		return null;
	}

	/**
	 * Возвращает блок механизма
	 * @param te TileEntity
	 * @return Bukkit Block
	 */
	public static org.bukkit.block.Block getBlock(TileEntity te) {
		return getBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
	}

	/**
	 * Возвращает Bukkit Player
	 * @param player Vanilla Player
	 * @return Bukkit Player
	 */
	public static Player getPlayer(EntityPlayer player) {
		if(player == null) return null;
		return (Player)craftBukkitAccessor.getBukkitEntity(player);
	}

	/**
	 * Получает Bukkit Entity
	 * @param entity Vanilla Entity
	 * @return Bukkit Entity
	 */
	public static org.bukkit.entity.Entity getBukkitEntity(Entity entity) {
		if(entity == null) return null;
		return craftBukkitAccessor.getBukkitEntity(entity);
	}

	/**
	 * Получить BlockFace по номеру стороны
	 * @param side Номер стороны
	 * @return BlockFace. Если определить не удалось, вернёт SELF
	 */
	public static BlockFace getBlockFace(int side) {
		switch(side) {
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
		switch(direction) {
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
	public static org.bukkit.inventory.ItemStack getItemStack(ItemStack stack) {
		if(stack == null) return null;
		return craftBukkitAccessor.asCraftMirror(stack);
	}

	/**
	 * Получить снимок блока
	 * @param world Мир
	 * @param x X координана
	 * @param y Y координана
	 * @param z Z координана
	 * @return Снимок блока
	 */
	public static BlockState getBlockState(World world, int x, int y, int z) {
		return craftBukkitAccessor.getBlockState(world, x, y, z);
	}

	/**
	 * Спавнит существо в мире с указанием причины для CreatureSpawnEvent
	 * @param world Мир
	 * @param entity Существо
	 * @param reason Причина
	 * @return Успешный спавн
	 */
	public static boolean spawnEntityInWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		if(world == null) throw new IllegalArgumentException("world is null!");
		if(entity == null) throw new IllegalArgumentException("entity is null!");

		return craftBukkitAccessor.spawnEntityInWorld(world, entity, reason);
	}

	/**
	 * Преобразует Bukkit World обратно
	 * @return Vanilla World
	 */
	public static World toVanillaWorld(org.bukkit.World world) {
		return craftBukkitAccessor.getWorldHandle(world);
	}

	/**
	 * Преобразует Bukkit Entity обратно
	 * @return Vanilla Entity
	 */
	public static Entity toVanillaEntity(org.bukkit.entity.Entity entity) {
		return craftBukkitAccessor.getEntityHandle(entity);
	}

}
