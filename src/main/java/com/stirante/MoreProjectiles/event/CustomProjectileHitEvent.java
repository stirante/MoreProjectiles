package com.stirante.MoreProjectiles.event;

import com.stirante.MoreProjectiles.projectile.CustomProjectile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * CustomProjectileHitEvent is fired when custom projectile hits entity or
 * block.
 */
public class CustomProjectileHitEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final CustomProjectile projectile;
    private final float damageMultiplier;
    private LivingEntity entity;
    private Block block;
    private BlockFace face;
    private boolean cancelled;

    /**
     * Instantiates a new custom projectile hit event.
     *
     * @param pro projectile
     * @param ent hit entity
     */
    public CustomProjectileHitEvent(CustomProjectile pro, float damageMultiplier, LivingEntity ent) {
        projectile = pro;
        entity = ent;
        this.damageMultiplier = damageMultiplier;
    }

    /**
     * Instantiates a new custom projectile hit event.
     *
     * @param pro projectile
     * @param b   hit block
     * @param f   block face
     */
    public CustomProjectileHitEvent(CustomProjectile pro, float damageMultiplier, Block b, BlockFace f) {
        projectile = pro;
        block = b;
        face = f;
        this.damageMultiplier = damageMultiplier;
    }

    /**
     * Gets the handler list.
     *
     * @return handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the projectile.
     *
     * @return projectile
     */
    public CustomProjectile getProjectile() {
        return projectile;
    }

    /**
     * Gets the damage multiplier.
     *
     * @return damage multiplier
     */
    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    /**
     * Gets hit entity.
     *
     * @return hit entity
     */
    public LivingEntity getHitEntity() {
        return entity;
    }

    /**
     * Gets hit block.
     *
     * @return hit block
     */
    public Block getHitBlock() {
        return block;
    }

    /**
     * Gets hit face.
     *
     * @return hit face
     */
    public BlockFace getHitFace() {
        return face;
    }

    /**
     * Gets the hit type.
     *
     * @return hit type
     */
    public HitType getHitType() {
        if (block == null && entity != null) return HitType.ENTITY;
        else if (block != null && entity == null && face != null) return HitType.BLOCK;
        else return null;
    }

    /**
     * Gets the projectile entity type.
     *
     * @return projectile entity type
     */
    public EntityType getProjectileType() {
        return getProjectile().getEntityType();
    }

    @Override
    public String toString() {
        if (getHitType() == HitType.ENTITY)
            return "{" + getClass().getName() + " projectile:" + projectile.toString() + ", hit entity:" + entity.toString() + "}";
        else if (getHitType() == HitType.BLOCK)
            return "{" + getClass().getName() + " projectile:" + projectile.toString() + ", hit block:" + block.toString() + ", face hit:" + face.toString() + "}";
        else return getClass().getName();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    /**
     * The Enum HitType.
     */
    public enum HitType {

        ENTITY, BLOCK
    }
}