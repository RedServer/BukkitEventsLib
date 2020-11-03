package theandrey.bukkit.event.helper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import theandrey.bukkit.event.BukkitEventUtils;

public final class WorldHelper {

	private WorldHelper() {
	}

	/**
	 * Проверяем возможность "легального" перемещения блока, примерно как поршни
	 * @return true если позволяет mobility flag блока и не имеет TileEntity
	 */
	@SuppressWarnings("deprecation")
	public static boolean isBlockMovable(org.bukkit.block.Block block) {
		if(block == null) throw new IllegalArgumentException("block is null");

		Block theBlock = Block.getBlockById(block.getTypeId());
		if(theBlock.getMobilityFlag() == 2) return false;

		TileEntity tileentity = BukkitEventUtils.toVanillaWorld(block.getWorld()).getTileEntity(block.getX(), block.getY(), block.getZ());
		return tileentity == null;
	}
}
