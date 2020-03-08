package cz.lukkin.kotlinproject.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ConsumerRepository : MongoRepository<Consumer, ObjectId> {
  fun existsByTokenAndEnabled(token: String, enable: Boolean): Boolean
  fun findByToken(token: String): Consumer
}



