package theandrey.bukkit.event;

import cpw.mods.fml.common.FMLLog;
import java.lang.reflect.Method;
import java.util.logging.Level;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class BukkitEventUtils {

	public static final String NMS_PACKAGE_VERSION;
	private static Method asBukkitCopyMethod;
	private static Method getBukkitEntityMethod;
	private static Method getBlockStateMethod;

	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		NMS_PACKAGE_VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
		try {
			Class<?> craftItem = Class.forName("org.bukkit.craftbukkit." + NMS_PACKAGE_VERSION + ".inventory.CraftItemStack");
			for(Method method : craftItem.getMethods()) {
				if(method.getName().equals("asBukkitCopy")) {
					asBukkitCopyMethod = method;
					break;
				}
			}

			Class<?> сraftBlockState = Class.forName("org.bukkit.craftbukkit." + NMS_PACKAGE_VERSION + ".block.CraftBlockState");
			getBlockStateMethod = сraftBlockState.getMethod("getBlockState", new Class[]{net.minecraft.world.World.class, int.class, int.class, int.class});
			getBukkitEntityMethod = net.minecraft.entity.Entity.class.getMethod("getBukkitEntity");
		} catch (Throwable ex) {
			throw new RuntimeException("[BukkitUtils] Произошла ошибка при инициализации методов", ex); // Крашим сервер, если эвенты настроить не удалось
		}
	}

	/**
	 * Получает Bukkit World
	 * @param world Vanilla World
	 * @return Bukkit World
	 */
	public static World getWorld(net.minecraft.world.World world) {
		return Bukkit.getWorld(world.getWorldInfo().getWorldName());
	}

	/**
	 * Получение Bukkit Block
	 * @param world Vanilla World
	 * @param x X блока
	 * @param y Y блока
	 * @param z Z блока
	 * @return Bukkit Block
	 */
	public static Block getBlock(net.minecraft.world.World world, int x, int y, int z) {
		World bworld = getWorld(world);
		if(bworld != null) return bworld.getBlockAt(x, y, z);
		return null;
	}

	/**
	 * Получение Bukkit Block по ChunkCoordinates
	 * @param world Vanilla World
	 * @param coord ChunkCoordinates
	 * @return Bukkit Block
	 */
	public static Block getBlock(net.minecraft.world.World world, ChunkCoordinates coord) {
		if(coord == null) return null;
		return getBlock(world, coord.posX, coord.posY, coord.posZ);
	}

	/**
	 * Возвращает блок механизма
	 * @param te TileEntity
	 * @return Bukkit Block
	 */
	public static Block getBlock(TileEntity te) {
		return getBlock(te.worldObj, te.xCoord, te.yCoord, te.zCoord);
	}

	/**
	 * Возвращает Bukkit Player
	 * @param player Vanilla Player
	 * @return Bukkit Player
	 */
	public static Player getPlayer(net.minecraft.entity.player.EntityPlayer player) {
		return (Player)getBukkitEntity(player);
	}

	/**
	 * Получает Bukkit Entity
	 * @param entity Vanilla Entity
	 * @return Bukkit Entity
	 */
	public static Entity getBukkitEntity(net.minecraft.entity.Entity entity) {
		if(entity != null) {
			try {
				return (Entity)getBukkitEntityMethod.invoke(entity);
			} catch (Throwable ex) {
				FMLLog.log(Level.SEVERE, ex, "[BukkitUtils] Не удалось получить Bukkit Entity.");
			}
		}
		return null;
	}

	public static BlockFace getBlockFace(int side) {
		return getBlockFace(ForgeDirection.VALID_DIRECTIONS[side]);
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
	 * @return Bukkit ItemStack
	 */
	public static ItemStack getItemStack(net.minecraft.item.ItemStack stack) {
		if(asBukkitCopyMethod != null) {
			try {
				return (ItemStack)asBukkitCopyMethod.invoke(null, stack);
			} catch (Throwable ex) {
				FMLLog.log(Level.SEVERE, ex, "[BukkitUtils] Не удалось получить Bukkit ItemStack.");
			}
		}
		return null;
	}

	/**
	 * Получает BlockState
	 * @param world
	 * @param x
	 * @return
	 */
	public static BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z) {
		try {
			return (BlockState)getBlockStateMethod.invoke(null, new Object[]{world, x, y, z});
		} catch (Exception ex) {
			FMLLog.log("BukkitUtils", Level.SEVERE, ex, "Не удалось получить BlockState.");
		}
		return null;
	}

}
