package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Простое создание ивентов
 */
public final class BukkitEventFactory {

	/**
	 * Ивент чата. Может быть вызван из любого потока.
	 * @param sender Отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Получатели
	 */
	public static AsyncPlayerChatEvent newPlayerChatEvent(EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
		Player bukkitSender = BukkitEventUtils.getPlayer(sender);
		Set<Player> bukkitRecipients = new HashSet<>();
		for(EntityPlayer r : recipients) bukkitRecipients.add(BukkitEventUtils.getPlayer(r));
		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bukkitSender, message, bukkitRecipients);
	}

	/**
	 * Ивент нанесения урона блоком Entity
	 * @param pos Координаты блока
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	public static EntityDamageByBlockEvent newEntityDamageByBlockEvent(ChunkPosition pos, net.minecraft.entity.Entity damaged, EntityDamageEvent.DamageCause cause, int damage) {
		Entity bukkitEntity = BukkitEventUtils.getBukkitEntity(damaged);
		Block block = bukkitEntity.getWorld().getBlockAt(pos.x, pos.y, pos.z);
		return new EntityDamageByBlockEvent(block, bukkitEntity, cause, damage);
	}

	/**
	 * Ивент нанесения урона одним Entity другому Entity
	 * @param attacker Кто атаковал
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	public static EntityDamageByEntityEvent newEntityDamageByEntityEvent(net.minecraft.entity.Entity attacker, net.minecraft.entity.Entity damaged, EntityDamageEvent.DamageCause cause, int damage) {
		Entity bukkitAttacker = BukkitEventUtils.getBukkitEntity(attacker);
		Entity bukkitEntity = BukkitEventUtils.getBukkitEntity(damaged);
		return new EntityDamageByEntityEvent(bukkitAttacker, bukkitEntity, cause, damage);
	}

	/**
	 * Ивент получения урона Entity
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	public static EntityDamageEvent newEntityDamageEvent(net.minecraft.entity.Entity damaged, EntityDamageEvent.DamageCause cause, int damage) {
		Entity bukkitEntity = BukkitEventUtils.getBukkitEntity(damaged);
		return new EntityDamageEvent(bukkitEntity, cause, damage);
	}

	/**
	 * Ивент выливания жидкости из ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(EntityPlayer player, int clickX, int clickY, int clickZ, net.minecraft.item.ItemStack stack) {
		return (PlayerBucketFillEvent)getPlayerBucketEvent(true, player, clickX, clickY, clickZ, -1, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param clickSide Индекс стороны блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(EntityPlayer player, int clickX, int clickY, int clickZ, int clickSide, net.minecraft.item.ItemStack stack) {
		return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, player, clickX, clickY, clickZ, clickSide, stack);
	}

	private static PlayerBucketEvent getPlayerBucketEvent(boolean isFilling, EntityPlayer player, int clickX, int clickY, int clickZ, int side, net.minecraft.item.ItemStack stack) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		ItemStack bukkitItem = BukkitEventUtils.getItemStack(stack);
		Block blockClicked = bukkitPlayer.getWorld().getBlockAt(clickX, clickY, clickZ);
		if(isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, BukkitEventUtils.getBlockFace(side), bukkitItem.getType(), bukkitItem);
		}
	}

}
