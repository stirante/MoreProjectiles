package com.stirante.MoreProjectiles.projectile;

import com.stirante.MoreProjectiles.Particles;
import com.stirante.MoreProjectiles.TypedRunnable;
import com.stirante.MoreProjectiles.event.CustomProjectileHitEvent;
import com.stirante.MoreProjectiles.event.ItemProjectileHitEvent;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Projectile made from item entity
 */
public class ItemProjectile extends EntityItem implements IProjectile, CustomProjectile {

    private final EntityLiving shooter;
    private final String name;
    private final List<Runnable> runnables = new ArrayList<>();
    private final List<TypedRunnable<ItemProjectile>> typedRunnables = new ArrayList<>();
    private boolean ignoreSomeBlocks = false;
    private int knockback;
    private int age;

    /**
     * Instantiates a new item projectile.
     *
     * @param name      projectile name
     * @param loc       location of projectile (sets position of projectile and shoots in pitch
     *                  and yaw direction)
     * @param itemstack item stack to shoot
     * @param shooter   projectile shooter
     * @param power     projectile power
     */
    @SuppressWarnings("deprecation")
    public ItemProjectile(String name, Location loc, org.bukkit.inventory.ItemStack itemstack, LivingEntity shooter, float power) {
        super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), null);
        if (CraftItemStack.asNMSCopy(itemstack) != null) setItemStack(CraftItemStack.asNMSCopy(itemstack));
        else
            setItemStack(new net.minecraft.server.v1_8_R1.ItemStack(Item.getById(itemstack.getTypeId()), itemstack.getAmount(), itemstack.getData().getData()));
        if (itemstack.getTypeId() == 0) System.out.println("You cannot shoot air!");
        this.name = name;
        this.pickupDelay = Integer.MAX_VALUE;
        this.shooter = ((CraftLivingEntity) shooter).getHandle();
        this.a(0.25F, 0.25F);
        setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        locX -= (MathHelper.cos(yaw / 180.0F * 3.1415927F) * 0.16F);
        locY -= 0.10000000149011612D;
        locZ -= (MathHelper.sin(yaw / 180.0F * 3.1415927F) * 0.16F);
        setPosition(locX, locY, locZ);
        float f = 0.4F;
        motX = (-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * f);
        motZ = (MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * f);
        motY = (-MathHelper.sin(pitch / 180.0F * 3.1415927F) * f);
        shoot(motX, motY, motZ, power * 1.5F, 1.0F);
        world.addEntity(this);
    }

    /**
     * Instantiates a new item projectile.
     *
     * @param name    projectile name
     * @param shooter projectile shooter (it uses entity's location to set x, y, z, pitch and
     *                yaw of projectile)
     * @param item    item stack to shoot
     * @param power   projectile power
     */
    public ItemProjectile(String name, LivingEntity shooter, org.bukkit.inventory.ItemStack item, float power) {
        super(((CraftLivingEntity) shooter).getHandle().world);
        this.name = name;
        this.pickupDelay = Integer.MAX_VALUE;
        setItemStack(CraftItemStack.asNMSCopy(item));
        this.shooter = ((CraftLivingEntity) shooter).getHandle();
        this.a(0.25F, 0.25F);
        setPositionRotation(shooter.getLocation().getX(), shooter.getLocation().getY() + shooter.getEyeHeight(), shooter.getLocation().getZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
        locX -= (MathHelper.cos(yaw / 180.0F * 3.1415927F) * 0.16F);
        locY -= 0.10000000149011612D;
        locZ -= (MathHelper.sin(yaw / 180.0F * 3.1415927F) * 0.16F);
        setPosition(locX, locY, locZ);
        float f = 0.4F;
        motX = (-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * f);
        motZ = (MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * f);
        motY = (-MathHelper.sin(pitch / 180.0F * 3.1415927F) * f);
        shoot(motX, motY, motZ, power * 1.5F, 1.0F);
        world.addEntity(this);
    }

    @Override
    public void s_() {
        K();
        BlockPosition blockposition = new BlockPosition(locX, locY, locZ);
        IBlockData iblockdata = world.getType(blockposition);
        Block block = iblockdata.getBlock();

        if (!isIgnored(Material.getMaterial(Block.getId(block)))) {
            AxisAlignedBB axisalignedbb = block.a(world, blockposition, iblockdata);

            if ((axisalignedbb != null) && (axisalignedbb.a(new Vec3D(locX, locY, locZ)))) {
                float damageMultiplier = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                CustomProjectileHitEvent event = new ItemProjectileHitEvent(this, damageMultiplier, world.getWorld().getBlockAt((int) locX, (int) locY, (int) locZ), BlockFace.UP, getItem());
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock())
                        Particles.displayBlockCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), getItem().getData().getData(), 0F, 0F, 0F, 1F, 20);
                    else
                        Particles.displayIconCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), 0F, 0F, 0F, 0.1F, 20);
                    die();
                }
            }
        }
        age += 1;
        Vec3D vec3d = new Vec3D(locX, locY, locZ);
        Vec3D vec3d1 = new Vec3D(locX + motX, locY + motY, locZ + motZ);
        MovingObjectPosition movingobjectposition = world.rayTrace(vec3d, vec3d1, false, true, false);

        vec3d = new Vec3D(locX, locY, locZ);
        vec3d1 = new Vec3D(locX + motX, locY + motY, locZ + motZ);
        if (movingobjectposition != null) {
            vec3d1 = new Vec3D(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
        }

        Entity entity = null;
        List list = world.getEntities(this, getBoundingBox().a(motX, motY, motZ).grow(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

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
                float damageMultiplier = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                CustomProjectileHitEvent event = new ItemProjectileHitEvent(this, damageMultiplier, (LivingEntity) movingobjectposition.entity.getBukkitEntity(), getItem());
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (getKnockback() > 0) {
                        float f4 = MathHelper.sqrt(motX * motX + motZ * motZ);
                        if (f4 > 0.0F) {
                            movingobjectposition.entity.g(motX * getKnockback() * 0.6000000238418579D / f4, 0.1D, motZ * getKnockback() * 0.6000000238418579D / f4);
                        }
                    }
                    if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock())
                        Particles.displayBlockCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), getItem().getData().getData(), 0F, 0F, 0F, 1F, 20);
                    else
                        Particles.displayIconCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), 0F, 0F, 0F, 0.1F, 20);
                    die();
                }
            } else if (movingobjectposition.a() != null) {
                if (!isIgnored(Material.getMaterial(Block.getId(block)))) {
                    motX = ((float) (movingobjectposition.pos.a - locX));
                    motY = ((float) (movingobjectposition.pos.b - locY));
                    motZ = ((float) (movingobjectposition.pos.c - locZ));
                    float f3 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                    locX -= motX / f3 * 0.0500000007450581D;
                    locY -= motY / f3 * 0.0500000007450581D;
                    locZ -= motZ / f3 * 0.0500000007450581D;
                    float damageMultiplier = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                    CustomProjectileHitEvent event = new ItemProjectileHitEvent(this, damageMultiplier, world.getWorld().getBlockAt((int) movingobjectposition.pos.a, (int) movingobjectposition.pos.b, (int) movingobjectposition.pos.c), CraftBlock.notchToBlockFace(movingobjectposition.direction), getItem());
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock())
                            Particles.displayBlockCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), getItem().getData().getData(), 0F, 0F, 0F, 1F, 20);
                        else
                            Particles.displayIconCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), 0F, 0F, 0F, 0.1F, 20);
                        die();
                    }
                }
            }
        }

        locX += motX;
        locY += motY;
        locZ += motZ;
        float f3 = 0.99F;
        float f1 = 0.05F;
        motX *= f3;
        motY *= f3;
        motZ *= f3;
        motY -= f1;
        setPosition(locX, locY, locZ);
        checkBlockCollisions();
        if (isAlive()) {
            if (this.age >= 1000) {
                die();
            }
            for (Runnable r : runnables) {
                r.run();
            }
            for (TypedRunnable<ItemProjectile> r : typedRunnables) {
                r.run(this);
            }
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
        motX = d0;
        motY = d1;
        motZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        lastYaw = yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        lastPitch = pitch = (float) (Math.atan2(d1, f3) * 180.0D / 3.1415927410125732D);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.DROPPED_ITEM;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public LivingEntity getShooter() {
        return (LivingEntity) shooter.getBukkitEntity();
    }

    @Override
    public void d(EntityHuman entityhuman) {
        if (entityhuman == shooter && age <= 3) return;
        LivingEntity living = entityhuman.getBukkitEntity();
        CustomProjectileHitEvent event = new ItemProjectileHitEvent(this, 0.5F, living, getItem());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock())
                Particles.displayBlockCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), getItem().getData().getData(), 0F, 0F, 0F, 1F, 20);
            else
                Particles.displayIconCrack(getBukkitEntity().getLocation(), getItem().getTypeId(), 0F, 0F, 0F, 0.1F, 20);
            die();
        }
    }

    @Override
    public String getProjectileName() {
        return name;
    }

    /**
     * Gets the item.
     *
     * @return the item
     */
    public ItemStack getItem() {
        return CraftItemStack.asCraftMirror(getItemStack());
    }

    @Override
    public void setInvulnerable(boolean value) {
        try {
            Field f = getClass().getDeclaredField("invulnerable");
            f.setAccessible(true);
            f.set(this, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public boolean isInvulnerable() {
        try {
            Field f = getClass().getDeclaredField("invulnerable");
            f.setAccessible(true);
            return f.getBoolean(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    @Override
    public void addRunnable(Runnable r) {
        runnables.add(r);
    }

    @Override
    public void removeRunnable(Runnable r) {
        runnables.remove(r);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
        typedRunnables.add((TypedRunnable<ItemProjectile>) r);
    }

    @Override
    public void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
        typedRunnables.remove(r);
    }

    @Override
    public net.minecraft.server.v1_8_R1.ItemStack getItemStack() {
        net.minecraft.server.v1_8_R1.ItemStack itemstack = getDataWatcher().getItemStack(10);

        if (itemstack == null) {
            return new net.minecraft.server.v1_8_R1.ItemStack(Blocks.STONE);
        }
        return itemstack;
    }

    private boolean isIgnored(Material m) {
        if (!isIgnoringSomeBlocks()) return false;
        switch (m) {
            case AIR:
            case GRASS:
            case DOUBLE_PLANT:
            case CROPS:
            case CARROT:
            case POTATO:
            case SUGAR_CANE_BLOCK:
            case DEAD_BUSH:
            case LONG_GRASS:
            case WATER:
            case STATIONARY_WATER:
            case SAPLING:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isIgnoringSomeBlocks() {
        return ignoreSomeBlocks;
    }

    @Override
    public void setIgnoreSomeBlocks(boolean ignoreSomeBlocks) {
        this.ignoreSomeBlocks = ignoreSomeBlocks;
    }

    @Override
    public void setKnockback(int i) {
        knockback = i;
    }

    @Override
    public int getKnockback() {
        return knockback;
    }

}