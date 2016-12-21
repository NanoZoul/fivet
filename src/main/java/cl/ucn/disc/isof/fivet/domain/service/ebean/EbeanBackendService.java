package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Expr;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.ServerConfig;
import com.durrutia.ebean.BaseModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Clase que se encarga de implementar en backend del sistema y hacer la conexion a la BD
 *
 * @author Tomás I. Alegre Sepúlveda
 * @version 20161026
 */
@Slf4j
public class EbeanBackendService implements BackendService {

    /**
     * EBean server
     */
    private final EbeanServer ebeanServer;

    /**
     *Configuracion del Backend
     */
    public EbeanBackendService(final String database) {

        log.debug("Loading EbeanBackend in database: {}", database);

        /**
         * Configuration
         */
        ServerConfig config = new ServerConfig();
        config.setName(database);
        config.setDefaultServer(true);
        config.loadFromProperties();

        // Don't try this at home
        //config.setAutoCommitMode(false);

        // config.addPackage("package.de.la.clase.a.agregar.en.el.modelo");
        config.addClass(BaseModel.class);

        config.addClass(Persona.class);
        config.addClass(Persona.Tipo.class);

        config.addClass(Paciente.class);
        config.addClass(Paciente.Sexo.class);

        config.addClass(Control.class);


        // http://ebean-orm.github.io/docs/query/autotune
        config.getAutoTuneConfig().setProfiling(false);
        config.getAutoTuneConfig().setQueryTuning(false);

        config.setEncryptKeyManager(new EncryptKeyManager() {

            @Override
            public void initialise() {
                log.debug("Initializing EncryptKey ..");
            }

            @Override
            public EncryptKey getEncryptKey(final String tableName, final String columnName) {

                log.debug("gettingEncryptKey for {} in {}.", columnName, tableName);

                // Return the encrypt key
                return () -> tableName + columnName;
            }
        });

        this.ebeanServer = EbeanServerFactory.create(config);

        log.debug("EBeanServer ready to go.");

    }


    /**
     * Metodo que retorna una persona segun su email
     * @param rutEmail contiene un rut o un emailp para buscarlo en la BD
     * @return persona
     */
    @Override
    public Persona getPersona(String rutEmail) {


        return this.ebeanServer.find(Persona.class)
                .where().or(
                        Expr.eq("rut",rutEmail),
                        Expr.eq("mail", rutEmail)
                ).findUnique();

    }

    /**
     * Metodo que obtiene todos los pacientes de la BD
     * @return Lista con todos los pacientes
     */
    @Override
    public List<Paciente> getPacientes() {

        return this.ebeanServer.find(Paciente.class).findList();
    }

    /**
     * Metodo que pbtiene un paciente segun el numero de paciente
     * @param numeroPaciente numero de paciente de una ficha medica
     * @return Paciente de la BD con el numero de ficha dado
     */
    @Override
    public Paciente getPaciente(Integer numeroPaciente) {
        return this.ebeanServer.find(Paciente.class).where(
                Expr.eq("numero", numeroPaciente)
        ).findUnique();
    }

    /**
     * Metodo que entrega los controles realizados por el veterinario
     * @param rutVeterinario rut del veterinario que realizo el control
     * @return Lista de los controles
     */
    @Override
    public List<Control> getControlesVeterinario(String rutVeterinario) {

        return this.ebeanServer.find(Control.class).where()
                .eq("veterinario.rut",rutVeterinario)
                .findList();

    }

    /**
     * Metodo que busca los pacientes por nombre
     * @param nombre es el nombre del paciente a buscar
     * @return Lista de pacientes con el nombre ingresado
     */
    @Override
    public List<Paciente> getPacientesPorNombre(String nombre) {

        return ebeanServer.find(Paciente.class).where()
                .eq("nombre",nombre)
                .findList();
    }

    /**
     * Metodo que agrega un control dado a un paciente dado. Ambos se ingresan por parametros
     * @param control control que se agregara el paciente
     * @param numeroPaciente numero del paciente al cual se le asignara el control
     */
    @Override
    public void agregarControl(Control control, Integer numeroPaciente) {

        Paciente paciente = ebeanServer.find(Paciente.class).where()
                .eq("numero", numeroPaciente)
                .findUnique();

        paciente.add(control);

        paciente.update();
    }

    /**
     * Inicializa la base de datos
     */
    @Override
    public void initialize() {

        log.info("Initializing Ebean ..");
    }

    /**
     * Cierra la conexion a la BD
     */
    @Override
    public void shutdown() {
        log.debug("Shutting down Ebean ..");

        // TODO: Verificar si es necesario des-registrar el driver
        this.ebeanServer.shutdown(true, false);
    }
}