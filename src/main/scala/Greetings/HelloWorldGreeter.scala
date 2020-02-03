package Greetings
import akka.actor.testkit.typed.scaladsl.LogCapturing
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
// This is a demo to show how to code Actors using the latest version of akka-actors-typed api
// This is different from the old API with respect to launching actors, Behaviors ,etc

// We create 2 child actors : Greeter => Greets when a message is sends and responds to the sender
//                            HelloBot => Simply sends a message to the Greeter and receives the responds

//Object to Launch the Greeter Actor
object Greeter {

  /* Defines the Structure of messages handled by this Actor */
  final case class Greet(who: String, replyTo: ActorRef[Greeted])
  final case class Greeted(who: String, from: ActorRef[Greet])

  /*Starts the actor and defines the behavior for the actor once started
    We have not extended the Actors trait like old API and no receive method is implemented*/
  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    println(s"Hello ${message.who}")
    message.replyTo ! Greeted(message.who, context.self)
    Behaviors.same
  }

}

//Object to launch the HelloBot Actor which sends the message to the Greeter Actor
object HelloBot {

  def apply(max: Int): Behavior[Greeter.Greeted] = {
    bot(0, max)
  }

  //Note : We are not modifying count directly to be pure functional
  // Here the "message" is refered in the bot method and not in apply method
  // So seems that the message and context is available implicitly for any method
 private def bot(count: Int, max: Int): Behavior[Greeter.Greeted] = {
    Behaviors.receive { (context, message) =>
      val n = count + 1
      println(s"Greeting $n for ${message.who}")
      if (n == max) {
        Behaviors.stopped
      } else {
        message.from ! Greeter.Greet(message.who, context.self)
        bot(n, max)
      }
    }

  }

}

// Supervisor Actor to launch the child actors
// This receives the "Start"  message
object GreeterMaster {

  final case class Start(name: String)

  def apply(): Behavior[Start] =
    Behaviors.setup { context =>
      val greeterActor = context.spawn(Greeter(), "Greeter")

      Behaviors.receiveMessage { message =>
        val HelloBotActor = context.spawn(HelloBot(max = 4),message.name)
        greeterActor ! Greeter.Greet(message.name, HelloBotActor)
        Behaviors.same
      }

    }
}

object GreeterMain extends App {

  val system: ActorSystem[GreeterMaster.Start] =
    ActorSystem(GreeterMaster(), "HelloWorld")
  
  system ! GreeterMaster.Start("Akka")
  system ! GreeterMaster.Start("World")
  system.terminate()
}
    
