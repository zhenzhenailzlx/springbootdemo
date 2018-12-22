1.拆分环境test、dev、prod   
application.properties 重命名为application.yml   
复制三份   
application.yml
```   
spring:      
  profiles:      
    active: dev
```      
application-dev.yml   
application-test.yml   
application-prod.yml    
java -jar xxx.jar --spring.profiles.active=dev    

2.热部署
```    
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-devtools</artifactId>  
    <optional>true</optional><!-- optional=true,依赖不会传递，该项目依赖devtools；之后依赖myboot项目的项目如果想要使用devtools，需要重新引入 -->   
</dependency>     
``` 
3.使用swagger 
```   
<dependency>
	<groupId>io.springfox</groupId>  
	<artifactId>springfox-swagger2</artifactId>   
	<version>2.8.0</version>   
</dependency>   
<dependency>   
	<groupId>io.springfox</groupId>   
	<artifactId>springfox-swagger-ui</artifactId>   
	<version>2.8.0</version>   
</dependency>   
``` 
APP上加註解@EnableSwagger2   
类上使用   
@Api(description = "客服的控制器")   
方法上   
@ApiOperation(value="测试连通")   
参数对象的属性上   
@ApiModelProperty("企业ID不能为空")   
参数对象上   
@ApiModel("demoDto测试")   
参数上   
@ApiParam(value = "主键", required = true)  
 

http://localhost:9300/springboot/swagger-ui.html#/   
   
4.返回前端的日期格式化  
```  
spring:   
  jackson:   
    time-zone: GMT+8   
    date-format: yyyy-MM-dd HH:mm:ss   
``` 
5.使用 lombok   
maven上下载lombookjar包   
java -jar lombook。jar执行，绑定eclipse的安装路径  
```  
<dependency>   
	<groupId>org.projectlombok</groupId>   
	<artifactId>lombok</artifactId>   
</dependency>  
```  

@Data  

@Slf4j  

6.使用druid数据库连接池 
```  
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid-spring-boot-starter</artifactId>
	<version>1.1.10</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>				

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
``` 

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

``` 
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        driverClassName: com.mysql.jdbc.Driver
        druid:
            url: jdbc:mysql://localhost:3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8
            username: root
            password: root
            initial-size: 10
            max-active: 100
            min-idle: 10
            max-wait: 60000
            pool-prepared-statements: true
            max-pool-prepared-statement-per-connection-size: 20
            time-between-eviction-runs-millis: 60000
            min-evictable-idle-time-millis: 300000
            #validation-query: SELECT 1 FROM DUAL
            test-while-idle: true
            test-on-borrow: false
            test-on-return: false
            stat-view-servlet:
                enabled: true
                url-pattern: /druid/*
                login-username: admin
                login-password: admin
            filter:
                stat:
                    log-slow-sql: true
                    slow-sql-millis: 1000
                    merge-sql: false
                wall:
                    config:
                        multi-statement-allow: true
``` 
 
 监控路径  
http://localhost:9300/springboot/druid   

7.代码中的常量使用枚举类型   
``` 
public enum ResultEnum {   
	
	UNKONW_ERROR(-1,"未知错误"),
	SUCCESS(0,"成功"),
	
	;
	private Integer code;
	private String msg;
	
	
	private ResultEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}  
}
```    

9.配置跨域 
```   
package com.zhenzhen.demo.springboot.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
@Component
public class HeadersCORSFilter implements Filter {
	

    public HeadersCORSFilter() {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String reqUrl = req.getHeader("Origin");
        this.crossDomain(req, resp, reqUrl);
        chain.doFilter(req, resp);
	}
	
	private void crossDomain(HttpServletRequest request, HttpServletResponse response, String url) {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", url );
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
    }

	public void init(FilterConfig fConfig) throws ServletException {}

}
```    

9. 使用BeanValidator 

```   
package com.zhenzhen.demo.springboot.common.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class BeanValidatorUtil {
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	public static String check(Object object) throws RuntimeException {
		StringBuffer resutlStr = new StringBuffer();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Object>> validateResult = validator.validate(object,new Class[0]);
		if (!validateResult.isEmpty()) {
			Iterator<ConstraintViolation<Object>> iterator = validateResult.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<Object> violation = iterator.next();
				resutlStr.append(violation.getMessage()+",");
			}
		}
		if(resutlStr.length()>0) {
			resutlStr.deleteCharAt(resutlStr.length()-1);
			return resutlStr.toString();
		}
		return null;
	}
}
```   

使用 
```  
package com.zhenzhen.demo.springboot.condition;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HelloCondition {
	@ApiModelProperty(value = "名字",required = true)
	@NotEmpty(message = "名字不能为空")
	private String name;
	
	@ApiModelProperty(value = "开始日期",required = true)
	@NotNull(message = "开始时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	
	@ApiModelProperty(value = "结束日期",required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "接收时间不能为空")
	private Date endDate;
	
	public void setStartDate(Date startDate) {
		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		this.endDate = endDate;
	}
}

package com.zhenzhen.demo.springboot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhenzhen.demo.springboot.common.utils.BeanValidatorUtil;
import com.zhenzhen.demo.springboot.condition.HelloCondition;
import com.zhenzhen.demo.springboot.dto.HelloDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@RestController
@Api(description = "hello的控制器")
@Log
public class HelloController {

	@GetMapping("/hello")
	@ApiOperation(value="hello 方法")
	public HelloDto hello(HelloCondition helloCondition) {
		BeanValidatorUtil.check(helloCondition);
		log.info("输入参数"+helloCondition);
		return new HelloDto("真哥", new Date());
	}
}
``` 
10. 封装result和统一异常处理  
```  
package com.zhenzhen.demo.springboot.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenzhen.demo.springboot.common.result.Result;
import com.zhenzhen.demo.springboot.common.utils.ResultUtil;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandle {


	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result handle(Exception e) {
		if(e instanceof SprintBootException) {
			SprintBootException sprintBootException = (SprintBootException)e;
			return ResultUtil.error(sprintBootException.getCode(), sprintBootException.getMessage());
		}else{
			log.error(e.toString());
			return ResultUtil.error("-1", "未知错误");
		}
	}
}

package com.zhenzhen.demo.springboot.common.utils;

import com.zhenzhen.demo.springboot.common.result.Result;

public class ResultUtil {
	
	public static Result success(Object object) {
		Result result = new Result();
		result.setCode("1");
		result.setData(object);
		result.setMessage("成功");
		return result;
	}
	
	public static Result success() {
		Result result = new Result();
		result.setCode("1");
		result.setMessage("成功");
		return result;
	}
	public static Result success(String code,String msg) {
		Result result = new Result();
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}
	
	public static Result success(String code,String msg,Object object) {
		Result result = new Result();
		result.setCode(code);
		result.setMessage(msg);
		result.setData(object);
		return result;
	}
	
	public static Result error(String code,String msg ) {
		Result result = new Result();
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}

}


package com.zhenzhen.demo.springboot.common.result;
public class Result {

	//1 表示成功，0表示失败
	private String code;
	private Object data;
	private String message;
	
	
	
	public Result() {
		super();
	}

	public Result(String code) {
		super();
		this.code = code;
	}
	

	public Result(Object data) {
		super();
		this.code = "1";
		this.data = data;
	}




	public Result(String code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}



	public Result(String code, Object data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}
	
	
	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}  
```  


11.使用aop统一处理参数和返回值日志  
```  
package com.zhenzhen.demo.springboot.common.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerMethodExecutionLogAspect {
	
	private static final String START_TIME = "requestStartTime";

	@Pointcut("execution(public * com.zhenzhen.demo.*.controller.*.*(..))")
	public void log() {
	}
	
	@Before("log()")
	public void doStart(JoinPoint joinpoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = attributes.getRequest();
		long start = System.currentTimeMillis();
		req.setAttribute(START_TIME, start);
		log.info("********request start,url = {}",req.getRequestURL().toString());
		log.info("Method = {}",req.getMethod());
		log.info("ip = {}",req.getRemoteAddr());
		log.info("class_method = {}",joinpoint.getSignature().getDeclaringTypeName()+"."+joinpoint.getSignature().getName());
		log.info("args = {}",joinpoint.getArgs());
		
	}
	
	@AfterReturning(returning = "object",pointcut="log()")
	public void doEnd(Object object) {
        log.info("args = {}",object.toString());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        String url = req.getRequestURL().toString();
		long start = (Long) req.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("********request completed. url:{}, cost:{}", url, end - start);
	}

} 
```   


12.配置包名和跳过测试   

```  
<build>   
    <finalName>sprintbootdemo</finalName>   
	<plugins>   
		<plugin>   
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<configuration>
				<skipTests>true</skipTests>
			</configuration>
		</plugin>
	</plugins>
</build>

``` 
