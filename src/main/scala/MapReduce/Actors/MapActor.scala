

import akka.actor.Actor
import akka.actor.ActorRef
import MapReduceApp._
import scala.collection.mutable.ArrayBuffer


class MapActor extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as",
    "at", "be", "do", "go", "if", "in", "is", "it", "of", "on", "the",
    "to")
    
  val defaultCount: Int = 1
  
  def receive : Receive = {
    case message : String  =>  {
      sender ! formatMessage(message)
      
    }
  }
  
  def formatMessage(message : String) : MapData = MapData {
    message.split("""\s+""").foldLeft(ArrayBuffer.empty[WordCount]){
      (buff , word) => {
        if (!STOP_WORDS_LIST.contains(word)) 
          buff += WordCount(word, 1)
        else
          buff
      }
    }
  }

}