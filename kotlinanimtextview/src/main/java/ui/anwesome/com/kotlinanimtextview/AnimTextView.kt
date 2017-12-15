package ui.anwesome.com.kotlinanimtextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 15/12/17.
 */
class AnimTextView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var texts:LinkedList<String> = LinkedList()
    val renderer = TextContainerRenderer(this)
    fun addText(text:String) {
        texts.add(text)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.draw(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.startUpdating()
            }
        }
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
    data class TextRect(var x:Float,var y:Float,var w:Float,var h:Float,var text:String) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#4527A0")
            canvas.save()
            canvas.translate(x+w/2,y+h/2)
            canvas.scale(scale,1f)
            canvas.drawRoundRect(RectF(-w/2,-h/2,w/2,h/2),w/10,h/2,paint)
            paint.textSize = h/3
            paint.color = Color.WHITE
            canvas.drawText(text,-paint.measureText(text)/2,paint.textSize/2,paint)
            canvas.restore()
        }
    }
    data class TextRectContainer(var w:Float,var h:Float,var texts:LinkedList<String>) {
        var textRects:ConcurrentLinkedQueue<TextRect> = ConcurrentLinkedQueue()
        val state = TextContainerState()
        var button:TextContainerButton?=null
        init {
            var n = (texts.size)/2
            val hGap = (3*h/5)/(2*n+1)
            var y = 2*h/5 + 3*hGap/2
            var x = w/10
            texts.forEach { text ->
                textRects.add(TextRect(x,y,2*w/5,h,text))
                x += w/2
                if(x > w) {
                    x = w/10
                    y += 2*hGap
                }
            }
            button = TextContainerButton(w/2,h/5,h/15)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            textRects.forEach { textRect ->
                textRect.draw(canvas,paint,state.scale)
            }
            button?.draw(canvas,paint,state.scale)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class TextContainerState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1f) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
    data class TextContainerAnimator(var container:TextRectContainer,var view:AnimTextView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun startUpdating() {
            if(!animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class TextContainerRenderer(var view:AnimTextView) {
        var time = 0
        var animator:TextContainerAnimator?=null
        fun draw(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = TextContainerAnimator(TextRectContainer(w,h,view.texts),view)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun startUpdating() {
            animator?.startUpdating()
        }
    }
}