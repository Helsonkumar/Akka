
//This is a demo to show how the actor model works
//An Actor is managed by a Supervisor for the Fault Tolerant Chain
//An Actor has State and Behavior
//An Actor changes its State and behavior via Messages sent by another actors
//An Actor System has set of hierarchy for making it fault tolerant

// We have MapActor , ReduceActor , AggregateActor

//As part of Application Development we need to do the following :
//Define the Message structure to be handled by various Actors
//

import scala.collection.mutable._
import akka.actor._
import akka.actor.ActorContext
import scala.concurrent._
import akka.actor.{Actor, Props , ActorRef}
import scala.concurrent.duration._
import akka.util._
import akka.pattern.ask

  
  sealed trait MapReduceMessage 
  case class WordCount(Word:String , Count :Int) extends MapReduceMessage 
  case class MapData(dataList : ArrayBuffer[WordCount]) extends MapReduceMessage
  case class ReduceData(ReduceDataMap : Map[String , Int]) extends MapReduceMessage 
  case object Result extends MapReduceMessage
  
  object MapReduceApp extends App{
  
    val _system = ActorSystem("MapReduceApp")
    val master = _system.actorOf(Props[MasterActor], name = "master")
    implicit val timeout = Timeout(5 seconds)
    implicit val ec = ExecutionContext.global
    
    master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
    master ! "Dog is man's best friend"
    master ! "Dog and Fox belong to the same family"
    master ! "Helson is good Fellow. Helson is also Bad fellow"
  
    Thread.sleep(50000)
    
    val future = master ? Result
    
    Await.result(future, 5 seconds)
    
    future foreach {m => println(m)}
    
    _system.terminate
    
    
}