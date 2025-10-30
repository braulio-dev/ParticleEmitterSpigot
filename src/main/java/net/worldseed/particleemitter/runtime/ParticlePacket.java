package net.worldseed.particleemitter.runtime;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class ParticlePacket {

    private final Particle particle;
    private final @Nullable Player source;
    private final double x;
    private final double y;
    private final double z;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final double extra;
    private final int count;
    private final boolean force;
    private final @Nullable Object data;

    public ParticlePacket(Particle particle, boolean force, double x, double y, double z, double offsetX, double offsetY, double offsetZ, double extra, int count) {
        this.particle = particle;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.count = count;
        this.force = force;
        this.source = null;
        this.data = null;
    }

    public ParticlePacket(Particle particle, double x, double y, double z, double offsetX, double offsetY, double offsetZ, double extra, int count, boolean force, @Nullable Player source, @Nullable Object data) {
        this.particle = particle;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.count = count;
        this.force = force;
        this.source = source;
        this.data = data;
    }

    public ParticlePacket withSource(Player source) {
        return new ParticlePacket(particle, x, y, z, offsetX, offsetY, offsetZ, extra, count, force, source, data);
    }

    public ParticlePacket withData(Object data) {
        return new ParticlePacket(particle, x, y, z, offsetX, offsetY, offsetZ, extra, count, force, source, data);
    }

    public void send(Collection<Player> players) {
        for (Player player : players) {
            player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
        }
    }

    public void send(Player player) {
        player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    public void send(World world, double radius) {
        world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    public void send(World world) {
        world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

}
