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




