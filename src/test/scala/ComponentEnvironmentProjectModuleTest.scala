import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ComponentEnvironmentProjectModuleTest extends UnitSpec {

  "A ComponentEnvironmentProject" should "be able to insert proejcts" in {
    val result = Await.result(db.run(insertProjectsAndRetrieve()), 2 seconds)
    assert(testProjects === result)
  }

  it should "be able to insert components" in {
    val result = Await.result(db.run(insertComponentsAndRetrieve()), 2 seconds)
    assert(testComponents === result)
  }

  it should "be able to insert environments" in {
    val result = Await.result(db.run(insertEnvironmentsAndRetrieve()), 2 seconds)
    assert(testEnvironments === result)
  }

}
