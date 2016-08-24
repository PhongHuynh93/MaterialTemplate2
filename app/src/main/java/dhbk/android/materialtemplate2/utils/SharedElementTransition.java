package dhbk.android.materialtemplate2.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import dhbk.android.materialtemplate2.BuildConfig;
import dhbk.android.materialtemplate2.R;


/**
 * Created by huynhducthanhphong on 8/23/16.
 * fixme - create the custom transition
 *
 * @see <a href="https://developer.android.com/training/transitions/custom-transitions.html"></a>
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SharedElementTransition extends Transition{
    private final String[] transitionProperties;
    private CircleTransition fabTransition;
    private ChangeBounds defaultTransition;
    private ChangeImageTransform imageTransition;
    private String fabTransitionName;
    /**
     * khi animation, ta phải có giá trị đầu và giá trị cuối cùng 1 khoảng thời gian.
     *
     * giá trị đầu: captureStartValues()
     * giá trị cuối: captureEndValues()
     */

    public SharedElementTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        fabTransition = new CircleTransition(context, attrs);
        imageTransition = new ChangeImageTransform(context, attrs);
        defaultTransition = new ChangeBounds(context, attrs);
        fabTransitionName = context.getString(R.string.transition_fab);
        transitionProperties = initTransProps();
        if (BuildConfig.DEBUG && TextUtils.isEmpty(fabTransitionName)) {
            throw new AssertionError("Transition name should not be empty");
        }
    }

    /**
     * applies animations at starting scene
     * @param transitionValues contains a reference to the view
     */
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        if (isFabTransition(transitionValues)) {
            fabTransition.captureStartValues(transitionValues);
        } else {
            defaultTransition.captureStartValues(transitionValues);
            imageTransition.captureStartValues(transitionValues);
        }
    }

    // applies animations at ending scene
    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        if (isFabTransition(transitionValues)) {
            fabTransition.captureEndValues(transitionValues);
        } else {
            defaultTransition.captureEndValues(transitionValues);
            imageTransition.captureStartValues(transitionValues);
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return super.createAnimator(sceneRoot, startValues, endValues);
    }

    private boolean isFabTransition(TransitionValues transitionValues) {
        View view = transitionValues.view;
        return fabTransitionName.equals(view.getTransitionName());
    }

    private String[] initTransProps() {
        ArrayList<String> transProps = new ArrayList<>();
        transProps.addAll(Arrays.asList(fabTransition.getTransitionProperties()));
        transProps.addAll(Arrays.asList(imageTransition.getTransitionProperties()));
        transProps.addAll(Arrays.asList(defaultTransition.getTransitionProperties()));
        return transProps.toArray(new String[transProps.size()]);
    }
}
