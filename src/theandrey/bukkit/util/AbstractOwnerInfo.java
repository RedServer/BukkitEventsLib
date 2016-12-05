package theandrey.bukkit.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class AbstractOwnerInfo {

	protected GameProfile ownerProfile;

	protected AbstractOwnerInfo() {
	}

	/**
	 * Установить владельца
	 * @param profile Профиль игрока игрока
	 */
	public final void setOwner(GameProfile profile) {
		if(profile != null) {
			ownerProfile = new GameProfile(profile.getId(), profile.getName()); // Создаём новый объект, чтобы не копировать лишие свойства
		} else {
			ownerProfile = null;
		}
	}

	/**
	 * Получить владельца. Может вернуть null, если владелец не установлен
	 * @return Профиль игрока
	 */
	public final GameProfile getOwner() {
		return ownerProfile;
	}

	/**
	 * Получить игрока-владельца. Игрок должен быть на сервере и в том же мире, где сам механизм.
	 * @return Игрок или null если не найден
	 */
	public final EntityPlayer getPlayer() {
		if(ownerProfile != null) return getWorld().getPlayerEntityByName(ownerProfile.getName());
		return null;
	}

	/**
	 * Считать данные из NBT
	 * @param nbt
	 */
	public abstract void readFromNBT(NBTTagCompound nbt);

	/**
	 * Сохранить данные в NBT
	 * @param nbt
	 */
	public abstract void writeToNBT(NBTTagCompound nbt);

	protected abstract World getWorld();

}
