package io.spencer.mateo.environmentmanager.db.entity

import java.sql.Timestamp

case class ReleaseTicket(id: Long = 0L,
                         title: String,
                         projectCd: String,
                         createdBy: String,
                         createdDate: Timestamp = new Timestamp(System.currentTimeMillis()),
                         isDeleted: Boolean = false)

case class ReleaseItem(id: Long = 0L,
                       sequence: Long = 0L,
                       releaseId: Long,
                       componentCd: String,
                       createdBy: String,
                       createdDate: Timestamp = new Timestamp(System.currentTimeMillis()),
                       isDeleted: Boolean = false)

case class ReleaseTicketEnv(id: Long = 0L,
                            releaseTicketId: Long,
                            environmentCd: String,
                            createdBy: String,
                            createdDate: Timestamp = new Timestamp(System.currentTimeMillis()),
                            status: Option[String],
                            isApproved: Boolean = false,
                            approvedBy: Option[String] = None,
                            approvedDate: Option[Timestamp] = None,
                            isDeleted: Boolean = false)
