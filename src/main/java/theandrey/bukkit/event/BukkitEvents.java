package theandrey.bukkit.event;

import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import theandrey.bukkit.event.util.FakeBlockImpl;

public final class BukkitEvents {

	private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

	private BukkitEvents() {
	}

	/**
	 * Вызывает событие наполнения ведра
	 * @param stack Предмет (ведро)
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, int blockX, int blockY, int blockZ) {
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
	public static boolean callBucketEmptyEvent(EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, int clickX, int clickY, int clickZ, BlockFace face) {
		PlayerBucketEmptyEvent event = BukkitEventFactory.newPlayerBucketEmptyEvent(player, clickX, clickY, clickZ, face, stack);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @param player Игрок
	 * @param stack Предмет (ведро)
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayer player, @Nullable net.minecraft.item.ItemStack stack, RayTraceResult target) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (target.typeOfHit != RayTraceResult.Type.BLOCK) throw new IllegalArgumentException("typeOfHit must be a BLOCK, given: " + target.typeOfHit);

		BlockPos pos = target.getBlockPos();
		return callBucketEmptyEvent(player, stack, pos.getX(), pos.getY(), pos.getZ(), BukkitEventUtils.getBlockFace(target.sideHit));
	}

	/**
	 * Отправляет ивент разрушения блока
	 * @param player Игрок
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockBreakEvent(EntityPlayer player, int x, int y, int z) {
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
	public static boolean callEntityDamageByEntityEvent(Entity attacker, Entity target, EntityDamageEvent.DamageCause cause, double damage) {
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
	 * @param face Сторона блока, по которому кликнули (в этом направление относительно него будет установлен новый блок).
	 * @param hand Активная рука
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockPlaceEvent(EntityPlayer player, int x, int y, int z, net.minecraft.block.Block blockPlaced, int metadata, BlockFace face, EnumHand hand) {
		if (player == null) throw new IllegalArgumentException("player is null");
		if (blockPlaced == null) throw new IllegalArgumentException("blockPlaced is null");

		Player bukkitPlayer = BukkitEventUtils.getPlayer(player);
		Block bukkitBlock = BukkitEventUtils.getBlock(player.getEntityWorld(), x, y, z);
		FakeBlockImpl placed = new FakeBlockImpl(bukkitBlock, BukkitEventUtils.getMaterial(blockPlaced), (byte)metadata);

		org.bukkit.inventory.ItemStack item;
		EquipmentSlot equipmentSlot;
		if (hand == EnumHand.MAIN_HAND) {
			item = bukkitPlayer.getInventory().getItemInMainHand();
			equipmentSlot = EquipmentSlot.HAND;
		} else {
			item = bukkitPlayer.getInventory().getItemInOffHand();
			equipmentSlot = EquipmentSlot.OFF_HAND;
		}

		BlockPlaceEvent event = new BlockPlaceEvent(placed, bukkitBlock.getState(), bukkitBlock.getRelative(face), item, bukkitPlayer, true, equipmentSlot);
		pluginManager.callEvent(event);
		return !event.isCancelled() && event.canBuild();
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
	public static boolean callPlayerInteractEvent(net.minecraft.entity.player.EntityPlayer player, int x, int y, int z, Action action, BlockFace blockFace, @Nullable net.minecraft.item.ItemStack stack) {
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
	public static boolean callBlockFromToEvent(net.minecraft.world.World world, int x, int y, int z, int xto, int yto, int zto) {
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
	public static boolean callPlayerShearEntityEvent(net.minecraft.entity.player.EntityPlayer player, net.minecraft.entity.Entity entity) {
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
	public static boolean callPlayerChatEvent(EntityPlayer sender, String message, Collection<EntityPlayer> recipients) {
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
	public static boolean callEntityChangeBlockEvent(Entity entity, int x, int y, int z, @Nullable BlockStateData newBlock) {
		if (entity == null) throw new IllegalArgumentException("entity is null!");

		EntityChangeBlockEvent event = new EntityChangeBlockEvent(
			BukkitEventUtils.getBukkitEntity(entity),
			BukkitEventUtils.getBlock(entity.world, x, y, z),
			Optional.ofNullable(newBlock).map(BlockStateData::getType).orElse(Material.AIR),
			Optional.ofNullable(newBlock).map(block -> (byte)block.getMeta()).orElse((byte)0)
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

}
