import akka.actor._

abstract class NodeActors
{
  // define global variables here
  // also set up the system in such a way like a binary tree, in order for testing purposes
  def new_entry(nodeActorsSimplified: NodeActors)

  def fail(nodeActorsSimplified: NodeActors)

  def status(nodeActorsSimplified: NodeActors, level: Int)

  def status(nodeActorsSimplified: NodeActors)

  def handle_broadcast()


}