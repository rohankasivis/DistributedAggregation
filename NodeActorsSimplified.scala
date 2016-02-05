import akka.actor._

abstract class NodeActorsSimplified
{
  // define global variables here
  // also set up the system in such a way like a binary tree, in order for testing purposes
  def new_actor(nodeActorsSimplified: NodeActorsSimplified)

  def fail(nodeActorsSimplified: NodeActorsSimplified)

  def status(nodeActorsSimplified: NodeActorsSimplified, level: Int)

  def status(nodeActorsSimplified: NodeActorsSimplified)

  def handle_broadcast()
}