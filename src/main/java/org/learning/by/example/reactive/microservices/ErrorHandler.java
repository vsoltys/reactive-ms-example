package org.learning.by.example.reactive.microservices;

import org.learning.by.example.reactive.microservices.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Component
class ErrorHandler {

    private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    private static BiFunction<HttpStatus, String, Mono<ServerResponse>> response =
            (status, value) -> ServerResponse.status(status).body(Mono.just(new ErrorResponse(value)),
                    ErrorResponse.class);

    private static HttpStatus getStatus(Throwable error){
        if(error.getClass().equals(InvalidParametersException.class)){
            return HttpStatus.BAD_REQUEST;
        }else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    };

    Mono<ServerResponse> notFound(ServerRequest request) {
        return response.apply(HttpStatus.NOT_FOUND, "not found");
    }

    Mono<ServerResponse> throwableError(Throwable error) {
        logger.error("error raised", error);

        return response.apply(getStatus(error), error.getMessage());
    }
}
