package io.spencer.mateo.environmentmanager.db.entity

case class User(userCd: String, name: String, email: Option[String], isDeleted: Boolean = false)
case class Group(grpCd: String, name: String, isDeleted: Boolean = false)
case class UserGroup(userCd: String, groupcd: String, isDeleted: Boolean = false)


