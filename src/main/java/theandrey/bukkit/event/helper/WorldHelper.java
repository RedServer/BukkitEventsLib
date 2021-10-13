package theandrey.bukkit.event.helper;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.tileentity.TileEntity;
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
	@SuppressWarnings("deprecation")
	public static boolean isBlockMovable(@Nonnull org.bukkit.block.Block block) {
		if (block == null) throw new IllegalArgumentException("block is null");

		Block theBlock = Block.getBlockById(block.getTypeId());
		if (theBlock.getMobilityFlag() == 2) return false;

		TileEntity tileentity = BukkitEventUtils.toVanillaWorld(block.getWorld()).getTileEntity(block.getX(), block.getY(), block.getZ());
		return tileentity == null;
	}

	/**
	 * Создаёт CustomExplosion и отсылает пакеты клиентам.
	 * Является аналогом {@link WorldServer#createExplosion(Entity, double, double, double, float, boolean)}
	 */
	@Nonnull
	public static CustomExplosion createExplosion(@Nonnull World world, @Nullable Entity exploder, double x, double y, double z, float size, boolean isFlaming, boolean isSmoking, @Nonnull ExplosionCause cause) {
		CustomExplosion explosion = new CustomExplosion(world, exploder, x, y, z, size);
		explosion.isFlaming = isFlaming;
		explosion.isSmoking = isSmoking;
		explosion.cause = Objects.requireNonNull(cause, "Explosion cause is null");

		if (ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;

		explosion.doExplosionA();
		explosion.doExplosionB(false);
		if (explosion.wasCanceled) return explosion;

		if (!isSmoking) {
			explosion.affectedBlockPositions.clear();
		}

		@SuppressWarnings("unchecked") List<EntityPlayerMP> playerList = world.playerEntities;
		for (EntityPlayerMP player : playerList) {
			if (!player.isDead && player.getDistanceSq(x, y, z) < 4096D) {
				player.playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, size, explosion.affectedBlockPositions,
						explosion.getAffectedPlayers().get(player)));
			}
		}

		return explosion;
	}
}
