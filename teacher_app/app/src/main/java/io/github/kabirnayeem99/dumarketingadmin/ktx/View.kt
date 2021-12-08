package io.github.kabirnayeem99.dumarketingadmin.ktx

import android.view.View
import io.github.kabirnayeem99.dumarketingadmin.util.animation.Definitions
import io.github.kabirnayeem99.dumarketingadmin.util.animation.ElasticAnimation
import io.github.kabirnayeem99.dumarketingadmin.util.animation.ElasticFinishListener


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
        elasticAnimation(0.9f, 0.9f, 400) {
        }.setOnFinishListener {
            onClickListener(view)
        }.doAction()
    }
}

fun View.animateElastic() {
    elasticAnimation(0.9f, 0.9f, 400) {}.doAction()
}