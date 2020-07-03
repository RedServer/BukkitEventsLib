package theandrey.bukkit.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.ForgeDirection;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.event.util.asm.CraftBukkitAccessor;

public final class BukkitEventUtils {

	static CraftBukkitAccessor craftBukkitAccessor;

	private BukkitEventUtils() {
	}

	/**
	 * Получает Bukkit World
	 * @param world Vanilla World
	 * @return Bukkit World
	 */
	public static World getWorld(net.minecraft.world.World world) {
		if(world == null) return null;
		return craftBukkitAccessor.getBukkitWorld(world);
	}

	/**
	 * Получение Bukkit Block
	 * @param world Vanilla World
	 * @return Bukkit Block
	 */
	public static Block getBlock(net.minecraft.world.World world, int x, int y, int z) {
		World bukkitWorld = getWorld(world);
		if(bukkitWorld != null) return bukkitWorld.getBlockAt(x, y, z);
		return null;
	}

	/**
	 * Получение Bukkit Block по ChunkCoordinates
	 * @param world Vanilla World
	 * @param cord ChunkCoordinates
	 * @return Bukkit Block
	 */
	public static Block getBlock(net.minecraft.world.World world, ChunkCoordinates cord) {
		if(cord == null) throw new IllegalArgumentException("coord is null");
		return getBlock(world, cord.posX, cord.posY, cord.posZ);
	}

	/**
	 * Получение Bukkit Block по ChunkPosition
	 * @param world Vanilla World
	 * @param pos ChunkPosition
	 * @return Bukkit Block
	 */
	public static Block getBlock(net.minecraft.world.World world, ChunkPosition pos) {
		if(pos == null) throw new IllegalArgumentException("pos is null");
		return getBlock(world, pos.x, pos.y, pos.z);
	}

	/**
	 * Возвращает блок механизма
	 * @param tile TileEntity
	 * @return Bukkit Block
	 */
	public static Block getBlock(TileEntity tile) {
		if(tile == null) throw new IllegalArgumentException("tile is null");
		return getBlock(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
	}

	/**
	 * Возвращает Bukkit Player
	 * @param player Vanilla Player
	 * @return Bukkit Player
	 */
	public static Player getPlayer(net.minecraft.entity.player.EntityPlayer player) {
		if(player == null) throw new IllegalArgumentException("player is null");
		return (Player)getBukkitEntity(player);
	}

	/**
	 * Получает Bukkit Entity
	 * @param entity Vanilla Entity
	 * @return Bukkit Entity
	 */
	public static Entity getBukkitEntity(net.minecraft.entity.Entity entity) {
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
	 * Конвертирует Vanilla ItemStack в Bukkit ItemStack (используется asCraftMirror)
	 * @param stack Vanilla ItemStack
	 * @return Bukkit ItemStack
	 */
	public static ItemStack getItemStack(net.minecraft.item.ItemStack stack) {
		if(stack == null) return null;
		return craftBukkitAccessor.asCraftMirror(stack);
	}

	/**
	 * Получает BlockState
	 * @return BlockState
	 */
	public static BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z) {
		if(world == null) throw new IllegalArgumentException("world is null");
		return craftBukkitAccessor.getBlockState(world, x, y, z);
	}

	/**
	 * Спавнит существо в мире с указанием причины для CreatureSpawnEvent
	 * @param world Мир
	 * @param entity Существо
	 * @param reason Причина
	 * @return Успешный спавн
	 */
	public static boolean spawnEntityInWorld(net.minecraft.world.World world, net.minecraft.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		if(world == null) throw new IllegalArgumentException("world is null!");
		if(entity == null) throw new IllegalArgumentException("entity is null!");

		return craftBukkitAccessor.spawnEntityInWorld(world, entity, reason);
	}

}
