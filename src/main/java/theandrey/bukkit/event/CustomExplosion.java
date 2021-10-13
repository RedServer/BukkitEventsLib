package theandrey.bukkit.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.block.BlockIgniteEvent;
import theandrey.bukkit.event.api.ExplosionCause;
import theandrey.bukkit.event.common.CustomExplosionEvent;

public class CustomExplosion extends Explosion {

	private final World worldObj;
	private final org.bukkit.World bWorld;
	private float explosionSizeEnt;
	private final Map<EntityPlayerMP, Vec3> affectedPlayers = new HashMap<>();

	/**
	 * Список существ затронутых взрывом
	 */
	public final List<Entity> entities = new ArrayList<>();

	/**
	 * Причина взрыва. Это поле должно быть обязательно заполнено
	 */
	public ExplosionCause cause;

	/**
	 * Флаг отмены ивента взрыва плагинами
	 */
	public boolean wasCanceled = false;

	/**
	 * @param world Мир
	 * @param exploder Существо, которое вызвало взрыв. Может быть null
	 * @param x Точка эпицентра
	 * @param y Точка эпицентра
	 * @param z Точка эпицентра
	 * @param size Мощность взрыва
	 */
	public CustomExplosion(@Nonnull World world, @Nullable Entity exploder, double x, double y, double z, float size) {
		super(world, exploder, x, y, z, size);
		this.worldObj = Objects.requireNonNull(world, "world");
		this.bWorld = BukkitEventUtils.getWorld(worldObj);
	}

	/**
	 * Does the first part of the explosion (calculate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doExplosionA() {
		final float size = explosionSize;
		HashSet<ChunkPosition> blockPositions = new HashSet<>();

		final int field_77289_h = 16;
		for (int i = 0; i < field_77289_h; i++) {
			for (int j = 0; j < field_77289_h; j++) {
				for (int k = 0; k < field_77289_h; k++) {
					if (i == 0 || i == field_77289_h - 1 || j == 0 || j == field_77289_h - 1 || k == 0 || k == field_77289_h - 1) {

						double d0 = (float)i / ((float)field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d1 = (float)j / ((float)field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d2 = (float)k / ((float)field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f1 = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
						double x = explosionX;
						double y = explosionY;
						double z = explosionZ;

						for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
							int blockX = MathHelper.floor_double(x);
							int blockY = MathHelper.floor_double(y);
							int blockZ = MathHelper.floor_double(z);
							Block block = worldObj.getBlock(blockX, blockY, blockZ);

							if (block.getMaterial() != Material.air) {
								float f3 = exploder != null ? exploder.func_145772_a(this, worldObj, blockX, blockY, blockZ, block)
										: block.getExplosionResistance(exploder, worldObj, blockX, blockY, blockZ, explosionX, explosionY, explosionZ);
								f1 -= (f3 + 0.3F) * f2;
							}

							if (f1 > 0.0F && (exploder == null || exploder.func_145774_a(this, worldObj, blockX, blockY, blockZ, block, f1))) {
								blockPositions.add(new ChunkPosition(blockX, blockY, blockZ));
							}

							x += d0 * (double)f2;
							y += d1 * (double)f2;
							z += d2 * (double)f2;
						}
					}
				}
			}
		}

		getAffectedBlockPositions().addAll(blockPositions);
		explosionSize *= 2.0F;
		int x1 = MathHelper.floor_double(explosionX - (double)explosionSize - 1.0D);
		int x2 = MathHelper.floor_double(explosionX + (double)explosionSize + 1.0D);
		int y1 = MathHelper.floor_double(explosionY - (double)explosionSize - 1.0D);
		int y2 = MathHelper.floor_double(explosionY + (double)explosionSize + 1.0D);
		int z1 = MathHelper.floor_double(explosionZ - (double)explosionSize - 1.0D);
		int z2 = MathHelper.floor_double(explosionZ + (double)explosionSize + 1.0D);

		entities.clear();
		entities.addAll(worldObj.getEntitiesWithinAABBExcludingEntity(exploder, AxisAlignedBB.getBoundingBox(x1, y1, z1, x2, y2, z2)));
		ForgeEventFactory.onExplosionDetonate(worldObj, this, entities, explosionSize);
		explosionSizeEnt = explosionSize; // Сохраняем, чтобы использовать в дальнейшем

		explosionSize = size;
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 * @param particles Spawn particles
	 */
	@Override
	public void doExplosionB(boolean particles) {
		worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		List<ChunkPosition> affectedBlocks = getAffectedBlockPositions();

		/* Send bukkit event */
		List<org.bukkit.entity.Entity> affectedEntityList = entities.stream()
				.map(BukkitEventUtils::getBukkitEntity)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(ArrayList::new));
		List<org.bukkit.block.Block> affectedBlockList = affectedBlocks.stream()
				.map(pos -> toBukkitBlock(bWorld, pos))
				.collect(Collectors.toCollection(ArrayList::new));

		/* Call event */
		CustomExplosionEvent bukkitEvent = new CustomExplosionEvent(
				BukkitEventUtils.getBukkitEntity(exploder),
				new Location(bWorld, explosionX, explosionY, explosionZ),
				cause, affectedBlockList, affectedEntityList,
				isFlaming, 1.0F / explosionSize);
		Bukkit.getPluginManager().callEvent(bukkitEvent);

		/* Process event result */
		affectedBlocks.clear();
		affectedBlocks.addAll(bukkitEvent.getAffectedBlocks().stream()
				.map(b -> new ChunkPosition(b.getX(), b.getY(), b.getZ()))
				.collect(Collectors.toList()));

		entities.clear();
		entities.addAll(bukkitEvent.getAffectedEntities().stream()
				.map(BukkitEventUtils::toVanillaEntity)
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));

		if (bukkitEvent.isCancelled()) {
			wasCanceled = true;
			return;
		}

		damageEntities();

		/* Do explosion */
		if (explosionSize >= 2.0F && isSmoking) {
			worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
		} else {
			worldObj.spawnParticle("largeexplode", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
		}

		if (isSmoking) {
			for (ChunkPosition chunkposition : affectedBlocks) {
				int blockX = chunkposition.chunkPosX;
				int blockY = chunkposition.chunkPosY;
				int blockZ = chunkposition.chunkPosZ;
				Block block = worldObj.getBlock(blockX, blockY, blockZ);

				if (particles) {
					double x = (float)blockX + worldObj.rand.nextFloat();
					double y = (float)blockY + worldObj.rand.nextFloat();
					double z = (float)blockZ + worldObj.rand.nextFloat();
					double d3 = x - explosionX;
					double d4 = y - explosionY;
					double d5 = z - explosionZ;
					double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / (d6 / (double)explosionSize + 0.1D);
					d7 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					worldObj.spawnParticle("explode", (x + explosionX) / 2.0D, (y + explosionY) / 2.0D, (z + explosionZ) / 2.0D, d3, d4, d5);
					worldObj.spawnParticle("smoke", x, y, z, d3, d4, d5);
				}

				if (block.getMaterial() != Material.air) {
					if (block.canDropFromExplosion(this)) {
						block.dropBlockAsItemWithChance(worldObj, blockX, blockY, blockZ, worldObj.getBlockMetadata(blockX, blockY, blockZ), bukkitEvent.getYield(), 0);
					}

					block.onBlockExploded(worldObj, blockX, blockY, blockZ, this);
				}
			}
		}

		if (bukkitEvent.isFlaming()) {
			for (ChunkPosition chunkposition : affectedBlocks) {
				int blockX = chunkposition.chunkPosX;
				int blockY = chunkposition.chunkPosY;
				int blockZ = chunkposition.chunkPosZ;
				Block block = worldObj.getBlock(blockX, blockY, blockZ);
				Block lowerBlock = worldObj.getBlock(blockX, blockY - 1, blockZ);

				if (block.getMaterial() == Material.air && lowerBlock.func_149730_j() && worldObj.rand.nextInt(3) == 0
						&& callBlockIgniteEvent(blockX, blockY, blockZ)) {
					worldObj.setBlock(blockX, blockY, blockZ, Blocks.fire);
				}
			}
		}
	}

	/**
	 * Перенос в метод. Фрагмент из doExplosionA()
	 */
	private void damageEntities() {
		Vec3 vec3 = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);

		for (Entity entity : entities) {
			if (entity.isDead) continue; // Skip invalid
			double distance = entity.getDistance(explosionX, explosionY, explosionZ) / (double)explosionSizeEnt;

			if (distance <= 1.0D) {
				double x = entity.posX - explosionX;
				double y = entity.posY + (double)entity.getEyeHeight() - explosionY;
				double z = entity.posZ - explosionZ;
				double d9 = MathHelper.sqrt_double(x * x + y * y + z * z);

				if (d9 != 0.0D) {
					x /= d9;
					y /= d9;
					z /= d9;
					double density = worldObj.getBlockDensity(vec3, entity.boundingBox);
					double damage = (1.0D - distance) * density;
					entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float)((int)((damage * damage + damage) / 2.0D * 8.0D * (double)explosionSizeEnt + 1.0D)));
					double protection = EnchantmentProtection.func_92092_a(entity, damage);
					entity.motionX += x * protection;
					entity.motionY += y * protection;
					entity.motionZ += z * protection;

					if (entity instanceof EntityPlayerMP) {
						affectedPlayers.put((EntityPlayerMP)entity, Vec3.createVectorHelper(x * damage, y * damage, z * damage));
					}
				}
			}
		}

		entities.clear();
	}

	/**
	 * Список игроков затронутых взрывом. Используется для оправки пакетов клиентам.
	 * Является аналогом {@link Explosion#func_77277_b()}
	 */
	public Map<EntityPlayerMP, Vec3> getAffectedPlayers() {
		return affectedPlayers;
	}

	@Override
	public Map<EntityPlayerMP, Vec3> func_77277_b() {
		return affectedPlayers; // Для совместимости
	}

	/**
	 * @return Возвращает список с правильной сигнатурой
	 */
	@SuppressWarnings("unchecked")
	private List<ChunkPosition> getAffectedBlockPositions() {
		return affectedBlockPositions;
	}

	private boolean callBlockIgniteEvent(int x, int y, int z) {
		BlockIgniteEvent event = new BlockIgniteEvent(bWorld.getBlockAt(x, y, z), BlockIgniteEvent.IgniteCause.EXPLOSION, BukkitEventUtils.getBukkitEntity(exploder));
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	private static org.bukkit.block.Block toBukkitBlock(org.bukkit.World world, ChunkPosition pos) {
		return world.getBlockAt(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
	}
}
