package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/*
* glow value ranges from 0.0 to 1.0
*/

public class GPUImageGlowFilter extends GPUImageFilter {
    public static final String GLOW_FRAGMENT_SHADER = "" +
    	    "extern vec2 size;\n" +
    	    "extern int samples = 5;\n" +
    	    "extern float quality = 2.5;\n" +
    	    "\n" +
    	    "vec4 effect(vec4 colour, Image tex, vec2 tc, vec2 sc)\n" +
    	    "{\n" +
    	    "	vec4 source = Texel(tex, tc);\n" +
    		"	vec4 sum = vec4(0);\n" +
    	    "	int diff = (samples - 1) / 2;\n" +
    	    "	vec2 sizeFactor = vec2(1) / size * quality;\n" +      
    		"	for (int x = -diff; x <= diff; x++)\n" +
    		"	{\n" +
    		"		for (int y = -diff; y <= diff; y++)\n" +
    		"		{\n" +
    		"			vec2 offset = vec2(x, y) * sizeFactor;\n" +
    		"			sum += Texel(tex, tc + offset);\n" +
    		"		}\n" +
    		"	}\n" +      
    		"	return ((sum / (samples * samples)) + source) * colour;\n" +
    	    "}\n"; 

    private int mGlowLocation;
    private float mGlow;

    public GPUImageGlowFilter() {
        this(1.2f);
    }

    public GPUImageGlowFilter(final float glow) {
        super(NO_FILTER_VERTEX_SHADER, GLOW_FRAGMENT_SHADER);
        mGlow = glow;
    }

    @Override
    public void onInit() {
        super.onInit();
        mGlowLocation = GLES20.glGetUniformLocation(getProgram(), "samples");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setGlow(mGlow);
    }

    public void setGlow(final float glow) {
        mGlow = glow;
        setFloat(mGlowLocation, mGlow);
    }
}
