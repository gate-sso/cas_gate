package com.cas_gate;

import com.cas_gate.gatemfa.GateGoogleAuthenticator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.naming.ConfigurationException;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws ConfigurationException {
        GateGoogleAuthenticator gateGoogleAuthenticator = new GateGoogleAuthenticator();
        Boolean response = gateGoogleAuthenticator.authenticateWithGateMFA("ajey@cas_gate.github.io", "958466");
        assertFalse(response);
    }
}
