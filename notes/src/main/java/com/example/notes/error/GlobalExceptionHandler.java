package com.example.notes.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) Bean Validation for @Valid @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var pd = baseProblem(HttpStatus.BAD_REQUEST, "Validation failed",
                URI.create("urn:problem:validation"), req);

        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::fieldErrorToMap)
                .toList();

        pd.setProperty("errors", errors);
        return pd;
    }

    // 2) Validation for @RequestParam/@PathVariable etc (Spring 6.1+)
    @ExceptionHandler(HandlerMethodValidationException.class)
    ProblemDetail handleMethodValidation(HandlerMethodValidationException ex, HttpServletRequest req) {
        var pd = baseProblem(HttpStatus.BAD_REQUEST, "Validation failed",
                URI.create("urn:problem:validation"), req);

        var errors = ex.getParameterValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream()
                        .map(error -> Map.of(
                                "parameter", String.valueOf(r.getMethodParameter().getParameterName()),
                                "message", String.valueOf(error.getDefaultMessage())
                        )))
                .toList();

        pd.setProperty("errors", errors);
        return pd;
    }


    // Optional (common if you validate in service layer or manually)
    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var pd = baseProblem(HttpStatus.BAD_REQUEST, "Validation failed",
                URI.create("urn:problem:validation"), req);

        var errors = ex.getConstraintViolations().stream()
                .map(this::violationToMap)
                .toList();

        pd.setProperty("errors", errors);
        return pd;
    }

    // 3) Domain "not found"
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        var pd = baseProblem(HttpStatus.NOT_FOUND, "Not found",
                URI.create("urn:problem:resource-not-found"), req);

        // Safe, user-facing detail (don’t include IDs if you consider them sensitive)
        pd.setDetail(ex.getMessage());
        return pd;
    }


    // --- helpers ---
    // shapes the fields for all problems
    private ProblemDetail baseProblem(HttpStatus status, String title, URI type, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        pd.setType(type);

        // instance should identify "this occurrence" (path is a practical choice)
        pd.setInstance(URI.create(req.getRequestURI()));

        // Extra non-standard fields are allowed (Spring supports this) :contentReference[oaicite:3]{index=3}
        pd.setProperty("timestamp", OffsetDateTime.now().toString());

        // If you already use correlation IDs, surface them:
        String requestId = req.getHeader("X-Request-Id");
        if (requestId != null && !requestId.isBlank()) {
            pd.setProperty("requestId", requestId);
        }

        return pd;
    }
    // Converts Spring’s internal FieldError object into a safe, client-friendly structure.
    private Map<String, String> fieldErrorToMap(FieldError fe) {
        return Map.of(
                "field", fe.getField(),
                "message", fe.getDefaultMessage()
        );
    }
    // structures constraint violations
    private Map<String, String> violationToMap(ConstraintViolation<?> v) {
        return Map.of(
                "path", String.valueOf(v.getPropertyPath()),
                "message", v.getMessage()
        );
    }
}
