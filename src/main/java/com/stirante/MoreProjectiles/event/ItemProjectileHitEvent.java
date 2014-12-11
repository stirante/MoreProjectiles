package com.stirante.MoreProjectiles.event;

import com.stirante.MoreProjectiles.projectile.CustomProjectile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * ItemProjectileHitEvent is fired when item projectile hits entity or block.
 */
public class ItemProjectileHitEvent extends CustomProjectileHitEvent {

    private final ItemStack item;

    /**
     * Instantiates a new item projectile hit event.
     *
     * @param pro  projectile
     * @param b    hit block
     * @param f    block face
     * @param item item
     */
    public ItemProjectileHitEvent(CustomProjectile pro, float damageMultiplier, Block b, BlockFace f, ItemStack item) {
        super(pro, damageMultiplier, b, f);
        this.item = item;
    }

    /**
     * Instantiates a new item projectile hit event.
     *
     * @param pro  projectile
     * @param ent  hit entity
     * @param item item
     */
    public ItemProjectileHitEvent(CustomProjectile pro, float damageMultiplier, LivingEntity ent, ItemStack item) {
        super(pro, damageMultiplier, ent);
        this.item = item;
    }

    /**
     * Gets the item stack.
     *
     * @return the item stack
     */
    public ItemStack getItemStack() {
        return item;
    }

}
