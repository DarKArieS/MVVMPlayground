package com.app.aries.mvvmplayground.ColorScaleLegendView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.ColorUtils
import com.app.aries.mvvmplayground.R
import timber.log.Timber


class ColorScaleLegendView(context : Context, attr: AttributeSet)
    : ConstraintLayout(context,attr){

    init{
        this.id = View.generateViewId()

        val colorScaleBar = provideColorScaleBar()
        val lowerBoundText = TextView(context).apply{text="0";id= View.generateViewId()}
        val upperBoundText = TextView(context).apply{text="80";id= View.generateViewId()}
        val unitText = TextView(context).apply{text="km/h";id= View.generateViewId()}

        addView(colorScaleBar)
        addView(lowerBoundText)
        addView(upperBoundText)
        addView(unitText)

        val consSet = ConstraintSet()
            // set colorScaleBar layout
            .apply {
                connect(
                    colorScaleBar.id, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                    dp2px(12f)
                )
                connect(
                    colorScaleBar.id, ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                    dp2px(8f)
                )
                constrainHeight(colorScaleBar.id, ConstraintSet.WRAP_CONTENT)
                constrainWidth(colorScaleBar.id, ConstraintSet.WRAP_CONTENT)
            }
            // set lowerBoundText
            .apply {
                connect(
                    lowerBoundText.id, ConstraintSet.LEFT,
                    colorScaleBar.id, ConstraintSet.RIGHT,
                    dp2px(4f)
                )
                connect(
                    lowerBoundText.id, ConstraintSet.BOTTOM,
                    colorScaleBar.id, ConstraintSet.BOTTOM,
                    dp2px(0f)
                )
                connect(
                    lowerBoundText.id, ConstraintSet.TOP,
                    colorScaleBar.id, ConstraintSet.BOTTOM,
                    dp2px(0f)
                )
                constrainHeight(lowerBoundText.id, ConstraintSet.WRAP_CONTENT)
                constrainWidth(lowerBoundText.id, ConstraintSet.WRAP_CONTENT)
            }
            // set upperBoundText
            .apply {
                connect(
                    upperBoundText.id, ConstraintSet.LEFT,
                    colorScaleBar.id, ConstraintSet.RIGHT,
                    dp2px(4f)
                )
                connect(
                    upperBoundText.id, ConstraintSet.TOP,
                    colorScaleBar.id, ConstraintSet.TOP,
                    dp2px(0f)
                )
                connect(
                    upperBoundText.id, ConstraintSet.BOTTOM,
                    colorScaleBar.id, ConstraintSet.TOP,
                    dp2px(0f)
                )
                constrainHeight(upperBoundText.id, ConstraintSet.WRAP_CONTENT)
                constrainWidth(upperBoundText.id, ConstraintSet.WRAP_CONTENT)
            }
            // set unitText
            .apply{
                connect(
                    unitText.id, ConstraintSet.LEFT,
                    lowerBoundText.id, ConstraintSet.RIGHT,
                    dp2px(4f)
                )
                connect(
                    unitText.id, ConstraintSet.BOTTOM,
                    lowerBoundText.id, ConstraintSet.BOTTOM,
                    dp2px(0f)
                )
                constrainHeight(unitText.id, ConstraintSet.WRAP_CONTENT)
                constrainWidth(unitText.id, ConstraintSet.WRAP_CONTENT)
            }

        consSet.applyTo(this)

    }

    private fun provideColorScaleBar():ImageView{
        val colorScale = ImageView(context)
        colorScale.id = View.generateViewId()

        // ImageView content
        val bm= Bitmap.createBitmap(dp2px(20f),dp2px(100f),Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val paint = Paint()

        val colorArray = IntArray(bm.height){
            val reverseIt = bm.height - it - 1
            valueToColor(
                reverseIt.toFloat(),
                0f,
                bm.height.toFloat()
            )
        }

        paint.shader = LinearGradient(0f,0f,0f,bm.height.toFloat(),colorArray,null,Shader.TileMode.CLAMP)
        paint.style = Paint.Style.FILL

        canvas.drawPaint(paint)
        colorScale.setImageBitmap(bm)

        return colorScale
    }


    private fun dp2px(dp: Float): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    companion object{
        fun valueToColor(value:Float, minimum: Float = 0f,maximum:Float = 100f):Int{
            val hsl = floatArrayOf(0f,0.7f,0.7f)
            val length = maximum - minimum

            val middlePoint = length/2
            val lightDegree = if(value>middlePoint){
                2f - value/middlePoint
            }else{
                value/middlePoint
            }

            hsl[2] = 0.3f + 0.4f * lightDegree
            hsl[0] = (240f - 240f * value / length)
            //Timber.tag("viewTest").d(hsl[0].toString())
            //Timber.tag("viewTest").d("$reverseIt "+ hsl[2].toString())
            return ColorUtils.HSLToColor(hsl)
        }
    }

}