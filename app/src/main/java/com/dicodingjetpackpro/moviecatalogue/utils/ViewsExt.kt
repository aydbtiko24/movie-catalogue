package com.dicodingjetpackpro.moviecatalogue.utils

import android.app.Activity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ShareCompat
import com.dicodingjetpackpro.moviecatalogue.R

// Created by aydbtiko on 4/29/2021.
/**
 * Start share compat
 * @param content text to be shared
 */
fun Activity.startShareIntent(content: String) {
    ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle(resources.getString(R.string.share_title))
        .setText(resources.getString(R.string.share_text, content))
        .startChooser()
}

/**
 * Add an action which will be invoked after transition completed
 * @return [MotionLayout.TransitionListener] added to motion layout
 */
inline fun MotionLayout.doOnTransitionCompleted(
    crossinline action: (currentStateId: Int) -> Unit
): MotionLayout.TransitionListener {
    val transitionListener = object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        }

        override fun onTransitionCompleted(p0: MotionLayout?, currentStateId: Int) {
            action.invoke(currentStateId)
        }
    }

    addTransitionListener(transitionListener)

    return transitionListener
}

/**
 * transition [MotionLayout] to given
 * @param state id  with screenWidth [ConstraintSet.WRAP_CONTENT]
 * screenHeight [ConstraintSet.WRAP_CONTENT]
 */
fun MotionLayout.doTransitionTo(
    state: Int
) {
    transitionToState(
        state,
        ConstraintSet.WRAP_CONTENT,
        ConstraintSet.WRAP_CONTENT
    )
}
