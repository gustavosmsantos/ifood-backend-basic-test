package com.ifood.exception.handling;

import com.ifood.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleStatusCodeException(EntityNotFoundException ex) {
        LOGGER.error("Entity could not be found", ex);
        return new ResponseEntity<>(getResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<Object> handleStatusCodeException(HttpStatusCodeException ex) {
        LOGGER.error("Status code exception reached", ex);
        HttpStatus statusCode = ex.getStatusCode();
        return new ResponseEntity<>(getResponse(ex.getMessage()), statusCode);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        LOGGER.error("Unexpected exception reached.", ex);
        return new ResponseEntity<>(getResponse("System reached an unexpected condition"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Response getResponse(String message) {
        return new Response(message);
    }

    class Response {

        private String message;

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
