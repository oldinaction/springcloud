## 说明

- 启动
    - redis、mysql
    - eureka-server、api-gateway-zuul、oauth-server、provider-user、consumer-movie-ribbon
- 获取令牌：http://localhost:5555/api-auth/oauth/token?username=smalle&password=123&grant_type=password&scope=all&client_id=gateway&client_secret=123
- 访问资源：http://localhost:5555/api-movie/movie/1?access_token=602dba4e-82f4-42a9-acba-c4b28b9c602a