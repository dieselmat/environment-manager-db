package io.spencer.mateo.environmentmanager.db.module

import io.spencer.mateo.environmentmanager.db.core.Profile
import io.spencer.mateo.environmentmanager.db.entity.{Component, Environment, Project}

trait ComponentEnvironmentProjectModule { self: Profile =>
  import profile.api._

  lazy val components = TableQuery[ComponentTable]
  lazy val environments = TableQuery[EnvironmentTable]
  lazy val projects = TableQuery[ProjectTable]

  class ComponentTable(_tag: Tag) extends Table[Component](_tag, "Component") {

    val componentCd  = column[String]("component_cd", O.PrimaryKey)
    val name    = column[String]("name")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (componentCd, name, isDeleted) <> (Component.tupled, Component.unapply)
  }


  class EnvironmentTable(_tag: Tag) extends Table[Environment](_tag, "Environment") {

    val environmentCd  = column[String]("environment_cd", O.PrimaryKey)
    val name    = column[String]("name")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (environmentCd, name, isDeleted) <> (Environment.tupled, Environment.unapply)
  }

  class ProjectTable(_tag: Tag) extends Table[Project](_tag, "Project") {

    val projectCd  = column[String]("project_cd", O.PrimaryKey)
    val name    = column[String]("name")
    val isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (projectCd, name, isDeleted) <> (Project.tupled, Project.unapply)
  }


}
