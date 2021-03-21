### Spring Cloud Gwteway

```kotlin
builder
.routes()
.route { route ->
    route.path("/sitn/**")
        .filters {
            it.stripPrefix(1)
        }
        .uri("http://localhost:8090")
}
.route { route ->
    route.path("/uitn/**")
        .filters {
            it.stripPrefix(1)
        }
        .uri("https://dev-uitn.crsdev.io")
}
.build()
```

Spring Cloud Gwteway와 Webflux를 이용하면 Non blocking 구조의  
API Gateway를 손쉽게 만들 수 있다.  

filter는 Servlet filter와 같이 origin에 요청 전달 전 후로 어떠한 액션을 취할 수 있다.    
예를들어 stripPrefix는 origin에 전달 할 path 가공에 대한 내용이다.  

만약 /sitn/**에서 stripPrefix(1)을 수행하면  
/sitn/studies/1 path는 origin에 /studies/1과 같이 전달 될 것이다.  

가이드 문서를 좀 더 봐야겠지만 테스트 해 본 결과 전달한 요청과 전달 된 응답이  
별도 가공 없이 그대로 오고 가는 것 같다.  