package theandrey.bukkit.event.helper;

import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet60Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import theandrey.bukkit.event.CustomExplosion;
import theandrey.bukkit.event.api.ExplosionCause;

public final class WorldHelper {

	private WorldHelper() {
	}

	/**
	 * Создаёт CustomExplosion и отсылает пакеты клиентам.
	 * Является аналогом {@link WorldServer#createExplosion(Entity, double, double, double, float, boolean)}
	 */
	public static CustomExplosion createExplosion(World world, Entity exploder, double x, double y, double z, float size, boolean isFlaming, boolean isSmoking, ExplosionCause cause) {
		CustomExplosion explosion = new CustomExplosion(world, exploder, x, y, z, size);
		explosion.isFlaming = isFlaming;
		explosion.isSmoking = isSmoking;
		explosion.cause = Objects.requireNonNull(cause, "Explosion cause is null");

		explosion.doExplosionA();
		explosion.doExplosionB(false);
		if(explosion.wasCanceled) return explosion;

		if(!isSmoking) {
			explosion.affectedBlockPositions.clear();
		}

		@SuppressWarnings("unchecked") List<EntityPlayerMP> playerList = world.playerEntities;
		for(EntityPlayerMP player : playerList) {
			if(!player.isDead && player.getDistanceSq(x, y, z) < 4096D) {
				player.playerNetServerHandler.sendPacketToPlayer(new Packet60Explosion(x, y, z, size, explosion.affectedBlockPositions,
						explosion.getAffectedPlayers().get(player)));
			}
		}

		return explosion;
	}
}
