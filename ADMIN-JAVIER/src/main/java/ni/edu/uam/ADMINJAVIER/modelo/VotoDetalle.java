package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;

import javax.persistence.*;

@Entity
@Tab(properties="oid, voto.codigo, categoria.nombre, pareja.descripcion, ranking")
@Getter @Setter
public class VotoDetalle {

    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @ManyToOne(optional = false)
    @JoinColumn(name="voto_id")
    @DescriptionsList(descriptionProperties="codigo")
    private Voto voto;

    @ManyToOne(optional = false)
    @JoinColumn(name="categoria_id")
    @DescriptionsList(descriptionProperties="nombre")
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name="pareja_id")
    @DescriptionsList(descriptionProperties="descripcion")
    private Pareja pareja;

    @Required
    private short ranking; // 1,2,3...
}
