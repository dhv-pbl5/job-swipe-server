package org.dhv.pbl5server.common_service.config;


import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.exception.UnauthorizedException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.model.ErrorResponse;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.rmi.ServerError;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle custom exception
     * 1. NotFoundObjectException
     * 2. BadRequestException
     * 3. ForbiddenException
     * 4. UnauthorizedException
     * 5. ServerError
     * 6. AccessDeniedException
     * 7. GlobalException
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(NotFoundObjectException.class)
    public ResponseEntity<ApiDataResponse> handleNotFoundObjectException(
            NotFoundObjectException ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiDataResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiDataResponse> handleForbiddenException(
            ForbiddenException ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiDataResponse> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ApiDataResponse> handleServerErrorException(
            ServerError ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiDataResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.FORBIDDEN);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiDataResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String msg =
                STR."\{ex.getName()} should be of type \{Objects.requireNonNull(ex.getRequiredType()).getName()}";
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR, msg);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse> handleGlobalException(
            Exception ex, HttpServletRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Handle rest response entity exception
     * 1. MethodArgumentNotValid
     * 2. NoHandlerFoundException
     * 3. MissingServletRequestParameter
     * 4. HttpRequestMethodNotSupported
     * 5. HttpMediaTypeNotSupported
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode status,
            @Nonnull WebRequest request
    ) {
        List<ObjectError> listError = ex.getBindingResult().getAllErrors();
        ObjectError objectError = listError.getLast();
        String error = ErrorUtils.convertToSnakeCase(Objects.requireNonNull(objectError.getCode()));
        String fieldName = ErrorUtils.convertToSnakeCase(((FieldError) objectError).getField());
        String resource = ErrorUtils.convertToSnakeCase(objectError.getObjectName());

        ErrorResponse errorResponse = ErrorUtils.getValidationError(resource, fieldName, error);
        ApiDataResponse responseDataAPI = ApiDataResponse.error(errorResponse);
        return new ResponseEntity<>(responseDataAPI, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @Nonnull NoHandlerFoundException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode status,
            @Nonnull WebRequest request
    ) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.PAGE_NOT_FOUND);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode status,
            @Nonnull WebRequest request
    ) {
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode status,
            @Nonnull WebRequest request
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods())
                .forEach(t -> builder.append(t).append(" "));

        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR, builder.toString());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatusCode status,
            @Nonnull WebRequest request
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR, builder.toString());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
