package cz.lukkin.kotlinproject.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SmsRepository : MongoRepository<Sms, ObjectId> {

  fun findAllByStatus(status: SmsStatus): List<Sms>
  fun findAllByTicketIdAndConsumerId(ticketId: String, consumerId: ObjectId): List<Sms>
}



