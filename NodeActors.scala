import akka.actor.Actor.Receive
import akka.actor._

// these are all of the necessary case classes
case class New(arg1:NodeActors)
case class Fail(arg1:NodeActors)
case class Aggregate(arg1:NodeActors, arg2:Int)
case class Local(arg1:Int)
case class Status(arg1:NodeActors, arg2:Option[Int])
case class Drop(arg1:NodeActors, arg2:Int)

// this is the class which the root/non-root will extend
abstract class NodeActors
{
  def new_entry(nodeActors: NodeActors)

  def remove_entry(nodeActors:NodeActors)

  def level(nodeActors:Set[NodeActors], level:Map[NodeActors, Int])

  def parent(nodeActors:Set[NodeActors], level:Map[NodeActors, Int])

  def broadcast(value:Int)
}