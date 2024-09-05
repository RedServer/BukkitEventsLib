package me.theandrey.bukkit.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import me.theandrey.bukkit.data.BlockStateData;
import me.theandrey.bukkit.internal.FakeBlockImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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

public final class BukkitEvents {

	private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

	private BukkitEvents() {
	}

	/**
	 * Вызывает событие наполнения ведра
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketFillEvent(EntityPlayerMP player, BlockPos pos, @Nullable net.minecraft.item.ItemStack stack) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");

		PlayerBucketFillEvent event = BukkitEventFactory.newPlayerBucketFillEvent(player, pos, stack);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayerMP player, BlockPos pos, BlockFace face, @Nullable net.minecraft.item.ItemStack stack) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");
		Objects.requireNonNull(face, "face");

		PlayerBucketEmptyEvent event = BukkitEventFactory.newPlayerBucketEmptyEvent(player, pos, face, stack);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Вызывает событие опустошения ведра
	 * @return true если событие не было отменено
	 */
	public static boolean callBucketEmptyEvent(EntityPlayerMP player, RayTraceResult target, @Nullable net.minecraft.item.ItemStack stack) {
		Objects.requireNonNull(player, "player");

		if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
			return callBucketEmptyEvent(player, target.getBlockPos(), Vanilla2Bukkit.getBlockFace(target.sideHit), stack);
		} else {
			throw new IllegalArgumentException("typeOfHit must be a BLOCK, given: " + target.typeOfHit);
		}
	}

	/**
	 * Отправляет ивент разрушения блока
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockBreakEvent(EntityPlayerMP player, BlockPos pos) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		Block block = bukkitPlayer.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());

		BlockBreakEvent event = new BlockBreakEvent(block, bukkitPlayer);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент нанесения урона одним существом другому
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	@SuppressWarnings("deprecation")
	public static boolean callEntityDamageByEntityEvent(Entity attacker, Entity target, EntityDamageEvent.DamageCause cause, double damage) {
		Objects.requireNonNull(attacker, "attacker");
		Objects.requireNonNull(target, "target");
		Objects.requireNonNull(cause, "cause");

		if (damage < 0) {
			throw new IllegalArgumentException("Invalid damage amount: " + damage);
		}

		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(Vanilla2Bukkit.getBukkitEntity(attacker), Vanilla2Bukkit.getBukkitEntity(target), cause, damage);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Отправляет событие установки блока игроком.
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockPlaceEvent(EntityPlayerMP player, BlockPos pos, IBlockState blockState, BlockFace blockFace, EnumHand hand) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");
		Objects.requireNonNull(blockState, "blockState");
		Objects.requireNonNull(blockFace, "blockFace");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		Block bukkitBlock = Vanilla2Bukkit.getBlock(player.getEntityWorld(), pos);
		FakeBlockImpl placed = new FakeBlockImpl(bukkitBlock, Vanilla2Bukkit.getMaterial(blockState.getBlock()), (byte)blockState.getBlock().getMetaFromState(blockState));

		org.bukkit.inventory.ItemStack item;
		EquipmentSlot equipmentSlot;

		if (hand == EnumHand.MAIN_HAND) {
			item = bukkitPlayer.getInventory().getItemInMainHand();
			equipmentSlot = EquipmentSlot.HAND;
		} else {
			item = bukkitPlayer.getInventory().getItemInOffHand();
			equipmentSlot = EquipmentSlot.OFF_HAND;
		}

		BlockPlaceEvent event = new BlockPlaceEvent(placed, bukkitBlock.getState(), bukkitBlock.getRelative(blockFace), item, bukkitPlayer, true, equipmentSlot);
		pluginManager.callEvent(event);
		return !event.isCancelled() && event.canBuild();
	}

	/**
	 * Отправляет ивент взаимодействия игрока с блоком, подставляя некоторые параметры.
	 * Используется тогда, когда важно проверить возможность взаимодействия игрока с блоком.
	 * Подставляемые параметры:
	 * @param player Игрок
	 * @param action Действие
	 * @param blockFace Сторона блока по которой кликнули
	 * @param stack Предмет в руке, которым кликнули по блоку
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerInteractEvent(EntityPlayerMP player, BlockPos pos, Action action, BlockFace blockFace, @Nullable net.minecraft.item.ItemStack stack) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(pos, "pos");
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(blockFace, "blockFace");

		Player bukkitPlayer = Vanilla2Bukkit.getPlayer(player);
		Block bukkitBlock = bukkitPlayer.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
		ItemStack bukkitStack = Vanilla2Bukkit.getItemStack(stack);
		PlayerInteractEvent event = new PlayerInteractEvent(bukkitPlayer, action, bukkitStack, bukkitBlock, blockFace);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент формирования одного блока другим. Используется для жидкостей
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callBlockFromToEvent(net.minecraft.world.World world, BlockPos from, BlockPos to) {
		World bukkitWorld = Vanilla2Bukkit.getWorld(world);
		BlockFromToEvent event = new BlockFromToEvent(
			bukkitWorld.getBlockAt(from.getX(), from.getY(), from.getZ()),
			bukkitWorld.getBlockAt(to.getX(), to.getY(), to.getZ())
		);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент стрижки моба
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callPlayerShearEntityEvent(EntityPlayerMP player, net.minecraft.entity.Entity entity) {
		Objects.requireNonNull(player, "player");
		Objects.requireNonNull(entity, "entity");

		PlayerShearEntityEvent event = new PlayerShearEntityEvent(Vanilla2Bukkit.getPlayer(player), Vanilla2Bukkit.getBukkitEntity(entity));
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
	public static boolean callPlayerChatEvent(EntityPlayerMP sender, String message, Collection<EntityPlayerMP> recipients) {
		AsyncPlayerChatEvent event = BukkitEventFactory.newPlayerChatEvent(sender, message, recipients);
		pluginManager.callEvent(event);

		return !event.isCancelled();
	}

	/**
	 * Отправляет ивент изменения блока мобом
	 * @param entity Моб
	 * @param newBlock Новый блок или null, если блок сломан
	 * @return Результат: выполнен успешно (разрешить) или отменён (запретить)
	 */
	public static boolean callEntityChangeBlockEvent(Entity entity, BlockPos pos, @Nullable BlockStateData newBlock) {
		Objects.requireNonNull(entity, "entity");

		EntityChangeBlockEvent event = new EntityChangeBlockEvent(
			Vanilla2Bukkit.getBukkitEntity(entity),
			Vanilla2Bukkit.getBlock(entity.world, pos.getX(), pos.getY(), pos.getZ()),
			Optional.ofNullable(newBlock).map(BlockStateData::getType).orElse(Material.AIR),
			Optional.ofNullable(newBlock).map(block -> (byte)block.getMeta()).orElse((byte)0)
		);

		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

}
