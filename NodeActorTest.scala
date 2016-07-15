import akka.actor.{ActorRef, ActorSystem, Props}

object NodeActorTest extends App
{

  var neighbors : Map[NodeActors, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], "root_node")
  val node_one = system.actorOf(Props[NonRoot], "node_one")
  val node_two = system.actorOf(Props[NonRoot], "node_two")

  val root:Root = new Root
  val nodeone:NonRoot= new NonRoot
  val nodetwo:NonRoot=new NonRoot

  // root node connects to node two
  root_node ! New(nodeone)
  node_one ! New(nodetwo)

  // node one connects to node two
  node_one ! New(nodetwo)
  node_two ! New(nodeone)

  // set up connection map
  neighbors = neighbors + (root -> Set(node_one))
  neighbors = neighbors + (nodeone -> Set(root_node, node_two))
  neighbors = neighbors + (nodetwo -> Set(node_two))

  // remove node two
  system stop node_two
  neighbors.get(nodetwo) match {
    case Some(s) => s.foreach { n => n ! Fail(nodetwo) }
    case None => ()
  }
  neighbors = neighbors - nodetwo
}