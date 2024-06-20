package me.theandrey.bukkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.theandrey.bukkit.api.ExplosionCause;
import me.theandrey.bukkit.event.CustomExplosionEvent;
import me.theandrey.bukkit.util.Bukkit2Vanilla;
import me.theandrey.bukkit.util.BukkitEventUtils;
import me.theandrey.bukkit.util.Vanilla2Bukkit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.block.BlockIgniteEvent;

public class CustomExplosion extends Explosion {

	private final org.bukkit.World bWorld;
	private float explosionSizeEnt;

	/** Список существ затронутых взрывом */
	public final List<Entity> damagedEntities = new ArrayList<>();

	/** Причина взрыва. Это поле должно быть обязательно заполнено */
	public ExplosionCause cause;

	/** Флаг отмены ивента взрыва плагинами */
	public boolean wasCanceled = false;

	/**
	 * @param world Мир
	 * @param exploder Существо, которое вызвало взрыв. Может быть null
	 * @param x Точка эпицентра
	 * @param y Точка эпицентра
	 * @param z Точка эпицентра
	 * @param size Мощность взрыва
	 */
	public CustomExplosion(World world, @Nullable Entity exploder, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
		super(world, exploder, x, y, z, size, causesFire, damagesTerrain);
		this.bWorld = Vanilla2Bukkit.getWorld(world);
	}

	/**
	 * Does the first part of the explosion (calculate)
	 */
	@Override
	public void doExplosionA() {
		HashSet<BlockPos> blockPositions = new HashSet<>();

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 16; k++) {
					if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {

						double d0 = (float)i / 15.0f * 2.0F - 1.0F;
						double d1 = (float)j / 15.0f * 2.0F - 1.0F;
						double d2 = (float)k / 15.0f * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f = this.size * (0.7F + world.rand.nextFloat() * 0.6F);
						double x = this.x;
						double y = this.y;
						double z = this.z;

						for (float f1 = 0.3F; f > 0.0F; f -= f1 * 0.75F) {
							BlockPos pos = new BlockPos(x, y, z);
							IBlockState state = world.getBlockState(pos);

							if (state.getMaterial() != Material.AIR) {
								float f2 = exploder != null
									? exploder.getExplosionResistance(this, world, pos, state)
									: state.getBlock().getExplosionResistance(world, pos, null, this);
								f -= (f2 + 0.3F) * f;
							}

							if (f > 0.0F && (exploder == null || exploder.canExplosionDestroyBlock(this, world, pos, state, f))) {
								blockPositions.add(pos);
							}

							x += d0 * (double)f1;
							y += d1 * (double)f1;
							z += d2 * (double)f1;
						}
					}
				}
			}
		}

		affectedBlockPositions.addAll(blockPositions);

		float f3 = this.size * 2.0F;
		int x1 = MathHelper.floor(this.x - (double)f3 - 1.0D);
		int x2 = MathHelper.floor(this.x + (double)f3 + 1.0D);
		int y1 = MathHelper.floor(this.y - (double)f3 - 1.0D);
		int y2 = MathHelper.floor(this.y + (double)f3 + 1.0D);
		int z1 = MathHelper.floor(this.z - (double)f3 - 1.0D);
		int z2 = MathHelper.floor(this.z + (double)f3 + 1.0D);

		damagedEntities.clear();
		damagedEntities.addAll(world.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB(x1, y1, z1, x2, y2, z2)));
		ForgeEventFactory.onExplosionDetonate(world, this, damagedEntities, f3);
		explosionSizeEnt = f3; // Сохраняем, чтобы использовать в дальнейшем
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 * @param spawnParticles Spawn particles
	 */
	@Override
	public void doExplosionB(boolean spawnParticles) {
		world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

		List<BlockPos> affectedBlocks = getAffectedBlockPositions();

		/* Send bukkit event */
		List<org.bukkit.entity.Entity> affectedEntityList = damagedEntities.stream()
			.map(Vanilla2Bukkit::getBukkitEntity)
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(ArrayList::new));

		List<org.bukkit.block.Block> affectedBlockList = affectedBlocks.stream()
			.map(this::toBukkitBlock)
			.collect(Collectors.toCollection(ArrayList::new));

		/* Call event */
		CustomExplosionEvent bukkitEvent = new CustomExplosionEvent(
			Vanilla2Bukkit.getBukkitEntity(exploder),
			new Location(bWorld, this.x, this.y, this.z),
			cause, affectedBlockList, affectedEntityList,
			causesFire, 1.0F / this.size);

		Bukkit.getPluginManager().callEvent(bukkitEvent);

		/* Process event result */
		affectedBlocks.clear();
		affectedBlocks.addAll(bukkitEvent.getAffectedBlocks().stream()
			.map(Bukkit2Vanilla::toBlockPos)
			.collect(Collectors.toSet()));

		damagedEntities.clear();
		damagedEntities.addAll(bukkitEvent.getAffectedEntities().stream()
			.map(Bukkit2Vanilla::getEntity)
			.filter(Objects::nonNull)
			.collect(Collectors.toList()));

		if (bukkitEvent.isCancelled()) {
			wasCanceled = true;
			return;
		}

		damageEntities();

		/* Do explosion */
		if (this.size >= 2.0F && this.damagesTerrain) {
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
		} else {
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
		}

		if (this.damagesTerrain) {
			for (BlockPos pos : affectedBlocks) {
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();

				if (spawnParticles) {
					double x = (float)pos.getX() + world.rand.nextFloat();
					double y = (float)pos.getY() + world.rand.nextFloat();
					double z = (float)pos.getZ() + world.rand.nextFloat();
					double d3 = x - this.x;
					double d4 = y - this.y;
					double d5 = z - this.z;
					double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / (d6 / (double)this.size + 0.1D);
					d7 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (x + this.z) / 2.0D, (y + this.y) / 2.0D, (z + this.z) / 2.0D, d3, d4, d5);
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, d3, d4, d5);
				}

				if (state.getMaterial() != Material.AIR) {
					if (block.canDropFromExplosion(this)) {
						block.dropBlockAsItemWithChance(world, pos, world.getBlockState(pos), bukkitEvent.getYield(), 0);
					}

					block.onBlockExploded(world, pos, this);
				}
			}
		}

		if (bukkitEvent.isFlaming()) {
			for (BlockPos pos : affectedBlocks) {
				IBlockState state = world.getBlockState(pos);
				IBlockState lowerState = world.getBlockState(pos.down());

				if (state.getMaterial() == Material.AIR && lowerState.isFullBlock() && world.rand.nextInt(3) == 0 && callBlockIgniteEvent(pos)) {
					world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}

	/**
	 * Перенос в метод. Фрагмент из doExplosionA()
	 */
	private void damageEntities() {
		Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

		for (Entity entity : damagedEntities) {
			if (entity.isDead || entity.isImmuneToExplosions()) continue;

			double distance = entity.getDistance(this.x, this.y, this.z) / (double)explosionSizeEnt;

			if (distance <= 1.0D) {
				double x = entity.posX - -this.x;
				double y = entity.posY + (double)entity.getEyeHeight() - -this.y;
				double z = entity.posZ - -this.z;
				double d13 = MathHelper.sqrt(x * x + y * y + z * z);

				if (d13 != 0.0D) {
					x /= d13;
					y /= d13;
					z /= d13;
					double density = world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
					double damage = (1.0D - distance) * density;

					BukkitEventUtils.setEntityDamage(exploder);
					entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)(int)((damage * damage + damage) / 2.0D * 8.0D * (double)explosionSizeEnt + 1.0D));
					BukkitEventUtils.setEntityDamage(null);

					double d11 = damage;
					if (entity instanceof EntityLivingBase) {
						d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, damage);
					}

					entity.motionX += x * d11;
					entity.motionY += y * d11;
					entity.motionZ += z * d11;

					if (entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)entity;

						if (!player.isSpectator() && (!player.isCreative() || !player.capabilities.isFlying)) {
							playerKnockbackMap.put(player, new Vec3d(x * damage, y * damage, z * damage));
						}
					}
				}
			}
		}

		damagedEntities.clear();
	}

	private boolean callBlockIgniteEvent(BlockPos pos) {
		BlockIgniteEvent event = new BlockIgniteEvent(bWorld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()), BlockIgniteEvent.IgniteCause.EXPLOSION, Vanilla2Bukkit.getBukkitEntity(exploder));
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	private org.bukkit.block.Block toBukkitBlock(BlockPos pos) {
		return bWorld.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
	}
}
