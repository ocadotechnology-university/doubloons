package com.ocadotechnology.category

import cats.effect.IO
import com.ocadotechnology.category.CategoryRepository
trait CategoryService {
  def getCategories: IO[Either[String, List[Category]]]
}

object CategoryService {
  def instance(categoryRepository: CategoryRepository): CategoryService = new CategoryService {
    override def getCategories: IO[Either[String, List[Category]]] = {
      categoryRepository.getCategories.map{result => Right(result)}
    }
  }
}
