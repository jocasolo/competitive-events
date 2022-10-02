package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import es.jocasolo.competitiveeventsapp.dto.event.SpinnerItem
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils

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

        val eventTypes: MutableList<SpinnerItem> = ArrayList()
        eventTypes.add(SpinnerItem("OTHERS", getString(R.string.events_other)))
        eventTypes.add(SpinnerItem("SPORTS", getString(R.string.events_sports)))
        eventTypes.add(SpinnerItem("ACADEMIC", getString(R.string.events_academic)))
        eventTypes.add(SpinnerItem("FAMILY", getString(R.string.events_family)))
        eventTypes.add(SpinnerItem("VIDEOGAMES", getString(R.string.events_videogames)))

        var arrayEventTypes = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                eventTypes
        ).also {  adapter ->
            cmbEventType?.adapter = adapter
        }


    }

    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validate()) {
            progressBar?.visibility = View.VISIBLE
            commit()
        }
    }

    private fun commit() {
        /*val userDTO = UserPostDTO(txtUsername?.text.toString(), txtEmail?.text.toString(), txtPassword?.text.toString())
        userService.create(userDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    showSuccessDialog()
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        if(errorDto.message == Message.ERROR_EMAIL_EXISTS){
                            txtEmail?.error = getString(R.string.error_email_exists)
                        }
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
                spinner?.visibility = View.INVISIBLE
            }
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
                spinner?.visibility = View.INVISIBLE
            }
        })*/
    }

    private fun showSuccessDialog() {
        MyDialog.confirmNavigate(this, getString(R.string.user_created_title), getString(R.string.user_created),
                R.id.action_Register_to_Login
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

}