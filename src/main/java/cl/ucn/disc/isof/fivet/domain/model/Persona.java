package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.EnumValue;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
    String rut;

    /**
     * Nombre completo
     */
    @Getter
    @Setter
    @NotEmpty(message = "El nombre no puede estar vacio")
    @Column(nullable = false)
    String nombre;

    /**
     * Password
     */
    @NotEmpty
    @Getter
    @Encrypted
    @Column(nullable = false)
    String password;

    /**
     * Tipo de la persona
     */
    @Getter
    @NotNull
    Tipo tipo;

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
