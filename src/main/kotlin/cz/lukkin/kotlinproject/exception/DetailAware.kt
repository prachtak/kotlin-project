package cz.lukkin.kotlinproject.exception

import java.util.*

interface DetailAware {
  val detail: Optional<Any>
}