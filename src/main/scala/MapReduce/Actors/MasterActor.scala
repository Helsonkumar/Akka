
import akka.actor.Actor
import akka.actor.ActorRef
import MapReduceApp._
import akka.actor._
import akka.routing._

class MasterActor extends Actor{
  
  val mapActor = context.actorOf(Props[MapActor].withRouter(RoundRobinPool(nrOfInstances = 4)) ,name = "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(RoundRobinPool(nrOfInstances =4)) , name = "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor], name = "aggregate")
  
  
  def receive : Receive = {
    case line:String => mapActor ! line
    case mapData : MapData => reduceActor ! mapData
    case reduceData : ReduceData => aggregateActor ! reduceData
    case Result => aggregateActor forward Result
  }
  
}