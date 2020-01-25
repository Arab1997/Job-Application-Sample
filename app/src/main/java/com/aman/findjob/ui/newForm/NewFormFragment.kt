package com.aman.findjob.ui.newForm

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aman.findjob.R
import com.aman.findjob.extention.createFactory
import com.aman.findjob.extention.currentDate
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.MainActivity
import com.aman.findjob.utils.DateUtils
import com.aman.findjob.utils.OnDateSetListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_new_form.*
import javax.inject.Inject


class NewFormFragment: DaggerFragment() {

    @Inject
    lateinit var repo: FormRepoI

    private lateinit var viewModel: FormViewModel

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setToolbar()
        addTextWatcher()

    }

    private fun init() {
        Log.d(TAG, " >>> Initializing viewModel")

        val factory = FormViewModel(repo).createFactory()
        viewModel = ViewModelProvider(this, factory).get(FormViewModel::class.java)
    }

    private fun addNewForm() {
        Log.d(TAG, " >>> Receive call for new Form ")

        viewModel.addForm(getAndValidateForm())

    }

    private fun addTextWatcher() {
        et_form_title.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_form_title.error = null
                } else {
                    til_form_title.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_form_discription.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_form_discription.error = null
                } else {
                    til_form_discription.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_budget.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_budget.error = null
                } else {
                    til_budget.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_start_date.text = Editable.Factory.getInstance()
            .newEditable(DateUtils.format(currentDate, DateUtils.DATE_FORMAT))
        et_start_date.setOnClickListener {
            DateUtils.datePicker(context!!, object : OnDateSetListener{
                override fun onDateSet(view: DatePicker?, timeInMillis: Long) {
                    et_start_date.text = Editable.Factory.getInstance()
                        .newEditable(DateUtils.format(timeInMillis, DateUtils.DATE_FORMAT))
                }
            }).show()
        }
    }

    private fun getAndValidateForm(): Form {
        val title = til_form_title.editText!!.text.toString()
        if (title.isEmpty()) {
            til_form_title.error = getString(R.string.required)
        }

        val description = til_form_discription.editText!!.text.toString()
        if (description.isEmpty()) {
            til_form_discription.error = getString(R.string.required)
        }

        val budget = til_budget.editText!!.text.toString()
        var budgetValue: Int = 0
        if (budget.isEmpty()) {
            til_budget.error = getString(R.string.required)
        } else {
            budgetValue = budget.toInt()
        }

        val currency = til_currency.hint.toString()

        val rate = til_rate.editText!!.text.toString()
        if (rate.isEmpty()) {
            til_rate.error = getString(R.string.required)
        }

        val paymentMode = til_payment_mode.editText!!.text.toString()
        if (paymentMode.isEmpty()) {
            til_payment_mode.error = getString(R.string.required)
        }

        val startDate = til_start_date.editText!!.text.toString()
        var startDateValue: Long = 0L
        if (startDate.isEmpty()) {
            til_start_date.error = getString(R.string.required)
        } else {
            startDateValue = startDate.toLong()
        }

        val jobTerm = til_job_terms.editText!!.text.toString()
        if (jobTerm.isEmpty()) {
            til_job_terms.error = getString(R.string.required)
        }

        return Form(title, description, budgetValue, currency, rate, paymentMode, startDateValue, jobTerm)

    }


    private fun setToolbar(){
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title =
            CLASS_SIMPLE_NAME
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            listener?.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_send -> {
                addNewForm()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val addItem = menu.findItem(R.id.menu_add)
        val sendItem = menu.findItem(R.id.menu_send)

        addItem.isVisible = false
        sendItem.isVisible = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackPressed()
    }

    companion object {
        private const val TAG = "NewFormFragement"
        const val CLASS_SIMPLE_NAME = "New Form"

        fun newInstance(): NewFormFragment = NewFormFragment()
    }
}