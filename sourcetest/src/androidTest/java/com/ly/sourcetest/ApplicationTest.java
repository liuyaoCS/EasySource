package com.ly.sourcetest;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }
    @SmallTest
    public void test(){
        Resources rs=getContext().getResources();
        AssetManager amr=rs.getAssets();

        AssetManager am=getContext().getAssets();


    }
}