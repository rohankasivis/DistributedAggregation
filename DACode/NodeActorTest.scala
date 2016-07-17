import akka.actor.{ActorRef, ActorSystem, Props}

object NodeActorTest extends App
{

  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], "root_node")
  val node_one = system.actorOf(Props[NonRoot], "node_one")
  val node_two = system.actorOf(Props[NonRoot], "node_two")


  root_node ! New(node_one)
  node_one  ! New(node_two)

  Thread.sleep(30000)

  // remove node two
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
}