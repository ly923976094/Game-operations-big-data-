package spark.day7.project3

import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import spark.day7.project1.LoggerLevels

/**
  * Created by Administrator on 2017/1/23.
  */
object ElasticSpark {
  def main(args: Array[String]): Unit = {
    LoggerLevels.setStreamingLogLevels()
    val conf = new SparkConf().setAppName("ElasticSpark").setMaster("local[2]")
    conf.set("es.nodes", "master,slave1,slave2")
    conf.set("es.port", "9200")
    conf.set("es.index.auto.create", "true")

    val sc = new SparkContext(conf)
    val start = 1463904522
    val end = 1463904540

    val tp = "1"
    val query: String =
      s"""{
         |  "query": {"match_all": {}},
         |       "filter" : {
         |          "bool": {
         |            "must": [
         |                {"term" : {"access.type" : $tp}},
         |                {
         |                "range": {
         |                  "access.time": {
         |                  "gte": "$start",
         |                  "lte": "$end"
         |                  }
         |                }
         |              }
         |            ]
         |          }
         |       }
         |}""".stripMargin
    val rdd1 = sc.esRDD("accesslog", query)

    println(rdd1.collect().toBuffer)
    println(rdd1.collect().size)
  }
}
