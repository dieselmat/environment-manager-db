package io.spencer.mateo.environmentmanager.db.repository

import java.sql.Date

import io.spencer.mateo.environmentmanager.db.core.{BaseEntity, BaseTable}
import slick.jdbc.SQLServerProfile


case class User(id: Long, userCd: String, email: String, isDeleted: Boolean = false)
case class Group(id: Long, groupCd: String, isDeleted: Boolean = false)
case class UserGroup(id: Long, userCd: String, groupCd: String, isDeleted: Boolean = false)

object UserGroupTables extends {
  val profile = SQLServerProfile
} with UserGroupTables

trait UserGroupTables {
  val profile: SQLServerProfile
  import profile.api._

  class UserTable(_tableTag: Tag) extends BaseTable[User](_tableTag, Some("user"), "User") {
    def * = (id, userCd, email, isDeleted) <> (User.tupled, User.unapply)
    def ? = (Rep.Some(id),Rep.Some(userCd),Rep.Some(email),Rep.Some(isDeleted)).shaped.<>({ r => import r._; _1.map(_ => User.tupled((_1.get,_2.get,_3.get,_4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    override val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val userCd: Rep[String] = column[String]("user_cd")
    val email: Rep[String] = column[String]("email")
    override val isDeleted: Rep[Boolean] = column[Boolean]("is_deleted", O.Default(false))
  }

  class GroupTable(_tableTag: Tag) extends BaseTable[Group](_tableTag, Some("group"), "Group") {
    def * = (id, groupCd, isDeleted) <> (Group.tupled, Group.unapply)
    def ? = (Rep.Some(id),Rep.Some(groupCd),Rep.Some(isDeleted)).shaped.<>({ r => import r._; _1.map(_ => Group.tupled((_1.get,_2.get,_3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    override val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val groupCd: Rep[String] = column[String]("group_cd")
    override val isDeleted: Rep[Boolean] = column[Boolean]("is_deleted", O.Default(false))
  }

  class UserGroupTable(_tableTag: Tag) extends BaseTable[UserGroup](_tableTag, Some("usergroup"), "UserGroup") {
    def * = (id, userCd, groupCd, isDeleted) <> (UserGroup.tupled, UserGroup.unapply)
    def ? = (Rep.Some(id),Rep.Some(userCd),Rep.Some(groupCd),Rep.Some(isDeleted)).shaped.<>({ r => import r._; _1.map(_ => UserGroup.tupled((_1.get,_2.get,_3.get,_4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    override val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val userCd: Rep[String] = column[String]("user_cd")
    val groupCd: Rep[String] = column[String]("group_cd")
    override val isDeleted: Rep[Boolean] = column[Boolean]("is_deleted", O.Default(false))
  }




}