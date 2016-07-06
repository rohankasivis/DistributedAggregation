import akka.actor
import akka.actor.Actor._

class Root extends NodeActors
{
  private var levels:Map[NodeActors, Int] = Map.empty
  private var sent_mass:Map[NodeActors, Int] = Map.empty
  private var received_mass:Map[NodeActors, Int] = Map.empty
  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  private var adjacent:Set[NodeActors] = Set.empty
  private var broadcast:Boolean = false

  def new_entry(nodeActors:NodeActors)
  {
    adjacent += nodeActors
    sent_mass = sent_mass + (nodeActors -> 0)
    received_mass = received_mass + (nodeActors -> 0)
  }

  def remove_entry(nodeActors:NodeActors)
  {

  }

  def level(nodeActors:NodeActors)
  {
    // not implemented here
  }

  def parent()
  {
    // not implemented here
  }

  def send(nodeActors:NodeActors, value:Int)
  {

  }

  def broadcast(value:Int)
  {
    // not implemented here
  }

  // helper function
  def get_index(values:Array[Int], value:Int)
  {
    for(i <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return null
  }

  def get_index(values:Array[NodeActors], value:NodeActors)
  {
    for((i) <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return null
  }

  def receive: Receive = {
    case New(arg1) => val result = {
      send(arg1, Status(0,0))
      new_entry(arg1)
    }

    case Fail(arg1) => val result = {
      val sent_val:Int = sent_mass(arg1)
      val received_val:Int = received_mass(arg1)
      remove_entry(arg1)
      aggregate_mass = aggregate_mass + sent_val - received_val
    }

    case Aggregate(arg1, arg2) => val result = {
      aggregate_mass = aggregate_mass + arg2
      val index:Int = get_index(adjacent, arg1)
      received_mass(index) = received_mass(index) + arg2
    }

    case Local(arg1) => val result = {
      aggregate_mass = aggregate_mass + arg1 - local_mass
      local_mass = arg1
    }

    case Status(arg1, arg2) => val result = {
      // dont do anything here
    }
  }
}
