package theandrey.bukkit.event;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
	 * @param stack Предмет (ведро)
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(@Nonnull EntityPlayer player, @Nullable ItemStack stack, int blockX, int blockY, int blockZ) {
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
	public static boolean callBucketEmptyEvent(@Nonnull EntityPlayer player, @Nullable ItemStack stack, int clickX, int clickY, int clickZ, int clickSide) {
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
	public static boolean callBucketEmptyEvent(@Nonnull EntityPlayer player, @Nullable ItemStack stack, @Nonnull MovingObjectPosition mop) {
		if(player == null) throw new IllegalArgumentException("player is null");
		if(mop.typeOfHit != EnumMovingObjectType.TILE) throw new IllegalArgumentException("MovingObjectPosition.typeOfHit must be a TILE, given: " + mop.typeOfHit);

		return callBucketEmptyEvent(player, stack, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
	}

	/**
	 * Отправляет ивент разрушения блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockBreakEvent(@Nonnull EntityPlayer player, int x, int y, int z) {
		if(player == null) throw new IllegalArgumentException("player is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		org.bukkit.block.Block block = bukkitPlayer.getWorld().getBlockAt(x, y, z);

		BlockBreakEvent event = new BlockBreakEvent(block, bukkitPlayer);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент нанесения урона одним существом другому
	 * @param attacker Атакующий
	 * @param target Пострадавший
	 * @param cause Причина
	 * @param damage Урон
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callEntityDamageByEntityEvent(@Nonnull Entity attacker, @Nonnull Entity target, @Nonnull EntityDamageEvent.DamageCause cause, int damage) {
		if(attacker == null) throw new IllegalArgumentException("attacker is null");
		if(target == null) throw new IllegalArgumentException("target is null");
		if(cause == null) throw new IllegalArgumentException("cause is null");
		if(damage < 0) throw new IllegalArgumentException("Invalid damage: " + damage);

		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(target), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент установки блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockPlaceEvent(@Nonnull EntityPlayer player, int x, int y, int z, @Nullable ItemStack stackBlock) {
		if(player == null) throw new IllegalArgumentException("player is null");

		BlockState replacedBlockState = BukkitEventUtils.getBlockState(player.worldObj, x, y, z);
		BlockPlaceEvent event = new BlockPlaceEvent(replacedBlockState.getBlock(), replacedBlockState, BukkitEventUtils.getBlock(player.worldObj, x, y, z), BukkitEventUtils.getItemStack(stackBlock), BukkitEventUtils.getPlayer(player), true);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент формирования одного блока другим. Используется в основном жидкостями.
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockFromToEvent(@Nonnull World worldObj, int x, int y, int z, int xto, int yto, int zto) {
		org.bukkit.World bukkitWorld = BukkitEventUtils.getWorld(worldObj);
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
	public static boolean callPlayerChatEvent(boolean async, @Nonnull EntityPlayer sender, @Nonnull String message, @Nonnull Collection<EntityPlayer> recipients) {
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
	public static boolean callPlayerShearEntityEvent(@Nonnull EntityPlayer player, @Nonnull net.minecraft.entity.Entity entity) {
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
	public static boolean callEntityChangeBlockEvent(@Nonnull Entity entity, int x, int y, int z, @Nullable BlockStateData newBlock) {
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
