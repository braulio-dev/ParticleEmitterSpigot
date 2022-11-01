package runtime;

import emitters.EmitterLifetime;
import emitters.EmitterRate;
import emitters.EmitterShape;
import emitters.init.EmitterInitialization;
import emitters.init.EmitterLocalSpace;
import emitters.shape.EmitterShapeEntityAABB;
import net.hollowcube.mql.foreign.Query;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import particle.ParticleAppearanceTinting;
import particle.ParticleInitialSpeed;
import particle.ParticleLifetime;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParticleEmitter extends ParticleInterface {
    private Set<Particle> particles = new HashSet<>();

    private double emitter_age;
    private final double emitter_random_1;
    private final double emitter_random_2;
    private final double emitter_random_3;
    private final double emitter_random_4;

    private final EmitterLocalSpace local_space;
    private final EmitterInitialization initialization;

    private final EmitterLifetime lifetime;
    private final EmitterRate rate;
    private final EmitterShape shape;

    private final ParticleAppearanceTinting particleColour;
    private final ParticleInitialSpeed particleSpeed;
    private final ParticleLifetime particleLifetime;
    private Vec offset = Vec.ZERO;

    @Query
    public int particle_count() {
        return particles.size();
    }
    @Query
    public double emitter_age() {
        return emitter_age;
    }
    @Query
    public double emitter_random_1() {
        return emitter_random_1;
    }
    @Query
    public double emitter_random_2() {
        return emitter_random_2;
    }
    @Query
    public double emitter_random_3() {
        return emitter_random_3;
    }
    @Query
    public double emitter_random_4() {
        return emitter_random_4;
    }
    @Query
    public double particle_age() {
        return 0;
    }
    @Query
    public double particle_random_1() {
        return 0;
    }
    @Query
    public double particle_random_2() {
        return 0;
    }
    @Query
    public double particle_random_3() {
        return 0;
    }
    @Query
    public double particle_random_4() {
        return 0;
    }

    public void setPosition(Vec offset) {
        this.offset = offset;
    }

    public ParticleEmitter(EmitterInitialization initialization, EmitterLocalSpace local_space,
                           EmitterLifetime lifetime, EmitterRate rate, EmitterShape shape,
                           ParticleInitialSpeed particleSpeed, ParticleAppearanceTinting particleColour, ParticleLifetime particleLifetime) {
        this.emitter_age = 0;

        this.emitter_random_1 = Math.random();
        this.emitter_random_2 = Math.random();
        this.emitter_random_3 = Math.random();
        this.emitter_random_4 = Math.random();

        this.initialization = initialization;
        this.local_space = local_space;

        this.lifetime = lifetime;
        this.rate = rate;
        this.shape = shape;
        this.particleSpeed = particleSpeed;
        this.particleColour = particleColour;
        this.particleLifetime = particleLifetime;

        initialization.initialize(this);
    }

    public Collection<ParticlePacket> tick() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        emitter_age += 1.0/1000;

        initialization.update(this);
        // particles.forEach(Particle::tick);
        // particles = particles.stream().filter(particle -> particle.isAlive()).collect(Collectors.toSet());

        EmitterLifetime.LifetimeState isActive = lifetime.getState(this);

        if (isActive == EmitterLifetime.LifetimeState.DEAD || isActive == EmitterLifetime.LifetimeState.INACTIVE)
            return List.of();

        boolean canCreateParticle = rate.canEmit(this);

        if (canCreateParticle) {
            Vec position = shape.emitPosition(this).add(this.offset);
            Vec direction = shape.emitDirection(this);
            if (direction == null) direction = Vec.ZERO;

            Particle particle = new Particle(position, direction, this, particleColour, particleLifetime);
            // particles.add(particle);
            return List.of(particle.getPacket());
        }

        return List.of();
    }

    @Override
    public String toString() {
        return "ParticleEmitter{" +
                "emitter_age=" + emitter_age +
                ", emitter_random1=" + emitter_random_1 +
                ", emitter_random2=" + emitter_random_2 +
                ", emitter_random3=" + emitter_random_3 +
                ", emitter_random4=" + emitter_random_4 +
                ", local_space=" + local_space +
                ", initialization=" + initialization +
                ", lifetime=" + lifetime +
                ", rate=" + rate +
                ", shape=" + shape +
                ", particleColour=" + particleColour +
                ", particleSpeed=" + particleSpeed +
                '}';
    }
}
