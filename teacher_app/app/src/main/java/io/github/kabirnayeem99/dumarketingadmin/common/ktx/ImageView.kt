package io.github.kabirnayeem99.dumarketingadmin.common.ktx

import android.widget.ImageView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingadmin.R


fun ImageView.load(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .fitCenter()
        .error(R.drawable.ic_fluent_image_48_regular)
        .dontAnimate()
        .into(this)

}