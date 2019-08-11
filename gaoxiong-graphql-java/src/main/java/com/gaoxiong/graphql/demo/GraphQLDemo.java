package com.gaoxiong.graphql.demo;

import com.gaoxiong.graphql.vo.User;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.*;

/**
 * @author gaoxiong
 * @ClassName GraphQLDemo
 * @Description TODO
 * @date 2019/8/11 0011 下午 6:10
 */

public class GraphQLDemo {
    public static void main ( String[] args ) {

        /**
         * schema{ #定义查询
         *     query:  UserQuery
         * }
         * type UserQuery{ #定义查询的类型
         *     user: User  #指定对象以及参数类型
         * }
         * type User{ #定义对象
         *     id:Long!
         *     name:String
         *     age: int
         * }
         */
//倒着来定义queryType
        GraphQLObjectType userObjectType  = GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLLong).build())
                .field(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString).build())
                .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLInt).build())
                .build();

        GraphQLFieldDefinition userFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                .name("user")
//                .dataFetcher(new StaticDataFetcher(new User((long) 1, "gaoxiong", 32)))
                .dataFetcher(env->{
                    Long id = env.getArgument("id");
                    //这时候就可以去数据库查询数据了,这里暂时不实现
                    return new User(id, "zhangsan", 33);
                })
                .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                .type(userObjectType)
                .build();

        GraphQLObjectType userQueryObjectType = GraphQLObjectType.newObject()
                .name("UserQuery")
                .field(userFieldDefinition)
                .build();
        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(userQueryObjectType).build();

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        String query = "{user(id:100){id,name,age}}";
        ExecutionResult result = graphQL.execute(query);
        System.out.println("query = " + query);
//        System.out.println(result.getErrors());
//        System.out.println(result.getData());
//        System.out.println(result.getExtensions());
        System.out.println(result.toSpecification());
    }


}
