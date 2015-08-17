package com.velocikey.android.learning.cinebox;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */

public class FullTestSuite extends TestSuite {
    // Class fields
    private static final String LOG_TAG = FullTestSuite.class.getSimpleName();

    public FullTestSuite() {
        super();
    }

    // Object Fields
    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
}
