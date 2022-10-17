package es.jocasolo.competitiveeventsapp.fragment.score

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.score.ScorePostDTO
import es.jocasolo.competitiveeventsapp.fragment.event.EventListener
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
class ScoreCreationFragment(private val previousFragment: EventMainFragment) : DialogFragment() {

    private var txtValue : TextView? = null
    private var btnCancel : Button? = null
    private var btnAdd : Button? = null
    private var imgUpload : ImageView? = null
    private var filePart : MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtValue = view.findViewById(R.id.txt_score_creation_value)
        btnCancel = view.findViewById(R.id.btn_score_creation_cancel)
        btnAdd = view.findViewById(R.id.btn_score_creation_add)
        imgUpload = view.findViewById(R.id.img_score_upload_image)

        btnAdd?.setOnClickListener { create(view) }
        btnCancel?.setOnClickListener { cancel() }

        view.findViewById<Button>(R.id.btn_score_creation_upload_image).setOnClickListener { imageChooser() }
        imgUpload?.setOnClickListener { imageChooser() }
    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validateValue()) {
            val score = ScorePostDTO(txtValue?.text.toString())
            score.imagePart = filePart

            // Pass data to previous fragment
            previousFragment.backStackAction(score)
            dismiss()
        }
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