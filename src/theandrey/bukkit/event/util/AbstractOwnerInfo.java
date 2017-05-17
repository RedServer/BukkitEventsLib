package theandrey.bukkit.event.util;

import com.mojang.authlib.GameProfile;
import java.lang.ref.WeakReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class AbstractOwnerInfo {

	protected GameProfile ownerProfile;
	protected WeakReference<EntityPlayer> playerEntity;

	protected AbstractOwnerInfo() {
	}

	/**
	 * Установить владельца
	 * @param profile Профиль игрока игрока
	 */
	public final void setProfile(GameProfile profile) {
		ownerProfile = profile;
		if(playerEntity != null) playerEntity.clear();
	}

	/**
	 * Получить владельца. Может вернуть null, если владелец не установлен
	 * @return Профиль игрока
	 */
	public final GameProfile getProfile() {
		return ownerProfile;
	}

	/**
	 * Получить игрока-владельца. Игрок должен быть на сервере и в том же мире, где сам механизм.
	 * @return Игрок или null если не найден
	 */
	public final EntityPlayer getPlayer() {
		EntityPlayer player = (playerEntity != null) ? playerEntity.get() : null;
		if(player == null && ownerProfile != null) {
			player = getWorld().getPlayerEntityByName(ownerProfile.getName());
			if(player != null) playerEntity = new WeakReference<>(player);
		}
		return player;
	}

	/**
	 * @see AbstractOwnerInfo#setProfile(com.mojang.authlib.GameProfile)
	 */
	@Deprecated
	public final void setOwner(GameProfile profile) {
		setProfile(profile);
	}

	/**
	 * @see AbstractOwnerInfo#getProfile()
	 */
	@Deprecated
	public final GameProfile getOwner() {
		return getProfile();
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
