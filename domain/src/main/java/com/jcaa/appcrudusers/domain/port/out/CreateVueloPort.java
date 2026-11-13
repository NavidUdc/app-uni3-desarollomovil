package com.jcaa.appcrudusers.domain.port.out;

import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;

/**
 * Synchronous contract; application execution must run outside the Android main thread.
 * Adapters translate infrastructure failures into DomainException.
 */
public interface CreateVueloPort {
    Vuelo create(VueloRegistration registration);
}
