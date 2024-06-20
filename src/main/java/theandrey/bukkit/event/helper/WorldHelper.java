package theandrey.bukkit.event.helper;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import theandrey.bukkit.event.BukkitEventUtils;
import theandrey.bukkit.event.CustomExplosion;
import theandrey.bukkit.event.api.ExplosionCause;

public final class WorldHelper {

	private WorldHelper() {
	}

	/**
	 * Проверяем возможность "легального" перемещения блока, примерно как поршни
	 * @return true если позволяет mobility flag блока и не имеет TileEntity
	 */
	public static boolean isBlockMovable(@Nonnull org.bukkit.block.Block block) {
		if (block == null) throw new IllegalArgumentException("block is null");
		IBlockState state = BukkitEventUtils.getBlockState(block);
		return state.getPushReaction() != EnumPushReaction.BLOCK && !state.getBlock().hasTileEntity(state);
	}

	/**
	 * Создаёт CustomExplosion и отсылает пакеты клиентам.
	 * Является аналогом {@link WorldServer#createExplosion(Entity, double, double, double, float, boolean)}
	 */
	@Nonnull
	public static CustomExplosion createExplosion(@Nonnull World world, @Nullable Entity exploder, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, @Nonnull ExplosionCause cause) {
		CustomExplosion explosion = new CustomExplosion(world, exploder, x, y, z, size, causesFire, damagesTerrain);
		explosion.cause = Objects.requireNonNull(cause, "Explosion cause is null");

		if (ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;

		explosion.doExplosionA();
		explosion.doExplosionB(false);
		if (explosion.wasCanceled) return explosion;

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
}
