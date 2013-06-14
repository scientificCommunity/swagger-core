import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._

import java.lang.reflect.Method

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class BasicResourceTest extends FlatSpec with ShouldMatchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[BasicResource], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)
    val api = apiResource.apis.head
    api.path should be ("/basic/{id}")

    val ops = api.operations
    ops.size should be (2)

    val getOp = ops.head
    getOp.httpMethod should be ("GET")
    getOp.parameters.size should be (1)
    val param = getOp.parameters.head
    param.name should be ("id")
    param.dataType should be ("string")
    param.required should be (true)

    apiResource.models should not be (None)
    apiResource.models.get.size should be (2)
    apiResource.models.get.map(model => {
      if(model._1 == "Sample") {
        val m = model._2
        m.description should be (Some("a sample model"))

        val id = m.properties("id")
        id.required should be (true)
        id.allowableValues should be (AllowableListValues(List("1" ,"2" ,"3")))
        id.description should be (Some("unique identifier"))        
      }
      else if(model._1 == "NotFoundModel") {
        val m = model._2
        m.description should be (Some("error response model"))

        val message = m.properties("message")
        message.required should be (false)
      }
    })
  }
}

@RunWith(classOf[JUnitRunner])
class ContainerResourceTest extends FlatSpec with ShouldMatchers {
  it should "read an api" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ContainerResource], config).getOrElse(fail("should not be None"))
    apiResource.apis.size should be (1)
    val api = apiResource.apis.head
    api.path should be ("/container/{id}")
    api.operations.size should be (2)

    val postOp = api.operations.head
    postOp.httpMethod should be ("POST")
    postOp.responseClass should be ("void")
    postOp.nickname should be ("addTest")
    postOp.parameters.size should be (2)

    val getOp = api.operations.last

    getOp.httpMethod should be ("GET")
    getOp.responseClass should be ("List[Sample]")
    getOp.nickname should be ("getTest")
    getOp.parameters.size should be (1)

    val getOpParam = getOp.parameters.head
    getOpParam.dataType should be ("string")
    getOpParam.required should be (true)
    getOpParam.name should be ("id")
    getOpParam.allowMultiple should be (false)
    getOpParam.allowableValues should be (AllowableRangeValues("0.0", "10.0"))
    getOpParam.paramType should be ("path")

    val models = apiResource.models.getOrElse(fail("no models found"))
    models.size should be (3)
  }
}

@RunWith(classOf[JUnitRunner])
class ModelExtractionTest extends FlatSpec with ShouldMatchers {
  it should "get the right models" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[NestedModelResource], config).getOrElse(fail("should not be None"))

    val models = apiResource.models.getOrElse(fail("no models found"))

    models.size should be (4)
    (models.keys.toSet & Set("Family", "Person", "Employer", "NotFoundModel")).size should be (4)
  }
}
