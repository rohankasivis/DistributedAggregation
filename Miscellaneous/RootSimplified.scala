import akka.actor._

class RootSimplified extends NodeActorsSimplified
{
  // private variables
  private var level:Int = 0
  private val adjacent : scala.collection.immutable.Map[NodeActorsSimplified, Set[NodeActorsSimplified]] = Map.empty
  private var broadcast:Boolean = false
  private val system = ActorSystem("Root_Actor")
  private val root_node = system.actorOf(Props[RootSimplified], name = "root_adjacent")

  def level(adjacent : Set[NodeActorsSimplified], levels : Map[NodeActorsSimplified, Int]) : Option[Int] = {
    // whenever level changes, need to broadcast it to the neighbors

    // setup actor - creates the topology
  }


  def new_actor(nodeActorsSimplified: NodeActorsSimplified) = {
    val node = system.actorOf(Props[NodeActorsSimplified], "new_node")
    root_node ! NewMessage(node)
    node ! NewMessage(node)
    val temp = adjacent + (root_node -> Set(node))
  }

  def fail(nodeActorsSimplified: NodeActorsSimplified) = {
    adjacent diff List(value)
  }

  def skip(nodeActorsSimplified: NodeActorsSimplified, level: Int) = {
    // in the case of root, simply just return
    return;
  }
}