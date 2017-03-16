package scala.day3

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by Administrator on 2017/1/16.
  */
class Master(val host: String, val port: Int) extends Actor {
  val idToWorker = new mutable.HashMap[String, WorkerInfo]()

  val workers = new mutable.HashSet[WorkerInfo]()

  val CHECK_INTERVAL = 15000

  override def preStart(): Unit = {
    println("preStart")
    import context.dispatcher
    context.system.scheduler.schedule(0 millis, CHECK_INTERVAL millis, self, CheckTimeOutWorker)

  }

  override def receive: Receive = {
    case RegisterWorker(id, memory, cores) => {
      if (!idToWorker.contains(id)) {
        val workerInfo = new WorkerInfo(id, memory, cores)
        idToWorker(id) = workerInfo
        workers += workerInfo
        sender ! RegisteredWorker((s"akka.tcp://MasterSystem@$host:$port/user/master"))
      }
    }
    case Heartbeat(id) => {
      if (idToWorker.contains(id)) {
        val workerInfo = idToWorker(id)
        val currentTime = System.currentTimeMillis()
        workerInfo.LastHeartbeatTime = currentTime
      }

    }
    case CheckTimeOutWorker => {

      val currentTime = System.currentTimeMillis()
      val toReomve = workers.filter(x => currentTime - x.LastHeartbeatTime > CHECK_INTERVAL)
      for (w <- toReomve){
        workers -= w
        idToWorker -= w.id
      }
      println(workers.size)
    }
  }
}

object Master {
  def main(args: Array[String]): Unit = {
    val host = "192.168.10.1"
    val port = 8888

    //创建ActorSystem的必要参数
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config = ConfigFactory.parseString(configStr)
    //创建一个actorSystem，辅助创建和监控下面actor
    val actorSystem = ActorSystem("MasterSystem", config)
    //创建一个actor
    val master = actorSystem.actorOf(Props(new Master(host, port)), "master")

    actorSystem.awaitTermination()

  }
}