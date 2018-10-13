package com.strv.vectordrawabletest

import android.animation.Animator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.annotation.IdRes
import android.support.constraint.motion.MotionLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isAnimationInProgress = false
    private var hasCircleToLetterTransitionStarted = false

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
        red_letters_container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {
                if (!hasCircleToLetterTransitionStarted && progress > 0.4) {
                    hasCircleToLetterTransitionStarted = true
                    val oneLetterDelay = (resources.getInteger(R.integer.anim_letter_duration) * 0.8).toLong()
                    animateVectorDrawable(letter_s)
                    Handler().postDelayed({ animateVectorDrawable(letter_t) }, oneLetterDelay)
                    Handler().postDelayed({ animateVectorDrawable(letter_r) }, oneLetterDelay * 2)
                    Handler().postDelayed({ animateVectorDrawable(letter_v) }, oneLetterDelay * 3)
                    Handler().postDelayed({ circularRevealDarkLogo() }, oneLetterDelay * 5)
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}

        })
        Handler().postDelayed({ red_letters_container.transitionToEnd() }, 50)
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

    private fun animateVectorDrawable(imageView: ImageView) {
        (imageView.drawable as AnimatedVectorDrawable).start()
    }

    private fun circularRevealDarkLogo() {
        val containerView = findViewById<View>(R.id.black_letters_container)
        val animator: Animator = ViewAnimationUtils.createCircularReveal(
            containerView,
            containerView.width / 2,
            containerView.height,
            0f,
            containerView.height * 1.5f
        )
        containerView.visibility = View.VISIBLE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                Handler().postDelayed({ animateLettersToSquares() }, 700)
            }
        })
    }

    private fun animateLettersToSquares() {
        val oneLetterDelay = (resources.getInteger(R.integer.anim_letter_duration) * 0.8).toLong()
        animateVectorDrawable(letter_r1)
        Handler().postDelayed({ animateVectorDrawable(letter_s1) }, oneLetterDelay)
        Handler().postDelayed({ animateVectorDrawable(letter_v1) }, oneLetterDelay * 2)
        Handler().postDelayed({ animateVectorDrawable(letter_t1) }, oneLetterDelay * 3)
    }
}
