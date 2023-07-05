package com.ocadotechnology.doubloons.category

import cats.implicits.*
import cats.effect.IO
import com.ocadotechnology.doubloons.category.CategoryRepository
import com.ocadotechnology.doubloons.BusinessError
trait CategoryService {
  def getCategories: IO[Either[BusinessError, List[Category]]]
}

object CategoryService {
  def instance(categoryRepository: CategoryRepository): CategoryService =
    new CategoryService {
      override def getCategories: IO[Either[BusinessError, List[Category]]] = {
        categoryRepository.getCategories.map { result => Right(result) }
      }
    }
}
