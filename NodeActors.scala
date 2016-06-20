import akka.actor.Actor.Receive
import akka.actor._

case class New(arg1:NodeActors)
case class Fail(arg1:NodeActors)
case class Aggregate(arg1:NodeActors, arg2:Int)
case class Local(arg1:Int)
case class Status(arg1:NodeActors, arg2:Int)
case class Drop(arg1:NodeActors, arg2:Int)

abstract class NodeActors
{
  // define global variables here
  // also set up the system in such a way like a binary tree, in order for testing purposes
  def new_entry(nodeActors: NodeActors)

  def remove_entry(nodeActors:NodeActors)

  def update_entry(nodeActors:NodeActors)

  def level(nodeActors:NodeActors)

  def parent()

  def send(nodeActors:NodeActors, value:Int)

  def broadcast(value:Int)
}