package db.dao

import org.junit.Test

class DaoTest {

	@Test
	def getTest() {
		ApartmentDao.get(1);
	}
}