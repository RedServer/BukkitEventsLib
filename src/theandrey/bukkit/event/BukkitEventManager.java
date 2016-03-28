package theandrey.bukkit.event;

import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.plugin.PluginManager;

public final class BukkitEventManager {

	private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

	/**
	 * Вызывает событие наполнения ведра
	 * @param player Игрок
	 * @param stack Предмет (ведро)
	 * @param blockX x
	 * @param blockY y
	 * @param blockZ z
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, int blockX, int blockY, int blockZ) {
		PlayerBucketFillEvent event = BukkitEventFactory.newPlayerBucketFillEvent(player, blockX, blockY, blockZ, stack);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @param player Игрок
	 * @param stack Предмет в руке (ведро)
	 * @param clickX x
	 * @param clickY y
	 * @param clickZ z
	 * @param clickSide
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, int clickX, int clickY, int clickZ, int clickSide) {
		PlayerBucketEmptyEvent event = BukkitEventFactory.newPlayerBucketEmptyEvent(player, clickX, clickY, clickZ, clickSide, stack);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @param player Игрок
	 * @param stack Предмет (ведро)
	 * @param mop
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, MovingObjectPosition mop) {
		if(mop.typeOfHit != EnumMovingObjectType.TILE) throw new IllegalArgumentException("MovingObjectPosition.typeOfHit != TILE (получено: " + mop.typeOfHit.name() + ")");
		return callBucketEmptyEvent(player, stack, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
	}

	/**
	 * Кидает эвент разрушения блока
	 * @param player
	 * @param z
	 * @param x
	 * @param y
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
	public static boolean callEntityDamageByEntityEvent(Entity attacker, Entity damagee, EntityDamageEvent.DamageCause cause, int damage) {
		if(attacker == null || damagee == null) return false;
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(damagee), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент установки блока
	 * @param player
	 * @param x
	 * @param stackBlock
	 * @param z
	 * @param y
	 * @return true если эвент не был отменен.
	 */
	public static boolean callBlockPlaceEvent(EntityPlayer player, int x, int y, int z, net.minecraft.item.ItemStack stackBlock) {
		if(player == null || stackBlock == null) return false;
		BlockState replacedBlockState = BukkitEventUtils.getBlockState(player.worldObj, x, y, z);
		BlockPlaceEvent event = new BlockPlaceEvent(replacedBlockState.getBlock(), replacedBlockState, BukkitEventUtils.getBlock(player.worldObj, x, y, z), BukkitEventUtils.getItemStack(stackBlock), BukkitEventUtils.getPlayer(player), true);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Кидает эвент перемещения блока (используется для жидкостей)
	 * @param worldObj
	 * @param zto
	 * @param x
	 * @param yto
	 * @param z
	 * @param y
	 * @param xto
	 * @return true если эвент не был отменен.
	 */
	public static boolean callBlockFromToEvent(net.minecraft.world.World worldObj, int x, int y, int z, int xto, int yto, int zto) {
		World bworld = BukkitEventUtils.getWorld(worldObj);
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
		AsyncPlayerChatEvent event = BukkitEventFactory.newPlayerChatEvent(sender, message, recipients);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	@Deprecated
	public static boolean callBucketFillEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, int x, int y, int z, int side) {
		return callBucketFillEvent(player, stack, x, y, z);
	}

}
