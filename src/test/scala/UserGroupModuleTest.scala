import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class UserGroupModuleTest extends UnitSpec{
  import schema._, profile.api._

  "A UserGroupModule" should "be able to insert users" in {
    val result = Await.result(db.run(insertUsersAndRetrieve()), 2 seconds)
    assert(result.length === testUsers.length)
  }

  it should "be able to insert groups" in {
    val result = Await.result(db.run(insertGroupsAndRetrieve()), 2 seconds)
    assert(result.length === testGroups.length)
  }

  it should "be able to insert userGroups uniquely" in {
    val userResult = Await.result(db.run(insertUsersAndRetrieve()), 2 seconds)
    val groupResult = Await.result(db.run(insertGroupsAndRetrieve()), 2 seconds)

    val result = Await.result(db.run(insertUserGroupsAndRetrieve()), 2 seconds)
    assert(result.length === testUserGroups.length)
    assert(testUserGroups === result)

    assertThrows[org.h2.jdbc.JdbcBatchUpdateException] {
      Await.result(db.run(insertUserGroupsAndRetrieve()), 2 seconds)
    }

  }


}
