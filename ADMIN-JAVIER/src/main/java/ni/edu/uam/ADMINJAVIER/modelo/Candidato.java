package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Candidato {
    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @Column(length=50)
    private String nombre;

    @ManyToOne(
            fetch=FetchType.LAZY,
            optional = false
    )
    @DescriptionsList
    private Facultad facultad;

    @ManyToOne(
            fetch=FetchType.LAZY,
            optional = false
    )
    @DescriptionsList
    private Evento evento;
}