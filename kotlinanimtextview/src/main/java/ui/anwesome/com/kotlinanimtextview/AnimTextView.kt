package ui.anwesome.com.kotlinanimtextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/12/17.
 */
class AnimTextView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    fun addText(text:String) {

    }
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    data class TextContainerButton(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f*scale)
            paint.strokeWidth = r/8
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#1565C0")
            canvas.drawCircle(0f,0f,r,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-2*r/3,0f,2*r/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
}