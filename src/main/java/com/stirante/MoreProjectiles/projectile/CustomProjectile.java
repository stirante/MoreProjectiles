package com.stirante.MoreProjectiles.projectile;

import com.stirante.MoreProjectiles.TypedRunnable;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

/**
 * Custom projectile interface.
 */
public interface CustomProjectile<T> {

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
     * Checks if entity is invulnerable.
     *
     * @return true, if entity is invulnerable
     */
    public boolean isInvulnerable();

    /**
     * Sets entity invulnerable.
     *
     * @param value invulnerable state
     */
    public void setInvulnerable(boolean value);

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
    public void addTypedRunnable(TypedRunnable<T> r);

    /**
     * Removes the typed runnable.
     *
     * @param r runnable
     */
    public void removeTypedRunnable(TypedRunnable<T> r);

    /**
     * Sets if is ignoring blocks like water.
     *
     * @return return modifiable list of ignored materials
     */
    public ArrayList<Material> getIgnoredBlocks();

    /**
     * Sets if is ignoring blocks like water.
     *
     * @return knockback level
     */
    public int getKnockback();

    /**
     * Sets if is ignoring blocks like water.
     *
     * @param i knockback level
     */
    public void setKnockback(int i);

}
