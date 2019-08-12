package com.gaoxiong.haoke.graphqlController.provider;

import com.gaoxiong.haoke.graphqlController.service.MyDataFetcher;
import com.gaoxiong.haoke.service.HouseResourcesService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName GraphQLProvider
 * @Description TODO
 * @date 2019/8/12 10:37
 */
@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Autowired
    private HouseResourcesService houseResourcesService;

    //注入所有的MyDataFetcher的实现类
    @Autowired
    private List<MyDataFetcher> myDataFetchers;

    @PostConstruct
    public void init() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:haoke.graphqls");
        GraphQLSchema graphQLSchema = buildSchema(file);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema ( File file ) {

        TypeDefinitionRegistry registry = new SchemaParser().parse(file);
        RuntimeWiring wiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(registry, wiring);
    }

    private RuntimeWiring buildWiring () {
        return RuntimeWiring.newRuntimeWiring()
                .type("HaokeQuery", builder ->{
                    for (MyDataFetcher myDataFetcher : myDataFetchers) {
                        builder.dataFetcher(myDataFetcher.fieldName(), myDataFetcher::dataFetcher);
                    }
                    return builder;
                })
                .build();

    }

    @Bean
    public GraphQL graphQL(){
        return this.graphQL;
    }

}
