package com.stirante.MoreProjectiles;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The Enum Particles. Contains a way to display every particle in the game.
 */
public enum Particles {

    /**
     * The huge explosion particles(s).
     */
    HUGE_EXPLOSION(EnumParticle.EXPLOSION_HUGE),

    /**
     * The large explode particles(s).
     */
    LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE),

    /**
     * The fireworks spark particles(s).
     */
    FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK),

    /**
     * The bubble particles(s).
     */
    BUBBLE(EnumParticle.WATER_BUBBLE),

    /**
     * The suspend particles(s).
     */
    SUSPEND(EnumParticle.SUSPENDED),

    /**
     * The depth suspend particles(s).
     */
    DEPTH_SUSPEND(EnumParticle.SUSPENDED_DEPTH),

    /**
     * The town aura particles(s).
     */
    TOWN_AURA(EnumParticle.TOWN_AURA),

    /**
     * The crit particles(s).
     */
    CRIT(EnumParticle.CRIT),

    /**
     * The magic crit particles(s).
     */
    MAGIC_CRIT(EnumParticle.CRIT_MAGIC),

    /**
     * The mob spell particles(s).
     */
    MOB_SPELL(EnumParticle.SPELL_MOB),

    /**
     * The mob spell ambient particles(s).
     */
    MOB_SPELL_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT),

    /**
     * The spell particles(s).
     */
    SPELL(EnumParticle.SPELL_MOB),

    /**
     * The instant spell particles(s).
     */
    INSTANT_SPELL(EnumParticle.SPELL_INSTANT),

    /**
     * The witch magic particles(s).
     */
    WITCH_MAGIC(EnumParticle.SPELL_WITCH),

    /**
     * The note particles(s).
     */
    NOTE(EnumParticle.NOTE),

    /**
     * The portal particles(s).
     */
    PORTAL(EnumParticle.PORTAL),

    /**
     * The enchantment table particles(s).
     */
    ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE),

    /**
     * The explode particles(s).
     */
    EXPLODE(EnumParticle.EXPLOSION_NORMAL),

    /**
     * The flame particles(s).
     */
    FLAME(EnumParticle.FLAME),

    /**
     * The lava particles(s).
     */
    LAVA(EnumParticle.LAVA),

    /**
     * The footstep particles(s).
     */
    FOOTSTEP(EnumParticle.FOOTSTEP),

    /**
     * The splash particles(s).
     */
    SPLASH(EnumParticle.WATER_SPLASH),

    /**
     * The large smoke particles(s).
     */
    LARGE_SMOKE(EnumParticle.SMOKE_LARGE),

    /**
     * The cloud particles(s).
     */
    CLOUD(EnumParticle.CLOUD),

    /**
     * The red dust particles(s).
     */
    RED_DUST(EnumParticle.REDSTONE),

    /**
     * The snowball poof particles(s).
     */
    SNOWBALL_POOF(EnumParticle.SNOWBALL),

    /**
     * The drip water particles(s).
     */
    DRIP_WATER(EnumParticle.DRIP_WATER),

    /**
     * The drip lava particles(s).
     */
    DRIP_LAVA(EnumParticle.DRIP_LAVA),

    /**
     * The snow shovel particles(s).
     */
    SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL),

    /**
     * The slime particles(s).
     */
    SLIME(EnumParticle.SLIME),

    /**
     * The heart particles(s).
     */
    HEART(EnumParticle.HEART),

    /**
     * The angry villager particles(s).
     */
    ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY),

    /**
     * The happy villager particles(s).
     */
    HAPPY_VILLAGER(EnumParticle.VILLAGER_HAPPY);

    /**
     * The Constant MAX_RANGE.
     */
    private static final double MAX_RANGE = 60.0D;

    private final EnumParticle particle;

    /**
     * Instantiates a new particles.
     *
     * @param enumParticle the particle
     */
    Particles(EnumParticle enumParticle) {
        this.particle = enumParticle;
    }

    /**
     * Gets the players in range.
     *
     * @param loc   the loc
     * @param range the range
     * @return the players in range
     */
    private static List<Player> getPlayersInRange(Location loc, double range) {
        List<Player> players = new ArrayList<Player>();
        double sqr = range * range;
        for (Player p : loc.getWorld().getPlayers())
            if (p.getLocation().distanceSquared(loc) <= sqr)
                players.add(p);
        return players;
    }

    /**
     * Creates the packet.
     *
     * @param particle the particle
     * @param loc      the loc
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     * @param offsetZ  the offset z
     * @param speed    the speed
     * @param amount   the amount
     * @return the object
     */
    private static PacketPlayOutWorldParticles createPacket(EnumParticle particle, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount, int... ids) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount of particles has to be greater than 0");
        try {
            return new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, amount, ids);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Particles] Failed to create a particle packet!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the icon crack packet.
     *
     * @param id      the id
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @return the object
     */
    private static PacketPlayOutWorldParticles createIconCrackPacket(int id, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket(EnumParticle.ITEM_CRACK, loc, offsetX, offsetY, offsetZ, speed, amount, id);
    }

    /**
     * Creates the block crack packet.
     *
     * @param id      the id
     * @param data    the data
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @return the object
     */
    private static PacketPlayOutWorldParticles createBlockCrackPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket(EnumParticle.BLOCK_CRACK, loc, offsetX, offsetY, offsetZ, speed, amount, id, data);
    }

    /**
     * Creates the block dust packet.
     *
     * @param id      the id
     * @param data    the data
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @return the object
     */
    private static PacketPlayOutWorldParticles createBlockDustPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket(EnumParticle.BLOCK_DUST, loc, offsetX, offsetY, offsetZ, speed, amount, id, data);
    }

    /**
     * Send packet.
     *
     * @param player the player
     * @param packet the packet
     */
    private static void sendPacket(Player player, PacketPlayOutWorldParticles packet) {
        if (packet != null) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Send packet.
     *
     * @param players the players
     * @param packet  the packet
     */
    private static void sendPacket(Collection<Player> players, PacketPlayOutWorldParticles packet) {
        for (Player p : players) {
            sendPacket(p, packet);
        }
    }

    /**
     * Displays an icon crack (item break) effect which is only visible for specific players.
     *
     * @param loc     the loc
     * @param id      the id
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @param players the players
     */
    public static void displayIconCrack(Location loc, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createIconCrackPacket(id, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) effect which is visible for all players whitin the maximum range of 20 me.paulbgd.blocks in the world of @param loc.
     *
     * @param loc     the loc
     * @param id      the id
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayIconCrack(Location loc, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        //displayIconCrack(loc, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays an icon crack (item break) effect which is visible for all players whitin a certain range in the the world of @param loc.
     *
     * @param loc     the loc
     * @param range   the range
     * @param id      the id
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayIconCrack(Location loc, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createIconCrackPacket(id, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block crack (block break) effect which is only visible for specific players.
     *
     * @param loc     the loc
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @param players the players
     */
    public static void displayBlockCrack(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createBlockCrackPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block crack (block break) effect which is visible for all players whitin the maximum range of 20 me.paulbgd.blocks in the world of @param loc.
     *
     * @param loc     the loc
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayBlockCrack(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        //displayBlockCrack(loc, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a block crack (block break) effect which is visible for all players whitin a certain range in the the world of @param loc.
     *
     * @param loc     the loc
     * @param range   the range
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayBlockCrack(Location loc, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createBlockCrackPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust effect which is only visible for specific players.
     *
     * @param loc     the loc
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @param players the players
     */
    public static void displayBlockDust(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createBlockDustPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust effect which is visible for all players whitin the maximum range of 20 me.paulbgd.blocks in the world of @param loc.
     *
     * @param loc     the loc
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayBlockDust(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayBlockDust(loc, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a block dust effect which is visible for all players whitin a certain range in the the world of @param loc.
     *
     * @param loc     the loc
     * @param range   the range
     * @param id      the id
     * @param data    the data
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public static void displayBlockDust(Location loc, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createBlockDustPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Creates the packet.
     *
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @return the object
     */
    private PacketPlayOutWorldParticles createPacket(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket(this.particle, loc, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a particle effect which is only visible for specific players.
     *
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     * @param players the players
     */
    public void display(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createPacket(loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect which is visible for all players whitin the maximum range of 20 me.paulbgd.blocks in the world of @param loc.
     *
     * @param loc     the loc
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public void display(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        display(loc, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a particle effect which is visible for all players whitin a certain range in the the world of @param loc.
     *
     * @param loc     the loc
     * @param range   the range
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @param offsetZ the offset z
     * @param speed   the speed
     * @param amount  the amount
     */
    public void display(Location loc, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of " + MAX_RANGE);
        sendPacket(getPlayersInRange(loc, range), createPacket(loc, offsetX, offsetY, offsetZ, speed, amount));
    }
}