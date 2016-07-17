import akka.actor._

case class FailMessage(arg1:Double)
case class New_Message(arg1:Double)

class NeighborSetActors extends Actor
{
  override def receive: Receive = {
    case New_Message(arg1) => NeighborSet.addNeighbor(arg1)
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

  neighborTree ! New_Message(5.4)
  neighborTree ! New_Message(3.1)
  neighborTree ! New_Message(7.8)
  neighborTree ! New_Message(3.4)
  neighborTree ! FailMessage(3.4)
  neighborTree ! FailMessage(7.1)
  neighborTree ! FailMessage(5.4)
}
