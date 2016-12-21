package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * Clase de testing del {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * Todos los test deben terminar antes de 60 segundos.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);

    /**
     * Configuracion de la base de datos:  h2, hsql, sqlite
     * WARN: hsql no soporta ENCRYPT
     */
    private static final String DB = "h2";

    /**
     * Backend
     */
    private BackendService backendService;

    /**
     * Cronometro
     */
    private Stopwatch stopWatch;

    /**
     * Antes de cada test
     */
    @Before
    public void beforeTest() {

        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * Despues del test
     */
    @After
    public void afterTest() {

        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */
    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .password("durrutia123")
                    .tipo(Persona.Tipo.CLIENTE)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Nombre distintos!", nombre, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre
            persona.setNombre(nombre + nombre);
            persona.update();
        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre+nombre, persona.getNombre());
        }
    }

    /**
     * Testing del metodo getPaciente
     */
    @Test
    public void testGetPaciente(){
        //Crear pacientes
        final Paciente paciente1 = Paciente.builder()
                .numero(987654321)
                .nombre("Rinho")
                .build();
        paciente1.insert();

        //Paciente del backend
        Paciente pacienteBackend1 = backendService.getPaciente(paciente1.getNumero());

        Assert.assertNotNull("El paciente es null",pacienteBackend1);
        Assert.assertEquals(paciente1.getNumero(),pacienteBackend1.getNumero());
        Assert.assertEquals(paciente1.getNombre(),pacienteBackend1.getNombre());
    }

    /**
     * Test del metodo getPaciente por nombre
     */
    @Test
    public void testGetPacientesPorNombre(){
        Persona persona1 = Persona.builder()
                .nombre("Alfredo")
                .mail("ahenriquez@sodired.cl")
                .rut("17725104-6")
                .password("123456")
                .build();
        persona1.insert();

        Paciente paciente1 = Paciente.builder()
                .color("blanco")
                .nombre("Rinho")
                .numero(123456789)
                .raza("Pastor Alemán")
                .sexo(Paciente.Sexo.MACHO)
                .build();
        paciente1.insert();

        Paciente paciente2 = Paciente.builder()
                .color("negro")
                .nombre("Wolf")
                .numero(987654321)
                .raza("Gran Danés")
                .sexo(Paciente.Sexo.MACHO)
                .build();
        paciente2.insert();

        //...

    }




}
