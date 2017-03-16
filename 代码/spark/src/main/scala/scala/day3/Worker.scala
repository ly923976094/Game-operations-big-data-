package scala.day3

import java.util.UUID


import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

/**
  * Created by Administrator on 2017/1/16.
  */
class Worker(val masterhost: String, val masterport: Int, val memory: Int, val cores: Int) extends Actor {
  var master: ActorSelection = _
  val workerId = UUID.randomUUID().toString
  val HEART_INTERVAL = 10000

  override def preStart(): Unit = {
    println("preStart invoked")
    //在master启动时会打印下面的那个协议, 可以先用这个做一个标志, 连接哪个master
    //继承actor后会有一个context, 可以通过它来连接
    //需要有/user, master要和master那边创建的名字保持一致
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterhost:$masterport/user/master")
    master ! RegisterWorker(workerId, memory, cores)
  }

  override def receive: Receive = {
    case RegisteredWorker(masterUrl) => {
      println(masterUrl)
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, HEART_INTERVAL millis, self, SendHearbeat)
    }
    case SendHearbeat => {
      println("send heartbeat to master")
      master ! Heartbeat(workerId)
    }
  }
}


object Worker {
  def main(args: Array[String]): Unit = {
    val host = "192.168.1.1"
    val port = 8888
    val masterhost = "192.168.10.1"
    val masterport = 8888

    val memory = 512
    val cores = 2

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)

    val actorSystem = ActorSystem("WorkerSystem", config)
    actorSystem.actorOf(Props(new Worker(masterhost, masterport, memory, cores)), "worker")

    actorSystem.awaitTermination()


  }
}