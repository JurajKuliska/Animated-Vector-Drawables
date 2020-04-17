package com.strv.vectordrawabletest

import android.animation.Animator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
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
		red_letters_container.setOnClickListener {
			resetToStart()
		}
	}

	private fun resetToStart() {
		if (!isAnimationInProgress) {
			isAnimationInProgress = true

			red_letters_container.visibility = View.INVISIBLE
			circularRevealDarkLogo(false)
			//after the circular reveal is gone
			Handler().postDelayed({
				loading_overlay.visibility = View.VISIBLE
				red_letters_container.visibility = View.VISIBLE
				black_letters_container.visibility = View.VISIBLE
				red_letters_container.setTransitionListener(null)
				black_letters_container.setTransitionListener(null)
				resetVectorDrawables()
				red_letters_container.transitionToStart()
				black_letters_container.transitionToStart()
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
			override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

			override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

			override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {}

			override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
				val oneLetterDelay = (resources.getInteger(R.integer.anim_letter_duration) * 0.8).toLong()
				animateVectorDrawable(letter_s, R.drawable.anim_s_circle)
				Handler().postDelayed({ animateVectorDrawable(letter_t, R.drawable.anim_t_circle) }, oneLetterDelay)
				Handler().postDelayed({ animateVectorDrawable(letter_r, R.drawable.anim_r_circle) }, oneLetterDelay * 2)
				Handler().postDelayed({ animateVectorDrawable(letter_v, R.drawable.anim_v_circle) }, oneLetterDelay * 3)
				Handler().postDelayed({ circularRevealDarkLogo(true) }, oneLetterDelay * 5)
			}

		})

		black_letters_container.setTransitionListener(object : MotionLayout.TransitionListener {
			override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

			override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

			override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

			override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
				isAnimationInProgress = false
			}
		})
	}

	private fun animateVectorDrawable(imageView: ImageView, @DrawableRes drawableRes: Int) {
		imageView.setImageDrawable(resources.getDrawable(drawableRes, theme))
		(imageView.drawable as AnimatedVectorDrawable).start()
	}

	private fun resetVectorDrawable(imageView: ImageView, @DrawableRes drawableRes: Int) {
		imageView.setImageDrawable(resources.getDrawable(drawableRes, theme))
	}

	//here the drawables are reset to their starting position but not to the animated drawable as that wasn't loaded at its starting position
	//it is loaded into the drawable just before the animation starts
	private fun resetVectorDrawables() {
		resetVectorDrawable(letter_s, R.drawable.ic_strv_s_circle)
		resetVectorDrawable(letter_s1, R.drawable.ic_strv_s)
		resetVectorDrawable(letter_t, R.drawable.ic_strv_t_circle)
		resetVectorDrawable(letter_t1, R.drawable.ic_strv_t)
		resetVectorDrawable(letter_r, R.drawable.ic_strv_r_circle)
		resetVectorDrawable(letter_r1, R.drawable.ic_strv_r)
		resetVectorDrawable(letter_v, R.drawable.ic_strv_v_circle)
		resetVectorDrawable(letter_v1, R.drawable.ic_strv_v)
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
		animateVectorDrawable(letter_r1, R.drawable.anim_r_rect)
		Handler().postDelayed({ animateVectorDrawable(letter_s1, R.drawable.anim_s_rect) }, oneLetterDelay * 2)
		Handler().postDelayed({ animateVectorDrawable(letter_v1, R.drawable.anim_v_rect) }, oneLetterDelay)
		Handler().postDelayed({ animateVectorDrawable(letter_t1, R.drawable.anim_t_rect) }, oneLetterDelay)
		Handler().postDelayed({ black_letters_container.transitionToEnd() }, oneLetterDelay * 3)
	}
}
