package theandrey.bukkit.event;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import theandrey.bukkit.event.util.FakeBlockImpl;

public final class BukkitEventManager {

	private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

	private BukkitEventManager() {
	}

	/**
	 * Вызывает событие наполнения ведра
	 * @param stack Предмет (ведро)
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(@Nonnull EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, int blockX, int blockY, int blockZ) {
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
	public static boolean callBucketEmptyEvent(@Nonnull EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, int clickX, int clickY, int clickZ, int clickSide) {
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
	public static boolean callBucketEmptyEvent(@Nonnull EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, @Nonnull MovingObjectPosition mop) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) throw new IllegalArgumentException("MovingObjectPosition.typeOfHit must be a BLOCK, given: " + mop.typeOfHit);

		return callBucketEmptyEvent(player, stack, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
	}

	/**
	 * Отправляет ивент разрушения блока
	 * @param player Игрок
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockBreakEvent(@Nonnull EntityPlayer player, int x, int y, int z) {
		if (player == null) throw new IllegalArgumentException("player is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		Block block = bukkitPlayer.getWorld().getBlockAt(x, y, z);

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
	@SuppressWarnings("deprecation")
	public static boolean callEntityDamageByEntityEvent(@Nonnull Entity attacker, @Nonnull Entity target, @Nonnull EntityDamageEvent.DamageCause cause, double damage) {
		if (attacker == null) throw new IllegalArgumentException("attacker is null");
		if (target == null) throw new IllegalArgumentException("target is null");
		if (cause == null) throw new IllegalArgumentException("cause is null");
		if (damage < 0) throw new IllegalArgumentException("Invalid damage: " + damage);

		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitEventUtils.getBukkitEntity(attacker), BukkitEventUtils.getBukkitEntity(target), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет событие установки блока игроком.
	 * @param player Игрок. Если null, метод вернёт false
	 * @param x Координата X, куда будет установлен блок
	 * @param y Координата Y, куда будет установлен блок
	 * @param z Координата Z, куда будет установлен блок
	 * @param blockPlaced Устанавливаемый блок
	 * @param metadata meta устанавливаемого блока
	 * @param side Сторона блока, по которому кликнули (в этом направление относительно него будет установлен новый блок). Укажите -1 если сторона неизвестна
	 * @param stackInHand Предмет в руке игрока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockPlaceEvent(@Nonnull EntityPlayer player, int x, int y, int z, @Nonnull net.minecraft.block.Block blockPlaced, int metadata, int side, @Nullable net.minecraft.item.ItemStack stackInHand) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (blockPlaced == null) throw new IllegalArgumentException("blockPlaced is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		Block bukkitBlock = BukkitEventUtils.getBlock(player.worldObj, x, y, z);
		FakeBlockImpl placed = new FakeBlockImpl(bukkitBlock, BukkitEventUtils.getMaterial(blockPlaced), (byte)metadata);
		ItemStack item = BukkitEventUtils.getItemStack(stackInHand);
		BlockFace face = (side == -1) ? BlockFace.SELF : BukkitEventUtils.getBlockFace(side).getOppositeFace();

		BlockPlaceEvent event = new BlockPlaceEvent(placed, bukkitBlock.getState(), bukkitBlock.getRelative(face), item, bukkitPlayer, true);
		pluginManager.callEvent(event);
		return (!event.isCancelled() && event.canBuild());
	}

	/**
	 * Отправляет ивент взаимодействия игрока с блоком, подставляя некоторые параметры.
	 * Используется тогда, когда важно проверить возможность взаимодействия игрока с блоком.
	 * Подставляемые параметры:
	 * @param player Игрок
	 * @param x X-координата блока по которому кликнули
	 * @param y Y-координата блока по которому кликнули
	 * @param z Z-координата блока по которому кликнули
	 * @param action Действие
	 * @param blockFace Сторона блока по которой кликнули
	 * @param stack Предмет в руке, которым кликнули по блоку
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerInteractEvent(@Nonnull net.minecraft.entity.player.EntityPlayer player, int x, int y, int z, @Nonnull Action action, @Nonnull BlockFace blockFace, @Nullable net.minecraft.item.ItemStack stack) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (action == null) throw new IllegalArgumentException("action is null");
		if (blockFace == null) throw new IllegalArgumentException("blockFace is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		Block bukkitBlock = bukkitPlayer.getWorld().getBlockAt(x, y, z);
		ItemStack bukkitStack = BukkitEventUtils.getItemStack(stack);
		PlayerInteractEvent event = new PlayerInteractEvent(bukkitPlayer, action, bukkitStack, bukkitBlock, blockFace);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент формирования одного блока другим. Используется для жидкостей
	 * @param world Мир
	 * @param x Координата блока
	 * @param y Координата блока
	 * @param z Координата блока
	 * @param xto Координата перемещения блока
	 * @param yto Координата перемещения блока
	 * @param zto Координата перемещения блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockFromToEvent(@Nonnull net.minecraft.world.World world, int x, int y, int z, int xto, int yto, int zto) {
		World bukkitWorld = BukkitEventUtils.getWorld(world);
		BlockFromToEvent event = new BlockFromToEvent(bukkitWorld.getBlockAt(x, y, z), bukkitWorld.getBlockAt(xto, yto, zto));
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент стрижки моба
	 * @param player Игрок
	 * @param entity Моб, которого стригут
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerShearEntityEvent(@Nonnull net.minecraft.entity.player.EntityPlayer player, @Nonnull net.minecraft.entity.Entity entity) {
		if (player == null) throw new IllegalArgumentException("player is null!");
		if (entity == null) throw new IllegalArgumentException("entity is null!");

		PlayerShearEntityEvent event = new PlayerShearEntityEvent(BukkitEventUtils.getPlayer(player), BukkitEventUtils.getBukkitEntity(entity));
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент чата
	 * @param sender Игрок-отправитель сообщения
	 * @param message Сообщение
	 * @param recipients Список получателей сообщения
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerChatEvent(@Nonnull EntityPlayer sender, @Nonnull String message, @Nonnull Collection<EntityPlayer> recipients) {
		AsyncPlayerChatEvent event = BukkitEventFactory.newPlayerChatEvent(sender, message, recipients);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент изменения блока мобом
	 * @param entity Моб
	 * @param x Координата блока
	 * @param y Координата блока
	 * @param z Координата блока
	 * @param newBlock Новый блок или null, если блок сломан
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	@SuppressWarnings("deprecation")
	public static boolean callEntityChangeBlockEvent(@Nonnull Entity entity, int x, int y, int z, @Nullable BlockStateData newBlock) {
		if (entity == null) throw new IllegalArgumentException("entity is null!");

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
