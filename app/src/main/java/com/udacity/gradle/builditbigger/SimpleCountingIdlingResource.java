package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Anamika Tripathi on 9/11/18.
 * An simple counter implementation of that determines idleness by
 * maintaining an internal counter. When the counter is 0 - it is considered to be idle, when it is
 * non-zero it is not idle. This is very similar to the way a Semaphore
 * behaves.
 * This class can then be used to wrap up operations that while in progress should block tests from
 * accessing the UI.
 */

public final class SimpleCountingIdlingResource implements IdlingResource {

    private final String resourceName;
    private volatile AtomicInteger counting = new AtomicInteger(0);
    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    /**
     * Creates a SimpleCountingIdlingResource
     *
     * @param resourceName the resource name this resource should report to Espresso.
     */
    public SimpleCountingIdlingResource(String resourceName) {
        this.resourceName = checkNotNull(resourceName);
    }

    @Override
    public String getName() {
        return resourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counting.get()==0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = resourceCallback;
    }

    /**
     * Increments the count of in-flight transactions to the resource being monitored.
     */
    public void increment() {
        counting.getAndIncrement();
    }

    /**
     * Decrements the count of in-flight transactions to the resource being monitored.
     *
     * If this operation results in the counter falling below 0 - an exception is raised.
     *
     * @throws IllegalStateException if the counter is below 0.
     */
    public void decrement() {
        int val = counting.getAndDecrement();
        if(val == 0){
            if(resourceCallback!= null){
                resourceCallback.onTransitionToIdle();
            }
        }

        if (val < 0) {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}

