package rewards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A system test that demonstrates how the effects of a given test can affect
 * all tests that follow.
 * <p>
 * JUnit makes no guarantee about the order that tests run in, so we force tests
 * to run in method name order using @FixMethodOrder(MethodSorters.NAME_ASCENDING)
 * in this particular testing scenario. (In general, you should not do this.)
 * <p>
 * TODO-08: MAKE SURE to revert the propagation attribute back to
 * REQUIRED in RewardNetworkImpl.
 * <p>
 * TODO-09: Examine the @Test logic below. Note that committed results from the
 * first test will invalidate the assertions in the second test. Run this test,
 * at the class level so that both tests run it should fail. Do you know why?
 * <p>
 * TODO-10: Add @Transactional on the class and re-run the test. It should pass.
 * Do you know why?
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SystemTestConfig.class})
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
public class RewardNetworkSideEffectTests {

	private static final String SAVINGS_SQL = "select SAVINGS from T_ACCOUNT_BENEFICIARY where NAME = ?";

	/**
	 * Amount of money in Annabelle's savings account before running the test
	 * methods
	 */
	private static Double annabelleInitialSavings;

	/**
	 * Amount of money in Corgan's savings account before running the test
	 * methods
	 */
	private static Double corganInitialSavings;

	/**
	 * The object being tested.
	 */
	@Autowired
	private RewardNetwork rewardNetwork;

	/**
	 * A template to use for test verification
	 */
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Determine the initial savings: note that we're doing this only once for
	 * all tests, so if a test changes the savings and commits the next test
	 * method might be affected!
	 */
	@BeforeEach
	public void determineInitialSavings() {
		if (annabelleInitialSavings == null) {
			annabelleInitialSavings = jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Annabelle");
			corganInitialSavings = jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Corgan");
		}
	}

	private void runTest() {
		Dining dining = Dining.createDining("100.00", "1234123412341234", "1234567890");
		rewardNetwork.rewardAccountFor(dining);
		assertEquals(Double.valueOf(annabelleInitialSavings + 4.00d),
				jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Annabelle"));
		assertEquals(Double.valueOf(corganInitialSavings + 4.00d),
				jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Corgan"));
	}

	/*
	 * Without the @Transactional annotation, the first test will pass, but the second test will fail. This is because
	 *  the results from the first test are not rolled back, and it will affect the results of the second test.
	 *
	 * When you add the @Transactional annotation at the class level, each test will run in a transaction and then
	 * roll back after each test.
	 */
	@Test
	public void testCollision1stTime() {
		runTest();
	}

	@Test
	public void testCollision2ndTime() {
		runTest();
	}
}
