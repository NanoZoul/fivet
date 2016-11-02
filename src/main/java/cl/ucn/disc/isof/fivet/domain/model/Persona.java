package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Diego Urrutia Astorga
 * @version 20161026
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Persona extends BaseModel {

    /**
     * RUT
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String rut;

    /**
     * Nombre completo
     */
    @Getter
    @Setter
    @NotEmpty(message = "El nombre no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    /**
     * Password
     */
    @NotEmpty
    @Getter
    @Encrypted
    @Column(nullable = false)
    private String password;

    /**
     * Tipo de la persona
     */
    @Getter
    @NotNull
    private Tipo tipo;

    /**
     * Listado de pacientes
     */
    @Getter
    @ManyToMany
    @OrderBy("numero")
    private List<Paciente> pacientes;

    /**
     * Tipo de rol
     */
    public enum Tipo {
        @EnumValue("Cliente")
        CLIENTE,

        @EnumValue("Veterinario")
        VETERINARIO,
    }


}
