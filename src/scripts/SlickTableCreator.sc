case class Column(name: String, ofType: String, defaultValue: Option[String] = None)

def camelToUnderscores(name: String) = "[A-Z\\d]".r.replaceAllIn(name, {m =>
  "_" + m.group(0).toLowerCase()
})

def columnNames(columns: List[Column]) = columns.map(_.name)

def buildForSelectAll(tableName: String, columns: List[Column]) = {
  "\t" + s"""def * = (${columnNames(columns).mkString(", ")}) <> (${tableName}.tupled, ${tableName}.unapply)""" + "\n"
}

def buildForSelectQuestion(tableName: String, columns: List[Column]) = {
  def buildRep(columns: List[Column]) = {
    columns.map(n => s"Rep.Some(${n.name})").mkString(",")
  }
  def buildGetPortion(size: Int): String = {
    (1 to size).toList.map(x => s"_${x}.get").mkString(",")
  }

  "\t" +
  s"""def ? = (${buildRep(columns)}).shaped.<>({ r => import r._; _1.map(_ => ${tableName}.tupled((${buildGetPortion(columns.length)}))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))""".stripMargin + "\n"
}

def buildColumn(column: Column) = {
  column match {
    case c @ Column("id", _, _) => s"""override val ${c.name}: Rep[${c.ofType}] = column[${c.ofType}]("${camelToUnderscores(c.name)}", O.AutoInc, O.PrimaryKey)"""
    case c @ Column("isDeleted", _, _) => s"""override val ${c.name}: Rep[${c.ofType}] = column[${c.ofType}]("${camelToUnderscores(c.name)}", O.Default(false))"""
    case Column(name, ofType, None) => s"""val ${name}: Rep[${ofType}] = column[${ofType}]("${camelToUnderscores(name)}")"""
    case Column(name, ofType, Some(default)) => s"""val ${name}: Rep[${ofType}] = column[${ofType}]("${camelToUnderscores(name)}", O.Default($default))"""
  }
}

def createTable(tableName: String, columns: List[Column]) = {
  val strBuff = new StringBuilder()
  strBuff.append(s"""class ${tableName}Table(_tableTag: Tag) extends BaseTable[${tableName}](_tableTag, Some("${tableName.toLowerCase}"), "${tableName}") {""" + "\n")
  strBuff.append(buildForSelectAll(tableName, columns))
  strBuff.append(buildForSelectQuestion(tableName, columns))
  columns.foreach(c => strBuff.append("\t"+ buildColumn(c) +"\n"))
  strBuff.append("}")
  strBuff
}


def caseClassIt(tableName: String, columns: List[Column]) = {
  def parameterIt(columns: List[Column]) = {
    columns.map(c => s"${c.name}: ${c.ofType}${if (c.defaultValue.isDefined) " = " + c.defaultValue.get else ""}").mkString(", ")

  }

  val strBuff = new StringBuilder
  strBuff.append(s"case class $tableName(${parameterIt(columns)})")

}

val tableName = "UserGroup"
val columns = List(
  Column("id", "Long"),
  Column("userCd", "String"),
  Column("groupCd", "String"),
  Column("isDeleted", "Boolean", Option("false"))

)


println(caseClassIt(tableName, columns))
println(createTable(tableName, columns))
