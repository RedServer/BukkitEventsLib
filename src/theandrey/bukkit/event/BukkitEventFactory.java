package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Простое создание эвентов
 */
public final class BukkitEventFactory {

	/**
	 * Создаёт эвент чата, но не вызывает его. Этот эвент может быть вызван из любого потока.
	 * @param sender Отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Получатели
	 * @return
	 */
	public static final AsyncPlayerChatEvent newPlayerChatEvent(EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
		Player bsender = BukkitEventUtils.getPlayer(sender);
		Set<Player> brecipients = new HashSet<>();
		for(EntityPlayer r : recipients) brecipients.add(BukkitEventUtils.getPlayer(r));
		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bsender, message, brecipients);
	}

}
