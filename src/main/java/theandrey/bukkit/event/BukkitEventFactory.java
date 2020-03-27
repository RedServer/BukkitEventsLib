package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
	public static AsyncPlayerChatEvent newPlayerChatEvent(EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
		Player bukkitSender = BukkitEventUtils.getPlayer(sender);
		Set<Player> bukkitRecipients = new HashSet<>();
		for(EntityPlayer player : recipients) bukkitRecipients.add(BukkitEventUtils.getPlayer(player));
		return new AsyncPlayerChatEvent(!Bukkit.isPrimaryThread(), bukkitSender, message, bukkitRecipients);
	}

	/**
	 * Ивент выливания жидкости из ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketFillEvent newPlayerBucketFillEvent(EntityPlayer player, int clickX, int clickY, int clickZ, ItemStack stack) {
		return (PlayerBucketFillEvent)getPlayerBucketEvent(true, player, clickX, clickY, clickZ, -1, stack);
	}

	/**
	 * Ивент наполнения ведра
	 * @param player Игрок
	 * @param clickSide Индекс стороны блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 */
	public static PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(EntityPlayer player, int clickX, int clickY, int clickZ, int clickSide, ItemStack stack) {
		return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, player, clickX, clickY, clickZ, clickSide, stack);
	}

	/**
	 * Ивент телепортации
	 * @param player Игрок
	 * @param xFrom Начальная точка
	 * @param yFrom Начальная точка
	 * @param zFrom Начальная точка
	 * @param xTo Точка назначения
	 * @param yTo Точка назначения
	 * @param zTo Точка назначения
	 */
	public static PlayerTeleportEvent newPlayerTeleportEvent(EntityPlayer player, double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, float pitch, float yaw, PlayerTeleportEvent.TeleportCause cause) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		return new PlayerTeleportEvent(
				bukkitPlayer,
				new Location(bukkitPlayer.getWorld(), xFrom, yFrom, zFrom, yaw, pitch),
				new Location(bukkitPlayer.getWorld(), xTo, yTo, zTo, yaw, pitch),
				cause
		);
	}

	private static PlayerBucketEvent getPlayerBucketEvent(boolean isFilling, EntityPlayer player, int clickX, int clickY, int clickZ, int side, ItemStack stack) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		org.bukkit.inventory.ItemStack bukkitItem = BukkitEventUtils.getItemStack(stack);
		Block blockClicked = bukkitPlayer.getWorld().getBlockAt(clickX, clickY, clickZ);

		if(isFilling) {
			return new PlayerBucketFillEvent(bukkitPlayer, blockClicked, BlockFace.SELF, bukkitItem.getType(), bukkitItem);
		} else {
			return new PlayerBucketEmptyEvent(bukkitPlayer, blockClicked, BukkitEventUtils.getBlockFace(side), bukkitItem.getType(), bukkitItem);
		}
	}

}
