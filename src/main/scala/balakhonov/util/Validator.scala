package balakhonov.util

import net.liftweb.http.S
import db.HibernateUtil
import db.dao.UserDao

object Validator {
  def isValidEmail(email: String): Boolean = if ("""^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$""".r.findFirstIn(email) == None) false else true

  def isValidPasswords(pass: String, pass2: String): Boolean = {
    if (!pass.equals(pass2)) {
      S.error("password", "Passwordsare not the same!");
    }

    true
  }

  def isValidLogin(email: String, password: String): Boolean = {
    if (!password.equals("zseqsc")) {
      S.error("password", "Invalid username/password!")
      false
    } else {
      true
    }

    var session = HibernateUtil.openSession();
    try {
      var user = UserDao.getUserByEmail(email)

      if (user == null || !user.getPassword().equals(Util.toMd5(password.trim()))) {
        S.error("password", "Invalid username/password!")
        false
      } else {
        true
      }
    } catch {
      case e: Exception => {
        e.printStackTrace();
        throw new Exception(e);
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}