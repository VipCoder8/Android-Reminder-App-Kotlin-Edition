package bee.corp.ktasker.view
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bee.corp.ktasker.R
import bee.corp.ktasker.model.TaskData
import bee.corp.utility.TaskDeleteClickListener
import bee.corp.utility.TaskEditClickListener
import bee.corp.utility.TaskInfoClickListener

class TasksAdapter(list: ArrayList<TaskData>) : RecyclerView.Adapter<TaskView>() {
    private var tasksList: ArrayList<TaskData>
    private lateinit var taskDeleteClickListener: TaskDeleteClickListener
    private lateinit var taskEditClickListener: TaskEditClickListener
    private lateinit var taskInfoClickListener: TaskInfoClickListener
    init {
        tasksList = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskView {
        return TaskView(R.layout.task_view, parent.context)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: TaskView, position: Int) {
        val taskData: TaskData = tasksList[position]
        holder.setTitle(taskData.title)
        holder.setTaskDeleteClickListener(object: BasicTaskDeleteClickListener {
            override fun onBasicTaskDeleteClickListener(position: Int) {
                taskDeleteClickListener.onItemDeleteButtonClicked(tasksList[position], position)
            }
        })
        holder.setTaskEditClickListener(object: BasicTaskEditClickListener {
            override fun onBasicTaskEditClickListener(position: Int) {
                taskEditClickListener.onItemEditButtonClicked(tasksList[position], position)
            }
        })
        holder.setTaskInfoClickListener(object: BasicTaskInfoClickListener {
            override fun onBasicTaskInfoClickListener(position: Int) {
                taskInfoClickListener.onItemInfoButtonClicked(tasksList[position], position)
            }
        })
    }

    fun addTaskDeleteClickListener(tcl: TaskDeleteClickListener) {
        taskDeleteClickListener = tcl
    }

    fun addTaskEditClickListener(tcl: TaskEditClickListener) {
        taskEditClickListener = tcl
    }

    fun addTaskInfoClickListener(tcl: TaskInfoClickListener) {
        taskInfoClickListener = tcl
    }
    interface BasicTaskDeleteClickListener {
        fun onBasicTaskDeleteClickListener(position: Int)
    }
    interface BasicTaskEditClickListener {
        fun onBasicTaskEditClickListener(position: Int)
    }
    interface BasicTaskInfoClickListener {
        fun onBasicTaskInfoClickListener(position: Int)
    }
}