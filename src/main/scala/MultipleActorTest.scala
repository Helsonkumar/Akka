// Demo to check the spawning of Different  actors
import akka.actor.typed.{ Behavior, ActorRef, ActorSystem }
import akka.actor.typed.scaladsl.Behaviors

object actor1 {
  def apply(): Behavior[Int] =
    Behaviors.receiveMessage { i =>
      println(this.hashCode + "  i ")
      Behaviors.same
    }
}

object Main {
  def apply(): Behavior[Int] = {
    Behaviors.setup { context =>
      val act1 = context.spawn(actor1(), "helson")

      val act2 = context.spawn(actor1(), "Gladys")

      Behaviors.receiveMessage { msg =>
        act1 ! msg
        act2 ! msg
        Behaviors.same
      }
    }
  }
}

object Driver extends App {
  val system: ActorSystem[Int] = ActorSystem(Main(), "System")

  system ! 28
  Thread.sleep(5000)
  system.terminate
} 
