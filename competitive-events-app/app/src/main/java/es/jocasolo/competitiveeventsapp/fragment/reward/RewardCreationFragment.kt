package es.jocasolo.competitiveeventsapp.fragment.reward

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.reward.RewardPostDTO
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
class RewardCreationFragment : Fragment() {

    private var txtTitle : TextView? = null
    private var txtDescription : TextView? = null
    private var txtPosition : EditText? = null
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
        return inflater.inflate(R.layout.fragment_rewards_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.event_new_reward)

        txtTitle = view.findViewById(R.id.txt_rewards_creation_title)
        txtDescription = view.findViewById(R.id.txt_rewards_creation_description)
        txtPosition = view.findViewById(R.id.txt_rewards_creation_position)
        radSortAsc = view.findViewById(R.id.rad_rewards_creation_asc)
        radSortDesc = view.findViewById(R.id.rad_rewards_creation_desc)
        btnCancel = view.findViewById(R.id.btn_rewards_creation_cancel)
        btnAdd = view.findViewById(R.id.btn_rewards_creation_add)
        imgUpload = view.findViewById(R.id.img_rewards_upload_image)

        btnAdd?.setOnClickListener { create(view) }
        btnCancel?.setOnClickListener { cancel() }

        view.findViewById<Button>(R.id.btn_rewards_creation_upload_image).setOnClickListener { imageChooser() }
        imgUpload?.setOnClickListener { imageChooser() }

        if(arguments?.containsKey("nextPosition") == true){
            txtPosition?.setText(arguments?.getInt("nextPosition")!!.toString())
        }

    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validateTitle()) {
            val reward = RewardPostDTO(txtTitle?.text.toString())
            reward.description = txtDescription?.text.toString()
            reward.requiredPosition = txtPosition?.text.toString().toInt()
            reward.imagePart = filePart
            reward.sortScore = getSortScore()

            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("reward", reward)

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
                    filePart = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(MediaType.parse("image/*"), file))

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