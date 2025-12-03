package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.View;

import javax.persistence.*;
import java.util.Collection;

@View(members =
        "evento;" +
                "detalles { facultad, puntuacion }"
)
@Entity
@Getter @Setter
public class Voto {
    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @Hidden
    @Column(length=64)
    private String token;

    @ManyToOne(
            fetch=FetchType.LAZY,
            optional = false
    )
    @DescriptionsList
    private Evento evento;

    @ElementCollection
    @ListProperties("facultad.nombre, puntuacion")
    Collection<VotoDetalle> detalles;
}