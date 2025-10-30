package net.worldseed.particleemitter.runtime;

import net.worldseed.particleemitter.emitters.EmitterShape;
import net.worldseed.particleemitter.generator.ParticleGenerator;
import net.hollowcube.mql.foreign.Query;
import net.worldseed.particleemitter.particle.ParticleAppearanceTinting;
import net.worldseed.particleemitter.particle.ParticleInitialSpeed;
import net.worldseed.particleemitter.particle.ParticleLifetime;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

public class Particle extends ParticleInterface {
    private final ParticleEmitter emitter;
    private final ParticleAppearanceTinting particleColour;
    private final ParticleLifetime particleLifetime;
    private final ParticlePacket packet;
    private final org.bukkit.Particle type;
    private final ParticleInitialSpeed speed;

    double particle_age;

    final double particle_random_1;
    final double particle_random_2;
    final double particle_random_3;
    final double particle_random_4;

    private double emitter_age_offset = 0;

    @Query
    public double particle_age() {
        return particle_age;
    }

    @Query
    public double particle_random_1() {
        return particle_random_1;
    }

    @Query
    public double particle_random_2() {
        return particle_random_2;
    }

    @Query
    public double particle_random_3() {
        return particle_random_3;
    }

    @Query
    public double particle_random_4() {
        return particle_random_4;
    }

    @Override
    public int particle_count() {
        return emitter.particle_count();
    }

    @Override
    public void reset() {
        emitter.reset();
        particle_age = 0;
    }

    @Override
    public int updatesPerSecond() {
        return emitter.updatesPerSecond();
    }

    @Query
    public double emitter_age() {
        return emitter.emitter_age() + emitter_age_offset;
    }

    @Query
    public double emitter_random_1() {
        return emitter.emitter_random_1();
    }

    @Query
    public double emitter_random_2() {
        return emitter.emitter_random_2();
    }

    @Query
    public double emitter_random_3() {
        return emitter.emitter_random_3();
    }

    @Query
    public double emitter_random_4() {
        return emitter.emitter_random_4();
    }

    public ParticlePacket getPacket() {
        return packet;
    }

    public Particle(org.bukkit.Particle type, EmitterShape shape, float yaw, Vector offset, ParticleEmitter emitter, ParticleAppearanceTinting particleColour, ParticleLifetime particleLifetime, ParticleInitialSpeed particleSpeed) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.particle_age = 0;

        this.particle_random_1 = Math.random();
        this.particle_random_2 = Math.random();
        this.particle_random_3 = Math.random();
        this.particle_random_4 = Math.random();

        this.emitter = emitter;
        this.type = type;

        this.particleColour = particleColour;
        this.particleLifetime = particleLifetime;
        this.speed = particleSpeed;

        Vector origin = rotateAroundOrigin(yaw, shape.emitPosition(this));
        Vector position = origin.add(offset);

        if (type == org.bukkit.Particle.SOUL_FIRE_FLAME
                || type == org.bukkit.Particle.FLAME
                || type == org.bukkit.Particle.SMOKE
                || type == org.bukkit.Particle.FIREWORK) {
            Vector s = new Vector(speed.speedX().evaluate(this), speed.speedY().evaluate(this), speed.speedZ().evaluate(this));
            Vector direction = shape.emitDirection(origin, this).multiply(s);

            if (shape.canRotate()) {
                direction = rotateAroundOrigin(yaw, direction);
            }
            this.packet = draw(position, direction);
        } else {
            this.packet = draw(position, new Vector());
        }
    }

    private Vector rotateAroundOrigin(float yaw, Vector emitPosition) {
        return emitPosition.rotateAroundY(Math.toRadians(yaw));
    }

    public ParticlePacket draw(Vector start, Vector direction) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ParticleAppearanceTinting.ColourEvaluated colour = particleColour.evaluate(this);

        if (type == org.bukkit.Particle.DUST_COLOR_TRANSITION) {
            int var17 = (int)(8.0D / (ThreadLocalRandom.current().nextDouble() * 0.8D + 0.2D));
            double lifetimeSeconds = ((int)Math.max((float)var17, 1.0F)) * 1.0 / 20;

            double currentAge = this.particle_age;
            this.particle_age += lifetimeSeconds;
            this.emitter_age_offset = lifetimeSeconds;
            ParticleAppearanceTinting.ColourEvaluated colourAfter = particleColour.evaluate(this);
            this.particle_age = currentAge;
            this.emitter_age_offset = 0;

            return ParticleGenerator.buildParticle(
                    type,
                    start.getX(),
                    start.getY(),
                    start.getZ(),
                    1,
                    direction.getX(),
                    direction.getY(),
                    direction.getZ(),
                    colour.r(),
                    colour.g(),
                    colour.b(),
                    colourAfter.r(),
                    colourAfter.g(),
                    colourAfter.b()
            );
        } else {
            return ParticleGenerator.buildParticle(
                    type,
                    start.getX(),
                    start.getY(),
                    start.getZ(),
                    1,
                    direction.getX(),
                    direction.getY(),
                    direction.getZ(),
                    colour.r(),
                    colour.g(),
                    colour.b());
        }
    }

    public void tick() {
        particle_age += 1.0 / updatesPerSecond();
    }

    public boolean isAlive() {
        return particleLifetime.isAlive(this);
    }
}
