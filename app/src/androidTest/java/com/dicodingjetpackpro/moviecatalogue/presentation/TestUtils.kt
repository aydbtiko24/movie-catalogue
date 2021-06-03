package com.dicodingjetpackpro.moviecatalogue.presentation

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher

/**
 * perform click on recyclerView's item view child actionView
 * @param viewActionId the id of action view.
 * @param position item position
 */
fun <T : RecyclerView.ViewHolder> clickOnActionView(
    @IdRes viewActionId: Int,
    position: Int = 0
): ViewAction {
    return actionOnItemAtPosition<T>(
        position,
        object : ViewAction {
            override fun getConstraints(): Matcher<View>? = null

            override fun getDescription(): String =
                "perform action click on child view with id: ${
                getApplicationContext<Context>().resources.getResourceName(
                    viewActionId
                )
                }"

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<View>(viewActionId).performClick()
            }
        }
    )
}

/**
 * Simplify testing Intents with Chooser
 * @param expectedShareText the actual shared data
 */
fun hasActionChooser(
    expectedShareText: String
): Matcher<Intent> = CoreMatchers.allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(
        `is`(Intent.EXTRA_INTENT),
        CoreMatchers.allOf(
            hasAction(Intent.ACTION_SEND),
            hasType("text/plain"),
            hasExtra(Intent.EXTRA_TEXT, expectedShareText),
        )
    )
)

fun getTestContext(): Context = getApplicationContext()
