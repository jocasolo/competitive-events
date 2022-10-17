package es.jocasolo.competitiveeventsapp.fragment.punishment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentPostDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PunishmentCreationFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var txtTitle : TextView? = null
    private var txtDescription : TextView? = null
    private var txtPosition : TextView? = null
    private var radSortAsc : RadioButton? = null
    private var radSortDesc : RadioButton? = null
    private var btnCancel : Button? = null
    private var btnAdd : Button? = null
    private var imgUpload : ImageView? = null
    private var filePart : MultipartBody.Part? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_punishments_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTitle = view.findViewById(R.id.txt_punishments_creation_title)
        txtDescription = view.findViewById(R.id.txt_punishments_creation_description)
        txtPosition = view.findViewById(R.id.txt_punishments_creation_position)
        radSortAsc = view.findViewById(R.id.rad_punishments_creation_asc)
        radSortDesc = view.findViewById(R.id.rad_punishments_creation_desc)
        btnCancel = view.findViewById(R.id.btn_punishments_creation_cancel)
        btnAdd = view.findViewById(R.id.btn_punishments_creation_add)
        imgUpload = view.findViewById(R.id.img_punishments_upload_image)

        btnAdd?.setOnClickListener { create(view) }
        btnCancel?.setOnClickListener { cancel() }

        view.findViewById<Button>(R.id.btn_punishments_creation_upload_image).setOnClickListener { imageChooser() }
        imgUpload?.setOnClickListener { imageChooser() }
    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validateTitle()) {
            val punishment = PunishmentPostDTO(txtTitle?.text.toString())
            punishment.description = txtDescription?.text.toString()
            punishment.requiredPosition = txtPosition?.text.toString().toInt()
            punishment.imagePart = filePart
            punishment.sortScore = getSortScore()

            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("punishment", punishment)

            findNavController().navigateUp()
        }
    }

    /**
     * Check that the username has a value and with valid characters.
     * @return True if the username is valid and false if not
     */
    private fun validateTitle() : Boolean {
        var result = true

        if(txtTitle?.text?.isEmpty()!!){
            txtTitle?.error = getString(R.string.error_required)
            result = false
        }

        return result
    }

    private fun getSortScore(): ScoreSortType {
        return if(radSortAsc?.isChecked == true){
            ScoreSortType.ASC
        } else {
            ScoreSortType.DESC
        }
    }

    private fun cancel(){
        findNavController().popBackStack()
    }

    private fun imageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1) {
            if (requestCode == 200) {
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {

                    Picasso.get().load(selectedImageUri).into(imgUpload)
                    imgUpload?.clearColorFilter()
                    imgUpload?.setPadding(0,0,0,5)

                    // Upload image to storage service
                    val filePath: String = getImagePath(selectedImageUri)
                    val file = File(filePath)
                    filePart = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(MediaType.parse("image/*"), file));

                }
            }
        }
    }

    private fun getImagePath(selectedImageUri: Uri): String {
        val filePath: String
        if ("content" == selectedImageUri.scheme) {
            val cursor = requireContext().contentResolver.query(selectedImageUri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null)
            cursor?.moveToFirst()
            filePath = cursor!!.getString(0)
            cursor.close()
        } else {
            filePath = selectedImageUri.path.toString()
        }
        return filePath
    }

}