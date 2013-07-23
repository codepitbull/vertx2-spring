# A Simplistic Spring Language Implementation for vert.x 2 #
This project contains the following parts:
## vertx2-spring-lang ##
The actual SPring language implementation for vert.x. It's just three classes so don't be afraid to take a look.
##vertx2-spring-lang-demo##
A little vert.x-module showing how to set up verticals using the language implementation and Spring.
## vertx2-spring-lang-app ##
Just one class in here with a _main_-method. Run it to start vert.x and have the modules deployed.
## Import to IDE ##
I tested with IntelliJ Idea. Simply clone the repo, point Idea to the included top-level _build.gradle_ and watch how the project is being imported.
##Installation##
Use _gradle install_ on _vertx2-spring-lang_ and _vertx2-spring-lang-demo_ to install the modules to your local maven repository.
## Running the Example'' ##
After installing the module start the class from _vertx2-spring-lang-app_, point your browser to [http://localhost:8071](http://localhost:8071).
##What just happened##
Take a look at _mod.json_ from vertx2-spring-lang-demo. The following line shows how to bootstrap the ApplicationContext:

    "main":"spring:de.codepitbull.vertx.spring.lang.demo.TestConfiguration"

Looking at _TestConfiguration.java_ you will see that _HttpVerticle_ is registered a Spring-bean. It derives from _SpringVerticle_ to allow auto wiring of _Container_ and _Vertc_. But just registering it doesn't do the job, we need to deploy it to vert.x.

For this I use the _InitBean_. It's a _ApplicationListener<ContextStartedEvent>_, so it's waiting until the context is started to do its work. It uses the following line to deploy the vertical:

    container.deployVerticle("spring:httpVerticle")

I am using the bean name as it is defined in _TestConfiguration_ to deploy the Spring Bean as a vertical.
##Limitations##
- It only supports _@Configuration_-annotated classes. The times of XML are over and I'd like to keep it that way.
- You can load ONE _@Configuration_-class for your vert.x instance. As long as I don't see a need to extend this I will keep it that way

###Register it the vert.x-way###
To use the language implementation in your vert.x-distribution you have to add it.
In the _conf_-directory open _langs.properties_ and add the following line:

__spring=de.codepitbull.vertx&tilde;spring-lang&tilde;1.0.0-final:de.codepitbull.vertx.spring.lang.SpringVerticleFactory__
