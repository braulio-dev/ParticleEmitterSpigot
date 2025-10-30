package net.worldseed.particleemitter.runtime;

import net.hollowcube.mql.MqlScript;
import net.hollowcube.mql.runtime.MqlMath;
import net.hollowcube.mql.runtime.MqlScopeImpl;
import net.hollowcube.mql.foreign.MqlForeignFunctions;
import net.hollowcube.mql.jit.MqlEnv;

public interface ParticleEmitterScript {
    double evaluate(@MqlEnv({"variable", "v"}) ParticleInterface particle);

    static ParticleEmitterScript fromDouble(double value) {
        return fromString(Double.toString(value));
    }

    static ParticleEmitterScript fromString(String s) {
        if (s == null) return fromDouble(0);

        // Parse the script once
        MqlScript script = MqlScript.parse(s.replace("Math", "math"));

        // Return an implementation that evaluates using the interpreter
        return particle -> {
            // Create a scope with math functions and the particle variable
            MqlScopeImpl.Mutable scope = new MqlScopeImpl.Mutable();
            scope.set("math", MqlMath.INSTANCE);
            scope.set("variable", MqlForeignFunctions.create(ParticleInterface.class, particle));
            scope.set("v", MqlForeignFunctions.create(ParticleInterface.class, particle));

            return script.evaluate(scope);
        };
    }
}