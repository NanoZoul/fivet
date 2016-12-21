package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Clase que representa a un control veterinario
 *
 * @author Tomás I. Alegre Sepúlveda
 * @version 20161026
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control extends BaseModel{

    /**
     * Fecha
     */
    @Getter
    @Setter
    @NotEmpty
    @Column
    private Date fecha;

    /**
     * Proximo Control
     */
    @Getter
    @Setter
    @NotEmpty
    private Date proxControl;
    /**
     * Temperatura
     */
    @Getter
    @Setter
    private Double temperatura;
    /**
     * Peso
     */
    @Getter
    @Setter
    private Double peso;
    /**
     * Altura
     */
    @Getter
    @Setter
    private Double altura;
    /**
     * Diagnostico
     */
    @Getter
    @Setter
    @NotEmpty
    private String diagnostico;
    /**
     * Nota
     */
    @Getter
    @Setter
    private String nota;

    /**
     * Veterinario
     */
    @Getter
    @Column(nullable = false)
    @ManyToOne
    private Persona veterinario;

}