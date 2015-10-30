import akka.actor._

class Root extends NodeActors
{
  private var level:Int = 0
  private val adjacent : Map[NodeActors, Set[NodeActors]] = Map.empty
  private var broadcast:Boolean = false

  private val system = ActorSystem("Root_Actor")
  private val root_node = system.actorOf(Props[Root], name = "root_adjacent")

  def new_actor() = {
    val node = system.actorOf(Props[NodeActors], "new_node")
    root_node ! NewMessage(node)
    node ! NewMessage(node)
    adjacent += (root_node -> Set(node))
  }

  def fail() = {
    adjacent diff List(value)
  }
}