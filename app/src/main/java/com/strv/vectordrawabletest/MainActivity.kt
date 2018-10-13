package com.strv.vectordrawabletest

import android.animation.Animator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.constraint.motion.MotionLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isAnimationInProgress = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTransitionListeners()

        Handler().postDelayed({ red_letters_container.transitionToEnd() }, 50)
        black_letters_container.setOnClickListener {
            resetToStart()
        }
    }

    private fun resetToStart() {
        if (!isAnimationInProgress) {
            isAnimationInProgress = true


            red_letters_container.visibility = View.GONE
            circularRevealDarkLogo(false)
            //after the circular reveal is gone
            Handler().postDelayed({
                loading_overlay.visibility = View.VISIBLE
                red_letters_container.visibility = View.VISIBLE
                black_letters_container.visibility = View.VISIBLE
                red_letters_container.setTransitionListener(null)
                black_letters_container.setTransitionListener(null)
                red_letters_container.transitionToStart()
                black_letters_container.transitionToStart()
                resetVectorDrawables()
                Handler().postDelayed({
                    black_letters_container.visibility = View.INVISIBLE
                    loading_overlay.visibility = View.GONE
                    setupTransitionListeners()
                    red_letters_container.transitionToEnd()
                }, 3000)
            }, 500)
        }
    }

    private fun setupTransitionListeners() {
        red_letters_container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                val oneLetterDelay = (resources.getInteger(R.integer.anim_letter_duration) * 0.8).toLong()
                animateVectorDrawable(letter_s)
                Handler().postDelayed({ animateVectorDrawable(letter_t) }, oneLetterDelay)
                Handler().postDelayed({ animateVectorDrawable(letter_r) }, oneLetterDelay * 2)
                Handler().postDelayed({ animateVectorDrawable(letter_v) }, oneLetterDelay * 3)
                Handler().postDelayed({ circularRevealDarkLogo(true) }, oneLetterDelay * 5)
            }

        })

        black_letters_container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                isAnimationInProgress = false
            }
        })
    }

    private fun animateVectorDrawable(imageView: ImageView) {
        (imageView.drawable as AnimatedVectorDrawable).start()
    }

    private fun resetVectorDrawable(imageView: ImageView) {
        (imageView.drawable as AnimatedVectorDrawable).reset()
    }

    private fun resetVectorDrawables() {
        resetVectorDrawable(letter_s)
        resetVectorDrawable(letter_s1)
        resetVectorDrawable(letter_t)
        resetVectorDrawable(letter_t1)
        resetVectorDrawable(letter_r)
        resetVectorDrawable(letter_r1)
        resetVectorDrawable(letter_v)
        resetVectorDrawable(letter_v1)
    }


    private fun circularRevealDarkLogo(reveal: Boolean) {
        val containerView = black_letters_container
        val animator: Animator = ViewAnimationUtils.createCircularReveal(
            containerView,
            containerView.width / 2,
            containerView.height,
            if (reveal) 0f else containerView.height * 1.5f,
            if (reveal) containerView.height * 1.5f else 0f
        )
        containerView.visibility = View.VISIBLE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                if (!reveal) {
                    containerView.visibility = View.INVISIBLE
                } else {
                    Handler().postDelayed({ animateLettersToSquares() }, 300)
                }
            }
        })
    }

    private fun animateLettersToSquares() {
        val oneLetterDelay = (resources.getInteger(R.integer.anim_letter_duration) * 0.5).toLong()
        animateVectorDrawable(letter_r1)
        Handler().postDelayed({ animateVectorDrawable(letter_s1) }, oneLetterDelay * 2)
        Handler().postDelayed({ animateVectorDrawable(letter_v1) }, oneLetterDelay)
        Handler().postDelayed({ animateVectorDrawable(letter_t1) }, oneLetterDelay)
        Handler().postDelayed({ black_letters_container.transitionToEnd() }, oneLetterDelay * 3)
    }
}
