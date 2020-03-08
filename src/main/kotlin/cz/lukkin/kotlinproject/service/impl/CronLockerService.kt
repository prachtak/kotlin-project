package cz.lukkin.kotlinproject.service.impl

import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.function.Consumer

@Service
@Slf4j
@RequiredArgsConstructor
class CronLockerService(private val cronLockerRepository: CronLockerRepository) : InitializingBean {

  override fun afterPropertiesSet() {
    val cronLocker = cronLockerRepository.findAll()
    cronLocker.forEach(Consumer { locker: CronLocker ->
      locker.lock = false
      locker.lastLock = Instant.now()
      cronLockerRepository.save(locker)
    })
  }

  fun isLocked(lockName: String): Boolean {
    val locker = cronLockerRepository.findByLockName(lockName)
    if (locker == null) {
      cronLockerRepository.save(CronLocker(lockName = lockName))
      return false
    }
    return if (locker.lock && locker.lastLock.plusSeconds(300L).isBefore(Instant.now())) {
      false
    } else locker.lock
  }

  fun lock(lockName: String) {
    val locker = cronLockerRepository.findByLockName(lockName)
    locker!!.lock = true
    cronLockerRepository.save(locker)
  }

  fun unlock(lockName: String) {
    val locker = cronLockerRepository.findByLockName(lockName)
    locker!!.lock = false
    cronLockerRepository.save(locker)
  }
}