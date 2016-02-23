package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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

	/**
	 * Создаёт эвент нанесения урона блоком Entity
	 * @param blockpos Координаты блока
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 * @return
	 */
	public static final EntityDamageByBlockEvent newEntityDamageByBlockEvent(ChunkPosition blockpos, net.minecraft.entity.Entity damaged, EntityDamageEvent.DamageCause cause, int damage) {
		Entity bentity = BukkitEventUtils.getBukkitEntity(damaged);
		Block block = bentity.getWorld().getBlockAt(blockpos.x, blockpos.y, blockpos.z);
		return new EntityDamageByBlockEvent(block, bentity, cause, damage);
	}

}
