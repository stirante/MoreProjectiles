package com.stirante.MoreProjectiles.projectile;

import com.stirante.MoreProjectiles.TypedRunnable;
import com.stirante.MoreProjectiles.event.CustomProjectileHitEvent;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Projectile made from every possible entity passed as parameter. Entity is
 * moved in 1 tick scheduler.
 *
 * @author stirante
 */
public class ProjectileScheduler implements Runnable, IProjectile, CustomProjectile<ProjectileScheduler> {

    private final String name;
    private final EntityLiving shooter;
    private final Entity e;
    private final int id;
    private final List<Runnable> runnables = new ArrayList<>();
    private final List<TypedRunnable<ProjectileScheduler>> typedRunnables = new ArrayList<>();
    private Random random;
    private int age;
    private int knockback;
    private ArrayList<Material> ignoredMaterials = new ArrayList<>();
    private Field f;

    /**
     * Creates new scheduler projectile
     *
     * @param name    projectile name used in events
     * @param e       parent entity
     * @param shooter shooter
     * @param power   shoot power
     * @param plugin  plugin instance used to schedule task
     */
    public ProjectileScheduler(String name, org.bukkit.entity.Entity e, LivingEntity shooter, float power, Plugin plugin) {
        this.name = name;
        this.shooter = ((CraftLivingEntity) shooter).getHandle();
        this.e = ((CraftEntity) e).getHandle();
        try {
            Field f = Entity.class.getDeclaredField("random");
            f.setAccessible(true);
            random = (Random) f.get(this.e);
        } catch (SecurityException | IllegalAccessException | NoSuchFieldException t) {
            throw new RuntimeException(t);
        }
        this.e.setPositionRotation(shooter.getLocation().getX(), shooter.getLocation().getY(), shooter.getLocation().getZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
        this.e.locX -= (MathHelper.cos(this.e.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.e.locY += 1.5D;
        this.e.locZ -= (MathHelper.sin(this.e.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.e.setPosition(this.e.locX, this.e.locY, this.e.locZ);
        float f = 0.4F;
        this.e.motX = (-MathHelper.sin(this.e.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.e.pitch / 180.0F * 3.1415927F) * f);
        this.e.motZ = (MathHelper.cos(this.e.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.e.pitch / 180.0F * 3.1415927F) * f);
        this.e.motY = (-MathHelper.sin(this.e.pitch / 180.0F * 3.1415927F) * f);
        shoot(this.e.motX, this.e.motY, this.e.motZ, power * 1.5F, 1.0F);
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 1);
        try {
            this.f = getClass().getDeclaredField("invulnerable");
        } catch (NoSuchFieldException er) {
            er.printStackTrace();
        }
    }

    @Override
    public void run() {
        BlockPosition blockposition = new BlockPosition(e.locX, e.locY, e.locZ);
        IBlockData iblockdata = e.world.getType(blockposition);
        Block block = iblockdata.getBlock();

        if (!ignoredMaterials.contains(Material.getMaterial(Block.getId(block)))) {
            AxisAlignedBB axisalignedbb = block.a(e.world, blockposition, iblockdata);

            if ((axisalignedbb != null) && (axisalignedbb.a(new Vec3D(e.locX, e.locY, e.locZ)))) {
                float damageMultiplier = MathHelper.sqrt(e.motX * e.motX + e.motY * e.motY + e.motZ * e.motZ);
                CustomProjectileHitEvent event = new CustomProjectileHitEvent(this, damageMultiplier, e.world.getWorld().getBlockAt((int) e.locX, (int) e.locY, (int) e.locZ), BlockFace.UP);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    e.die();
                    Bukkit.getScheduler().cancelTask(id);
                }
            }
        }
        age += 1;
        Vec3D vec3d = new Vec3D(e.locX, e.locY, e.locZ);
        Vec3D vec3d1 = new Vec3D(e.locX + e.motX, e.locY + e.motY, e.locZ + e.motZ);
        MovingObjectPosition movingobjectposition = e.world.rayTrace(vec3d, vec3d1, false, true, false);

        vec3d = new Vec3D(e.locX, e.locY, e.locZ);
        vec3d1 = new Vec3D(e.locX + e.motX, e.locY + e.motY, e.locZ + e.motZ);
        if (movingobjectposition != null) {
            vec3d1 = new Vec3D(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
        }

        Entity entity = null;
        List list = e.world.getEntities(e, e.getBoundingBox().a(e.motX, e.motY, e.motZ).grow(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        for (Object aList : list) {
            Entity entity1 = (Entity) aList;

            if ((entity1.ad()) && ((entity1 != shooter) || (age >= 5))) {
                float f1 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.getBoundingBox().grow(f1, f1, f1);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.a(vec3d, vec3d1);

                if (movingobjectposition1 != null) {
                    double d1 = vec3d.distanceSquared(movingobjectposition1.pos);

                    if ((d1 < d0) || (d0 == 0.0D)) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if ((movingobjectposition != null) && (movingobjectposition.entity != null) && ((movingobjectposition.entity instanceof EntityHuman))) {
            EntityHuman entityhuman = (EntityHuman) movingobjectposition.entity;

            if ((entityhuman.abilities.isInvulnerable) || (((shooter instanceof EntityHuman)) && (!((EntityHuman) shooter).a(entityhuman)))) {
                movingobjectposition = null;
            }

        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entity != null && movingobjectposition.entity instanceof EntityLiving) {
                float damageMultiplier = MathHelper.sqrt(e.motX * e.motX + e.motY * e.motY + e.motZ * e.motZ);
                CustomProjectileHitEvent event = new CustomProjectileHitEvent(this, damageMultiplier, (LivingEntity) movingobjectposition.entity.getBukkitEntity());
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (getKnockback() > 0) {
                        float f4 = MathHelper.sqrt(e.motX * e.motX + e.motZ * e.motZ);
                        if (f4 > 0.0F) {
                            movingobjectposition.entity.g(e.motX * getKnockback() * 0.6000000238418579D / f4, 0.1D, e.motZ * getKnockback() * 0.6000000238418579D / f4);
                        }
                    }
                    e.die();
                    Bukkit.getScheduler().cancelTask(id);
                }
            } else if (movingobjectposition.a() != null) {
                if (!ignoredMaterials.contains(Material.getMaterial(Block.getId(block)))) {
                    e.motX = ((float) (movingobjectposition.pos.a - e.locX));
                    e.motY = ((float) (movingobjectposition.pos.b - e.locY));
                    e.motZ = ((float) (movingobjectposition.pos.c - e.locZ));
                    float f3 = MathHelper.sqrt(e.motX * e.motX + e.motY * e.motY + e.motZ * e.motZ);
                    e.locX -= e.motX / f3 * 0.0500000007450581D;
                    e.locY -= e.motY / f3 * 0.0500000007450581D;
                    e.locZ -= e.motZ / f3 * 0.0500000007450581D;
                    float damageMultiplier = MathHelper.sqrt(e.motX * e.motX + e.motY * e.motY + e.motZ * e.motZ);
                    CustomProjectileHitEvent event = new CustomProjectileHitEvent(this, damageMultiplier, e.world.getWorld().getBlockAt((int) movingobjectposition.pos.a, (int) movingobjectposition.pos.b, (int) movingobjectposition.pos.c), CraftBlock.notchToBlockFace(movingobjectposition.direction));
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        e.die();
                        Bukkit.getScheduler().cancelTask(id);
                    }
                }
            }
        }

        e.locX += e.motX;
        e.locY += e.motY;
        e.locZ += e.motZ;
        float f3 = 0.99F;
        float f1 = 0.05F;
        e.motX *= f3;
        e.motY *= f3;
        e.motZ *= f3;
        e.motY -= f1;
        e.setPosition(e.locX, e.locY, e.locZ);
        if (e.isAlive()) {
            if (this.age >= 1000) {
                e.die();
                Bukkit.getScheduler().cancelTask(id);
            }
            for (Runnable r : runnables) {
                r.run();
            }
            for (TypedRunnable<ProjectileScheduler> r : typedRunnables) {
                r.run(this);
            }
        } else {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    @Override
    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= f2;
        d1 /= f2;
        d2 /= f2;
        d0 += random.nextGaussian() * 0.007499999832361937D * f1;
        d1 += random.nextGaussian() * 0.007499999832361937D * f1;
        d2 += random.nextGaussian() * 0.007499999832361937D * f1;
        d0 *= f;
        d1 *= f;
        d2 *= f;
        e.motX = d0;
        e.motY = d1;
        e.motZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        e.lastYaw = e.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        e.lastPitch = e.pitch = (float) (Math.atan2(d1, f3) * 180.0D / 3.1415927410125732D);
    }

    @Override
    public EntityType getEntityType() {
        return e.getBukkitEntity().getType();
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return e.getBukkitEntity();
    }

    @Override
    public LivingEntity getShooter() {
        return (LivingEntity) shooter.getBukkitEntity();
    }

    @Override
    public String getProjectileName() {
        return name;
    }

    @Override
    public boolean isInvulnerable() {
        return getEntity().spigot().isInvulnerable();
    }

    @Override
    public void setInvulnerable(boolean value) {
        try {
            f.setAccessible(true);
            f.set(this, value);
        } catch (SecurityException | IllegalAccessException t) {
            t.printStackTrace();
        }
    }

    @Override
    public void addRunnable(Runnable r) {
        runnables.add(r);
    }

    @Override
    public void removeRunnable(Runnable r) {
        runnables.remove(r);
    }

    @Override
    public void addTypedRunnable(TypedRunnable<ProjectileScheduler> r) {
        typedRunnables.add(r);
    }

    @Override
    public void removeTypedRunnable(TypedRunnable<ProjectileScheduler> r) {
        typedRunnables.remove(r);
    }

    @Override
    public ArrayList<Material> getIgnoredBlocks() {
        return ignoredMaterials;
    }

    @Override
    public int getKnockback() {
        return knockback;
    }

    @Override
    public void setKnockback(int i) {
        knockback = i;
    }

}
