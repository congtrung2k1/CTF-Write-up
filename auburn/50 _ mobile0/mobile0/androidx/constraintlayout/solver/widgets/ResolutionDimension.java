// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

public class ResolutionDimension extends ResolutionNode
{
    float value;
    
    public ResolutionDimension() {
        this.value = 0.0f;
    }
    
    public void remove() {
        this.state = 2;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.value = 0.0f;
    }
    
    public void resolve(final int n) {
        if (this.state == 0 || this.value != n) {
            this.value = (float)n;
            if (this.state == 1) {
                this.invalidate();
            }
            this.didResolve();
        }
    }
}
