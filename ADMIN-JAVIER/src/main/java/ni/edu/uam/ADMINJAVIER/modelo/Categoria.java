package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"evento_id", "nombre"}
        )
)
@Getter
@Setter
public class Categoria {

    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @ManyToOne(optional = false)
    @JoinColumn(name="evento_id")
    @DescriptionsList
    private Evento evento;

    @SearchKey
    @Required
    @Column(length=50)
    private String nombre;
}