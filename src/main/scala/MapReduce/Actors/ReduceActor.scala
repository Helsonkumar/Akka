
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable.Map
import scala.collection._
import MapReduceApp._

class ReduceActor extends Actor{
  
  def receive : Receive = {
    case MapData(dataList) => sender ! reduceMessage(dataList)
  }
  
  def reduceMessage(message : IndexedSeq[WordCount]) : ReduceData = ReduceData{
    message.foldLeft(Map.empty[String, Int]) {
      (map_buff , wordcount) => {
        if (map_buff contains wordcount.Word)
          map_buff + (wordcount.Word -> (map_buff.get(wordcount.Word).get + 1))
        else
          map_buff + (wordcount.Word -> 1)
      }
    }
    
    
  }
  
}