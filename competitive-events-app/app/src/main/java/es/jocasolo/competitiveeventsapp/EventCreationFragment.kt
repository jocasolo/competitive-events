package es.jocasolo.competitiveeventsapp

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.SpinnerEventType
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventCreationFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var txtTitle : TextView? = null
    private var txtSubtitle : TextView? = null
    private var txtDescription : TextView? = null
    private var progressBar : ProgressBar? = null
    private var cmbEventType : Spinner? = null
    private var imgEventImage : ImageView? = null

    private var filePart : MultipartBody.Part? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_title)
        txtSubtitle = view.findViewById(R.id.txt_event_subtitle)
        txtDescription = view.findViewById(R.id.txt_event_description)
        progressBar = view.findViewById(R.id.spn_event_creation)
        cmbEventType = view.findViewById(R.id.combo_events_type)
        imgEventImage = view.findViewById<ImageView>(R.id.img_event_upload_image)
        imgEventImage?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary_dark), android.graphics.PorterDuff.Mode.SRC_IN)

        // Image button
        view.findViewById<Button>(R.id.btn_event_upload_image).setOnClickListener { imageChooser() }
        view.findViewById<ImageView>(R.id.img_event_upload_image).setOnClickListener { imageChooser() }

        val eventTypes: MutableList<SpinnerEventType> = ArrayList()
        eventTypes.add(SpinnerEventType(EventType.SPORTS, getString(R.string.events_sports)))
        eventTypes.add(SpinnerEventType(EventType.OTHER, getString(R.string.events_other)))
        eventTypes.add(SpinnerEventType(EventType.ACADEMIC, getString(R.string.events_academic)))
        eventTypes.add(SpinnerEventType(EventType.FAMILY, getString(R.string.events_family)))
        eventTypes.add(SpinnerEventType(EventType.VIDEOGAMES, getString(R.string.events_videogames)))

        var arrayEventTypes = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                eventTypes
        ).also {  adapter ->
            cmbEventType?.adapter = adapter
        }
        cmbEventType?.setSelection(0)

        view.findViewById<Button>(R.id.btn_event_creation).setOnClickListener { create(view) }

    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validate()) {
            progressBar?.visibility = View.VISIBLE
            commit()
        }
    }

    private fun commit() {

        // Build request
        val eventDTO = EventPostDTO(txtTitle?.text.toString())
        eventDTO.subtitle = txtSubtitle?.text.toString()
        eventDTO.description = txtDescription?.text.toString()
        eventDTO.approvalNeeded = false
        eventDTO.inscription = EventInscriptionType.PUBLIC
        eventDTO.visibility = EventVisibilityType.PRIVATE
        //eventDTO.initDate = Date()
        //eventDTO.endDate = Date()
        eventDTO.maxPlaces = 100
        val type = cmbEventType?.selectedItem as SpinnerEventType
        eventDTO.type = type.key

        progressBar?.visibility = View.VISIBLE
        eventService.create(eventDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    val newEvent = response.body()
                    if (newEvent != null) {
                        uploadImage(newEvent.id)
                    }
                    showSuccessDialog()
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
                progressBar?.visibility = View.INVISIBLE
            }
            override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
                progressBar?.visibility = View.INVISIBLE
            }
        })

    }

    private fun showSuccessDialog() {
        MyDialog.confirmNavigate(this, getString(R.string.user_created_title), getString(R.string.user_created),
                R.id.action_event_creation_to_home
        )
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

    /**
     * Validate all the fields of the form.
     * @return True if all fields are correct and false if not
     */
    private fun validate() : Boolean {
        val validUsername = validateUsername()
        return validUsername
    }

    /**
     * Check that the username has a value and with valid characters.
     * @return True if the username is valid and false if not
     */
    private fun validateUsername() : Boolean {
        var result = true

        /*if(txtUsername?.text?.isEmpty()!!){
            txtUsername?.error = getString(R.string.error_required)
            result = false

        } else if(!usernamePattern.matcher(txtUsername?.text.toString()).matches()) {
            txtUsername?.error = getString(R.string.error_username_not_valid)
            result = false
        }*/

        return result
    }

    private fun uploadImage(id : String) {
        filePart?.let {
            eventService.updateImage(it, id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
                override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                }
                override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                }
            })
        }
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

                    Picasso.get().load(selectedImageUri).into(imgEventImage)
                    imgEventImage?.clearColorFilter()
                    imgEventImage?.setPadding(0,0,0,5)

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