package cz.lukkin.kotlinproject.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CronLockerRepository : MongoRepository<CronLocker, ObjectId> {
  fun findByLockName(lockName: String): CronLocker?
}