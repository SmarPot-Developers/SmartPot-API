package smartpot.com.api.Sessions.Service;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import smartpot.com.api.Exception.ApiException;
import smartpot.com.api.Exception.ApiResponse;
import smartpot.com.api.Sessions.Model.Entity.Session;
import smartpot.com.api.Sessions.Repository.RSession;
import smartpot.com.api.Users.Model.DTO.UserDTO;
import smartpot.com.api.Users.Service.SUserI;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@Service
public class SSession implements SSessionI {

    private final RSession repositorySession;
    private final SUserI user;

    @Autowired
    public SSession(RSession repositorySession, SUserI user) {
        this.repositorySession = repositorySession;
        this.user = user;
    }

    @Override
    public Session getSessionById(String sessionId) {
        if (!ObjectId.isValid(sessionId)) {
            throw new ApiException(new ApiResponse(
                    "la sesion con id '" + sessionId + "' no es válido. Asegúrate de que tiene 24 caracteres y solo incluye dígitos hexadecimales (0-9, a-f, A-F).",
                    HttpStatus.BAD_REQUEST.value()
            ));
        }
        return repositorySession.findById(sessionId).orElseThrow(() -> new ApiException(
                new ApiResponse("la sesion con id '" + sessionId + "' no fue encontrado.",
                        HttpStatus.NOT_FOUND.value())
        ));
    }

    @Override
    public List<Session> getSessionsByUser(String user) {
        return repositorySession.findByUser(user);
    }

    @Override
    public List<Session> getAllSessions() {
        return repositorySession.findAll();
    }


   /* public List<Session> getSessionsInRange(Date startDate, Date endDate) {
    public Optional<Session> getSessionById(String sessionId) {
        return repositorySession.findById(sessionId);
    }
        return repositorySession.findByRegistrationBetween(startDate, endDate);
    }*/

    @Override
    public long countSessionsByUser(String user) {
        return repositorySession.countByUser(user);
    }
/*
    public List<Session> getFutureSessions(Date currentDate) {
        return repositorySession.findByRegistrationAfter(currentDate);
    }*/

    @Override
    public Session createSession(Session newSession) throws Exception {
        // Validar que el campo "user" no esté vacío
        if (newSession.getUser() == null) {
            throw new IllegalArgumentException("La sesión debe estar asociada a un usuario válido.");
        }

        // Verificar que el usuario referenciado exista en la base de datos
        Optional<UserDTO> userOptional = Optional.ofNullable(user.getUserById(newSession.getUser()));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("El usuario especificado no existe.");
        }

        // Validar que la fecha de registro sea hoy o en el futuro
        Date today = new Date();
        if (newSession.getRegistration() == null) {
            throw new IllegalArgumentException("La fecha de registro debe ser hoy o en el futuro.");
        }

        // (Opcional) Verificar si ya existe una sesión activa para el usuario
        // Esto depende de la lógica de negocio: si se permite solo una sesión activa por usuario, implementa esta verificación.
        long activeSessions = repositorySession.countByUser(newSession.getUser());
        if (activeSessions > 0) {
            throw new IllegalStateException("El usuario ya tiene una sesión activa.");
        }

        // Si pasa todas las validaciones, guarda la nueva sesión
        return repositorySession.save(newSession);
    }

    @Override
    public void deleteSessionById(String sessionId) {
        if (!ObjectId.isValid(sessionId)) {
            throw new ApiException(new ApiResponse(
                    "la sesion con id '" + sessionId + "' no es válido. Asegúrate de que tiene 24 caracteres y solo incluye dígitos hexadecimales (0-9, a-f, A-F).",
                    HttpStatus.BAD_REQUEST.value()
            ));
        }
        // Intenta encontrar la sesión por ID
        Optional<Session> sessionOptional = repositorySession.findById(sessionId);

        // Si la sesión existe, elimínala
        if (sessionOptional.isPresent()) {
            repositorySession.deleteById(sessionId);
        } else {
            // Si la sesión no existe, lanza una excepción
            throw new ApiException(
                    new ApiResponse("La sesión con ID '" + sessionId + "' no fue encontrada.",
                            HttpStatus.NOT_FOUND.value())
            );
        }

    }

    @Override
    public void deleteSessionByIdUser(String userId) {
        if (!ObjectId.isValid(userId)) {
            throw new ApiException(new ApiResponse(
                    "El user con  '" + userId + "' no es válido. Asegúrate de que tiene 24 caracteres y solo incluye dígitos hexadecimales (0-9, a-f, A-F).",
                    HttpStatus.BAD_REQUEST.value()
            ));
        }
        List<Session> sessions = repositorySession.findByUser(userId);

        if (sessions.isEmpty()) {
            throw new ApiException(new ApiResponse(
                    "No se encontraron sesiones asociadas al usuario con ID '" + userId + "'.",
                    HttpStatus.NOT_FOUND.value()
            ));
        }

        for (Session session : sessions) {
            repositorySession.deleteById(session.getId().toString());
        }

    }

    @Override
    public List<Session> getSessionByUser(String userId) {
        if (!ObjectId.isValid(userId)) {
            throw new ApiException(new ApiResponse(
                    "El user con  '" + userId + "' no es válido. Asegúrate de que tiene 24 caracteres y solo incluye dígitos hexadecimales (0-9, a-f, A-F).",
                    HttpStatus.BAD_REQUEST.value()
            ));
        }
        List<Session> sessions = repositorySession.findByUser(userId);

        if (sessions.isEmpty()) {
            throw new ApiException(new ApiResponse(
                    "No se encontraron sesiones asociadas al usuario con ID '" + userId + "'.",
                    HttpStatus.NOT_FOUND.value()
            ));
        }
        return sessions;
    }
}
