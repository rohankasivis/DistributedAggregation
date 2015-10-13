import akka.actor._

case class FailMessage(arg1:Double)
case class NewMessage(arg1:Double)

class NeighborSetActors extends Actor
{
  override def receive: Receive = {
    case NewMessage(arg1) => NeighborSet.addNeighbor(arg1)
      println(arg1 + " has successfully been added to the NeighborTree.")
      sender ! arg1 + " has successfully been added to the NeighborTree."

    case FailMessage(arg1) => NeighborSet.removeNeighbor(arg1)
      println(arg1 + " has successfully been removed from the NeighborTree.")
      sender ! arg1 + " has successfully been removed from the NeighborTree."
  }
}

object NeighborTest extends App
{
  val system = ActorSystem("NeighborSetSystem")
  val neighborTree = system.actorOf(Props[NeighborSetActors], name = "Neighbors")

  neighborTree ! NewMessage(5.4)
  neighborTree ! NewMessage(3.1)
  neighborTree ! NewMessage(7.8)
  neighborTree ! NewMessage(3.4)
  neighborTree ! FailMessage(3.4)
  neighborTree ! FailMessage(7.1)
  neighborTree ! FailMessage(5.4)
}
