package db.dao
import org.hibernate.Session;

import db.mapping.User;

object UserDao extends DAO[User] {

  /**
   *
   */
  def getUserByEmail(eMail: String): User = {
    if (eMail == null || eMail.isEmpty()) {
      throw new IllegalArgumentException("E-mail should not be null or empty");
    }

    def process(session: Session): User = {
      var query = session.createQuery("from User where email = :email ");
      query.setParameter("email", eMail);

      return query.uniqueResult().asInstanceOf[User];
    }

    execute(s => process(s))
  }
}