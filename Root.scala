import akka.actor
import akka.actor.Actor._
import akka.actor.{ActorSystem, Props}

class Root extends NodeActors
{
  private var levels:Map[NodeActors, Int] = Map.empty
  private var sent_mass:Map[NodeActors, Int] = Map.empty
  private var received_mass:Map[NodeActors, Int] = Map.empty
  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  private var adjacent:Set[NodeActors] = Set.empty
  private var broadcast:Boolean = false
  val system = ActorSystem("NodeActors")

  def new_entry(nodeActors:NodeActors)
  {
    adjacent += nodeActors
    sent_mass = sent_mass + (nodeActors -> 0)
    received_mass = received_mass + (nodeActors -> 0)
  }

  def remove_entry(nodeActors:NodeActors)
  {
    adjacent -= nodeActors
  }

  def level(nodeActors:Set[NodeActors], levels:Map[NodeActors, Int]):Int=
  {
    // not implemented here
    0
  }

  def parent(nodeActors:Set[NodeActors], level:Map[NodeActors, Int])
  {
    // not implemented here
  }

  def send(nodeActors:NodeActors, value:Status)
  {
    val act = system.actorOf(Props[NodeActors])
    act ! value
  }

  def broadcast(value:Int)
  {
    // not implemented here
  }

  // helper function
  def get_index(values:Array[Int], value:Int):Int=
  {
    for(i <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return 0
  }

  def get_index(values:Array[NodeActors], value:NodeActors):Int=
  {
    for((i) <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return 0
  }

  def receive: Receive = {
    case New(arg1) => val result = {
      send(arg1, Status(null, 0))
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
      val index:Int = get_index(adjacent.toArray, arg1)
      received_mass.get(arg1).get + arg2
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