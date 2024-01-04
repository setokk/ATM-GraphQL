package edu.setokk.atm.error;

import edu.setokk.atm.error.exception.BusinessLogicException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ExceptionResolver implements DataFetcherExceptionResolver {
    private final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable ex,
                                                     DataFetchingEnvironment env)
    {
        if (ex instanceof BusinessLogicException)
        {
            BusinessLogicException blex = (BusinessLogicException) ex;

            GraphQLError error = GraphqlErrorBuilder.newError()
                    .message(blex.getHttpStatus().value() + ":,:" + blex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();

            return Mono.just(List.of(error));
        } else if (ex instanceof AccessDeniedException) {
            log.warn("Unauthorized access to: " + env.getSource());
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .message("Unauthorized access denied")
                    .build();

            return Mono.just(List.of(error));
        } else {
            return null;
        }
    }
}
