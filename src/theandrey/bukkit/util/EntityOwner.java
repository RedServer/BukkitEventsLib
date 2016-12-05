package theandrey.bukkit.util;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLLog;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

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
			UUID id = UUID.fromString(nbt.getString(TAG_OWNER_ID));
			String name = nbt.getString(TAG_OWNER_NAME);
			ownerProfile = new GameProfile(id, name);
		} catch (IllegalArgumentException ex) { // если UUID невалидный
			FMLLog.log(Level.ERROR, ex, String.format("Error reading owner GameProfile (%s - %s [%.2f, %.2f, %.2f])", entity.getClass().getName(), entity.worldObj.getWorldInfo().getWorldName(), entity.posX, entity.posY, entity.posZ));
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
