package com.smartcommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import graphql.scalars.ExtendedScalars;

@Configuration
public class GraphQLConfig implements RuntimeWiringConfigurer {

    @Override
    public void configure(graphql.schema.idl.RuntimeWiring.Builder builder) {
        builder.scalar(ExtendedScalars.GraphQLLong);
    }
}
