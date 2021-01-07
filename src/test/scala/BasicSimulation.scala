
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicItSimulation extends Simulation {

  val sessionHeaders = Map("Authorization" -> "Bearer 04ef8a2a-229d-312f-a23a-85e1117d8e46",
                           "Content-Type" -> "application/json")
  
  val httpProtocol = http
    .baseUrl("https://sbxgw.ecosystem-a2.posti.fi:443") // Here is the root for all relative URLs
    

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
      .get("/pizza/1.0")
      .headers(sessionHeaders))
     // Note that Gatling has recorder real time pauses

  //setUp(scn.inject(constantConcurrentUsers(10).during(10.seconds)))

  setUp(
    scn.inject(
      nothingFor(4.seconds), // 1
      atOnceUsers(10), // 2
      rampUsers(10).during(5.seconds), // 3
      constantUsersPerSec(20).during(15.seconds), // 4
      constantUsersPerSec(20).during(15.seconds).randomized, // 5
      rampUsersPerSec(10).to(20).during(10.seconds), // 6
      rampUsersPerSec(10).to(20).during(10.seconds).randomized, // 7
      heavisideUsers(10000).during(90.seconds) // 8
    ).protocols(httpProtocol)
  )
}
