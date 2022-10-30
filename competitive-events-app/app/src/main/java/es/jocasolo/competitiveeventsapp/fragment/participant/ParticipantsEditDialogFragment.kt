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
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPutDTO
import es.jocasolo.competitiveeventsapp.enums.actions.ParticipantActions
import es.jocasolo.competitiveeventsapp.enums.event.EventStatusType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
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


class ParticipantsEditDialogFragment(
    private val previousListFragment: ParticipantsListFragment,
    var participant: ParticipantDTO,
    var eventId: String) : DialogFragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var txtUsername : TextView? = null
    private var cmbParticipantActions : Spinner? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_participant_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtUsername = view.findViewById(R.id.txt_dialog_participant_name)
        cmbParticipantActions = view.findViewById(R.id.cmb_participant_actions)

        // Combo box participant actions
        val eventActions: MutableList<SpinnerParticipantActions> = ArrayList()

        when(participant.status) {
            EventUserStatusType.ACCEPTED -> {
                eventActions.add(SpinnerParticipantActions(ParticipantActions.DELETE, getString(R.string.action_delete)))
            }
            EventUserStatusType.INVITED -> {
                eventActions.add(SpinnerParticipantActions(ParticipantActions.DELETE, getString(R.string.action_delete)))
            }
            EventUserStatusType.WAITING_APPROVAL -> {
                eventActions.add(SpinnerParticipantActions(ParticipantActions.ACCEPT, getString(R.string.action_accept)))
                eventActions.add(SpinnerParticipantActions(ParticipantActions.REJECT, getString(R.string.action_reject)))
            }
            EventUserStatusType.REJECTED -> {
                eventActions.add(SpinnerParticipantActions(ParticipantActions.DELETE, getString(R.string.action_delete)))
            }
            EventUserStatusType.DELETED -> {
                eventActions.add(SpinnerParticipantActions(ParticipantActions.INVITE, getString(R.string.action_invite)))
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            eventActions
        ).also {  adapter ->
            cmbParticipantActions?.adapter = adapter
        }
        cmbParticipantActions?.setSelection(0)

        // Buttons
        view.findViewById<Button>(R.id.btn_participant_actions_cancel).setOnClickListener { cancel() }
        view.findViewById<Button>(R.id.btn_participant_actions_confirm).setOnClickListener { accept(view) }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun cancel(){
        dismiss()
    }

    private fun accept(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)

        val selectedAction = cmbParticipantActions?.selectedItem as SpinnerParticipantActions
        val action = selectedAction.key

        val eventUserPutDTO = EventUserPutDTO(participant.id)
        eventUserPutDTO.status = when(action) {
            ParticipantActions.ACCEPT -> {EventUserStatusType.ACCEPTED}
            ParticipantActions.REJECT -> {EventUserStatusType.REJECTED}
            ParticipantActions.DELETE -> {EventUserStatusType.DELETED}
            ParticipantActions.INVITE -> {EventUserStatusType.INVITED}
        }

        eventService.updateUser(eventId, eventUserPutDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    showSuccessDialog()
                    previousListFragment.backStackAction(eventUserPutDTO)
                    dismiss()
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
                        showErrorDialog(getString(R.string.error_user_invite))
                    }
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })

    }

    private fun showSuccessDialog() {
        MyDialog.message(this, getString(R.string.user_action), getString(R.string.user_action_success))
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}