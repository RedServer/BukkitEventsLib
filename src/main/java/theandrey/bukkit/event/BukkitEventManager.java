package theandrey.bukkit.event;

import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
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
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayer player, net.minecraft.item.ItemStack stack, MovingObjectPosition mop) {
		if(mop.typeOfHit != EnumMovingObjectType.TILE) throw new IllegalArgumentException("MovingObjectPosition.typeOfHit != TILE (получено: " + mop.typeOfHit.name() + ")");
		return callBucketEmptyEvent(player, stack, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
	}

	/**
	 * Отправляет ивент разрушения блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
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
	 * Отпроавляет ивент нанесения урона одним существом другому
	 * @param attacker Атакующий
	 * @param target Пострадавший
	 * @param cause Причина
	 * @param damage Урон
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callEntityDamageByEntityEvent(Entity attacker, Entity target, EntityDamageEvent.DamageCause cause, int damage) {
		if(attacker == null || target == null) return false;
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(target), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент установки блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockPlaceEvent(EntityPlayer player, int x, int y, int z, net.minecraft.item.ItemStack stackBlock) {
		if(player == null || stackBlock == null) return false;
		BlockState replacedBlockState = BukkitEventUtils.getBlockState(player.worldObj, x, y, z);
		BlockPlaceEvent event = new BlockPlaceEvent(replacedBlockState.getBlock(), replacedBlockState, BukkitEventUtils.getBlock(player.worldObj, x, y, z), BukkitEventUtils.getItemStack(stackBlock), BukkitEventUtils.getPlayer(player), true);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправлянт ивент формирования одного блока другим. Используется в основном жидкостями.
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockFromToEvent(net.minecraft.world.World worldObj, int x, int y, int z, int xto, int yto, int zto) {
		World bukkitWorld = BukkitEventUtils.getWorld(worldObj);
		BlockFromToEvent event = new BlockFromToEvent(bukkitWorld.getBlockAt(x, y, z), bukkitWorld.getBlockAt(xto, yto, zto));
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент чата
	 * @param async Асинхронный? (отправлен не из главного потока)
	 * @param sender Игрок-отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Список получателей сообщения
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerChatEvent(boolean async, EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
		AsyncPlayerChatEvent event = BukkitEventFactory.newPlayerChatEvent(sender, message, recipients);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент стрижки моба
	 * @param player Игрок
	 * @param entity Моб, которого стригут
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerShearEntityEvent(net.minecraft.entity.player.EntityPlayer player, net.minecraft.entity.Entity entity) {
		if(player == null) throw new IllegalArgumentException("player is null!");
		if(entity == null) throw new IllegalArgumentException("entity is null!");
		PlayerShearEntityEvent event = new PlayerShearEntityEvent(BukkitEventUtils.getPlayer(player), BukkitEventUtils.getBukkitEntity(entity));
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Событие изменения блока мобом
	 * @param entity Моб
	 * @param x Координата блока
	 * @param y Координата блока
	 * @param z Координата блока
	 * @param newBlock Новый блок или null, если блок сломан
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callEntityChangeBlockEvent(Entity entity, int x, int y, int z, BlockStateData newBlock) {
		if(entity == null) throw new IllegalArgumentException("entity is null!");

		EntityChangeBlockEvent event = new EntityChangeBlockEvent(
				BukkitEventUtils.getBukkitEntity(entity),
				BukkitEventUtils.getBlock(entity.worldObj, x, y, z),
				(newBlock != null) ? newBlock.getType() : Material.AIR,
				(newBlock != null) ? (byte)newBlock.getMeta() : 0
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

}
