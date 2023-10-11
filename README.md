# 🧟 Undead4j - LiveViews for the JVM

## What is Undead4j?
Undead4j is working towards an implementation of the LiveView protocol for the JVM.  

## Why?
I've written (or co-written) LiveView implementations for [Javascript](https://liveviewjs) and 
[Go](https://github.com/canopyclimate/golive) and thought it would be interesting to write a version for the JVM.  
Also, Java 21 has a preview feature called "[String Templates](https://openjdk.org/jeps/430)" that was the missing
piece that made the templating part straight forward.

## Status
This is very early / PoC stage / "nothing works".  Trying to get the basics working including:
 * Templating - LiveViews require access to both the static and dynamic parts of a template which we use "String Templates" to implement.
 * Protocol Ser/De - Communication between browser and server is over web sockets using JSON so we need to serialize/deserialize message properly.
 * Example Project - It is both motivating and validating to have an example project so there is one based on [Javalin](https://javalin.io/).

## TODO
 * Protocol Logic - Once we can parse the messages, we then need to implement the logic for each message type.
 * Routing - LiveViews are a mix both HTTP and WS routing so we need to figure that out.
 * Building the client javascript - Need to be able to configure and add to the client javascript code.  Right now it is a compiled version from a LiveViewJS project.
 * Components - At some point we'll add LiveComponent support
 * Lots of other things probably...

## Feedback Welcome
I haven't written Java professionally in about 10 years so would love any feedback and/or PRs!