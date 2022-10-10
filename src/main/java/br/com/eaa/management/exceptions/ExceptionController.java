package br.com.eaa.management.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @Override
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        String msg = "";
        if(ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for(ObjectError object : list) {
                msg += object.getDefaultMessage()+"\n";
            }
        }else {
            msg= ex.getMessage();
        }
        ErrorObject objetoError = new ErrorObject();
        objetoError.setError(msg);
        objetoError.setCode(status.value()+ "=>" + status.getReasonPhrase());
        return new ResponseEntity<>(objetoError, headers, status);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    public ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
        String msg = "";
        if(ex instanceof DataIntegrityViolationException) {
            msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
        }else if(ex instanceof ConstraintViolationException) {
            msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
        } else if(ex instanceof SQLException){
            msg = ((SQLException) ex).getCause().getCause().getMessage();
        }else {
            msg = ex.getMessage();
        }
        ErrorObject objetoError = new ErrorObject();
        objetoError.setError(msg);
        objetoError.setCode(HttpStatus.INTERNAL_SERVER_ERROR+ "==>"+ HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ResponseEntity<>(objetoError,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotFoundResourceException.class})
    public ResponseEntity<ErrorObject> notFoundException(Exception ex){
        ErrorObject objetoError = new ErrorObject();
        objetoError.setError(ex.getMessage());
        objetoError.setCode(HttpStatus.NOT_FOUND.value()+ "=>" +HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(objetoError, HttpStatus.NOT_FOUND);
    }
}
