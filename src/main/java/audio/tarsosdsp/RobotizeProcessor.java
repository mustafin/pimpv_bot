package audio.tarsosdsp;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.effects.DelayEffect;

/**
 * Created by musta on 2016-09-11.
 */
public class RobotizeProcessor implements AudioProcessor {

    private PitchShifter pf;
    private DelayEffect delay;

    public RobotizeProcessor(double sampleRate, int bufferSize, int overlap) {
        pf = new PitchShifter(0.8, sampleRate, bufferSize, overlap);
        delay = new DelayEffect(0.04, 0.6, sampleRate);
    }

    @Override
    public boolean process(AudioEvent audioEvent) {

        float floatBuffer[] = audioEvent.getFloatBuffer();
        float floatBuffer2[] = new float[floatBuffer.length];
        System.arraycopy(floatBuffer, 0, floatBuffer2, 0, floatBuffer.length);
        pf.process(audioEvent);
        delay.process(audioEvent);


        int overlap = audioEvent.getOverlap();
        for (int i = overlap; i < floatBuffer.length; i++) {
            floatBuffer[i] = floatBuffer[i] + floatBuffer2[i];
        }

        return true;
    }

    @Override
    public void processingFinished() {

    }
}
