package com.yellowbite.movienewsreminder2.util;

public class CountDownThread extends Thread
{
    private final int duration;

    private BreakCondition breakCondition;
    private Runnable onFinishedRunnable;

    public CountDownThread(int duration, BreakCondition breakCondition, Runnable onFinishedRunnable)
    {
        this.duration = duration;
        this.breakCondition = breakCondition;
        this.onFinishedRunnable = onFinishedRunnable;
    }

    @Override
    public void run()
    {
        final int TIME_PER_INTERVALL = 100;

        try
        {
            for(int i = 0; i <= duration; i += TIME_PER_INTERVALL)
            {
                if(this.breakCondition.isTrue())
                {
                    break;
                }

                Thread.sleep(TIME_PER_INTERVALL);
            }

            this.onFinishedRunnable.run();
        } catch (InterruptedException ignored) {}
    }

    public interface BreakCondition
    {
        boolean isTrue();
    }
}
