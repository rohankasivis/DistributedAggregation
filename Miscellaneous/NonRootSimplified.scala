import akka.actor._

class NonRootSimplified extends NodeActorsSimplified
{
  var levels : Array[Int]
  private val adjacent : Map[NodeActorsSimplified, Set[NodeActorsSimplified]] = Map.empty
  private var broadcast:Boolean = false

  def new_actor(nodeActorsSimplified: NodeActorsSimplified) = {

  }

  def fail(nodeActorsSimplified: NodeActorsSimplified) = {

  }

  def status(nodeActorsSimplified: NodeActorsSimplified, level: Int) = {

  }

  def status(nodeActorsSimplified: NodeActorsSimplified) = {
    if(level(adjacent, levels) != level(adjacent - nodeActorsSimplified, levels - ))
  }

  def handle_broadcast() = {
    if(broadcast)
    {
      // send message here
      broadcast = false;
    }
  }

  // helper function
  def remove_element(levels: Array[Int], nodeActorsSimplified: NodeActorsSimplified) : Array[Int] = {
    var position: Int = findElement()
    return levels.filter(! _.contains(nodeActorsSimplified))
  }
}