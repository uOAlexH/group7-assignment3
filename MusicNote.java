
// Represents a musical note with a string for its tone and duration in beats
public class MusicNote { 

    final private static String[] VALID_TONES = {
        "DO", "RE", "MI", "FA", "SOL", "LA", "SI", "DO-OCTAVE"
    };
    
    String tone;
    int beats;
    
    public MusicNote(String tone, int beats){

        if(beats <= 0){
            throw new IllegalArgumentException("Beats must be positive.");
        }
        boolean validTone = false;
        for(String valid : VALID_TONES){
            if(tone.equals(valid)){
                validTone = true;
                break;
            }
        }
        if(!validTone){
            throw new IllegalArgumentException("Invalid tone: " + tone);
        }

        this.tone = tone;
        this.beats = beats;
    }
}
