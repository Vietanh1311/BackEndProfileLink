package com.example.profilelinks.error;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@RestControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";

    private String applicationName="GHTK-ProfileLink";

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(PATH_KEY, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                    .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                    .withCause(((DefaultProblem) problem).getCause())
                    .withDetail(problem.getDetail())
                    .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
                .withTitle("Method argument not valid")
                .withStatus(defaultConstraintViolationStatus())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
                .with(FIELD_ERRORS_KEY, fieldErrors)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(EmailAlreadyUsedException  ex, NativeWebRequest request) {
        EmailAlreadyUsedException problem = new EmailAlreadyUsedException();
        return create(problem, request, HeaderUtil.createFailureAlert(applicationName,  true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex, NativeWebRequest request) {
        LoginAlreadyUsedException problem = new LoginAlreadyUsedException();
        return create(problem, request, HeaderUtil.createFailureAlert(applicationName,  true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidPasswordException(InvalidPasswordException ex, NativeWebRequest request) {
        return create(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInternalServerErrorException(InternalServerErrorException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.CONFLICT)
                .with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEntityNotFound(EntityNotFoundException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Object does not exist")
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEntityExistsException(EntityExistsException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.CONFLICT)
                .withTitle("Object already exists")
                .build();
        return create(ex, problem, request);

    }

//    @ExceptionHandler
//    public ResponseEntity<Problem> errBindException(BindException ex, NativeWebRequest request) {
//        Problem problem = Problem.builder()
//                .withStatus(Status.BAD_REQUEST)
//                .withTitle(ex.getMessage())
//                .build();
//        return create(ex, problem, request);
//    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameNotFoundException(UsernameNotFoundException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }
}
