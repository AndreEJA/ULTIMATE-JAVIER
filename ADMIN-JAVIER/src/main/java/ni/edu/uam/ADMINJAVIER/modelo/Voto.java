package ni.edu.uam.ADMINJAVIER.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.Date;



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

    @Hidden // no querés mostrar hashes en UI normal
    @Column(length = 128, nullable = false)
    private String tokenHash;

    @SearchKey
    @ReadOnly
    @Column(length = 12)
    private String codigo;

    @PrePersist
    private void beforeCreate() {
        if (codigo == null || codigo.trim().isEmpty()) {
            codigo = java.util.UUID.randomUUID().toString()
                    .replace("-", "").substring(0, 10).toUpperCase();
        }

        if (tokenHash == null || tokenHash.trim().isEmpty()) {
            // Genera un token aleatorio y guarda SOLO el hash
            String token = randomToken();
            tokenHash = sha256(token);
        }

        if (creadoEn == null) creadoEn = new Date();
    }

    private static String randomToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString(); // 64 chars hex
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="evento_id")
    @DescriptionsList(descriptionProperties="nombre")
    private Evento evento;

    private boolean verificado;

    //@ReadOnly
    //private LocalDateTime creadoEn = LocalDateTime.now();
    @ReadOnly
    @Column(updatable = false)
    private Date creadoEn;


    @OneToMany(mappedBy="voto", cascade=CascadeType.ALL, orphanRemoval=true)
    @ListProperties("categoria.nombre, pareja.descripcion, ranking")
    private Collection<VotoDetalle> detalles = new ArrayList<>();
}