package io.github.kabirnayeem99.dumarketingadmin.common.ktx

import android.view.View
import android.view.animation.TranslateAnimation
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.Definitions
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.ElasticAnimation
import io.github.kabirnayeem99.dumarketingadmin.common.util.animation.ElasticFinishListener


inline fun View.elasticAnimation(
    scaleX: Float = Definitions.DEFAULT_SCALE_X,
    scaleY: Float = Definitions.DEFAULT_SCALE_Y,
    duration: Int = Definitions.DEFAULT_DURATION,
    crossinline block: () -> Unit
): ElasticAnimation {
    return ElasticAnimation(this)
        .setScaleX(scaleX)
        .setScaleY(scaleY)
        .setDuration(duration)
        .setOnFinishListener(ElasticFinishListener { block() })
}


inline fun View.animateAndOnClickListener(crossinline onClickListener: (view: View) -> Unit) {
    setOnClickListener { view ->
        elasticAnimation(0.7f, 0.7f, 120) {
        }.setOnFinishListener {
            onClickListener(view)
        }.doAction()
    }
}

fun View.animateElastic() {
    elasticAnimation(0.9f, 0.9f, 400) {}.doAction()
}

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}