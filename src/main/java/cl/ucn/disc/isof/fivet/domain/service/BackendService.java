package cl.ucn.disc.isof.fivet.domain.service;

import cl.ucn.disc.isof.fivet.domain.model.Persona;

/**
 * Interface que representa las operaciones de acceso al backend.
 */
public interface BackendService {

    /**
     * Obtiene una persona desde el backend dado su rut.
     *
     * @param rut
     * @return the Persona
     */
    Persona getPersona(final String rut);

    /**
     * Inicializa el backend.
     */
    void initialize();

    /**
     * Cierra la conexion al backend.
     */
    void shutdown();

}
