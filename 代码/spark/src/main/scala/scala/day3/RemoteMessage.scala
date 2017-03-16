package scala.day3

/**
  * Created by Administrator on 2017/1/16.
  */
trait RemoteMessage extends Serializable

case class RegisteredWorker(masterUrl: String) extends RemoteMessage

case object CheckTimeOutWorker

case class RegisterWorker(id: String, memory: Int, cores: Int) extends RemoteMessage

case class Heartbeat(id: String) extends RemoteMessage

case object SendHearbeat
