package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;



@Tab(properties="oid, codigo, evento.nombre, verificado, creadoEn")
@Entity
@Getter @Setter
public class Voto {

    @Id
    @Hidden
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;

    @SearchKey
    @ReadOnly
    @Column(length = 12)
    private String codigo;

    @PrePersist
    private void crearCodigo() {
        if (codigo == null || codigo.trim().isEmpty()) {
            codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="evento_id")
    @DescriptionsList(descriptionProperties="nombre")
    private Evento evento;

    @Hidden // no querés mostrar hashes en UI normal
    @Column(length = 128, nullable = false)
    private String tokenHash;

    private boolean verificado;

    @ReadOnly
    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToMany(mappedBy="voto", cascade=CascadeType.ALL, orphanRemoval=true)
    @ListProperties("categoria.nombre, pareja.descripcion, ranking")
    private Collection<VotoDetalle> detalles = new ArrayList<>();


    // getters/setters
}