package es.jocasolo.competitiveeventsapp.fragment.score

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.score.ScorePostDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.fragment.event.EventMainFragment
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.lang3.StringUtils
import java.io.File


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ScoreCreationDialogFragment(
    private val previousFragment: EventMainFragment,
    private val scoreValueType: ScoreValueType) : DialogFragment() {

    private var txtValue : TextView? = null
    private var txtHour : TextView? = null
    private var txtMinute : TextView? = null
    private var txtSecond : TextView? = null
    private var btnCancel : Button? = null
    private var btnAdd : Button? = null
    private var imgUpload : ImageView? = null
    private var filePart : MultipartBody.Part? = null
    private var linearLayoutScoreValue : LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_score_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtValue = view.findViewById(R.id.txt_score_creation_value)
        txtHour = view.findViewById(R.id.txt_score_hour)
        txtMinute = view.findViewById(R.id.txt_score_minute)
        txtSecond = view.findViewById(R.id.txt_score_second)
        btnCancel = view.findViewById(R.id.btn_score_creation_cancel)
        btnAdd = view.findViewById(R.id.btn_score_creation_add)
        imgUpload = view.findViewById(R.id.img_score_upload_image)
        linearLayoutScoreValue = view.findViewById(R.id.linear_layout_score_time)

        btnAdd?.setOnClickListener { create(view) }
        btnCancel?.setOnClickListener { cancel() }

        view.findViewById<Button>(R.id.btn_score_creation_upload_image).setOnClickListener { imageChooser() }
        imgUpload?.setOnClickListener { imageChooser() }

        when(scoreValueType) {
            ScoreValueType.NUMERIC -> txtValue?.inputType = EditorInfo.TYPE_CLASS_NUMBER
            ScoreValueType.DECIMAL -> txtValue?.inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
            ScoreValueType.TIME -> {
                txtValue?.visibility = View.GONE
                linearLayoutScoreValue?.visibility = View.VISIBLE
            }
        }
    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        val score = ScorePostDTO()
        score.imagePart = filePart
        if(scoreValueType != ScoreValueType.TIME){
            if(validateValue()) {
                score.value = txtValue?.text.toString()
            }
        } else {
            score.value = getTimeScoreAsLong()
        }

        // Pass data to previous fragment
        previousFragment.backStackAction(score)
        dismiss()
    }

    private fun getTimeScoreAsLong(): String {
        var value : Long = 0

        if(isValidTime()){
            if(StringUtils.isNotEmpty(txtHour?.text.toString())){
                value += txtHour?.text.toString().toLong() * 3600000
            }
            if(StringUtils.isNotEmpty(txtMinute?.text.toString())){
                value += txtMinute?.text.toString().toLong() * 60000
            }
            if(StringUtils.isNotEmpty(txtSecond?.text.toString())){
                value += txtSecond?.text.toString().toLong() * 1000
            }
        }

        return value.toString()
    }

    private fun isValidTime(): Boolean {
        var validHour = true
        var validMinute = true
        var validSecond = true

        if(StringUtils.isNotEmpty(txtHour?.text?.toString()) && txtHour?.text.toString().toInt() !in 0..23){
            txtHour?.error = getString(R.string.error_value_not_valid)
            validHour = false
        }

        if(StringUtils.isEmpty(txtMinute?.text?.toString()) && txtMinute?.text.toString().toInt() !in 0..59){
            txtMinute?.error = getString(R.string.error_value_not_valid)
            validMinute = false
        }

        if(StringUtils.isEmpty(txtSecond?.text?.toString()) && txtSecond?.text.toString().toInt() !in 0..59){
            txtSecond?.error = getString(R.string.error_value_not_valid)
            validSecond = false
        }

        return validHour && validMinute && validSecond
    }

    /**
     * Check that the username has a value and with valid characters.
     * @return True if the username is valid and false if not
     */
    private fun validateValue() : Boolean {
        var result = true

        if(StringUtils.isEmpty(txtValue?.text)){
            txtValue?.error = getString(R.string.error_required)
            result = false
        }

        return result
    }

    private fun cancel(){
        dismiss()
    }

    private fun imageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1) {
            if (requestCode == 200) {
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {

                    Picasso.get().load(selectedImageUri).into(imgUpload)
                    imgUpload?.clearColorFilter()
                    imgUpload?.setPadding(0, 0, 0, 5)

                    // Upload image to storage service
                    val filePath: String = getImagePath(selectedImageUri)
                    val file = File(filePath)
                    filePart = MultipartBody.Part.createFormData(
                        "file", file.name, RequestBody.create(
                            MediaType.parse(
                                "image/*"
                            ), file
                        )
                    );

                }
            }
        }
    }

    private fun getImagePath(selectedImageUri: Uri): String {
        val filePath: String
        if ("content" == selectedImageUri.scheme) {
            val cursor = requireContext().contentResolver.query(
                selectedImageUri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                null,
                null
            )
            cursor?.moveToFirst()
            filePath = cursor!!.getString(0)
            cursor.close()
        } else {
            filePath = selectedImageUri.path.toString()
        }
        return filePath
    }

}