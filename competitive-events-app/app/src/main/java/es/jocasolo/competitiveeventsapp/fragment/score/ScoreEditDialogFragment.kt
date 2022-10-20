package es.jocasolo.competitiveeventsapp.fragment.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScorePutDTO
import es.jocasolo.competitiveeventsapp.enums.actions.ScoreActions
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import es.jocasolo.competitiveeventsapp.fragment.event.EventMainFragment
import es.jocasolo.competitiveeventsapp.service.ScoreService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerScoreActions
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ScoreEditDialogFragment(
    private val previousFragment: EventMainFragment,
    private val scoreDTO : ScoreDTO,
    val eventId: String) : DialogFragment() {

    private val scoreService = ServiceBuilder.buildService(ScoreService::class.java)

    private var cmbScoreActions : Spinner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_score_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cmbScoreActions = view.findViewById(R.id.cmb_score_actions)

        // Combo box participant actions
        val scoreActions: MutableList<SpinnerScoreActions> = ArrayList()

        when(scoreDTO.status) {
            ScoreStatusType.VALID -> scoreActions.add(SpinnerScoreActions(ScoreActions.INVALIDATE, getString(R.string.action_invalidate)))
            ScoreStatusType.NOT_VALID -> scoreActions.add(SpinnerScoreActions(ScoreActions.VALIDATE, getString(R.string.action_validate)))
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            scoreActions
        ).also { adapter ->
            cmbScoreActions?.adapter = adapter
        }
        cmbScoreActions?.setSelection(0)

        view.findViewById<TextView>(R.id.txt_score_edit_username)?.text = scoreDTO.user?.id
        view.findViewById<TextView>(R.id.txt_score_edit_value)?.text = scoreDTO.value
        view.findViewById<Button>(R.id.btn_score_actions_confirm).setOnClickListener { confirm(view) }
        view.findViewById<Button>(R.id.btn_score_actions_cancel)?.setOnClickListener { cancel() }

    }

    private fun confirm(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)

        val scorePutDTO = ScorePutDTO()
        scorePutDTO.eventId = eventId

        val selectedAction = cmbScoreActions?.selectedItem as SpinnerScoreActions
        val action = selectedAction.key

        scorePutDTO.status = when(action) {
            ScoreActions.INVALIDATE -> ScoreStatusType.NOT_VALID
            ScoreActions.VALIDATE -> ScoreStatusType.VALID
        }

        scoreService.update(scoreDTO.id, scorePutDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    showSuccessDialog()
                    previousFragment.backStackAction(scorePutDTO)
                    dismiss()
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
                        showErrorDialog(getString(R.string.error_score_action))
                    }
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })

    }

    private fun cancel(){
        dismiss()
    }

    private fun showSuccessDialog() {
        MyDialog.message(this, getString(R.string.score_action), getString(R.string.score_action_success))
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}