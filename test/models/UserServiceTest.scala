package models

import org.specs2.mutable.Specification

class UserServiceTest extends Specification {

  val userRepo = new ModelsTest[UserRepository]
  //scalastyle:off
  val user = User("Kajal","", "Jain", "jainkajal", "Kajal1", "Kajal1", "9876543210", "female", 18, "Others", "User")
  val admin = User("Nancy","","Jain","jainnancy","Nancy1","Nancy1","9797685354","female",24,"Cooking","Admin")
  val updateProfile = UpdateProfile("Nancy","","Jain","9797685354","female",24,"Cooking")
  //scalastyle:on
  "store user Details" should {
    "store user profile" in {
      val storeResult = userRepo.result(userRepo.repository.store(user))
      storeResult must equalTo(true)
    }
  }
/*
  "Retrieve data" should {
    "details of User" in {
      val retrievedResult: Option[User] = userRepo.result(userRepo.repository
        .getUser(user.username))
      retrievedResult must beSome(user)
    }

    "details of No User" in {
      val retrievedResult: Option[User] = userRepo.result(userRepo.repository
        .getUser("Abc1")
      )
      retrievedResult must beNone
    }
  }

  "Check Password" should {
    "When password match" in {
      val result: Boolean = userRepo.result(userRepo.repository
        .checkPasswords(user.username,user.password))
      result must equalTo(true)
    }

    "When password mismatch" in {
      val result: Boolean = userRepo.result(userRepo.repository
        .checkPasswords(user.username,"Kajaljain1"))
      result must equalTo(false)
    }
  }

  "Check isAdmin" should {
    "when user is admin" in {
      val result: Boolean = userRepo.result(userRepo.repository
          .isAdmin(admin.username))
      result must equalTo(true)
    }

    "when user is not admin" in {
      val result: Boolean = userRepo.result(userRepo.repository
        .isAdmin(user.username))
      result must equalTo(false)
    }
  }

  "Check update password" should {
    "password updated" in {
      val result: Boolean = userRepo.result(userRepo.repository
          .updatePassword(user.username,user.password))
      result must equalTo(true)
    }

    "password not updated" in {
      val result: Boolean = userRepo.result(userRepo.repository
        .updatePassword("shivangi",user.password))
      result must equalTo(false)
    }
  }

  "Check update profile details" should {
    "Profile Updated" in {
      val result: Boolean = userRepo.result(userRepo.repository
          .updateDetails(updateProfile,"jainnancy"))
      result must equalTo(true)
    }

    "Profile not Updated" in {
      val result: Boolean = userRepo.result(userRepo.repository
        .updateDetails(updateProfile,"jainkajal1"))
      result must equalTo(false)
    }
  }*/
}