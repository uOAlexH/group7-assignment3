public class Main {

    final private static int lengthOfOneBeat = 500; // in milliseconds

    // Task 1: notes of a scale. Each note lasts 1 beat, except the last which we made last 2 beats.
    private static final MusicNote[] TASK1_SCALE_NOTES = {
            new MusicNote("DO", 1),
            new MusicNote("RE", 1),
            new MusicNote("MI", 1),
            new MusicNote("FA", 1),
            new MusicNote("SOL", 1),
            new MusicNote("LA", 1),
            new MusicNote("SI", 1),
            new MusicNote("DO-OCTAVE", 2)
    };

    // Task 2: notes of "Twinkle Twinkle Little Star". Each note lasts 1 beat, except every 7th note which lasts 2 beats.
    private static final MusicNote[] TASK2_TWINKLE_NOTES = {
            new MusicNote("DO", 1),  new MusicNote("DO", 1),  new MusicNote("SOL", 1), new MusicNote("SOL", 1),
            new MusicNote("LA", 1),  new MusicNote("LA", 1),  new MusicNote("SOL", 2),

            new MusicNote("FA", 1),  new MusicNote("FA", 1),  new MusicNote("MI", 1),  new MusicNote("MI", 1),
            new MusicNote("RE", 1),  new MusicNote("RE", 1),  new MusicNote("DO", 2),

            new MusicNote("SOL", 1), new MusicNote("SOL", 1), new MusicNote("FA", 1),  new MusicNote("FA", 1),
            new MusicNote("MI", 1),  new MusicNote("MI", 1),  new MusicNote("RE", 2),

            new MusicNote("SOL", 1), new MusicNote("SOL", 1), new MusicNote("FA", 1),  new MusicNote("FA", 1),
            new MusicNote("MI", 1),  new MusicNote("MI", 1),  new MusicNote("RE", 2),

            new MusicNote("DO", 1),  new MusicNote("DO", 1),  new MusicNote("SOL", 1), new MusicNote("SOL", 1),
            new MusicNote("LA", 1),  new MusicNote("LA", 1),  new MusicNote("SOL", 2),

            new MusicNote("FA", 1),  new MusicNote("FA", 1),  new MusicNote("MI", 1),  new MusicNote("MI", 1),
            new MusicNote("RE", 1),  new MusicNote("RE", 1),  new MusicNote("DO", 2)
    };

    // Main method to run tasks based on command line arguments
    public static void main(String[] args) throws Exception {

        // If no arguments, run both tasks
        if(args.length == 0){
            System.out.println("No command line arguments. Running both tasks.");

            System.out.println("\n-----Running Task 1: Scale-----\n");
            MusicPlayer task1 = new MusicPlayer(TASK1_SCALE_NOTES, lengthOfOneBeat);
            task1.run();

            Thread.sleep(2000); // 2 pause between tasks

            System.out.println("\n-----Running Task 2: Twinkle Twinkle Little Star-----\n");
            MusicPlayer task2 = new MusicPlayer(TASK2_TWINKLE_NOTES, lengthOfOneBeat);
            task2.run();
        
        // If arguments provided, run specified tasks
        } else {
            for(String arg : args){
                if(arg.equals("task1")){
                    System.out.println("\n-----Running Task 1: Scale-----\n");
                    MusicPlayer task1 = new MusicPlayer(TASK1_SCALE_NOTES, lengthOfOneBeat);
                    task1.run();
                } else if(arg.equals("task2")){
                    System.out.println("\n-----Running Task 2: Twinkle Twinkle Little Star-----\n");
                    MusicPlayer task2 = new MusicPlayer(TASK2_TWINKLE_NOTES, lengthOfOneBeat);
                    task2.run();
                } else {
                    System.out.println("Unknown argument: " + arg + ". Valid arguments are: task1, task2");
                }
            }
        }
    }
}
