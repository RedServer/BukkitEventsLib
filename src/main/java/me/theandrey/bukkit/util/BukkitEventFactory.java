package me.theandrey.bukkit.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public static AsyncPlayerChatEvent newPlayerChatEvent(EntityPlayerMP sender, String message, Collection<EntityPlayerMP> recipients) {
		if (sender == null) throw new IllegalArgumentException("sender is null");
		if (message == null) throw new IllegalArgumentException("message is null");
		if (recipients == null) throw new IllegalArgumentException("recipients is null");

		Player bukkitSender = Vanilla2Bukkit.getPlayer(sender);
		Set<Player> bukkitRecipients = recipients.stream()
			.map(Vanilla2Bukkit::getPlayer)
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(HashSet::new));

		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bukkitSender, message, bukkitRecipients);
	}

	/**
	 * Ивент выливания жидкости из ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(EntityPlayerMP player, int clickX, int clickY, int clickZ, @Nullable ItemStack stack) {
		return (PlayerBucketFillEvent)newPlayerBucketEvent(true, player, clickX, clickY, clickZ, BlockFace.SELF, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param face Сторона блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(EntityPlayerMP player, int clickX, int clickY, int clickZ, BlockFace face, @Nullable ItemStack stack) {
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
	public static PlayerTeleportEvent newPlayerTeleportEvent(EntityPlayerMP player, double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, float pitch, float yaw, PlayerTeleportEvent.TeleportCause cause) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (cause == null) throw new IllegalArgumentException("cause is null");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		return new PlayerTeleportEvent(bukkitPlayer,
			new Location(bukkitPlayer.getWorld(), xFrom, yFrom, zFrom, yaw, pitch),
			new Location(bukkitPlayer.getWorld(), xTo, yTo, zTo, yaw, pitch),
			cause);
	}

	private static PlayerBucketEvent newPlayerBucketEvent(boolean isFilling, EntityPlayerMP player, int clickX, int clickY, int clickZ, BlockFace face, @Nullable ItemStack stack) {
		if (player == null) throw new IllegalArgumentException("player is null");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		org.bukkit.inventory.ItemStack bukkitItem = Vanilla2Bukkit.getItemStack(stack);
		Block blockClicked = bukkitPlayer.getWorld().getBlockAt(clickX, clickY, clickZ);

		if (isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, face, bukkitItem.getType(), bukkitItem);
		}
	}

}
