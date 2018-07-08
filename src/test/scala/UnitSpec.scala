import io.spencer.mateo.environmentmanager.db.core.Profile
import io.spencer.mateo.environmentmanager.db.entity._
import io.spencer.mateo.environmentmanager.db.module.{ComponentEnvironmentProjectModule, ReleaseTicketItemEnvModule, UserGroupModule}
import org.scalatest._
import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

abstract class UnitSpec extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors
  with BeforeAndAfter {

  class Schema(val profile: JdbcProfile) extends Profile
    with UserGroupModule
    with ComponentEnvironmentProjectModule
    with ReleaseTicketItemEnvModule

  val schema = new Schema(slick.jdbc.H2Profile)
  import schema._ , profile.api._

  val db = Database.forConfig("testDB")

  before {

    val createSchema = for {
      _ <- users.schema.create
      _ <- groups.schema.create
      _ <- userGroups.schema.create
      _ <- components.schema.create
      _ <- environments.schema.create
      _ <- projects.schema.create
      _ <- releaseTickets.schema.create
      _ <- releaseItems.schema.create
      _ <- releaseItemEnvs.schema.create
    } yield ()

    Await.result(db.run(createSchema), 2 seconds)
  }

  after {
    val dropSchema = for {
      _ <- releaseItemEnvs.schema.drop
      _ <- releaseItems.schema.drop
      _ <- releaseTickets.schema.drop
      _ <- userGroups.schema.drop
      _ <- users.schema.drop
      _ <- groups.schema.drop
      _ <- components.schema.drop
      _ <- environments.schema.drop
      _ <- projects.schema.drop
    } yield ()

    Await.result(db.run(dropSchema), 2 seconds)
  }

  lazy val user1 = User("uzumaki.naruto", name = "Naruto Uzumaki", email = Option("naruto.uzumaki@konoha.com"))
  lazy val user2 = User("uzumaki.minato", name = "Minato Uzumaki", email = Option("minato.uzumaki@konoha.com"))

  lazy val testUsers = Seq(user1, user2)

  lazy val user_group = Group(grpCd = "user-group", name = "User Group")
  lazy val admin_group = Group(grpCd = "admin-group", name = "Admin Group")

  lazy val testGroups = Seq(user_group, admin_group)

  lazy val testUserGroups = testUsers.zip(testGroups).map (ug => UserGroup(ug._1.userCd, ug._2.grpCd))

  lazy val component1  = Component("quartz", "Quarts Scheduler")
  lazy val component2  = Component("main-db", "Main DB")
  lazy val component3  = Component("spark", "Spark")

  lazy val testComponents = Seq (component1, component2, component3)

  lazy val env1 = Environment("DEV","DEV")
  lazy val env2 = Environment("SIT","SIT")
  lazy val env3 = Environment("UAT","UAT")
  lazy val env4 = Environment("PROD","PROD")

  lazy val testEnvironments = Seq (env1, env2, env3, env4)

  lazy val project1 = Project("Project 1", "1st Project")
  lazy val project2 = Project("Project 2", "2nd Project")
  lazy val project3 = Project("Project 3", "3rd Project")
  lazy val project4 = Project("Project 4", "4th Project")

  lazy val testProjects = Seq (project1, project2, project3,project4)

  lazy val releaseTicket1 = ReleaseTicket(0L, "First Release", project1.projectCd, user1.userCd)
  lazy val releaseTicket2 = ReleaseTicket(0L, "Second Release", project1.projectCd, user1.userCd)

  lazy val testReleases = Seq(releaseTicket1, releaseTicket2)

  lazy val releaseItem1_ReleaseTicket1 = ReleaseItem(0L,0L, releaseTicket1.id, component1.componentCd, user1.userCd)
  lazy val releaseItem2_ReleaseTicket1 = ReleaseItem(0L,0L, releaseTicket1.id, component2.componentCd, user1.userCd)

  lazy val releaseItem1_ReleaseTicket2 = ReleaseItem(0L,0L, releaseTicket2.id, component1.componentCd, user1.userCd)
  lazy val releaseItem2_ReleaseTicket2 = ReleaseItem(0L,0L, releaseTicket2.id, component3.componentCd, user1.userCd)

  lazy val releaseEnvDEV_ReleaseTicket1 = ReleaseTicketEnv(releaseTicketId = 0L, environmentCd = env1.environmentCd, createdBy = user1.userCd, status = None)
  lazy val releaseEnvSIT_ReleaseTicket1 = ReleaseTicketEnv(releaseTicketId = 0L, environmentCd = env2.environmentCd, createdBy = user1.userCd, status = None)
  lazy val releaseEnvPROD_ReleaseTicket1 = ReleaseTicketEnv(releaseTicketId = 0L, environmentCd= env4.environmentCd, createdBy = user1.userCd, status = None)

  lazy val testReleaseEnvs = Seq(releaseEnvDEV_ReleaseTicket1, releaseEnvSIT_ReleaseTicket1, releaseEnvPROD_ReleaseTicket1)

  def insertUsersAndRetrieve() = for {
    _ <- users ++= testUsers
    u <- users.result
  } yield u

  def insertGroupsAndRetrieve() = for {
    _ <- groups ++= testGroups
    g <- groups.result
  } yield g

  def insertUserGroupsAndRetrieve() = for {
    _ <- userGroups ++= testUserGroups
    ug <- userGroups.result
  } yield ug

  def insertComponentsAndRetrieve() = for {
    _ <- components ++= testComponents
    c <- components.result
  } yield c

  def insertEnvironmentsAndRetrieve() = for {
    _ <- environments ++= testEnvironments
    e <- environments.result
  } yield e

  def insertProjectsAndRetrieve() = for {
    _ <- projects ++= testProjects
    p <- projects.result
  } yield p

  def insertAllUserGroupsComponentsEnvironmentsProjects() = for {
      _ <- insertUsersAndRetrieve()
      _ <- insertGroupsAndRetrieve()
      _ <- insertUserGroupsAndRetrieve()
      _ <- insertComponentsAndRetrieve()
      _ <- insertEnvironmentsAndRetrieve()
      _ <- insertProjectsAndRetrieve()
  } yield ()

  def insertReleaseTicketsAndRetrieve() = for {
    _ <- releaseTickets ++= testReleases
    r <- releaseTickets.result
  } yield r

  def insertReleaseItemsAndRetrieve(pReleaseItems: Seq[ReleaseItem]) = for {
    _ <- releaseItems ++= pReleaseItems
    r <- releaseItems.result
  } yield r

  def insertReleaseEnvsAndRetrieve(pReleaseEnv: Seq[ReleaseTicketEnv]) = for {
    _ <- releaseItemEnvs ++= pReleaseEnv
    r <- releaseItemEnvs.result
  } yield r

}
