package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

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
	 * Эвент выливания жидкости из ведра
	 * @param player Игрок
	 * @param clickX
	 * @param clickY
	 * @param clickZ
	 * @param stack Предмет в руке (ведро)
	 * @return Эвент
	 */
	public static final PlayerBucketFillEvent newPlayerBucketFillEvent(EntityPlayer player, int clickX, int clickY, int clickZ, ItemStack stack) {
		return (PlayerBucketFillEvent)getPlayerBucketEvent(true, player, clickX, clickY, clickZ, -1, stack);
	}

	/**
	 * Эвент наполнения ведра
	 * @param player Игрок
	 * @param clickX
	 * @param clickY
	 * @param clickZ
	 * @param clickSide Индекс стороны блока по которой был сделан клик
	 * @param stack Предмет в руке (ведро)
	 * @return Эвент
	 */
	public static final PlayerBucketEmptyEvent newPlayerBucketEmptyEvent(EntityPlayer player, int clickX, int clickY, int clickZ, int clickSide, ItemStack stack) {
		return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, player, clickX, clickY, clickZ, clickSide, stack);
	}

	private static PlayerBucketEvent getPlayerBucketEvent(boolean isFilling, EntityPlayer player, int clickX, int clickY, int clickZ, int side, ItemStack stack) {
		Player bplayer = BukkitEventUtils.getPlayer(player);
		org.bukkit.inventory.ItemStack bitem = BukkitEventUtils.getItemStack(stack);
		Block blockClicked = bplayer.getWorld().getBlockAt(clickX, clickY, clickZ);
		if(isFilling) {
			return new PlayerBucketFillEvent(bplayer, blockClicked, BlockFace.SELF, bitem.getType(), bitem);
		} else {
			return new PlayerBucketEmptyEvent(bplayer, blockClicked, BukkitEventUtils.getBlockFace(side), bitem.getType(), bitem);
		}
	}

}
