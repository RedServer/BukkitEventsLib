package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Простое создание ивентов
 */
public final class BukkitEventFactory {

	private BukkitEventFactory() {
	}

	/**
	 * Ивент чата. Может быть вызван из любого потока.
	 * @param sender Отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Получатели
	 */
	@Nonnull
	public static AsyncPlayerChatEvent newPlayerChatEvent(@Nonnull EntityPlayer sender, @Nonnull String message, @Nonnull Collection<EntityPlayer> recipients) {
		if (sender == null) throw new IllegalArgumentException("sender is null");
		if (message == null) throw new IllegalArgumentException("message is null");
		if (recipients == null) throw new IllegalArgumentException("recipients is null");

		Player bukkitSender = BukkitEventUtils.getPlayer(sender);
		Set<Player> bukkitRecipients = recipients.stream()
				.map(BukkitEventUtils::getPlayer)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(HashSet::new));

		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bukkitSender, message, bukkitRecipients);
	}

	/**
	 * Ивент выливания жидкости из ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 */
	@Nonnull
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(@Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, @Nullable ItemStack stack) {
		return (PlayerBucketFillEvent)newPlayerBucketEvent(true, player, clickX, clickY, clickZ, BlockFace.SELF, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param face Сторона блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	@Nonnull
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(@Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, BlockFace face, @Nullable ItemStack stack) {
		return (PlayerBucketEmptyEvent)newPlayerBucketEvent(false, player, clickX, clickY, clickZ, face, stack);
	}

	/**
	 * Ивент телепортации
	 * @param xFrom Начальная точка
	 * @param yFrom Начальная точка
	 * @param zFrom Начальная точка
	 * @param xTo Точка назначения
	 * @param yTo Точка назначения
	 * @param zTo Точка назначения
	 */
	@Nonnull
	public static PlayerTeleportEvent newPlayerTeleportEvent(@Nonnull EntityPlayer player, double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, float pitch, float yaw, @Nonnull PlayerTeleportEvent.TeleportCause cause) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (cause == null) throw new IllegalArgumentException("cause is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		return new PlayerTeleportEvent(bukkitPlayer,
				new Location(bukkitPlayer.getWorld(), xFrom, yFrom, zFrom, yaw, pitch),
				new Location(bukkitPlayer.getWorld(), xTo, yTo, zTo, yaw, pitch),
				cause);
	}

	@Nonnull
	private static PlayerBucketEvent newPlayerBucketEvent(boolean isFilling, @Nonnull EntityPlayer player, int clickX, int clickY, int clickZ, BlockFace face, @Nullable ItemStack stack) {
		if (player == null) throw new IllegalArgumentException("player is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		org.bukkit.inventory.ItemStack bukkitItem = BukkitEventUtils.getItemStack(stack);
		Block blockClicked = bukkitPlayer.getWorld().getBlockAt(clickX, clickY, clickZ);

		if (isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, face, bukkitItem.getType(), bukkitItem);
		}
	}

}
