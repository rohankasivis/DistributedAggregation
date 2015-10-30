import akka.actor._

class NonRoot extends NodeActors
{
  private var level:Int = _
  private val adjacent : Map[NodeActors, Set[NodeActors]] = Map.empty
  private var broadcast:Boolean = false

  def new_actor() = {

  }

  def fail() = {

  }
}