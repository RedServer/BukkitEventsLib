package me.theandrey.bukkit.util;

import java.util.Objects;
import javax.annotation.Nullable;
import me.theandrey.bukkit.CustomExplosion;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;

public final class WorldHelper {

	@Nullable
	private static CraftBukkitAccessor accessorInstance;

	private WorldHelper() {
	}

	/**
	 * Проверяем возможность "легального" перемещения блока, примерно как поршни
	 * @return true если позволяет mobility flag блока и не имеет TileEntity
	 */
	public static boolean isBlockMovable(org.bukkit.block.Block block) {
		IBlockState state = Bukkit2Vanilla.getBlockState(block);
		return state.getPushReaction() != EnumPushReaction.BLOCK && !state.getBlock().hasTileEntity(state);
	}

	/**
	 * Создаёт CustomExplosion и отсылает пакеты клиентам.
	 * Является аналогом {@link WorldServer#createExplosion(Entity, double, double, double, float, boolean)}
	 */
	public static CustomExplosion createExplosion(World world, @Nullable Entity exploder, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, ExplosionCause cause) {
		CustomExplosion explosion = new CustomExplosion(world, exploder, x, y, z, size, causesFire, damagesTerrain);
		explosion.cause = Objects.requireNonNull(cause, "cause");

		if (ForgeEventFactory.onExplosionStart(world, explosion)) {
			return explosion;
		}

		explosion.doExplosionA();
		explosion.doExplosionB(false);

		if (explosion.wasCanceled) {
			return explosion;
		}

		if (!damagesTerrain) {
			explosion.clearAffectedBlockPositions();
		}

		for (EntityPlayer player : world.playerEntities) {
			if (!player.isDead && player.getDistanceSq(x, y, z) < 4096D) {
				((EntityPlayerMP)player).connection.sendPacket(new SPacketExplosion(x, y, z, size, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(player)));
			}
		}

		return explosion;
	}

	/**
	 * Спавнит существо в мире с указанием причины для CreatureSpawnEvent
	 * @return Успешный спавн
	 */
	public static boolean spawnEntityInWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		return getAccessor().spawnEntityInWorld(
			Objects.requireNonNull(world, "world"),
			Objects.requireNonNull(entity, "entity"),
			Objects.requireNonNull(reason, "reason")
		);
	}

	private static CraftBukkitAccessor getAccessor() {
		if (accessorInstance == null) {
			accessorInstance = CraftBukkitAccessor.get();
		}
		return accessorInstance;
	}
}
