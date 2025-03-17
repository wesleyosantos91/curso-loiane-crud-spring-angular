package io.github.wesleyosantos91.api.exception;

import static java.util.List.of;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.github.wesleyosantos91.api.v1.response.CustomProblemDetail;
import io.github.wesleyosantos91.api.v1.response.ErrorResponse;
import io.github.wesleyosantos91.domain.exception.ResourceNotFoundException;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);
    public static final String NO_TRACE_ID = "no-trace-id";
    private final MessageSource messageSource;
    private final Tracer tracer;

    public ApiExceptionHandler(MessageSource messageSource, Tracer tracer) {
        this.messageSource = messageSource;
        this.tracer = tracer;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        final String traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : NO_TRACE_ID;

        final List<ErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse(fieldError.getField(), messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())))
                .toList();

        final CustomProblemDetail problemDetail =
                new CustomProblemDetail(BAD_REQUEST,"Validation failed", "The following errors occurred:", traceId, errors);
        final HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
        ServerHttpObservationFilter.findObservationContext(httpServletRequest).ifPresent(context -> context.setError(ex));

        LOGGER.error("Validation failed: {}", errors);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException ex) {

        final String traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : NO_TRACE_ID;

        final CustomProblemDetail problemDetail =
                new CustomProblemDetail(NOT_FOUND, "Resource not found",
                        "The following errors occurred:", traceId, of(new ErrorResponse(null, getRootCauseMessage(ex))));
        problemDetail.setTitle(NOT_FOUND.getReasonPhrase());
        ServerHttpObservationFilter.findObservationContext(request).ifPresent(context -> context.setError(ex));

        return ResponseEntity.status(NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {

        final String traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : NO_TRACE_ID;

        if (ex.getRequiredType() == UUID.class) {

            final CustomProblemDetail problemDetail =
                    new CustomProblemDetail(BAD_REQUEST, "Invalid format",
                            "Invalid UUID format. Please provide a valid UUID.",
                            traceId, of(new ErrorResponse(ex.getPropertyName(), "Invalid UUID format.")));
            problemDetail.setTitle(BAD_REQUEST.getReasonPhrase());
            ServerHttpObservationFilter.findObservationContext(request).ifPresent(context -> context.setError(ex));

            return ResponseEntity.status(BAD_REQUEST).body(problemDetail);

        }
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(HttpServletRequest request, DataIntegrityViolationException ex) {

        final String traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : NO_TRACE_ID;

        final String userMessage = "Conflict processing the request. A record with a unique value already exists.";
        final String violatedConstraint = extractConstraintName(ex);
        final String field = mapConstraintToField(violatedConstraint);
        final String violatedValue = extractValueFromMessage(ex.getMessage());

        final CustomProblemDetail problemDetail;

        if (Objects.nonNull(field)) {
            problemDetail = new CustomProblemDetail(HttpStatus.CONFLICT, userMessage,
                    "Unique constraint violation. Field: " + field + " with value: " + violatedValue,
                    traceId, of(new ErrorResponse(field, "Unique value already exists.")));

            problemDetail.setTitle(HttpStatus.CONFLICT.getReasonPhrase());
            LOGGER.warn("Known constraint violation (constraint: {}, field: {}): {}", violatedConstraint, field, ex.getMessage());
            ServerHttpObservationFilter.findObservationContext(request).ifPresent(context -> context.setError(ex));
        } else {
            problemDetail = new CustomProblemDetail(HttpStatus.CONFLICT, userMessage,
                    "Unknown constraint violation", traceId, of(new ErrorResponse("unknown", "Unknown constraint violation.")));
            problemDetail.setTitle(HttpStatus.CONFLICT.getReasonPhrase());
            LOGGER.error("Unknown constraint violation: {}", ex.getMessage(), ex);
            ServerHttpObservationFilter.findObservationContext(request).ifPresent(context -> context.setError(ex));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    private String extractConstraintName(DataIntegrityViolationException ex) {
        return Optional.ofNullable(ex.getMostSpecificCause())
                .filter(PSQLException.class::isInstance)
                .map(PSQLException.class::cast)
                .map(PSQLException::getServerErrorMessage)
                .map(ServerErrorMessage::getConstraint)
                .orElse(null);
    }

    private String mapConstraintToField(String constraint) {
        return switch (constraint) {
            case "tb_courses_name_key" -> "name";
            default -> null;
        };
    }

    private String extractValueFromMessage(String message) {
        final var matcher = Pattern.compile("Key \\(name\\)=\\((.*?)\\)").matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }
}
