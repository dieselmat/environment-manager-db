import io.spencer.mateo.environmentmanager.db.entity.ReleaseTicket

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class ReleaseTicketItemEnvModuleTest extends UnitSpec {

  "A ReleaseTicketItemEnvModule" should "be able to raise tickets" in {
    Await.result(db.run(insertAllUserGroupsComponentsEnvironmentsProjects()), 2 seconds)
    val result = Await.result(db.run(insertReleaseTicketsAndRetrieve()), 2 seconds)
    assert (result.length === testReleases.length)
  }

  it should "be able to add release items" in {
    Await.result(db.run(insertAllUserGroupsComponentsEnvironmentsProjects()), 2 seconds)
    val Vector(releaseTicket1, releaseTicket2) = Await.result(db.run(insertReleaseTicketsAndRetrieve()), 2 seconds)


    val neoReleaseItem1Ticket1 = releaseItem1_ReleaseTicket1.copy(releaseId = releaseTicket1.id)
    val neoReleaseItem2Ticket1 = releaseItem2_ReleaseTicket1.copy(releaseId = releaseTicket1.id)

    val neoReleaseItem1Ticket2 = releaseItem1_ReleaseTicket2.copy(releaseId = releaseTicket2.id)
    val neoReleaseItem2Ticket2 = releaseItem2_ReleaseTicket2.copy(releaseId = releaseTicket2.id)

    val items = Seq(neoReleaseItem1Ticket1, neoReleaseItem2Ticket1, neoReleaseItem1Ticket2, neoReleaseItem2Ticket2)
    val releaseItems = Await.result(db.run(insertReleaseItemsAndRetrieve(items)), 2 seconds)

    assert(items.length === releaseItems.length)
    releaseItems foreach { r => assert(r.id > 0L)}

  }

  it should "be able to add release environments" in {

    Await.result(db.run(insertAllUserGroupsComponentsEnvironmentsProjects()), 2 seconds)
    val Vector(releaseTicket1, _) = Await.result(db.run(insertReleaseTicketsAndRetrieve()), 2 seconds)

    val innerReleaseEnvs = testReleaseEnvs map (_.copy(releaseTicketId = releaseTicket1.id))

    val dbReleaseEnvs = Await.result(db.run(insertReleaseEnvsAndRetrieve(innerReleaseEnvs)), 2 seconds)

    assert(innerReleaseEnvs.length === dbReleaseEnvs.length)

    dbReleaseEnvs foreach { r =>
      assert(r.id > 0L)

    }

  }

}
