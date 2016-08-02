import akka.actor.ActorRef

//object Root {}
class Root extends NodeActors
{

  def receive: Receive = {
    case New(neighbor) => {
      System.out.println("received New(" + neighbor + ") in " + self)
      neighbor ! Status(self, Some(0))
      adjacent += neighbor
      sent_mass = sent_mass + (neighbor -> 0)
      received_mass = received_mass + (neighbor -> 0)
    }

    case Fail(neighbor) => {
      System.out.println("received Fail(" + neighbor + ") in " + self)
      sent_mass.get(neighbor) match
      {
        case Some(sent) =>
          received_mass.get(neighbor) match
          {  
            case Some(received) => {
              adjacent -= neighbor
              aggregate_mass = aggregate_mass + sent - received
            }
            case None => ()
          }
        case None => ()
      }
    }

    case Aggregate(sender, aggregate) => {
      System.out.println("received Aggregate(" + sender + ", " + aggregate + ") in " + self)
      received_mass.get(sender) match
      {
        case Some(received) => {
          aggregate_mass = aggregate_mass + aggregate
          received_mass = received_mass + (sender -> (received + aggregate))
        }
        case None => ()
      }
      System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
    }

    case Local(local) => {
      aggregate_mass = aggregate_mass + local - local_mass
      local_mass = local
      System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
    }

    case Status(sender, status) => {

    }
  }
}
