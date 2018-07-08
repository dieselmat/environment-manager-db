package io.spencer.mateo.environmentmanager.db.module

import java.sql.Timestamp

import io.spencer.mateo.environmentmanager.db.core.Profile
import io.spencer.mateo.environmentmanager.db.entity.{ReleaseItem, ReleaseTicket, ReleaseTicketEnv}

trait ReleaseTicketItemEnvModule {
  self: Profile with ComponentEnvironmentProjectModule with UserGroupModule =>

  import profile.api._

  lazy val releaseTickets = TableQuery[ReleaseTicketTable]
  lazy val releaseItems = TableQuery[ReleaseItemTable]
  lazy val releaseItemEnvs = TableQuery[ReleaseTicketEnvTable]

  class ReleaseTicketTable(_tag: Tag) extends Table[ReleaseTicket](_tag, "ReleaseTicket") {

    val id          = column[Long]("id", O.PrimaryKey, O.AutoInc)
    val title       = column[String]("title")
    val projectCd   = column[String]("project_cd")
    val createBy    = column[String]("created_by")
    val createDate  = column[Timestamp]("created_date", O.SqlType("timestamp default CURRENT_TIMESTAMP"))
    val isDeleted   = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, title, projectCd, createBy, createDate, isDeleted) <> (ReleaseTicket.tupled, ReleaseTicket.unapply)

    def project = foreignKey("release_ticket_project_fk", projectCd, projects)(_.projectCd)

    def createdByUser = foreignKey("release_ticket_created_by_user_fk", createBy, users)(_.userCd)
  }

  class ReleaseItemTable(_tag: Tag) extends Table[ReleaseItem](_tag, "ReleaseItem") {

    val id              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    val sequence        = column[Long]("sequence")
    val releaseTicketId = column[Long]("release_ticket_id")
    val componentCd     = column[String]("component_cd")
    val createBy        = column[String]("created_by")
    val createDate      = column[Timestamp]("created_date", O.SqlType("timestamp default CURRENT_TIMESTAMP"))
    val isDeleted       = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, sequence, releaseTicketId, componentCd, createBy, createDate, isDeleted) <> (ReleaseItem.tupled, ReleaseItem.unapply)

    def releaseTicket = foreignKey("release_item_release_ticket_fk", releaseTicketId, releaseTickets)(_.id)
    def component = foreignKey("release_item_component_fk", componentCd, components)(_.componentCd)
    def createdByUser = foreignKey("release_item_created_by_user_fk", createBy, users)(_.userCd)
  }

  class ReleaseTicketEnvTable(_tag: Tag) extends Table[ReleaseTicketEnv](_tag, "ReleaseTicketEnv") {

    val id              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    val releaseTicketId = column[Long]("release_ticket_id")
    val environmentCd   = column[String]("environment_cd")
    val createBy        = column[String]("created_by")
    val createDate      = column[Timestamp]("created_date", O.SqlType("timestamp default CURRENT_TIMESTAMP"))
    val status          = column[Option[String]]("status")
    val isApproved      = column[Boolean]("is_approved", O.Default(false))
    val approvedBy      = column[Option[String]]("approved_by")
    val approvedDate    = column[Option[Timestamp]]("approved_date")
    val isDeleted       = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, releaseTicketId, environmentCd, createBy, createDate, status, isApproved, approvedBy, approvedDate, isDeleted) <> (ReleaseTicketEnv.tupled, ReleaseTicketEnv.unapply)

    def releaseTicket = foreignKey("release_ticket_env_release_ticket_fk", releaseTicketId, releaseTickets)(_.id)
    def environment = foreignKey("release_ticket_env_environment_fk", environmentCd, environments)(_.environmentCd)
    def createdByUser = foreignKey("release_ticket_env_created_by_user_fk", createBy, users)(_.userCd)
    def approvedByUser = foreignKey("release_ticket_env_approved_by_user_fk", approvedBy, users)(_.userCd)
  }

}
