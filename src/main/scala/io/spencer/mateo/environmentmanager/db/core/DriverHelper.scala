package io.spencer.mateo.environmentmanager.db.core

import slick.jdbc.SQLServerProfile.api._

object DriverHelper {
  val user = "postgres"
  val url = "jdbc:postgresql://localhost:5432/LocalDB"
  val password = "admin"
  val jdbcDriver = "org.postgresql.Driver"
  val db = Database.forURL(url, user, password, driver = jdbcDriver)
}
