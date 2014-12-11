package com.stirante.MoreProjectiles.projectile;

import com.stirante.MoreProjectiles.TypedRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Custom projectile interface.
 */
public interface CustomProjectile {

    /**
     * Gets the entity type.
     *
     * @return entity type of projectile
     */
    public EntityType getEntityType();

    /**
     * Gets the entity.
     *
     * @return the entity
     */
    public Entity getEntity();

    /**
     * Gets the shooter.
     *
     * @return the shooter
     */
    public LivingEntity getShooter();

    /**
     * Gets the projectile name.
     *
     * @return the projectile name
     */
    public String getProjectileName();

    /**
     * Sets entity invulnerable.
     *
     * @param value invulnerable state
     */
    public void setInvulnerable(boolean value);

    /**
     * Checks if entity is invulnerable.
     *
     * @return true, if entity is invulnerable
     */
    public boolean isInvulnerable();

    /**
     * Adds the runnable.
     *
     * @param r runnable
     */
    public void addRunnable(Runnable r);

    /**
     * Removes the runnable.
     *
     * @param r runnable
     */
    public void removeRunnable(Runnable r);

    /**
     * Adds the typed runnable.
     *
     * @param r runnable
     */
    public void addTypedRunnable(TypedRunnable<? extends CustomProjectile> r);

    /**
     * Removes the typed runnable.
     *
     * @param r runnable
     */
    public void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> r);

    /**
     * Checks if is ignoring blocks like water.
     *
     * @return true, if is ignoring blocks like water.
     */
    public boolean isIgnoringSomeBlocks();

    /**
     * Sets if is ignoring blocks like water.
     *
     * @param ignoreSomeBlocks is ignoring blocks.
     */
    public void setIgnoreSomeBlocks(boolean ignoreSomeBlocks);

    /**
     * Sets if is ignoring blocks like water.
     *
     * @param i knockback level
     */
    public void setKnockback(int i);

    /**
     * Sets if is ignoring blocks like water.
     *
     * @return knockback level
     */
    public int getKnockback();

}
