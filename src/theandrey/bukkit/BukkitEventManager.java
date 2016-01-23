package theandrey.bukkit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
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
		Player bukkitPlayer = BukkitUtils.getPlayer(player);
		ItemStack bukkitStack = BukkitUtils.getItemStack(stack);
		PlayerBucketFillEvent event = new PlayerBucketFillEvent(
				bukkitPlayer,
				bukkitPlayer.getWorld().getBlockAt(x, y, z),
				BukkitUtils.getBlockFace(side),
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
		Player bukkitPlayer = BukkitUtils.getPlayer(player);
		ItemStack bukkitStack = BukkitUtils.getItemStack(stack);
		PlayerBucketEmptyEvent event = new PlayerBucketEmptyEvent(
				bukkitPlayer,
				bukkitPlayer.getWorld().getBlockAt(x, y, z),
				BukkitUtils.getBlockFace(side),
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
	public static boolean callBlockBreak(EntityPlayer player, int x, int y, int z) {
		Player bukkitPlayer = BukkitUtils.getPlayer(player);
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
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(BukkitUtils.getBukkitEntity(attacker), BukkitUtils.getBukkitEntity(damagee), cause, damage);
		pluginManager.callEvent(event);
		return !event.isCancelled();
	}

}
