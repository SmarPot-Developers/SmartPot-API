package smartpot.com.api.Mail.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartpot.com.api.Mail.Model.DAO.Service.EmailServiceI;
import smartpot.com.api.Mail.Model.DTO.EmailDTO;
import smartpot.com.api.Responses.ErrorResponse;

@RestController
@RequestMapping("/Emails")
@Tag(name = "Correos", description = "Operaciones relacionadas con correos")
public class EmailController {
    private final EmailServiceI emailServiceI;

    /**
     * Constructor del controlador {@link EmailController}.
     * <p>Se utiliza la inyección de dependencias para asignar el servicio {@link EmailServiceI} que gestionará las operaciones
     * relacionadas con los correos.</p>
     *
     * @param emailServiceI El servicio que contiene la lógica de negocio para manejar correos.
     * @throws NullPointerException Si el servicio proporcionado es {@code null}.
     * @see EmailServiceI
     */
    @Autowired
    public EmailController(EmailServiceI emailServiceI) {
        this.emailServiceI = emailServiceI;
    }

    /**
     * Recupera todos los correos registrados en el sistema.
     * <p>Este método devuelve una lista con todos los correos que están registrados en el sistema.</p>
     * <p>Si no se encuentran coreos, se devolverá una lista vacía con el código HTTP 200.</p>
     * <p>En caso de error (por ejemplo, problemas con la conexión a la base de datos o un fallo en el servicio),
     * se devolverá un mensaje de error con el código HTTP 404.</p>
     *
     * @return Un objeto {@link ResponseEntity} que contiene una lista de todos los correos registrados (código HTTP 200).
     * En caso de error, se devolverá un mensaje de error con el código HTTP 404.
     *
     * <p><b>Respuestas posibles:</b></p>
     * <ul>
     *   <li><b>200 OK</b>: Se retorna una lista de objetos {@link EmailDTO} con la información de todos los correos registrados.<br></li>
     *   <li><b>404 Not Found</b>: No se encontraron correos registrados o hubo un error al recuperar los datos.<br></li>
     * </ul>
     */
    @GetMapping("/All")
    @Operation(summary = "Obtener todos los correos",
            description = "Recupera todos los correos registrados en el sistema. "
                    + "En caso de no haber correos, se devolverá una excepción.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Correos encontrados",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = EmailDTO.class)))),
                    @ApiResponse(responseCode = "404",
                            description = "No se encontraron correos registrados.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<?> getAllCrops() {
        try {
            return new ResponseEntity<>(emailServiceI.getAllEmails(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }
}