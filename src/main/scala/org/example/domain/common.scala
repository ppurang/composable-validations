package org.example.domain

import scalaz.\/

object common {
  type or[+A, +B] = \/[A, B]
}
