import akka.actor._

// these are all of the necessary case classes
case class New(arg1:ActorRef)
case class Fail(arg1:ActorRef)
case class Aggregate(arg1:ActorRef, arg2:Int)
case class Local(arg1:Int)
case class Status(arg1:ActorRef, arg2:Option[Int])
case class Drop(arg1:ActorRef, arg2:Int)
case class SendAggregate()
case class Broadcast()

// this is the class which the root/non-root will extend
abstract class  NodeActors extends Actor
{

  protected var adjacent:Set[ActorRef] = Set.empty
  protected var local_mass:Int = 0
  protected var aggregate_mass:Int = 0
  protected var sent_mass:Map[ActorRef, Int] = Map.empty
  protected var received_mass:Map[ActorRef, Int] = Map.empty
  protected var levels:Map[ActorRef, Int] = Map.empty
  protected var broadcast:Boolean = false

}