package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
public class Pareja {

    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @ManyToOne(optional = false)
    @JoinColumn(name="evento_id")
    @DescriptionsList(descriptionProperties="nombre")
    private Evento evento;

    @ManyToOne(optional = false)
    @JoinColumn(name="facultad_id")
    @DescriptionsList(descriptionProperties="acronimo,nombre")
    private Facultad facultad;

    // <- AQUÍ va la lista de candidatos
    @OneToMany(mappedBy = "pareja")
    @ListProperties("nombre, facultad.nombre")  // lo que querés ver en la tabla
    private Collection<Candidato> candidatos = new ArrayList<>();


    @SearchKey
    @ReadOnly
    @Column(length=160)
    private String descripcion;


    @PrePersist @PreUpdate
    private void calcularDescripcion() {
        String ev = (evento == null || evento.getNombre() == null) ? "SIN_EVENTO" : evento.getNombre();

        String fac;
        if (facultad == null) fac = "SIN_FACULTAD";
        else if (facultad.getAcronimo() != null && !facultad.getAcronimo().trim().isEmpty())
            fac = facultad.getAcronimo().trim();
        else fac = facultad.getNombre();

        this.descripcion = fac + " - " + ev;
    }

}