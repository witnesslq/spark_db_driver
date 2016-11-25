package com.ymy.mongodb

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import com.mongodb.spark.sql._
/**
  * Created by yinmuyang on 16/11/8.
  */
//case class Course(id:String,name:String)
object TestMongoDB2 {
    def main(args: Array[String]) {
//        val mongoUrl = "mongodb://192.168.100.182:27017"
//        val database = "workout"
//        val collection = "courses"
//        val sparkConf = new SparkConf().setAppName("T").setMaster("local")
//        .set("spark.mongodb.input.uri","mongodb://192.168.100.182:27017/")
//        .set("spark.mongodb.input.database","workout")
//        .set("spark.mongodb.input.collection","courses")

//        val sc = new SparkContext(sparkConf)
//        val sql = new SQLContext(sc)
        val spark = SparkSession.builder()
            .appName("mongodb_test").master("local").getOrCreate()
            spark.sqlContext.setConf("spark.mongodb.input.uri","mongodb://192.168.100.182:27017/")
            spark.sqlContext.setConf("spark.mongodb.input.database","workout")
            spark.sqlContext.setConf("spark.mongodb.input.collection","courses")
            spark.conf.set("spark.mongodb.input.uri","mongodb://192.168.100.182:27017/")
            spark.conf.set("spark.mongodb.input.database","workout")
            spark.conf.set("spark.mongodb.input.collection","courses")
        val rdd = spark.sqlContext.loadFromMongoDB()
        rdd.show(10)

        val df = MongoSpark.load(spark.sqlContext)
        df.collect().foreach(println(_))
//
//        val mongoC = Map(
//            Host-> List("192.168.100.182:27017"),
//            Database -> database,
//            Collection -> collection,
//            SamplingRatio -> 1.0,
//            WriteConcern -> "normal")
//        val readConfig = MongodbConfigBuilder(mongoC).build
//        val mongoRDD = sparkSession.sqlContext.fromMongoDB(readConfig)
//        mongoRDD.toDF().show(1000)
    }
}
