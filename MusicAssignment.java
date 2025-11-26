

public class MusicAssignment {


    // Duration of each note/rest in milliseconds
    private static final int SLEEP_TIME = 500;

    // Shared "beat counter" for synchronization
    private static volatile int step = 0;


    public static void main(String[] args) throws Exception {
        System.out.println("\n=== Task 1: scale with two threads ===");
        System.out.println("Note: The final note (DO octave) is played by both threads at the same time, though it is sometimes difficult to hear since they tend to overlap perfectly.\nTo verify that both are playing, you can change one thread to play a different note at the final step (see line 92 of this file).\n");
        
        Thread.sleep(1000); // pause before task 1
        
        runTask1();

        Thread.sleep(1000); // pause before task 2

        System.out.println("\n=== Task 2: Twinkle Twinkle with two threads ===");
        System.out.println("Thread 1 plays: DO, MI, SOL, SI while Thread 2 plays: RE, FA, LA\n");
        runTask2();
    }

    private static void play(String note, String threadName, FilePlayer filePlayer, int sleepTime) {
        
        System.out.println(threadName + " playing note: " + note);
        String filepath = "Sounds/" + note.toLowerCase() + ".wav";

        filePlayer.play(filepath);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Synchronization helper. Uses yield to tell the thread to wait until the step variable is an expected value
    // this is used in both tasks to coordinate which thread plays which note at what time.
    private static void waitForStep(int expected) {
        while (step != expected) {
            Thread.yield(); // from slides
        }
    }



    // ---------- Task 1: scale ----------



    private static void runTask1() throws InterruptedException {
        // each thread uses the step variable to know when it is its turn to play a note.

        // step goes 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
        // Notes:
        // step 0: DO   (Thread 1)
        // step 1: RE   (Thread 2)
        // step 2: MI   (Thread 1)
        // step 3: FA   (Thread 2)
        // step 4: SOL  (Thread 1)
        // step 5: LA   (Thread 2)
        // step 6: SI   (Thread 1)
        // step 7: DO_OCTAVE (both threads at the same time)

        step = 0;
        FilePlayer filePlayer = new FilePlayer();

        Thread t1 = new Thread(() -> {
            // Thread 1: do, mi, sol, si, do-octave
            waitForStep(0); // DO
            play("DO", "Thread1", filePlayer, SLEEP_TIME);
            step = 1;

            waitForStep(2); // MI
            play("MI", "Thread1", filePlayer, SLEEP_TIME);
            step = 3;

            waitForStep(4); // SOL
            play("SOL", "Thread1", filePlayer, SLEEP_TIME);
            step = 5;

            waitForStep(6); // SI
            play("SI", "Thread1", filePlayer, SLEEP_TIME);
            step = 7;

            waitForStep(7); // DO-OCTAVE (together)
            play("DO-OCTAVE", "Thread1", filePlayer, SLEEP_TIME);
            // play("RE", "Thread1", filePlayer, 0); // uncomment this line and comment out the one above to hear both threads playing different notes at the end

            // no need to change step anymore
        }, "Task1-T1");

        Thread t2 = new Thread(() -> {
            // Thread 2: re, fa, la, do-octave
            waitForStep(1); // RE
            play("RE", "Thread2", filePlayer, SLEEP_TIME);
            step = 2;

            waitForStep(3); // FA
            play("FA", "Thread2", filePlayer, SLEEP_TIME);
            step = 4;

            waitForStep(5); // LA
            play("LA", "Thread2", filePlayer, SLEEP_TIME);
            step = 6;

            waitForStep(7); // DO-OCTAVE (together)
            play("DO-OCTAVE", "Thread2", filePlayer, 0);
            // no need to change step anymore
        }, "Task1-T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }



    // ---------- Task 2: Twinkle Twinkle ----------



    // The melody "Twinkle Twinkle Little Star"
    private static final String[] TWINKLE = {
            "DO",  "DO",  "SOL", "SOL", "LA",  "LA",  "SOL",
            "FA",  "FA",  "MI",  "MI",  "RE",  "RE",  "DO",
            "SOL", "SOL", "FA",  "FA",  "MI",  "MI",  "RE",
            "SOL", "SOL", "FA",  "FA",  "MI",  "MI",  "RE",
            "DO",  "DO",  "SOL", "SOL", "LA",  "LA",  "SOL",
            "FA",  "FA",  "MI",  "MI",  "RE",  "RE",  "DO"
    };

    private static void runTask2() throws InterruptedException {
        // Here we use "step" again, but as an index in TWINKLE array
        // Each note in the array is "owned" by exactly one thread.
        // If a thread sees that the current step points to a note it owns, it plays it and increments step.

        step = 0;
        FilePlayer filePlayer = new FilePlayer();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TWINKLE.length; i++) {
                String n = TWINKLE[i];
                if (isThread1Note(n)) {
                    waitForStep(i);
                    if((step+1) % 7 == 0) // every 7th note lasts twice as long
                        play(n,"Thread1", filePlayer, SLEEP_TIME*2);
                    else
                        play(n, "Thread1", filePlayer, SLEEP_TIME);
                    step = i + 1;
                }
            }
        }, "Twinkle-T1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TWINKLE.length; i++) {
                String n = TWINKLE[i];
                if (isThread2Note(n)) {
                    waitForStep(i);
                    if((step+1) % 7 == 0) // every 7th note lasts twice as long
                        play(n,"Thread2", filePlayer, SLEEP_TIME*2);
                    else
                        play(n, "Thread2", filePlayer, SLEEP_TIME);
                    step = i + 1;
                }
            }
        }, "Twinkle-T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    // Thread 1 can only play: do, mi, sol, si, do-octave
    private static boolean isThread1Note(String n) {
        return n.equals("DO") || n.equals("MI") || n.equals("SOL") || n.equals("SI");
    }

    // Thread 2 can only play: re, fa, la, do-octave
    private static boolean isThread2Note(String n) {
        return n.equals("RE") || n.equals("FA") || n.equals("LA");
    }
}
