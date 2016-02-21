package theandrey.bukkit.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public final class BukkitEventManager {

	private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

	/**
	 * Вызывает событие наполнения ведра
	 * @param player Игрок
	 * @param stack Предмет (ведро)
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param side
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, int x, int y, int z, int side) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		ItemStack bukkitStack = BukkitEventUtils.getItemStack(stack);
		PlayerBucketFillEvent event = new PlayerBucketFillEvent(
				bukkitPlayer,
				bukkitPlayer.getWorld().getBlockAt(x, y, z),
				BukkitEventUtils.getBlockFace(side),
				(bukkitStack != null) ? bukkitStack.getType() : Material.WATER_BUCKET,
				bukkitStack
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @param player Игрок
	 * @param stack Предмет (ведро)
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param side
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, int x, int y, int z, int side) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		ItemStack bukkitStack = BukkitEventUtils.getItemStack(stack);
		PlayerBucketEmptyEvent event = new PlayerBucketEmptyEvent(
				bukkitPlayer,
				bukkitPlayer.getWorld().getBlockAt(x, y, z),
				BukkitEventUtils.getBlockFace(side),
				(bukkitStack != null) ? bukkitStack.getType() : Material.WATER_BUCKET,
				bukkitStack
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент разрушения блока
	 * @return true если эвент не был отменен.
	 */
	public static boolean callBlockBreakEvent(EntityPlayer player, int x, int y, int z) {
		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		if(bukkitPlayer == null) return false;
		Block block = bukkitPlayer.getWorld().getBlockAt(x, y, z);
		BlockBreakEvent event = new BlockBreakEvent(block, bukkitPlayer);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Вызов эвента нанесения урона одним существом другому
	 * @param attacker Атакующий
	 * @param damagee Пострадавший
	 * @param cause Причина
	 * @param damage Урон
	 * @return true, если эвет не был отменён
	 */
	@SuppressWarnings("deprecation")
	public static boolean callEntityDamageByEntityEvent(Entity attacker, Entity damagee, EntityDamageEvent.DamageCause cause, int damage) {
		if(attacker == null || damagee == null) return false;
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(damagee), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент установки блока
	 * @param player Игрок
	 * @param x
	 * @param y
	 * @param z
	 * @param stack Предмет в руке (блок)
	 * @return true если эвент не был отменен.
	 */
	public static boolean callBlockPlaceEvent(EntityPlayer player, int x, int y, int z, net.minecraft.item.ItemStack stack) {
		if(player == null || stack == null) return false;
		BlockState replacedBlockState = BukkitEventUtils.getBlockState(player.worldObj, x, y, z);
		BlockPlaceEvent event = new BlockPlaceEvent(
				replacedBlockState.getBlock(),
				replacedBlockState,
				BukkitEventUtils.getBlock(player.worldObj, x, y, z),
				BukkitEventUtils.getItemStack(stack),
				BukkitEventUtils.getPlayer(player),
				true
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент взаимодействия игрока с блоком, подставляя некоторые параметры.
	 * Используется тогда, когда важно проверить возможность взаимодействия игрока с блоком.
	 * Подставляемые параметры:
	 * @param player Игрок
	 * @param x X-координата блока по которому кликнули
	 * @param y Y-координата блока по которому кликнули
	 * @param z Z-координата блока по которому кликнули
	 * @param action Действие
	 * @param face Сторона блока по которой кликнули
	 * @param stack Предмет в руке, которым кликнули по блоку
	 * @return true если эвент не был отменен.
	 */
	public static boolean callPlayerInteractEvent(net.minecraft.entity.player.EntityPlayer player, int x, int y, int z, Action action, BlockFace face, net.minecraft.item.ItemStack stack) {
		Player bplayer = BukkitEventUtils.getPlayer(player);
		Block bblock = bplayer.getWorld().getBlockAt(x, y, z);
		ItemStack bstack = BukkitEventUtils.getItemStack(stack);
		PlayerInteractEvent event = new PlayerInteractEvent(bplayer, action, bstack, bblock, face);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент перемещения блока (используется для жидкостей)
	 * @param world Мир
	 * @param x Координата блока
	 * @param y Координата блока
	 * @param z Координата блока
	 * @param xto Координата перемещения блока
	 * @param yto Координата перемещения блока
	 * @param zto Координата перемещения блока
	 * @return true если эвент не был отменен.
	 */
	public static boolean callBlockFromToEvent(net.minecraft.world.World world, int x, int y, int z, int xto, int yto, int zto) {
		World bworld = BukkitEventUtils.getWorld(world);
		BlockFromToEvent event = new BlockFromToEvent(bworld.getBlockAt(x, y, x), bworld.getBlockAt(xto, yto, zto));
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет эвент чата
	 * @param async Асинхронный? (отправлен не из главного потока)
	 * @param sender Игрок-отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Список получателей сообщения
	 * @return true, если эвент не был отменён
	 */
	public static boolean callPlayerChatEvent(boolean async, EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
		Player bsender = BukkitEventUtils.getPlayer(sender);
		Set<Player> brecipients = new HashSet<>();
		for(EntityPlayer r : recipients) brecipients.add(BukkitEventUtils.getPlayer(r));
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, bsender, message, brecipients);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	@Deprecated
	public static boolean callBlockBreak(EntityPlayer player, int x, int y, int z) {
		return callBlockBreakEvent(player, x, y, z);
	}

	@Deprecated
	public static boolean callBlockPlace(EntityPlayer player, int x, int y, int z, net.minecraft.item.ItemStack stack) {
		return callBlockPlaceEvent(player, x, y, z, stack);
	}

	@Deprecated
	public static boolean callPlayerInteract(net.minecraft.entity.player.EntityPlayer player, int x, int y, int z, Action action, BlockFace face, net.minecraft.item.ItemStack stack) {
		return callPlayerInteractEvent(player, x, y, z, action, face, stack);
	}

}
