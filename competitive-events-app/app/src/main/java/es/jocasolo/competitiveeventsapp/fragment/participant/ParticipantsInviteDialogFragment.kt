package es.jocasolo.competitiveeventsapp.fragment.participant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPostDTO
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerParticipantActions
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection


class ParticipantsInviteDialogFragment(
    private val previousListFragment: ParticipantsListFragment,
    var eventId: String
) : DialogFragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var txtUsername : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_participant_invite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtUsername = view.findViewById(R.id.txt_participant_invite_id)

        // Combo box participant actions
        val eventActions: MutableList<SpinnerParticipantActions> = ArrayList()

        // Buttons
        view.findViewById<Button>(R.id.btn_participant_invite_cancel).setOnClickListener { cancel() }
        view.findViewById<Button>(R.id.btn_participant_invite_confirm).setOnClickListener { inviteUser(
            view
        ) }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun cancel(){
        dismiss()
    }

    private fun inviteUser(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)

        if(validateUsername()){
            val request  = EventUserPostDTO(txtUsername?.text.toString())
            eventService.addUser(
                eventId,
                request,
                UserAccount.getInstance(requireContext()).getToken()
            ).enqueue(object :
                Callback<EventUserDTO> {
                override fun onResponse(
                    call: Call<EventUserDTO>,
                    response: Response<EventUserDTO>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        showSuccessDialog()
                        val eventUserDTO = response.body()
                        if (eventUserDTO != null) {
                            closeDialog(eventUserDTO)
                        }
                    } else {
                        try {
                            val errorDto = Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorDTO::class.java
                            ) as ErrorDTO
                            showErrorDialog(getString(Message.forCode(errorDto.message)))
                        } catch (e: Exception) {
                            showErrorDialog(getString(R.string.error_user_invite))
                        }
                    }
                }

                override fun onFailure(call: Call<EventUserDTO>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                }
            })
        }

    }

    private fun closeDialog(eventUserDTO: EventUserDTO){
        // Pass data to previous fragment
        previousListFragment.backStackAction(eventUserDTO)
        dismiss()
    }

    /**
     * Check that the username has a value and with valid characters.
     * @return True if the username is valid and false if not
     */
    private fun validateUsername() : Boolean {
        var result = true

        if(txtUsername?.text?.isEmpty()!!){
            txtUsername?.error = getString(R.string.error_required)
            result = false
        }

        return result
    }

    private fun showSuccessDialog() {
        MyDialog.message(
            this,
            getString(R.string.user_invited),
            getString(R.string.user_invited_confirmed)
        )
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}