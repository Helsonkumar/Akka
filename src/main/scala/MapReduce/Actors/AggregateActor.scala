
import akka.actor.Actor
import akka.actor.ActorRef
import MapReduceApp._
import scala.collection.mutable._

class AggregateActor extends Actor{
  
  val finalReducedMap = new HashMap[String , Int] 
  
  def receive: Receive = {
    case ReduceData(dataMap) => finalReducer(dataMap)
    case Result => sender ! finalReducedMap
  }
  
  def finalReducer(message : Map[String, Int]) : Unit = {
    for ((key , value) <- message) {
      if (finalReducedMap contains key)
        finalReducedMap(key) = finalReducedMap.get(key).get + value
      else
        finalReducedMap += (key -> value)
    }
    
    
  }
  
  
  
}