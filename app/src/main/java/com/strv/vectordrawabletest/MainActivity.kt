package com.strv.vectordrawabletest

import android.animation.Animator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private var isRevealed = false

    private val squareVector: AnimatedVectorDrawable by lazy {
        resources.getDrawable(
            R.drawable.anim_morph,
            theme
        ) as AnimatedVectorDrawable
    }
    private val circleVector: AnimatedVectorDrawable by lazy {
        resources.getDrawable(
            R.drawable.anim_morph_reversed,
            theme
        ) as AnimatedVectorDrawable
    }
    private var isSquare = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.image).setOnClickListener {
            (it as ImageView).setImageDrawable(if (isSquare) squareVector else circleVector)
            (it.drawable as AnimatedVectorDrawable).start()
            isSquare = !isSquare
        }
        setOnClickListenerForAnimationForView(R.id.letter_v)
        setOnClickListenerForAnimationForView(R.id.letter_t)
        setOnClickListenerForAnimationForView(R.id.letter_s)
        setOnClickListenerForAnimationForView(R.id.letter_r)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_circular_reveal -> {
                circularRevealDarkLogo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setOnClickListenerForAnimationForView(@IdRes id: Int) {
        findViewById<ImageView>(id).setOnClickListener {
            ((it as ImageView).drawable as AnimatedVectorDrawable).start()
        }
    }

    private fun circularRevealDarkLogo() {
        val containerView = findViewById<View>(R.id.black_letters_container)
        val animator: Animator = ViewAnimationUtils.createCircularReveal(
            containerView,
            containerView.width / 2,
            containerView.height,
            if (!isRevealed) 0f else containerView.height * 1.5f,
            if (!isRevealed) containerView.height * 1.5f else 0f
        )
        containerView.visibility = View.VISIBLE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                if (!isRevealed) {
                    containerView.visibility = View.INVISIBLE
                }
            }
        })
        isRevealed = !isRevealed
    }
}
