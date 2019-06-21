# Micronaut, Dragon-Slayer (Spring/boot) or just another framework
Code used for my conf talk "Micronaut, Dragon-Slayer (Spring/boot) or just another framework "

We will look at simple use case, we will build web application in Spring Boot and Micronaut. Both Code 
bases need to meet this requirements
 
- REST API
- DB (connect to DB and be able to read and write data to it)
- Service (way to organize code into "Services")
- Template engine for HTML (some way of creating dynamic front end)
- Security (ability to protect parts of application)

## Environment on my machine :)

- Apache Maven 3.5.2
- Java version: 11.0.2, vendor: AdoptOpenJDK

## Sprint Boot example

First we need to go to start.spring.io and add this dependencies
- Lombok
- Spring Web Starter
- Sping Security
- Thymeleaf
- H2 Database
- Spring Data JPA

After we setup the name and group we can download the code and start coding.

Result of coding can be seen in **bookstore-springboot**.

### DB

For database we will use H2 in memory database.

#### Pojo

Let us open class *Book*, as you can see it is simple *Entity* class, Lombok is used so 
that we don't need to write all getters and setters. 

#### Repository

We also have very simple repository *BookRepository*. There is almost no code there
since we relay completely on Spring Data to do all for us.

### Service

Next let us open *BookService*. As you can see it is simple standard Spring service.
All that we needed to do is add annotation **@Service**, inject BookRepository and define simple methods.

### REST API

REST API is defined in *BookControler* as standard Spring RestController. Again 
we relay on Spring annotations to do the heavy lifting for us.

Only thing left is to define to end points. One for retrieving all books
- /books
and one more for adding books
- /admin/book?title=<book title>

We use GET in both cases for simplicity, never do something like this in production.

### Security

Security is handled in *SimpleSecurityJavaConfig* which extends WebSecurityConfigurerAdapter.
Here we define that endpoint **/books** is open to everyone, while endpoint /admin/book is accessible only by authorised users.

### HTML template

Last part of chalange is to have way to generate dynamic front end, we will use Thymeleaf for that.

In **/resources/template** there is **login.html**, last thing is to connect some url with this template, for that we will use
**SimpleConfig** which implements WebMvcConfigurer, and add code to map requests to **/login** with our login.html template.

After we compile the code and start application, spring will generate random password for us, as username use **user**. Use it when accessing protected part of application.
```
Using generated security password: 1ed406bf-a80e-4763-b106-4b75ff73c6e6
```

## Micronaut

Let us look now how we can solve same challenge in Micronaut.

There is no web starter like there is for Spring (start.spring.io). First we need to install [SDKMan](https://sdkman.io/).
With SDKMan we can install a lot of things. 

```bash
$ sdk list
```

To install Micronaut enter this command

```bash
$ sdk install micronaut

```

if you already have Micronaut installed, you can check which version you have by typing 

```bash

$ sdk list micronaut
================================================================================
Available Micronaut Versions
================================================================================
 > * 1.0.4                                                                      
     1.0.3                                                                      
     1.0.2                                                                      

================================================================================
+ - local version
* - installed
> - currently in use
================================================================================


```

Now that we have micronaut installed we can start its CLI

```
$ mn
```

To list all possible profiles of micronaut we need to enter

```
mn> list-profiles
| Available Profiles
--------------------
  cli                 The cli profile
  configuration       The profile for creating the configuration
  federation          The federation profile
  function-aws        The function profile for AWS Lambda
  function-aws-alexa  The function profile for AWS Alexa-Lambda
  grpc                Profile for Creating GRPC Services
  kafka               The Kafka messaging profile
  profile             A profile for creating new Micronaut profiles
  rabbitmq            The RabbitMQ messaging profile
  service             The service profile

```

To list all profiles run this command

```
mn> profile-info service
```


### Initial skeleton application

To create init application we need to run this command

```
$ mn create-app <app name>
```

it will use gradel by default, to create app using maven as build tool do this 

```
$ mn create-app <app name> --build maven
```

to add Hibernate JPA we need to add **--features=hibernate-jpa**, to also add security run this command

```
$ mn create-app <app name> --build maven --features=hibernate-jpa,security-session 
```

for example in our case full command wold be 

```
$ mn create-app bookstore-micronaut --build maven --features=hibernate-jpa,security-session
```

Let us rename package to xyz.itshark.conf.talk.bookstore.micronaut, and let us for now delete tests.

In resources/applicatino.yml for now comment security part

```yaml

---
micronaut:
    application:
        name: bookstore-micronaut

---
datasources:
  default:
    url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    driverClassName: org.h2.Driver
    username: sa
    password: ''    
jpa:
  default:
    properties:
      hibernate:
        hbm2ddl:
          auto: update


#---
#micronaut:
#  security:
#    enabled: true
#    endpoints:
#      login:
#        enabled: true
#      logout:
#        enabled: true
#    session:
#      enabled: true
#      loginSuccessTargetUrl: /
#      loginFailureTargetUrl: /
---
datasources.default: {}
```

Result of this command is in directory **bookstore-micronaut-step-01**.

### Database

First we need to add Pojo class for Book which will be Entity class.

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
```

In this case we will not use Lombok, so we will write code for getters and setters. 
Lombok generate getter and setters in comple time, since Micronaut is also doing a log of work in compile time they can clash one with other. 
They can work together but for this simple use case it is faster just not to use Lombok.

Next is to add Repository, in this case we can't use Spring Data so we need to write code like this

```java
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class BookRepository  {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(@CurrentSession EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        String sqlString = "Select o from Book as o";
        TypedQuery<Book> query = entityManager.createQuery(sqlString, Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Book save(String title) {
        Book book = new Book();
        book.setTitle(title);
        entityManager.persist(book);

        return book;
    }
}
```

Result of this command is in directory **bookstore-micronaut-step-02**.

### Service

Let us create **BookService** now

```java
@Singleton
public class BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book addBook(String title) {
        return bookRepository.save(title);
    }
}
```

Result of this command is in directory **bookstore-micronaut-step-03**.


### REST API

To add REST API we need to add **BookController**

```java
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import xyz.itshark.conf.talk.bookstore.micronaut.pojo.Book;
import xyz.itshark.conf.talk.bookstore.micronaut.service.BookService;

import java.util.List;

@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @Get("/books")
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @Get("/admin/book")
    public Book addBook(@QueryValue("title") String title) {
        return bookService.addBook(title);
    }
}
```

Result of this command is in directory **bookstore-micronaut-step-04**.

At this moment you can build the code and start applicatino and check if all works as intended.

### Security & HTML Template

Let us know add security. 

First uncomment security part of resources/application.yaml, then change url for failed authentication to **/login/authFailed**

```yaml

---
micronaut:
    application:
        name: bookstore-micronaut

---
datasources:
  default:
    url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    driverClassName: org.h2.Driver
    username: sa
    password: ''    
jpa:
  default:
    properties:
      hibernate:
        hbm2ddl:
          auto: update


---
micronaut:
  security:
    enabled: true
    endpoints:
      login:
        enabled: true
      logout:
        enabled: true
    session:
      enabled: true
      loginSuccessTargetUrl: /
      loginFailureTargetUrl: /login/authFailed
---
datasources.default: {}
```

Then we need to add AuthenticationProvider, let us create class **SimpleAuthenticationProvider**
We will define user with username **user** and password **password**


```java
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class SimpleAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if(authenticationRequest.getIdentity().equals("user") && authenticationRequest.getSecret().equals("password")) {
            return Flowable.just(new UserDetails("user",new ArrayList()));
        } else return Flowable.just(new AuthenticationFailed());
    }
}
```

Next thing we need to do is to secure our end points. For that we will use annotation **@Secured**

```java
@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @Get("/books")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @Get("/admin/book")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Book addBook(@QueryValue("title") String title) {
        return bookService.addBook(title);
    }
}
```

Just like with the Spring Boot example, url */books* will be open to everyone, while url */admin/book* only to authenticated users.

Let us now add loging and home page to allow users to login and also to demonstrage HTML template engine. 
Again we will use Thymeleaf, so we need to update pom.xml and add dependencies 

```xml
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views</artifactId>
    </dependency>
    <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf</artifactId>
      <version>3.0.11.RELEASE</version>
    </dependency>
```

Then create directory **views** in directory **resources** and add files auth.html and home.html.

auth.html
```html
<!DOCTYPE html>
<html>
<head>
    <title>Auth</title>
</head>
<body>
<div th:if="${errors}">
    Invalid username and password.
</div>
<form action="/login" method="POST">
    <div><label> User Name : <input type="text" name="username" id="username"/> </label></div>
    <div><label> Password: <input type="password" name="password" id="password"/> </label></div>
    <div><input type="submit" value="Login"/></div>
</form>
</body>
</html>
```

home.html
```html
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
<section>
    <h1 th:if="${loggedIn}">username: <span th:text="${username}"></span></h1>
    <h1 th:unless="${loggedIn}">You are not logged in <a href="/login/auth">Login</a></h1>
</section>
</body>
</html>
```

Last thing left is to connect this templates with urls. For that we will add LoginController and HomeController

```java
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Secured("isAnonymous()")
@Controller("/")
public class HomeController {

    @Get("/")
    @View("home")
    Map<String, Object> index(@Nullable Principal principal) {
        Map<String, Object> data = new HashMap<>();
        data.put("loggedIn", principal!=null);
        if (principal!=null) {
            data.put("username", principal.getName());
        }
        return data;
    }
}

```


```java
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Secured("isAnonymous()")
@Controller("/login")
public class LoginController {

    @Get("/auth")
    @View("auth")
    public Map<String, Object> auth() {
        return new HashMap<>();
    }

    @Get("/authFailed")
    @View("auth")
    public Map<String, Object> authFailed() {
        return Collections.singletonMap("errors", true);
    }
}
```

Result of this command is in directory **bookstore-micronaut-final**.