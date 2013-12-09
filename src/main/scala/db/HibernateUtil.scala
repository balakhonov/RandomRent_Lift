package db

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

object HibernateUtil {
  val LOG: Logger = Logger.getLogger(HibernateUtil.getClass());

  var sessionFactory: SessionFactory = null;
  var statistics: Statistics = null;

  def buildSessionFactory() {
    LOG.info("Build SessionFactory");

    var configuration: Configuration = new Configuration();
    // if (localeTesting) {
    // configuration.configure("local-hibernate.cfg.xml");
    // } else {
    configuration.configure();
    // }

    sessionFactory = configuration.buildSessionFactory();
  }

  def getSessionFactory(): SessionFactory = {
    return sessionFactory;
  }

  def openSession(): Session = {
    if (sessionFactory == null) {
      HibernateUtil.buildSessionFactory();
    }

    statistics = sessionFactory.getStatistics();
    LOG.trace(statistics);

    return sessionFactory.openSession();
  }
}