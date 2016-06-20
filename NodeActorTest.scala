import akka.actor._

object NodeActorTest extends App
{
  var neighbors : Map[NodeActors, Set[NodeActors]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], "root_node")
  val node_one = system.actorOf(Props[NonRoot], "node_one")
  val node_two = system.actorOf(Props[NonRoot], "node_two")

  // root node connects to node two
  root_node ! New(node_one)
  node_one ! New(node_one)

  // node one connects to node two
  node_one ! New(node_two)
  node_two ! New(node_one)

  // set up connection map
  neighbors = neighbors + (root_node -> Set(node_one))
  neighbors = neighbors + (node_one -> Set(root_node, node_two))
  neighbors = neighbors + (node_two -> Set(node_two))

  // remove node two
  system stop node2
  map.get(node2) match {
  Some(s) => s.foreach { n => n ! FailMessage(node2) }
  None => ()
  }
  neighbors = neighbors - node2
}