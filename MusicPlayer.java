
// play music using two threads with synchronization with each thread playing specific notes
public class MusicPlayer {

    private int lengthOfOneBeat; // in milliseconds
   
    private String[] notes; // sequence of notes to play
    private int[] beats; // corresponding number of beats for each note

    private static volatile int step; // Shared "beat counter" for synchronization
    
    FilePlayer filePlayer;; // Responsible for playing sound files

    public MusicPlayer(MusicNote[] musicNotes, int lengthOfOneBeat) {
        this.lengthOfOneBeat = lengthOfOneBeat;

        this.notes = new String[musicNotes.length];
        this.beats = new int[musicNotes.length];
        
        for (int i = 0; i < musicNotes.length; i++) {
            this.notes[i] = musicNotes[i].tone;
            this.beats[i] = musicNotes[i].beats;
        }
        this.filePlayer = new FilePlayer();
        step = 0;
    }

    // Plays a note for the specified duration
    private void playNote(String note, int duration) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " playing note: " + note);
        String filepath = "Sounds/" + note.toLowerCase() + ".wav";

        filePlayer.play(filepath);
        try {
            Thread.sleep(duration);
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


    // Thread 1 can only play: do, mi, sol, si, and do-octave
    private static boolean isThread1Note(String n) {
        return n.equals("DO") || n.equals("MI") || n.equals("SOL") || n.equals("SI") || n.equals("DO-OCTAVE");
    }
    // Thread 2 can only play: re, fa, la, and do-octave
    private static boolean isThread2Note(String n) {
        return n.equals("RE") || n.equals("FA") || n.equals("LA") || n.equals("DO-OCTAVE");
    }


    public void run() throws InterruptedException {

        step = 0;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < notes.length; i++) {
                String n = notes[i];
                int duration = beats[i]*lengthOfOneBeat;

                if (isThread1Note(n)) {
                    waitForStep(i);
                    playNote(n, duration);
                    step = i + 1;
                }
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < notes.length; i++) {
                String n = notes[i];
                int duration = beats[i]*lengthOfOneBeat;

                if (isThread2Note(n)) {
                    waitForStep(i);
                    playNote(n, duration);
                    step = i + 1;
                }
            }
        }, "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }   
}
