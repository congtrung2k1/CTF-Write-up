// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import android.transition.Transition$EpicenterCallback;
import android.graphics.Rect;
import android.transition.Transition$TransitionListener;
import java.util.List;
import java.util.Collection;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.transition.TransitionSet;
import java.util.ArrayList;
import android.view.View;
import android.transition.Transition;

class FragmentTransitionCompat21 extends FragmentTransitionImpl
{
    private static boolean hasSimpleTarget(final Transition transition) {
        return !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetIds()) || !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetNames()) || !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetTypes());
    }
    
    @Override
    public void addTarget(final Object o, final View view) {
        if (o != null) {
            ((Transition)o).addTarget(view);
        }
    }
    
    @Override
    public void addTargets(final Object o, final ArrayList<View> list) {
        final Transition transition = (Transition)o;
        if (transition == null) {
            return;
        }
        if (transition instanceof TransitionSet) {
            final TransitionSet set = (TransitionSet)transition;
            for (int transitionCount = set.getTransitionCount(), i = 0; i < transitionCount; ++i) {
                this.addTargets(set.getTransitionAt(i), list);
            }
        }
        else if (!hasSimpleTarget(transition) && FragmentTransitionImpl.isNullOrEmpty(transition.getTargets())) {
            for (int size = list.size(), j = 0; j < size; ++j) {
                transition.addTarget((View)list.get(j));
            }
        }
    }
    
    @Override
    public void beginDelayedTransition(final ViewGroup viewGroup, final Object o) {
        TransitionManager.beginDelayedTransition(viewGroup, (Transition)o);
    }
    
    @Override
    public boolean canHandle(final Object o) {
        return o instanceof Transition;
    }
    
    @Override
    public Object cloneTransition(final Object o) {
        Object clone = null;
        if (o != null) {
            clone = ((Transition)o).clone();
        }
        return clone;
    }
    
    @Override
    public Object mergeTransitionsInSequence(final Object o, Object o2, final Object o3) {
        final Transition transition = null;
        Object setOrdering = o;
        final Transition transition2 = (Transition)o2;
        final Transition transition3 = (Transition)o3;
        if (setOrdering != null && transition2 != null) {
            setOrdering = new TransitionSet().addTransition((Transition)setOrdering).addTransition(transition2).setOrdering(1);
        }
        else if (setOrdering == null) {
            setOrdering = transition;
            if (transition2 != null) {
                setOrdering = transition2;
            }
        }
        if (transition3 != null) {
            o2 = new TransitionSet();
            if (setOrdering != null) {
                ((TransitionSet)o2).addTransition((Transition)setOrdering);
            }
            ((TransitionSet)o2).addTransition(transition3);
            return o2;
        }
        return setOrdering;
    }
    
    @Override
    public Object mergeTransitionsTogether(final Object o, final Object o2, final Object o3) {
        final TransitionSet set = new TransitionSet();
        if (o != null) {
            set.addTransition((Transition)o);
        }
        if (o2 != null) {
            set.addTransition((Transition)o2);
        }
        if (o3 != null) {
            set.addTransition((Transition)o3);
        }
        return set;
    }
    
    @Override
    public void removeTarget(final Object o, final View view) {
        if (o != null) {
            ((Transition)o).removeTarget(view);
        }
    }
    
    @Override
    public void replaceTargets(final Object o, final ArrayList<View> list, final ArrayList<View> list2) {
        final Transition transition = (Transition)o;
        if (transition instanceof TransitionSet) {
            final TransitionSet set = (TransitionSet)transition;
            for (int transitionCount = set.getTransitionCount(), i = 0; i < transitionCount; ++i) {
                this.replaceTargets(set.getTransitionAt(i), list, list2);
            }
        }
        else if (!hasSimpleTarget(transition)) {
            final List targets = transition.getTargets();
            if (targets != null && targets.size() == list.size() && targets.containsAll(list)) {
                int size;
                if (list2 == null) {
                    size = 0;
                }
                else {
                    size = list2.size();
                }
                for (int j = 0; j < size; ++j) {
                    transition.addTarget((View)list2.get(j));
                }
                for (int k = list.size() - 1; k >= 0; --k) {
                    transition.removeTarget((View)list.get(k));
                }
            }
        }
    }
    
    @Override
    public void scheduleHideFragmentView(final Object o, final View view, final ArrayList<View> list) {
        ((Transition)o).addListener((Transition$TransitionListener)new Transition$TransitionListener() {
            public void onTransitionCancel(final Transition transition) {
            }
            
            public void onTransitionEnd(final Transition transition) {
                transition.removeListener((Transition$TransitionListener)this);
                view.setVisibility(8);
                for (int size = list.size(), i = 0; i < size; ++i) {
                    ((View)list.get(i)).setVisibility(0);
                }
            }
            
            public void onTransitionPause(final Transition transition) {
            }
            
            public void onTransitionResume(final Transition transition) {
            }
            
            public void onTransitionStart(final Transition transition) {
            }
        });
    }
    
    @Override
    public void scheduleRemoveTargets(final Object o, final Object o2, final ArrayList<View> list, final Object o3, final ArrayList<View> list2, final Object o4, final ArrayList<View> list3) {
        ((Transition)o).addListener((Transition$TransitionListener)new Transition$TransitionListener() {
            public void onTransitionCancel(final Transition transition) {
            }
            
            public void onTransitionEnd(final Transition transition) {
            }
            
            public void onTransitionPause(final Transition transition) {
            }
            
            public void onTransitionResume(final Transition transition) {
            }
            
            public void onTransitionStart(final Transition transition) {
                final Object val$enterTransition = o2;
                if (val$enterTransition != null) {
                    FragmentTransitionCompat21.this.replaceTargets(val$enterTransition, list, null);
                }
                final Object val$exitTransition = o3;
                if (val$exitTransition != null) {
                    FragmentTransitionCompat21.this.replaceTargets(val$exitTransition, list2, null);
                }
                final Object val$sharedElementTransition = o4;
                if (val$sharedElementTransition != null) {
                    FragmentTransitionCompat21.this.replaceTargets(val$sharedElementTransition, list3, null);
                }
            }
        });
    }
    
    @Override
    public void setEpicenter(final Object o, final Rect rect) {
        if (o != null) {
            ((Transition)o).setEpicenterCallback((Transition$EpicenterCallback)new Transition$EpicenterCallback() {
                public Rect onGetEpicenter(final Transition transition) {
                    final Rect val$epicenter = rect;
                    if (val$epicenter != null && !val$epicenter.isEmpty()) {
                        return rect;
                    }
                    return null;
                }
            });
        }
    }
    
    @Override
    public void setEpicenter(final Object o, final View view) {
        if (view != null) {
            final Transition transition = (Transition)o;
            final Rect rect = new Rect();
            this.getBoundsOnScreen(view, rect);
            transition.setEpicenterCallback((Transition$EpicenterCallback)new Transition$EpicenterCallback() {
                public Rect onGetEpicenter(final Transition transition) {
                    return rect;
                }
            });
        }
    }
    
    @Override
    public void setSharedElementTargets(final Object o, final View e, final ArrayList<View> list) {
        final TransitionSet set = (TransitionSet)o;
        final List targets = set.getTargets();
        targets.clear();
        for (int size = list.size(), i = 0; i < size; ++i) {
            FragmentTransitionImpl.bfsAddViewChildren(targets, list.get(i));
        }
        targets.add(e);
        list.add(e);
        this.addTargets(set, list);
    }
    
    @Override
    public void swapSharedElementTargets(final Object o, final ArrayList<View> list, final ArrayList<View> list2) {
        final TransitionSet set = (TransitionSet)o;
        if (set != null) {
            set.getTargets().clear();
            set.getTargets().addAll(list2);
            this.replaceTargets(set, list, list2);
        }
    }
    
    @Override
    public Object wrapTransitionInSet(final Object o) {
        if (o == null) {
            return null;
        }
        final TransitionSet set = new TransitionSet();
        set.addTransition((Transition)o);
        return set;
    }
}
