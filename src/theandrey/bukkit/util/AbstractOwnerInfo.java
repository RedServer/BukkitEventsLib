package theandrey.bukkit.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class AbstractOwnerInfo {

	protected String playerName;

	protected AbstractOwnerInfo() {
	}

	/**
	 * Установить владельца
	 * @param name Никнейм игрока
	 */
	public final void setOwner(String name) {
		this.playerName = name;
	}

	/**
	 * Получить владельца. Может вернуть null, если владелец не установлен
	 * @return Никнейм игрока
	 */
	public final String getOwner() {
		return playerName;
	}

	/**
	 * Получить игрока-владельца. Игрок должен быть на сервере и в том же мире, где сам механизм.
	 * @return Игрок или null если не найден
	 */
	public final EntityPlayer getPlayer() {
		if(playerName != null && !playerName.isEmpty()) return getWorld().getPlayerEntityByName(playerName);
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
