package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Anamika Tripathi on 9/11/18.
 */
@RunWith(AndroidJUnit4.class)
public class EndpointAsyncTaskTest {
    Context context;

    @Test
    public void testVerifyJoke() throws InterruptedException {
        assertTrue(true);
        final CountDownLatch latch = new CountDownLatch(1);
        context = InstrumentationRegistry.getContext();
        EndpointAsyncTask testTask = new EndpointAsyncTask(context,
                new EndpointAsyncTask.ExecutionListener() {
            @Override
            public void changeProgressBarViewStatus(boolean var) {
                // no use here
            }

            @Override
            public void startDisplayActivity(String result) {
                // no test here
            }
        }
        ) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                assertNotNull(result);
                assertTrue(result.length() > 0);
                latch.countDown();
            }
        };
        testTask.execute();
        latch.await();
    }
}