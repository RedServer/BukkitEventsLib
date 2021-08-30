package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

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
	@Nonnull
	public static AsyncPlayerChatEvent newPlayerChatEvent(@Nonnull EntityPlayer sender, @Nonnull String message, @Nonnull Collection<EntityPlayer> recipients) {
		if(sender == null) throw new IllegalArgumentException("sender is null");
		if(message == null) throw new IllegalArgumentException("message is null");
		if(recipients == null) throw new IllegalArgumentException("recipients is null");

		Player bukkitSender = BukkitEventUtils.getPlayer(sender);
		Set<Player> bukkitRecipients = recipients.stream()
				.map(BukkitEventUtils::getPlayer)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(HashSet::new));

		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bukkitSender, message, bukkitRecipients);
	}

	/**
	 * Ивент нанесения урона блоком Entity
	 * @param position Координаты блока
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	@Nonnull
	public static EntityDamageByBlockEvent newEntityDamageByBlockEvent(@Nonnull ChunkPosition position, @Nonnull Entity damaged, @Nonnull EntityDamageEvent.DamageCause cause, int damage) {
		if(position == null) throw new IllegalArgumentException("position is null");
		if(damaged == null) throw new IllegalArgumentException("damaged is null");
		if(cause == null) throw new IllegalArgumentException("cause is null");
		if(damage < 0) throw new IllegalArgumentException("Invalid damage: " + damage);

		org.bukkit.entity.Entity bukkitEntity = BukkitEventUtils.getBukkitEntity(damaged);
		org.bukkit.block.Block block = bukkitEntity.getWorld().getBlockAt(position.x, position.y, position.z);
		return new EntityDamageByBlockEvent(block, bukkitEntity, cause, damage);
	}

	/**
	 * Ивент нанесения урона одним Entity другому Entity
	 * @param attacker Кто атаковал
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	@Nonnull
	public static EntityDamageByEntityEvent newEntityDamageByEntityEvent(@Nonnull Entity attacker, @Nonnull Entity damaged, @Nonnull EntityDamageEvent.DamageCause cause, int damage) {
		if(attacker == null) throw new IllegalArgumentException("attacker is null");
		if(damaged == null) throw new IllegalArgumentException("damaged is null");
		if(cause == null) throw new IllegalArgumentException("cause is null");
		if(damage < 0) throw new IllegalArgumentException("Invalid damage: " + damage);

		return new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(damaged), cause, damage);
	}

	/**
	 * Ивент получения урона Entity
	 * @param damaged Entity получивший урон
	 * @param cause Причина
	 * @param damage Урон
	 */
	@Nonnull
	public static EntityDamageEvent newEntityDamageEvent(@Nonnull Entity damaged, @Nonnull EntityDamageEvent.DamageCause cause, int damage) {
		if(damaged == null) throw new IllegalArgumentException("damaged is null");
		if(cause == null) throw new IllegalArgumentException("cause is null");
		if(damage < 0) throw new IllegalArgumentException("Invalid damage: " + damage);

		return new EntityDamageEvent(BukkitEventUtils.getBukkitEntity(damaged), cause, damage);
	}

	/**
	 * Ивент выливания жидкости из ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 */
	@Nonnull
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(@Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, @Nullable ItemStack stack) {
		return (PlayerBucketFillEvent)getPlayerBucketEvent(true, player, clickX, clickY, clickZ, -1, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param clickSide Индекс стороны блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	@Nonnull
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(@Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, int clickSide, @Nullable ItemStack stack) {
		return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, player, clickX, clickY, clickZ, clickSide, stack);
	}

	@Nonnull
	private static PlayerBucketEvent getPlayerBucketEvent(boolean isFilling, @Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, int side, @Nullable ItemStack stack) {
		if(player == null) throw new IllegalArgumentException("player is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		org.bukkit.inventory.ItemStack bukkitItem = BukkitEventUtils.getItemStack(stack);
		org.bukkit.block.Block blockClicked = bukkitPlayer.getWorld().getBlockAt(clickX, clickY, clickZ);
		if(isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, org.bukkit.block.BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, BukkitEventUtils.getBlockFace(side), bukkitItem.getType(), bukkitItem);
		}
	}

}
