package spark.day7.project1

import org.apache.commons.lang3.time.FastDateFormat


/**
  * Created by Administrator on 2017/1/22.
  */
object FileUtils {

  val dateFormat = FastDateFormat.getInstance("yyyy年MM月dd日,E,HH:mm:ss")

  def filterByTime(fields: Array[String], startTime: Long, endTime: Long): Boolean = {
    val time = fields(1)
    val logTime = dateFormat.parse(time).getTime
    logTime >= startTime && logTime <= endTime
  }

  def filterByType(fields: Array[String], evenType: String): Boolean = {
    val _type = fields(0)
    evenType == _type
  }

  def filterByTypes(fields: Array[String], evenTypes: String*): Boolean = {
    val _type = fields(0)
    for (et <- evenTypes) {
      if (_type == et)
        return true
    }
    false

  }

  def filterByTypeAndTime(fields: Array[String], evenType: String, beginTime: Long, endTime: Long): Boolean = {
    val _type = fields(0)
    val _time = fields(1)
    val logTime = dateFormat.parse(_time).getTime
    evenType == _type && logTime >= beginTime && logTime < endTime
  }


}
