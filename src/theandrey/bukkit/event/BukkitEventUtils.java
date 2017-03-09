package theandrey.bukkit.event;

import cpw.mods.fml.common.FMLLog;
import java.lang.reflect.Method;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.util.asm.ASMAccessor;
import theandrey.bukkit.util.asm.CraftBukkitAccessor;

public final class BukkitEventUtils {

	private static final Method getBlockStateMethod;
	private static final CraftBukkitAccessor craftBukkitAccessor = ASMAccessor.instance().createAccessor();

	static {
		try {
			String packageName = Bukkit.getServer().getClass().getPackage().getName();
			String nmsPackageVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
			Class<?> сraftBlockState = Class.forName("org.bukkit.craftbukkit." + nmsPackageVersion + ".block.CraftBlockState");
			getBlockStateMethod = сraftBlockState.getMethod("getBlockState", new Class[]{net.minecraft.world.World.class, int.class, int.class, int.class});
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("[BukkitEventUtils] Произошла ошибка при инициализации методов", ex); // Крашим сервер, если эвенты настроить не удалось
		}
	}

	private BukkitEventUtils() {
	}

	/**
	 * Получает Material блока
	 * @param block Блок
	 * @return Bukkit Material
	 */
	@SuppressWarnings("deprecation")
	public static Material getMaterial(net.minecraft.block.Block block) {
		return Material.getMaterial(net.minecraft.block.Block.getIdFromBlock(block));
	}

	/**
	 * Получает Material предмета
	 * @param item Предмет
	 * @return Bukkit Material
	 */
	@SuppressWarnings("deprecation")
	public static Material getMaterial(net.minecraft.item.Item item) {
		return Material.getMaterial(net.minecraft.item.Item.getIdFromItem(item));
	}

	/**
	 * Получает Bukkit World
	 * @param world Vanilla World
	 * @return Bukkit World
	 */
	public static World getWorld(net.minecraft.world.World world) {
		return craftBukkitAccessor.getBukkitWorld(world);
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
	 * Возвращает блок механизма
	 * @param te TileEntity
	 * @return Bukkit Block
	 */
	public static Block getBlock(TileEntity te) {
		return getBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
	}

	/**
	 * Возвращает Bukkit Player
	 * @param player Vanilla Player
	 * @return Bukkit Player
	 */
	public static Player getPlayer(net.minecraft.entity.player.EntityPlayer player) {
		if(player == null) return null;
		return (Player)craftBukkitAccessor.getBukkitEntity(player);
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
	 * Конвертирует Vanilla ItemStack в Bukkit ItemStack
	 * @param stack Vanilla ItemStack
	 * @return Bukkit ItemStack. Вернёт null, если в параметре был передан null
	 */
	public static ItemStack getItemStack(net.minecraft.item.ItemStack stack) {
		if(stack == null) return null;
		return craftBukkitAccessor.asCraftMirror(stack);
	}

	/**
	 * Получить BlockState
	 * @param world Мир
	 * @param x X координана
	 * @param y Y координана
	 * @param z Z координана
	 * @return Снимок блока
	 */
	public static BlockState getBlockState(net.minecraft.world.World world, int x, int y, int z) {
		try {
			return (BlockState)getBlockStateMethod.invoke(null, new Object[]{world, x, y, z});
		} catch (ReflectiveOperationException ex) {
			FMLLog.log("BukkitUtils", Level.ERROR, ex, "Не удалось получить BlockState.");
		}
		return null;
	}

}
