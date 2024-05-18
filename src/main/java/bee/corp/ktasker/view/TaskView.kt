package bee.corp.ktasker.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bee.corp.ktasker.R

@SuppressLint("ClickableViewAccessibility")
class TaskView(resid: Int, c: Context) : RecyclerView.ViewHolder(LayoutInflater.from(c).inflate(resid, null)) {

    private var titleView: TextView = super.itemView.findViewById(R.id.title_view)
    var editButton: ImageButton = super.itemView.findViewById(R.id.edit_button)
    var deleteButton: ImageButton = super.itemView.findViewById(R.id.delete_button)
    var infoButton: ImageButton = super.itemView.findViewById(R.id.info_button)
    private var scaleUpAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_up)
    private var scaleDownAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_down)

    private lateinit var taskDeleteClickListener: TasksAdapter.BasicTaskDeleteClickListener
    private lateinit var taskEditClickListener: TasksAdapter.BasicTaskEditClickListener
    private lateinit var taskInfoClickListener: TasksAdapter.BasicTaskInfoClickListener
    init {
        super.itemView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                super.itemView.startAnimation(scaleUpAnimation)
            }
            if(event.action == MotionEvent.ACTION_DOWN) {
                super.itemView.startAnimation(scaleDownAnimation)
            }
            true
        }
    }
    fun setTaskDeleteClickListener(tcl: TasksAdapter.BasicTaskDeleteClickListener) {
        this.taskDeleteClickListener = tcl
        this.deleteButton.setOnClickListener {
            this@TaskView.taskDeleteClickListener.onBasicTaskDeleteClickListener(adapterPosition)
        }
    }
    fun setTaskEditClickListener(tcl: TasksAdapter.BasicTaskEditClickListener) {
        this.taskEditClickListener = tcl
        this.editButton.setOnClickListener {
            this@TaskView.taskEditClickListener.onBasicTaskEditClickListener(adapterPosition)
        }
    }
    fun setTaskInfoClickListener(tcl: TasksAdapter.BasicTaskInfoClickListener) {
        this.taskInfoClickListener = tcl
        this.infoButton.setOnClickListener {
            this@TaskView.taskInfoClickListener.onBasicTaskInfoClickListener(adapterPosition)
        }
    }
    fun setTitle(title: String) { titleView.text = title }
    fun getTitle() : String { return titleView.text.toString() }
}