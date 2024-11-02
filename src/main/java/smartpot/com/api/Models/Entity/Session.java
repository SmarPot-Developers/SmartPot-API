package smartpot.com.api.Models.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sesiones")
public class Session implements Serializable {

    /**
     * Representa una sesión de usuario en el sistema.
     * * Esta clase contiene información sobre una sesión, incluyendo la fecha de registro
     * * y la asociación con un usuario. Se utiliza en la colección "sesiones" de MongoDB.
     */

    @Id
    @Field("id")
    private String id;

   /* @NotNull(message = "La fecha de registro no puede estar vacía")
    @FutureOrPresent(message = "La fecha de registro debe ser hoy o en el futuro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Field("registration")
    private Date registration;*/

    @DBRef
    @NotNull(message = "La sesión debe estar asociada a un usuario")
    @Field("user")
    private User user;
}
