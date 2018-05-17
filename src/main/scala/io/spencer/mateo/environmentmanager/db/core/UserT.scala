package io.spencer.mateo.environmentmanager.db.core

import slick.jdbc.SQLServerProfile.api._

case class User(id: Long, userCd: Long)

trait Tables {

  class UserTable(_tableTag: Tag) extends BaseTable[User](_tableTag, Some("user"), "User") {
    def * = (id, userCd) <> (User.tupled, User.unapply)
    def ? = (Rep.Some(id),Rep.Some(userCd)).shaped.<>({ r => import r._; _1.map(_ => User.tupled((_1.get,_2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
    override val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val userCd: Rep[Long] = column[Long]("userCd")
  }

}
