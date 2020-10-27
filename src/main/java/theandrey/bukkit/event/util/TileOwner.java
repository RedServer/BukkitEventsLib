package theandrey.bukkit.event.util;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import theandrey.bukkit.event.ModMain;

/**
 * Используется для хранения информации о владельце TileEntity
 * @author Andrey
 */
public final class TileOwner extends AbstractOwnerInfo {

	public static final String TAG_OWNER_NAME = "CustomOwnerName";
	public static final String TAG_OWNER_ID = "CustomOwnerId";

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
		try {
			String idStr = nbt.getString(TAG_OWNER_ID);
			if(idStr == null || idStr.isEmpty()) return;
			UUID id = UUID.fromString(idStr);
			String name = nbt.getString(TAG_OWNER_NAME);
			ownerProfile = new GameProfile(id, name);
		} catch (IllegalArgumentException ex) { // если UUID невалидный
			String worldName = (tile.getWorldObj() != null) ? tile.getWorldObj().getWorldInfo().getWorldName() : "**null**";
			ModMain.logger.log(Level.ERROR, String.format("Error reading owner GameProfile (%s - %s [%d, %d, %d])", tile.getClass().getName(), worldName, tile.xCoord, tile.yCoord, tile.zCoord), ex);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		if(ownerProfile != null) {
			nbt.setString(TAG_OWNER_ID, ownerProfile.getId().toString());
			nbt.setString(TAG_OWNER_NAME, ownerProfile.getName());
		}
	}

	@Override
	protected World getWorld() {
		return tile.getWorldObj();
	}

}
