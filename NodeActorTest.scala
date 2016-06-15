import akka.actor._

object NodeActorTest
{
  var neighbors : Map[NodeActors, Set[NodeActors]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[NodeActors], "root_node")
  val node_one = system.actorOf(Props[NodeActors], "node_one")
  val node_two = system.actorOf(Props[NodeActors], "node_two")


}