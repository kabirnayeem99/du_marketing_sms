@file:Suppress("unused")

package io.github.kabirnayeem99.dumarketingadmin.common.util.animation

import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.Definitions.DEFAULT_ANIMATION_ANCHOR
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.Definitions.DEFAULT_DURATION
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.Definitions.DEFAULT_SCALE_X
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.Definitions.DEFAULT_SCALE_Y


@PublishedApi
internal object Definitions {
    /** The default target elastic scale size of the animation. */
    const val DEFAULT_SCALE: Float = 0.9f

    /** The default target elastic scale-x size of the animation. */
    const val DEFAULT_SCALE_X: Float = 0.85f

    /** The default target elastic scale-y size of the animation. */
    const val DEFAULT_SCALE_Y: Float = 0.85f

    /** The default duration of the animation. */
    const val DEFAULT_DURATION: Int = 400

    /** The default anchor of the elastic animation. */
    const val DEFAULT_ANIMATION_ANCHOR = 0.5f
}

/** ElasticFinishListener is for listening elastic animation terminated status. */
fun interface ElasticFinishListener {

    /** invoked when the elastic animation is terminated. */
    fun onFinished()
}

/** ElasticAnimation implements elastic animations for android views or view groups. */
class ElasticAnimation(private val view: View) {

    @JvmField
    @set:JvmSynthetic
    var scaleX = DEFAULT_SCALE_X

    @JvmField
    @set:JvmSynthetic
    var scaleY = DEFAULT_SCALE_Y

    @JvmField
    @set:JvmSynthetic
    var duration = DEFAULT_DURATION

    @JvmField
    @set:JvmSynthetic
    var listener: ViewPropertyAnimatorListener? = null

    @JvmField
    @set:JvmSynthetic
    var finishListener: ElasticFinishListener? = null

    var isAnimating: Boolean = false
        private set

    /** Sets a target elastic scale-x size of the animation. */
    fun setScaleX(scaleX: Float): ElasticAnimation = apply { this.scaleX = scaleX }

    /** Sets a target elastic scale-y size of the animation. */
    fun setScaleY(scaleY: Float): ElasticAnimation = apply { this.scaleY = scaleY }

    /** Sets a duration of the animation. */
    fun setDuration(duration: Int): ElasticAnimation = apply { this.duration = duration }

    /** Sets an animator listener of the animation. */
    fun setListener(listener: ViewPropertyAnimatorListener): ElasticAnimation = apply {
        this.listener = listener
    }

    /** An animator listener of the animation. */
    fun setOnFinishListener(finishListener: ElasticFinishListener?): ElasticAnimation = apply {
        this.finishListener = finishListener
    }

    /** An [ElasticFinishListener] listener of the animation. */
    @JvmSynthetic
    inline fun setOnFinishListener(crossinline block: () -> Unit): ElasticAnimation = apply {
        this.finishListener = ElasticFinishListener { block() }
    }

    /** starts elastic animation. */
    fun doAction() {
        if (!isAnimating && view.scaleX == 1f) {
            val animatorCompat = ViewCompat.animate(view)
                .setDuration(duration.toLong())
                .scaleX(scaleX)
                .scaleY(scaleY)
                .setInterpolator(CycleInterpolator(DEFAULT_ANIMATION_ANCHOR)).apply {
                    listener?.let { setListener(it) } ?: setListener(object :
                        ViewPropertyAnimatorListener {
                        override fun onAnimationCancel(view: View?) = Unit
                        override fun onAnimationStart(view: View?) {
                            isAnimating = true
                        }

                        override fun onAnimationEnd(view: View?) {
                            finishListener?.onFinished()
                            isAnimating = false
                        }
                    })
                }
            if (view is ViewGroup) {
                (0 until view.childCount).map { view.getChildAt(it) }.forEach { child ->
                    ViewCompat.animate(child)
                        .setDuration(duration.toLong())
                        .scaleX(scaleX)
                        .scaleY(scaleY)
                        .setInterpolator(CycleInterpolator(DEFAULT_ANIMATION_ANCHOR))
                        .withLayer()
                        .start()
                }
            }
            animatorCompat.withLayer().start()
        }
    }
}