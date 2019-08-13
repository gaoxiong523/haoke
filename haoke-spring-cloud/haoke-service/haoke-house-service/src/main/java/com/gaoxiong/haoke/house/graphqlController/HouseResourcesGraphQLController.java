package com.gaoxiong.haoke.house.graphqlController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName HouseResourcesGraphQLController
 * @Description TODO
 * @date 2019/8/12 10:33
 */
@RestController
@RequestMapping("/graphql")
@CrossOrigin
public class HouseResourcesGraphQLController {

    @Autowired()
    private GraphQL graphQL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @GetMapping()
    public Map<String, Object> graphql( @RequestParam("query") String query ){
        Map<String, Object> map = graphQL.execute(query).toSpecification();
        return map;
    }

    @PostMapping
    public Map<String, Object> postgraphql( @RequestBody String json ) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(json);
        if (jsonNode.has("query")) {
            String query = jsonNode.get("query").asText();
            Map<String, Object> map = graphQL.execute(query).toSpecification();
            return map;
        }
        return null;
    }


}
