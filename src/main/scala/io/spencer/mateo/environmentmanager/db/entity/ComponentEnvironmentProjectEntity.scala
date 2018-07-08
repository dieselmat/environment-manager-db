package io.spencer.mateo.environmentmanager.db.entity

case class Component(componentCd: String, name: String, isDeleted: Boolean = false)
case class Environment(environmentCd: String, name: String, isDeleted: Boolean = false)
case class Project(projectCd: String, name: String, isDeleted: Boolean = false)