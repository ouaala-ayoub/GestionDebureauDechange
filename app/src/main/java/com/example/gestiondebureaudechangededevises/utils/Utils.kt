package com.example.gestiondebureaudechangededevises.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.gestiondebureaudechangededevises.data.models.Error
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.NumberFormat
import java.util.*

interface DataUpdateListener {
    fun onDataUpdated()
}

interface OnDialogClicked {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
}

interface SelectionResult {
    fun onResultOk(data: Intent)
    fun onResultFailed()
}

interface AdditionalCode<T> {
    fun onResponse(responseBody: Response<T>)
    fun onFailure()
}

fun getError(
    TAG: String,
    responseBody: ResponseBody?,
    code: Int, function: String = ""
): Error {
    return try {

        val test = responseBody?.charStream()?.readText()
        Log.e(TAG, "JSONObject or msg $function: $test ")
        val error = Gson().fromJson(test, ErrorResponse::class.java)
        Log.e(TAG, "error $function: $error")

        Error(error.message, code)
    } catch (e: Exception) {
        Log.e(TAG, "getError $function: $e.stackTrace")
        val error = Error("unexpected error", code)
        Log.e(TAG, "error parsing JSON error message $function: $error")
        return error
    }
}

fun <T> handleApiRequest(
    apiCall: Call<T>,
    loadingLiveData: MutableLiveData<Boolean>?,
    dataLiveData: MutableLiveData<T?>? = null,
    TAG: String,
    additionalCode: AdditionalCode<T>? = null,
    function: String = "",
    errorMessage: MutableLiveData<String>? = null
) {
    loadingLiveData?.postValue(true)

    apiCall.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {

            if (response.isSuccessful) {
                dataLiveData?.postValue(response.body())
                Log.d(TAG, "onResponse: ${response.body()}")
            } else {
                dataLiveData?.postValue(null)
                val error = getError(TAG, response.errorBody(), response.code(), function)
                errorMessage?.postValue(error.message)
            }
            additionalCode?.onResponse(response)
            loadingLiveData?.postValue(false)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            loadingLiveData?.postValue(false)
            dataLiveData?.postValue(null)
            Log.e(TAG, "onFailure: ${t.message}")
            additionalCode?.onFailure()
        }
    })
}


inline fun <reified T> goToActivity(
    context: Context,
    extra: Pair<String, java.io.Serializable>? = null
) {
    val intent = Intent(context, T::class.java)
    if (extra != null)
        intent.putExtra(extra.first, extra.second)
    context.startActivity(intent)
}

fun EditText.updateLiveData(setLiveDataFunction: (String) -> Unit) {
    doOnTextChanged { text, _, _, _ ->
        setLiveDataFunction(text.toString())
    }
}

fun EditText.updateLiveData(liveData: MutableLiveData<String?>) {
    doOnTextChanged { text, _, _, _ ->
        liveData.postValue(text.toString())
    }
}

fun makeDialog(
    context: Context,
    onDialogClicked: OnDialogClicked,
    title: String?,
    message: String?,
    view: View? = null,
    negativeText: String = context.resources.getString(R.string.Cancel),
    positiveText: String = context.resources.getString(R.string.Yes),
    @StyleRes style: Int? = null,

    ): AlertDialog {
    val builder = if (style != null) AlertDialog.Builder(context, style)
    else
        AlertDialog.Builder(context)
    val myDialog = builder
        .setTitle(title)
        .setMessage(message)
        .setView(view)
        .setCancelable(false)
        .setPositiveButton(positiveText) { _, _ ->
            onDialogClicked.onPositiveButtonClicked()
        }

        .setNegativeButton(negativeText) { _, _ ->
            onDialogClicked.onNegativeButtonClicked()
        }
        .create()
    myDialog.setOnCancelListener {
        it.dismiss()
    }

    return myDialog
}


fun Fragment.startActivityResult(selectionResult: SelectionResult): ActivityResultLauncher<Intent> {
    return this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if (data != null) {
                selectionResult.onResultOk(data)
            } else {
                selectionResult.onResultFailed()
            }
        } else {
            selectionResult.onResultFailed()
        }
    }
}

inline fun <reified Activity> Context.launchActivityForResult(
    registerLauncher: ActivityResultLauncher<Intent>,
    extra: Pair<String, java.io.Serializable>? = null
) {
    val intent = Intent(this, Activity::class.java)
    if (extra != null)
        intent.putExtra(extra.first, extra.second)
    registerLauncher.launch(intent)
}

fun Context.shortToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()

fun Context.longToast(message: String, length: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, message, length).show()

fun formatNumberWithCommas(number: Number): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    return numberFormat.format(number)
}

fun formatNumberWithSpaces(number: Number): String {
    val numberFormat = NumberFormat.getInstance(Locale.getDefault())
    return numberFormat.format(number)
}

fun reverseFormatNumberWithCommas(formattedNumber: String): Double {
    val sanitizedNumber = formattedNumber.replace(",", "") // Remove commas
    return try {
        val number = sanitizedNumber.toDoubleOrNull() ?: 0.0
        number ?: 0.0 // Return 0.0 if parsing fails
    } catch (e: Exception) {
        0.0 // Handle parsing errors as needed
    }
}

fun MaterialAutoCompleteTextView.setWithList(
    list: List<String?>,
): ArrayAdapter<String?> {
    val adapter = ArrayAdapter(context, R.layout.list_item, list)
    setAdapter(adapter)
    return adapter
}

fun <T> showPopUpMenu(
    anchor: View,
    currentElement: T,
    id: String,
    onEditClicked: (T) -> Unit,
    onEditCredentialsClicked: ((String) -> Unit)? = null,
    onDeleteClicked: (String) -> Unit,
    menu: Int
) {

    val popupMenu = PopupMenu(anchor.context, anchor)
    popupMenu.menuInflater.inflate(menu, popupMenu.menu)

    // Set click listeners for menu items
    popupMenu.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.delete -> {
                onDeleteClicked(id)
                true
            }
            R.id.edit -> {
                onEditClicked(currentElement)
                true
            }
            R.id.edit_password -> {
                onEditCredentialsClicked?.invoke(id)
                true
            }
            else -> false
        }
    }

    popupMenu.show()
}