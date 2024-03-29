package com.ocadotechnology.doubloons.category

import cats.effect.IO
import cats.implicits.*
import com.ocadotechnology.doubloons.database.DatabaseConfig
import doobie.*
import doobie.implicits.*
import doobie.refined.implicits.*

trait CategoryRepository {
  def getCategories: IO[List[Category]]
}

object CategoryRepository {
  def instance(dbConfig: DatabaseConfig): CategoryRepository =
    new CategoryRepository {
      override def getCategories: IO[List[Category]] = {
        sql"""SELECT * FROM categories ORDER BY category_id"""
          .query[Category]
          .to[List]
          .transact(dbConfig.xa)
      }
    }
}
