package theandrey.bukkit.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import theandrey.bukkit.event.api.IExplosionEvent;

/**
 * Используется для взрывов
 */
public abstract class CustomExplosion {

	public boolean isFlaming = false;
	public boolean isSmoking = true;
	private final int field_77289_h = 16;
	private final Random explosionRNG = new Random();
	private final World worldObj;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public Entity exploder;
	public float explosionSize;
	public final List<ChunkPosition> affectedBlockPositions = new ArrayList<>();
	public boolean wasCanceled = false;
	private final Explosion classicExplosion; // некоторые методы требуют этот объект
	private final ChunkPosition explodedBlock;
	private final String ownerUsername;

	/**
	 * @param world Мир
	 * @param explodedEntity Entity, который вызвал взрыв, если нет - null
	 * @param explodedBlock Координаты блока, который вызвал взрыв, если нет - null
	 * @param explosionX Точка X взрыва
	 * @param explosionY Точка Y взрыва
	 * @param explosionZ Точка Z взрыва
	 * @param explosionSize Радиус взрыва
	 * @param ownerUsername Имя владельца взорвавшегося Entity/блока. null - если неизвестно.
	 */
	public CustomExplosion(World world, Entity explodedEntity, ChunkPosition explodedBlock, double explosionX, double explosionY, double explosionZ, float explosionSize, String ownerUsername) {
		this.worldObj = world;
		this.exploder = explodedEntity;
		this.explodedBlock = explodedBlock;
		this.explosionSize = (float)Math.max((double)explosionSize, 0.0D);
		this.explosionX = explosionX;
		this.explosionY = explosionY;
		this.explosionZ = explosionZ;
		classicExplosion = new Explosion(world, explodedEntity, explosionX, explosionY, explosionZ, explosionSize);
		this.ownerUsername = ownerUsername;
	}

	/**
	 * Запускает взрыв
	 */
	public void doExplosion() {
		doExplosionA();
		doExplosionB(true);
	}

	protected abstract Event getBukkitEvent(Location explosionPoint, org.bukkit.entity.Entity explodedEntity, org.bukkit.block.Block explodedBlock, List<org.bukkit.block.Block> blocks);

	protected void doExplosionA() {
		if(this.explosionSize >= 0.1F) {
			float f = this.explosionSize;
			HashSet<ChunkPosition> hashset = new HashSet<>();

			for(int i = 0; i < this.field_77289_h; ++i) {
				for(int j = 0; j < this.field_77289_h; ++j) {
					for(int k = 0; k < this.field_77289_h; ++k) {
						if(i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1) {
							double d3 = (double)((float)i / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
							double d4 = (double)((float)j / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
							double d5 = (double)((float)k / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
							double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
							d3 /= d6;
							d4 /= d6;
							d5 /= d6;
							float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
							double d0 = this.explosionX;
							double d1 = this.explosionY;
							double d2 = this.explosionZ;

							for(float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
								int l = MathHelper.floor_double(d0);
								int i1 = MathHelper.floor_double(d1);
								int j1 = MathHelper.floor_double(d2);
								int k1 = this.worldObj.getBlockId(l, i1, j1);
								if(k1 > 0) {
									Block block = Block.blocksList[k1];
									float damagee = this.exploder != null ? this.exploder.func_82146_a(classicExplosion, block, l, i1, j1) : block.getExplosionResistance(this.exploder);
									f1 -= (damagee + 0.3F) * f2;
								}

								if(f1 > 0.0F && i1 < 256 && i1 >= 0) hashset.add(new ChunkPosition(l, i1, j1));

								d0 += d3 * (double)f2;
								d1 += d4 * (double)f2;
								d2 += d5 * (double)f2;
							}
						}
					}
				}
			}

			this.affectedBlockPositions.addAll(hashset);
			this.explosionSize *= 2.0F;
			int i = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - 1.0D);
			int j = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + 1.0D);
			int k = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - 1.0D);
			int l1 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + 1.0D);
			int i2 = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - 1.0D);
			int j2 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + 1.0D);
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)i, (double)k, (double)i2, (double)j, (double)l1, (double)j2));
			Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.explosionX, this.explosionY, this.explosionZ);

			for(Entity entity : list) {
				double d7 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;
				if(d7 <= 1.0D) {
					double d0 = entity.posX - this.explosionX;
					double d1 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
					double d2 = entity.posZ - this.explosionZ;
					double d8 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
					if(d8 != 0.0D) {
						d0 /= d8;
						d1 /= d8;
						d2 /= d8;
						double d9 = (double)this.worldObj.getBlockDensity(vec3, entity.boundingBox);
						double d10 = (1.0D - d7) * d9;
						org.bukkit.entity.Entity bukkkitEntity = BukkitEventUtils.getBukkitEntity(entity);
						int damageDone = (int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)this.explosionSize + 1.0D);
						if(bukkkitEntity != null) {
							if(this.exploder == null) {
								org.bukkit.block.Block bblock = (explodedBlock != null) ? BukkitEventUtils.getBlock(worldObj, explodedBlock.x, explodedBlock.y, explodedBlock.z) : null;
								EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(bblock, bukkkitEntity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, damageDone);
								Bukkit.getPluginManager().callEvent(event);
								if(!event.isCancelled()) {
									bukkkitEntity.setLastDamageCause(event);
									entity.attackEntityFrom(DamageSource.explosion, event.getDamage());
									double d11 = EnchantmentProtection.func_92092_a(entity, d10);
									entity.motionX += d0 * d11;
									entity.motionY += d1 * d11;
									entity.motionZ += d2 * d11;
								}
							} else {
								org.bukkit.entity.Entity bukkitExploder = BukkitEventUtils.getBukkitEntity(exploder);
								EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(bukkitExploder, bukkkitEntity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damageDone);
								Bukkit.getPluginManager().callEvent(event);
								if(!event.isCancelled()) {
									bukkkitEntity.setLastDamageCause(event);
									entity.attackEntityFrom(DamageSource.explosion, event.getDamage());
									entity.motionX += d0 * d10;
									entity.motionY += d1 * d10;
									entity.motionZ += d2 * d10;
								}
							}
						}
					}
				}
			}

			this.explosionSize = f;
		}
	}

	protected void doExplosionB(boolean par1) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		if(this.explosionSize >= 2.0F && this.isSmoking) {
			this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
		} else {
			this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
		}

		if(this.isSmoking) {
			org.bukkit.World bukkitWorld = BukkitEventUtils.getWorld(worldObj);
			org.bukkit.entity.Entity bukkitExploder = (this.exploder == null) ? null : BukkitEventUtils.getBukkitEntity(exploder);
			org.bukkit.block.Block bukkitBlock = (explodedBlock != null) ? bukkitWorld.getBlockAt(explodedBlock.x, explodedBlock.y, explodedBlock.z) : null;
			Location location = new Location(bukkitWorld, this.explosionX, this.explosionY, this.explosionZ);
			ArrayList<org.bukkit.block.Block> blockList = new ArrayList<>();

			for(int i = affectedBlockPositions.size() - 1; i >= 0; --i) {
				ChunkPosition block = this.affectedBlockPositions.get(i);
				org.bukkit.block.Block block1 = bukkitWorld.getBlockAt(block.x, block.y, block.z);
				if(block1.getType() != Material.AIR) blockList.add(block1);
			}

			Event bukkitEvent = getBukkitEvent(location, bukkitExploder, bukkitBlock, blockList);
			if(bukkitEvent == null) throw new NullPointerException("Получен null Event");
			if(!(bukkitEvent instanceof IExplosionEvent)) throw new RuntimeException(bukkitEvent.toString() + " не реализует " + IExplosionEvent.class.getName());
			Bukkit.getServer().getPluginManager().callEvent(bukkitEvent);
			this.affectedBlockPositions.clear();

			for(org.bukkit.block.Block bblock : ((IExplosionEvent)bukkitEvent).getBlocks()) {
				ChunkPosition coords = new ChunkPosition(bblock.getX(), bblock.getY(), bblock.getZ());
				this.affectedBlockPositions.add(coords);
			}

			if((bukkitEvent instanceof Cancellable) && ((Cancellable)bukkitEvent).isCancelled()) {
				this.wasCanceled = true;
				return;
			}

			for(ChunkPosition chunkposition : affectedBlockPositions) {
				int i = chunkposition.x;
				int j = chunkposition.y;
				int k = chunkposition.z;
				int l = this.worldObj.getBlockId(i, j, k);
				if(par1) {
					double d0 = (double)((float)i + this.worldObj.rand.nextFloat());
					double d1 = (double)((float)j + this.worldObj.rand.nextFloat());
					double d2 = (double)((float)k + this.worldObj.rand.nextFloat());
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
					d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					this.worldObj.spawnParticle("explode", (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
					this.worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
				}

				if(l > 0 && l != Block.fire.blockID) {
					Block var35 = Block.blocksList[l];
					if(var35.canDropFromExplosion(classicExplosion)) var35.dropBlockAsItemWithChance(this.worldObj, i, j, k, this.worldObj.getBlockMetadata(i, j, k), 0.3F, 0);

					if(this.worldObj.setBlockAndMetadataWithUpdate(i, j, k, 0, 0, this.worldObj.isRemote)) {
						this.worldObj.notifyBlocksOfNeighborChange(i, j, k, 0);
					}

					var35.onBlockDestroyedByExplosion(this.worldObj, i, j, k);
				}
			}
		}

		if(this.isFlaming) {
			for(ChunkPosition chunkposition : affectedBlockPositions) {
				int i = chunkposition.x;
				int j = chunkposition.y;
				int k = chunkposition.z;
				int l = this.worldObj.getBlockId(i, j, k);
				int var32 = this.worldObj.getBlockId(i, j - 1, k);
				if(l == 0 && Block.opaqueCubeLookup[var32] && this.explosionRNG.nextInt(3) == 0) {
					this.worldObj.setBlockWithNotify(i, j, k, Block.fire.blockID);
				}
			}
		}

	}

}
