package com.ymy.mongodb


import com.stratio.datasource.mongodb.config.MongodbConfigBuilder
import com.stratio.datasource.mongodb.config.MongodbConfig._
import com.stratio.datasource.mongodb._
import org.apache.spark.sql.{SparkSession}

/**
  * Created by yinmuyang on 16/11/8.
  */
object TestMongoDB {
    def main(args: Array[String]) {
        val spark = SparkSession.builder().appName("test_mongodb").master("local")
            .getOrCreate()
        val mongoUrl = "mongodb://192.168.100.182:27017"
        val database = "workout"
        val collection = "courses"

        val mongoC = Map(
            Host-> List("192.168.100.182:27017"),
            Database -> database,
            Collection -> collection,
            SamplingRatio -> 1.0,
            WriteConcern -> "normal")
        val readConfig = MongodbConfigBuilder(mongoC).build
        val mongoRDD = spark.sqlContext.fromMongoDB(readConfig)
        mongoRDD.toDF().show(1000)
    }
}
