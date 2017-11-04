package com.example.user.soil_supervise_kotlin.Fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.user.soil_supervise_kotlin.Interfaces.FragmentBackPressedListener
import com.example.user.soil_supervise_kotlin.OtherClass.ProgressDialog
import com.example.user.soil_supervise_kotlin.OtherClass.MySharedPreferences
import com.example.user.soil_supervise_kotlin.R
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.vibrator
import org.json.JSONException
import org.json.JSONObject

class SettingFragment : BaseFragment(), FragmentBackPressedListener
{

    companion object
    {
        fun newInstance(): SettingFragment
        {
            val fragment = SettingFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private var _mainSettingDataTemp = arrayOfNulls<String>(4)
    private var _mainSettingDataText = arrayOfNulls<String>(5)
    private var _recyclerSetting: RecyclerView? = null
    private var _mAdapter = SettingRecyclerViewAdapter()
    private var _sharePref: MySharedPreferences? = null

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        Log.e("SettingFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.e("SettingFragment", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = inflater!!.inflate(R.layout.fragment_setting, container, false)
        _recyclerSetting = view.findViewById<RecyclerView>(R.id._recyclerSetting) as RecyclerView

        _sharePref = MySharedPreferences.initInstance(activity)

        _mainSettingDataText = arrayOf("1.ESP8266之IP位址 ", "2.ESP8266之通訊埠 ", "3.Server IP位置 ",
                "4.歷史資料儲存檔名 ", "5.AUTO TOGGLE ")
        _mainSettingDataTemp = arrayOf(_sharePref!!.getIPAddress(), _sharePref!!.getPort(),
                _sharePref!!.getServerIP(), _sharePref!!.getFileSavedName())

        val layoutManger = LinearLayoutManager(this.context)
        layoutManger.orientation = LinearLayoutManager.VERTICAL
        _recyclerSetting!!.layoutManager = layoutManger

        _recyclerSetting!!.adapter = _mAdapter

        Log.e("SettingFragment", "onCreateView")
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        Log.e("SettingFragment", "onActivityCreated")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Log.e("SettingFragment", "onViewCreated")

        btn_sensor_setting.text = getString(R.string.sensor_setting)
        btn_sensor_setting.setOnClickListener {
            val dialog = dialogSensorSetter(activity)
            dialog.show()
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.setCanceledOnTouchOutside(true)
        }

        btn_done_setting.text = getString(R.string.done)
        btn_done_setting.setOnClickListener {
            doneSetting()
        }
    }

    override fun onStart()
    {
        super.onStart()
        Log.e("SettingFragment", "onStart")
    }

    override fun onResume()
    {
        super.onResume()
        Log.e("SettingFragment", "onResume")
    }

    override fun onPause()
    {
        super.onPause()
        Log.e("SettingFragment", "onPause")
    }

    override fun onStop()
    {
        super.onStop()
        Log.e("SettingFragment", "onStop")
    }

    override fun OnFragmentBackPressed()
    {
        val vpMain = activity.findViewById<ViewPager>(R.id._vpMain) as ViewPager
        vpMain.currentItem = 1
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        Log.e("SettingFragment", "onDestroyView")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.e("SettingFragment", "onDestroy")
    }

    override fun onDetach()
    {
        super.onDetach()
        Log.e("SettingFragment", "onDetach")
    }

    inner class SettingRecyclerViewAdapter : RecyclerView.Adapter<SettingRecyclerViewAdapter.ViewHolder>()
    {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tx_settingTitle: TextView? = null
            var edit_setting: EditText? = null
            var switch_setting: Switch? = null

            init
            {
                tx_settingTitle = itemView.findViewById<TextView>(R.id.tx_settingTitle) as TextView
                edit_setting = itemView.findViewById<EditText>(R.id.edit_setting) as EditText
                switch_setting = itemView.findViewById<Switch>(R.id.switch_setting) as Switch
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingRecyclerViewAdapter.ViewHolder
        {
            val converterView = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_setting, parent, false)
            return ViewHolder(converterView)
        }

        override fun getItemCount(): Int
        {
            return 5
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int)
        {
            if (holder!!.adapterPosition == 4)
            {
                holder.switch_setting!!.visibility = View.VISIBLE
                holder.switch_setting!!.isChecked = _sharePref!!.getIsAutoToggle()
                if (_sharePref!!.getIsAutoToggle())
                {
                    holder.switch_setting!!.text = getString(R.string.on)
                }
                else
                {
                    holder.switch_setting!!.text = getString(R.string.off)
                }
                holder.switch_setting!!.setOnCheckedChangeListener { compoundButton, b ->
                    if (compoundButton.id == R.id.switch_setting)
                    {
                        if (b)
                        {
                            _sharePref!!.PutBoolean("getIsAutoToggle", true)
                            holder.switch_setting!!.text = getString(R.string.on)
                            toast("自動遙控開啟")
                        }
                        else
                        {
                            _sharePref!!.PutBoolean("getIsAutoToggle", false)
                            holder.switch_setting!!.text = getString(R.string.off)
                            toast("自動遙控關閉")
                        }
                    }
                }
                holder.tx_settingTitle!!.text = _mainSettingDataText[4]
                holder.edit_setting!!.visibility = View.GONE
            }
            else
            {
                holder.switch_setting!!.visibility = View.GONE
                holder.tx_settingTitle!!.text = _mainSettingDataText[holder.adapterPosition]

                val editable_setting = SpannableStringBuilder(_mainSettingDataTemp[holder.adapterPosition])
                holder.edit_setting!!.text = editable_setting

                holder.edit_setting!!.addTextChangedListener(object : TextWatcher
                {
                    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                    {
                    }

                    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                    {
                    }

                    override fun afterTextChanged(arg0: Editable)
                    {
                        //get data in _mainSettingDataTemp array
                        _mainSettingDataTemp[holder.adapterPosition] = arg0.toString()
                    }
                })
            }
        }
    }

    private fun doneSetting()
    {
        val regIPAddress = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
        val regPort = Regex("([0-9][0-9])|([0-9][0-9][0-9])")

        if (_mainSettingDataTemp[0]!!.matches(regIPAddress) && _mainSettingDataTemp[1]!!.matches(regPort) &&
                _mainSettingDataTemp[2]!!.matches(regIPAddress) && _mainSettingDataTemp[3] != "")
        {
            toast("設定成功")
            _sharePref!!.PutString("getIPAddress", _mainSettingDataTemp[0])
            _sharePref!!.PutString("getPort", _mainSettingDataTemp[1])
            _sharePref!!.PutString("getServerIP", _mainSettingDataTemp[2])
            _sharePref!!.PutString("getFileSavedName", _mainSettingDataTemp[3])
        }
        else
        {
            val v = context.vibrator
            v.vibrate(500)
            toast("設定失敗")
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean)
    {
        super.setUserVisibleHint(isVisibleToUser)
        Log.e("SettingFragment", isVisibleToUser.toString())
    }

    private fun dialogSensorSetter(context: Context): AlertDialog
    {
        val nullParent: ViewGroup? = null
        val convertView = LayoutInflater.from(context).inflate(R.layout.dialog_sensor_setting, nullParent)

        val recycler_in_dialog = convertView.findViewById<RecyclerView>(R.id.recycler_in_dialog) as RecyclerView
        val btn_insert = convertView.findViewById<Button>(R.id.btn_insert) as Button
        val btn_drop = convertView.findViewById<Button>(R.id.btn_drop) as Button
        val btn_sensor_done = convertView.findViewById<Button>(R.id.btn_sensor_done) as Button

        val dialog = android.app.AlertDialog.Builder(context).setView(convertView).create()

        val layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = LinearLayoutManager.VERTICAL
        recycler_in_dialog.layoutManager = layoutManger

        val dialogAdapter = DialogRecyclerViewAdapter()
        recycler_in_dialog.adapter = dialogAdapter
        val mDividerLine = SimpleDividerItemDecoration(context)
        recycler_in_dialog.addItemDecoration(mDividerLine)

        btn_insert.setOnClickListener {
            val id = dialogAdapter.itemCount
            tryEditSensorQuantity("create_sensor", (id + 1).toString(), dialogAdapter)
        }
        btn_drop.setOnClickListener {
            if (_sharePref!!.getSensorQuantity() == 5)
            {
                toast("At least 5 sensor")
            }
            else
            {
                val id = dialogAdapter.itemCount
                tryEditSensorQuantity("delete_sensor", id.toString(), dialogAdapter)
            }
        }
        btn_sensor_done.setOnClickListener {
            val wrongInput = checkInputType()

            if (wrongInput == -1)
            {
                toast("設定成功")
                dialog.dismiss()
            }
            else
            {
                val v = context.vibrator
                v.vibrate(500)
                toast("(" + _sharePref!!.getSensorName(wrongInput) + ")" + "輸入有誤")
            }
        }

        return dialog
    }

    inner class DialogRecyclerViewAdapter : RecyclerView.Adapter<DialogRecyclerViewAdapter.ViewHolder>()
    {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var checkText: CheckBox? = null
            var editName: EditText? = null
            var editCondition: EditText? = null
            var editPin: EditText? = null

            init
            {
                checkText = itemView.findViewById<CheckBox>(R.id.checkText) as CheckBox
                editName = itemView.findViewById<EditText>(R.id.editName) as EditText
                editCondition = itemView.findViewById<EditText>(R.id.editCondition) as EditText
                editPin = itemView.findViewById<EditText>(R.id.editPin) as EditText
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DialogRecyclerViewAdapter.ViewHolder
        {
            val converterView = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_sensor_setting, parent, false)
            return ViewHolder(converterView)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int)
        {
            holder!!.checkText?.setOnCheckedChangeListener { _, b ->
                if (b)
                {
                    _sharePref!!.PutBoolean("getCheck" + holder.adapterPosition.toString(), true)
                    _sharePref!!.PutInt("getSensor" + holder.adapterPosition.toString() + "Visibility", View.VISIBLE)
                }
                else
                {
                    _sharePref!!.PutBoolean("getCheck" + holder.adapterPosition.toString(), false)
                    _sharePref!!.PutInt("getSensor" + holder.adapterPosition.toString() + "Visibility", View.GONE)
                }
            }
            holder.checkText?.isChecked = _sharePref!!.getCheck(holder.adapterPosition)

            holder.editName?.text = SpannableStringBuilder(_sharePref!!.getSensorName(holder.adapterPosition))
            holder.editName?.addTextChangedListener(object : TextWatcher
            {
                override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                {
                }

                override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                {
                }

                override fun afterTextChanged(arg0: Editable)
                {
                    //get data in _mainSettingDataTemp array
                    _sharePref!!.PutString("getSensor" + holder.adapterPosition.toString() + "Name", arg0.toString())
                }
            })

            holder.editCondition?.text = SpannableStringBuilder(_sharePref!!.getSensorCondition(holder.adapterPosition))
            holder.editCondition?.addTextChangedListener(object : TextWatcher
            {
                override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                {
                }

                override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int)
                {
                }

                override fun afterTextChanged(arg0: Editable)
                {
                    //get data in _mainSettingDataTemp array
                    _sharePref!!.PutString("getSensor" + holder.adapterPosition.toString() + "Condition", arg0.toString())
                }
            })

            holder.editPin?.text = SpannableStringBuilder(_sharePref!!.getSensorPin(holder.adapterPosition))
            holder.editPin?.addTextChangedListener(object : TextWatcher
            {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                }

                override fun afterTextChanged(p0: Editable?)
                {
                    _sharePref!!.PutString("getSensor" + holder.adapterPosition.toString() + "Pin", p0.toString())
                }
            })

        }

        override fun getItemCount(): Int
        {
            return _sharePref!!.getSensorQuantity()
        }
    }

    inner class SimpleDividerItemDecoration constructor(context: Context) : RecyclerView.ItemDecoration()
    {
        private val mDivider = ContextCompat.getDrawable(context, R.drawable.divider_line)

        override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?)
        {
            val left = parent!!.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount)
            {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    private fun tryEditSensorQuantity(query: String, sensorID: String, dialogAdapter: DialogRecyclerViewAdapter)
    {
        Log.e("SettingFragment", "tryEditSensorQuantity")

        val queue: RequestQueue = Volley.newRequestQueue(context)
        val progressDialog = ProgressDialog.dialogProgress(activity, "連接中…", View.VISIBLE)

        if (!progressDialog.isShowing)
        {
            progressDialog.show()
            progressDialog.setCancelable(false)
        }
        val ServerIP = _sharePref!!.getServerIP()
        val user = _sharePref!!.getUser()
        val pass = _sharePref!!.getPass()
        val phpAddress = "http://$ServerIP/$query.php?&server=$ServerIP&user=$user&pass=$pass&sensor_id=$sensorID"

        val connectRequest = JsonObjectRequest(phpAddress, null, object : Response.Listener<JSONObject>
        {
            override fun onResponse(p0: JSONObject?)
            {
                try
                {
                    val success = p0?.getInt("success")
                    val message = p0?.getString("message")
                    if (success == 1 && message == "Created Successfully.")
                    {
                        if (progressDialog.isShowing)
                        {
                            progressDialog.dismiss()
                        }
                        dialogAdapter.notifyItemInserted(_sharePref!!.getSensorQuantity())
                        _sharePref!!.PutInt("getSensorQuantity", _sharePref!!.getSensorQuantity() + 1)
                        toast("已新增Sensor")
                    }
                    else if (success == 1 && message == "Deleted Successfully.")
                    {
                        if (progressDialog.isShowing)
                        {
                            progressDialog.dismiss()
                        }
                        dialogAdapter.notifyItemRemoved(_sharePref!!.getSensorQuantity() - 1)
                        _sharePref!!.PutInt("getSensorQuantity", _sharePref!!.getSensorQuantity() - 1)
                        toast("已移除Sensor")
                    }
                    else
                    {
                        if (progressDialog.isShowing)
                        {
                            progressDialog.dismiss()
                        }
                        toast("操作失敗")
                    }
                }
                catch (e: JSONException)
                {
                    Log.e("editSensor", e.toString())
                }
            }
        }, object : Response.ErrorListener
        {
            override fun onErrorResponse(p0: VolleyError?)
            {
                if (progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                VolleyLog.e("ERROR", p0.toString())
                toast("CONNECT ERROR")
            }
        })
        val Timeout = 9000
        val policy = DefaultRetryPolicy(Timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        connectRequest.retryPolicy = policy
        queue.add(connectRequest)
    }

    private fun checkInputType(): Int
    {
        val regWarningCondition = Regex("[0-9]*.?[0-9]+")
        val regPin = Regex("[0-9]{1,2}")
        val sensorQuantity = _sharePref!!.getSensorQuantity()

        for (i in 0 until sensorQuantity)
        {
            val warnCondition = _sharePref!!.getSensorCondition(i)
            val pin = _sharePref!!.getSensorPin(i)

            if (!warnCondition.matches(regWarningCondition) || !pin.matches(regPin))
            {
                return i
            }
        }

        return -1
    }

}