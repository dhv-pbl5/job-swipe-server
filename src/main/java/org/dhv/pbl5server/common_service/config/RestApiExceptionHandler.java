package org.dhv.pbl5server.common_service.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.*;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.model.ErrorResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.rmi.ServerError;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle custom exception
     * 1. NotFoundObjectException
     * 2. BadRequestException
     * 3. InternalServerError
     * 4. ForbiddenException
     * 5. UnauthorizedException
     * 6. ServerError
     * 7. AccessDeniedException
     * 8. GlobalException
     * 9. RuntimeException
     * 10. AuthenticationCredentialsNotFoundException
     * 11. InvalidDataException
     * 12. InvalidDataAccessApiUsageException
     */
    @ExceptionHandler(NotFoundObjectException.class)
    public ResponseEntity<ApiDataResponse> handleNotFoundObjectException(
        NotFoundObjectException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiDataResponse> handleBadRequestException(
        BadRequestException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiDataResponse> handleInternalServerException(
        InternalServerException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR_CODE, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiDataResponse> handleForbiddenException(
        ForbiddenException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiDataResponse> handleUnauthorizedException(
        UnauthorizedException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ApiDataResponse> handleServerErrorException(
        ServerError ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiDataResponse> handleAccessDeniedException(
        AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.FORBIDDEN);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiDataResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String msg = "%s should be of type %s".formatted(ex.getName(),
            Objects.requireNonNull(ex.getRequiredType()).getName());
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, msg);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse> handleGlobalException(
        Exception ex, HttpServletRequest request) {
        LogUtils.error("Global exception", ex);
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR_CODE, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiDataResponse> handleRuntimeException(
        RuntimeException ex, HttpServletRequest request) {
        LogUtils.error("Global exception", ex);
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.INTERNAL_SERVER_ERROR_CODE, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiDataResponse> handleAuthenticationCredentialsNotFoundException(
        AuthenticationCredentialsNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.UNAUTHORIZED);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiDataResponse> handleInvalidDataException(
        InvalidDataException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorUtils.getValidationError(ex.resource, ex.fieldName, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiDataResponse> handleInvalidDataAccessApiUsageException(
        InvalidDataAccessApiUsageException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle rest response entity exception
     * 1. MethodArgumentNotValid
     * 2. NoHandlerFoundException
     * 3. MissingServletRequestParameter
     * 4. HttpRequestMethodNotSupported
     * 5. HttpMediaTypeNotSupported
     * 6. NoResourceFoundException
     * 7. HttpMessageNotReadable
     * 8. HttpMessageNotWriteable
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        List<ObjectError> listError = ex.getBindingResult().getAllErrors();
        ObjectError objectError = listError.get(listError.size() - 1);
        String error = CommonUtils.convertToSnakeCase(Objects.requireNonNull(objectError.getCode()));
        String fieldName = CommonUtils.convertToSnakeCase(((FieldError) objectError).getField());
        String resource = CommonUtils.convertToSnakeCase(objectError.getObjectName());

        ErrorResponse errorResponse = ErrorUtils.getValidationError(resource, fieldName, error);
        ApiDataResponse responseDataAPI = ApiDataResponse.error(errorResponse);
        return new ResponseEntity<>(responseDataAPI, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        @Nonnull NoHandlerFoundException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.PAGE_NOT_FOUND);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, ex.getMessage());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods())
            .forEach(t -> builder.append(t).append(" "));

        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, builder.toString());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ErrorResponse error = new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, builder.toString());
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
        @Nonnull NoResourceFoundException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        ErrorResponse error = ErrorUtils.getExceptionError(ErrorMessageConstant.PAGE_NOT_FOUND);
        ApiDataResponse response = ApiDataResponse.error(error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @Nonnull HttpMessageNotReadableException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        ApiDataResponse response = ApiDataResponse
            .error(new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        @Nonnull HttpMessageNotWritableException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {
        ApiDataResponse response = ApiDataResponse
            .error(new ErrorResponse(ErrorMessageConstant.BAD_REQUEST_ERROR_CODE, ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
