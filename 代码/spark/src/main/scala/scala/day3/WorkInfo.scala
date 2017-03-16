package scala.day3

/**
  * Created by Administrator on 2017/1/16.
  */
class WorkerInfo (val id: String, val memory: Int, val cores: Int) {
  //TODO 上一次心跳
  var LastHeartbeatTime : Long = _

}
