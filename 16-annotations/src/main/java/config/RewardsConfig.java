package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * TODO-07: Perform component-scanning and run the test again
 * - Add an appropriate annotation to this class to cause component scanning.
 * - Set the base package to pick up all the classes we have annotated so far.
 * - Save all changes, Re-run the RewardNetworkTests.  It should now pass.
 */
/*
    We took everything out of this file because we are now using Annotation-based Configuration.
    We enabled @ComponentScan, and now all of our dependencies are using annotations in their own classes in order
    to be configured properly.
 */
@Configuration
@ComponentScan("rewards.internal")
public class RewardsConfig {

	// TODO-02: Remove all of the @Bean methods above.
	// - Remove the code that autowires DataSource as well.
    // - Run the RewardNetworkTests test. It should fail. Why?

}
