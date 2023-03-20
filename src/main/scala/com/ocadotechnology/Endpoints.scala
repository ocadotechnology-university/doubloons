package com.ocadotechnology

import sttp.tapir.*
import Library.*
import cats.effect.IO
import com.ocadotechnology.Employees.Employee
import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.auto.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter




object Endpoints:
  /*case class User(name: String) extends AnyVal
  val helloEndpoint: PublicEndpoint[User, Unit, String, Any] = endpoint.get
    .in("hello")
    .in(query[User]("firstName"))
    .out(stringBody)
  val helloServerEndpoint: ServerEndpoint[Any, IO] = helloEndpoint.serverLogicSuccess(user => IO.pure(s"Hello ${user.name}"))*/


  val getUser: PublicEndpoint[String, String, User, Any] = endpoint.get
    .in("user" / path[String]("email"))
    .out(jsonBody[User])
    .errorOut(jsonBody[String])

  val getUserServerEndpoint: ServerEndpoint[Any, IO] = getUser.serverLogic(email => Main.getUser(email))
  //val getUserServerEndpoint: ServerEndpoint[Any, IO] = getUser.serverLogicSuccess(email => Main.getUser(email).unsafeToFuture())






  val booksListing: PublicEndpoint[Unit, Unit, List[Book], Any] = endpoint.get
    .in("books" / "list" / "all")
    .out(jsonBody[List[Book]])
  val booksListingServerEndpoint: ServerEndpoint[Any, IO] = booksListing.serverLogicSuccess(_ => IO.pure(Library.books))

  val employeeEndpoint: PublicEndpoint[Unit, Unit, List[Employee], Any] = endpoint.get
    .in("employees").out(jsonBody[List[Employee]])
  val employeeListingServerEndpoint: ServerEndpoint[Any, IO] = employeeEndpoint.serverLogicSuccess(_ => IO.pure(Employees.employeeList))

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(booksListingServerEndpoint, employeeListingServerEndpoint, getUserServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "doubloons", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

object Employees:
  case class Employee (email: String, teamID: Int, firstName: String, lastName: String, password: String, picture: String)

  val employeeList = List(
    Employee("admin@ocado.com", 1, "Name", "Surname", "pass", "urlToAvatar")
  )


object Library:
  case class Author(name: String)
  case class Book(title: String, year: Int, author: Author)

  val books = List(
    Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe")),
    Book("On the Niemen", 1888, Author("Eliza Orzeszkowa")),
    Book("The Art of Computer Programming", 1968, Author("Donald Knuth")),
    Book("Pharaoh", 1897, Author("Boleslaw Prus"))
  )
