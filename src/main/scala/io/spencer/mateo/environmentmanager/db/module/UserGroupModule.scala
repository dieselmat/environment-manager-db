package io.spencer.mateo.environmentmanager.db.module

import io.spencer.mateo.environmentmanager.db.core.Profile
import io.spencer.mateo.environmentmanager.db.entity.{Group, User, UserGroup}

trait UserGroupModule { self: Profile =>
  import profile.api._

  lazy val users = TableQuery[UserTable]
  lazy val groups = TableQuery[GroupTable]
  lazy val userGroups = TableQuery[UserGroupTable]

  class UserTable(_tag: Tag) extends Table[User](_tag, "User") {
    val userCd  = column[String]("user_cd", O.PrimaryKey)
    val name    = column[String]("name")
    val email    = column[Option[String]]("email")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (userCd, name, email, isDeleted) <> (User.tupled, User.unapply)
  }


  class GroupTable(_tag: Tag) extends Table[Group](_tag, "Group") {
    val groupCd  = column[String]("group_cd", O.PrimaryKey)
    val name    = column[String]("name")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (groupCd, name, isDeleted) <> (Group.tupled, Group.unapply)
  }

  class UserGroupTable(_tag: Tag) extends Table[UserGroup](_tag, "UserGroup") {
    val userCd  = column[String]("user_cd")
    val groupCd  = column[String]("group_cd")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (userCd, groupCd, isDeleted) <> (UserGroup.tupled, UserGroup.unapply)

    def pk = primaryKey("user_group_pk", (userCd, groupCd))
    def user = foreignKey("user_group_user_fk", userCd, users)(_.userCd)
    def group = foreignKey("user_group_group_fk", groupCd, groups)(_.groupCd)
  }


}
