package me.theandrey.bukkit.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
	 * @param player Отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Получатели
	 */
	public static AsyncPlayerChatEvent newPlayerChatEvent(EntityPlayerMP player, String message, Collection<EntityPlayerMP> recipients) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(message, "message");
		Objects.requireNonNull(recipients, "recipients");

		Player bukkitSender = Vanilla2Bukkit.getPlayer(player);
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
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(EntityPlayerMP player, BlockPos pos, @Nullable ItemStack stack) {
		return (PlayerBucketFillEvent)newPlayerBucketEvent(true, player, pos, BlockFace.SELF, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param face Сторона блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(EntityPlayerMP player, BlockPos pos, BlockFace face, @Nullable ItemStack stack) {
		return (PlayerBucketEmptyEvent)newPlayerBucketEvent(false, player, pos, face, stack);
	}

	/**
	 * Ивент телепортации
	 * @param from Начальная точка
	 * @param to Точка назначения
	 */
	public static PlayerTeleportEvent newPlayerTeleportEvent(EntityPlayerMP player, Vec3d from, Vec3d to, float pitch, float yaw, PlayerTeleportEvent.TeleportCause cause) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		Objects.requireNonNull(cause, "cause");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		return new PlayerTeleportEvent(bukkitPlayer,
			new Location(bukkitPlayer.getWorld(), from.x, from.y, from.z, yaw, pitch),
			new Location(bukkitPlayer.getWorld(), to.x, to.y, to.z, yaw, pitch),
			cause);
	}

	private static PlayerBucketEvent newPlayerBucketEvent(boolean isFilling, EntityPlayerMP player, BlockPos pos, BlockFace face, @Nullable ItemStack stack) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		org.bukkit.inventory.ItemStack bukkitItem = Vanilla2Bukkit.getItemStack(stack);
		Block blockClicked = bukkitPlayer.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());

		if (isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, face, bukkitItem.getType(), bukkitItem);
		}
	}

}
