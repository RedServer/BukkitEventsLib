package theandrey.bukkit.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Используется для хранения информации о владельце TileEntity
 * @author Andrey
 */
public final class TileOwner extends AbstractOwnerInfo {

	public static final String TAG_OWNER_NAME = "CustomOwnerName";

	private final TileEntity tile;

	/**
	 * Конструктор
	 * @param tile TileEntity, в котором будет будет хранится информация
	 */
	public TileOwner(TileEntity tile) {
		this.tile = tile;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		playerName = nbt.getString(TAG_OWNER_NAME);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		if(playerName != null) nbt.setString(TAG_OWNER_NAME, playerName);
	}

	@Override
	protected World getWorld() {
		return tile.worldObj;
	}

}
