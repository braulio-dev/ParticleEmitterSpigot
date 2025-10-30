package net.worldseed.particleemitter.emitters;

import org.bukkit.util.Vector;
import net.worldseed.particleemitter.runtime.ParticleInterface;

public interface EmitterShape {
    Vector emitPosition(ParticleInterface particleEmitter);
    Vector emitDirection(Vector origin, ParticleInterface particleEmitter);
    boolean canRotate();
}