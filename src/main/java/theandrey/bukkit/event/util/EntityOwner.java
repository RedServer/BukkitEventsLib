package theandrey.bukkit.event.util;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import theandrey.bukkit.event.ModMain;

/**
 * Хранит информацию о владельце Entity
 * @author Andrey
 */
public final class EntityOwner extends AbstractOwnerInfo {

	public static final String TAG_OWNER_NAME = "CustomEntityOwnerName";
	public static final String TAG_OWNER_ID = "CustomEntityOwnerId";

	private final Entity entity;

	/**
	 * Конструктор
	 * @param entity Entity, в котором будет будет хранится информация
	 */
	public EntityOwner(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		try {
			String idstr = nbt.getString(TAG_OWNER_ID);
			if(idstr == null || idstr.isEmpty()) return;
			UUID id = UUID.fromString(idstr);
			String name = nbt.getString(TAG_OWNER_NAME);
			ownerProfile = new GameProfile(id, name);
		} catch (IllegalArgumentException ex) { // если UUID невалидный
			String worldName = (entity.worldObj != null) ? entity.worldObj.getWorldInfo().getWorldName() : "**null**";
			ModMain.logger.log(Level.ERROR, String.format("Error reading owner GameProfile (%s - %s [%.2f, %.2f, %.2f])", entity.getClass().getName(), worldName, entity.posX, entity.posY, entity.posZ), ex);
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
		return entity.worldObj;
	}

}
