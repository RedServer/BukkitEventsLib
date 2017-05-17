package theandrey.bukkit.event.api;

import theandrey.bukkit.event.util.BukkitGameProfile;

/**
 * Реализуется ивентами, которые содержат информацию о владельце механизма, моба и др.
 */
public interface IHasOwnerInfo {

	/**
	 * Получить блок механизма
	 * @return
	 */
	public BukkitGameProfile getOwnerProfile();

}
