package com.smartcommerce.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
public class GraphQLAuthInterceptor implements WebGraphQlInterceptor {

    private static final Set<String> PUBLIC_QUERIES = Set.of("categories", "category");

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String query = request.getDocument();
        boolean isPublic = PUBLIC_QUERIES.stream().anyMatch(q -> query != null && query.contains(q));
        
        if (isPublic) {
            log.debug("Public GraphQL query executed");
        } else {
            log.debug("Protected GraphQL query executed");
        }
        
        return chain.next(request);
    }
}
