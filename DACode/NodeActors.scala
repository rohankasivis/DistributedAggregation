import akka.actor._

// these are all of the necessary case classes
case class New(arg1:ActorRef)
case class Fail(arg1:ActorRef)
case class Aggregate(arg1:ActorRef, arg2:Int)
case class Local(arg1:Int)
case class Status(arg1:ActorRef, arg2:Option[Int])
case class Drop(arg1:ActorRef, arg2:Int)
case class SendAggregate()
case class sendBroadcast()

// this is the class which the root/non-root will extend
abstract class  NodeActors extends Actor
{
  def new_entry(nodeActors: ActorRef)

  def remove_entry(nodeActors:ActorRef)

  def level(nodeActors:Set[ActorRef], level:Map[ActorRef, Int]): Option[Int]

  def parent(nodeActors:Set[ActorRef], levels:Map[ActorRef, Int]): Option[ActorRef]

  def broadcast_var()
}