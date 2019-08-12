package com.gaoxiong.haoke.graphqlController;

import com.gaoxiong.haoke.graphqlController.provider.GraphQLProvider;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName HouseResourcesGraphQLController
 * @Description TODO
 * @date 2019/8/12 10:33
 */
@RestController
@RequestMapping("/graphql")
public class HouseResourcesGraphQLController {

    @Autowired()
    private GraphQL graphQL;

    @GetMapping()
    public Map<String, Object> graphql( @RequestParam("query") String query ){
        Map<String, Object> map = graphQL.execute(query).toSpecification();
        return map;
    }



}
