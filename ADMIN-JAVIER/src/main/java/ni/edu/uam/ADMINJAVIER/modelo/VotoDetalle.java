package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;

import javax.persistence.*;

@Embeddable
@Getter @Setter
public class VotoDetalle {
    @ManyToOne(
            fetch=FetchType.LAZY,
            optional = false
    )
    @DescriptionsList
    private Facultad facultad;

    @Column(length=2)
    private int puntuacion;
}
