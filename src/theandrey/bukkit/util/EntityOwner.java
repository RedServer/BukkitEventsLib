package theandrey.bukkit.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Хранит информацию о владельце Entity
 * @author Andrey
 */
public final class EntityOwner extends AbstractOwnerInfo {

	public static final String TAG_OWNER_NAME = "CustomEntityOwnerName";

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
		playerName = nbt.getString(TAG_OWNER_NAME);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		if(playerName != null) nbt.setString(TAG_OWNER_NAME, playerName);
	}

	@Override
	protected World getWorld() {
		return entity.worldObj;
	}

}
