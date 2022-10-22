package es.jocasolo.competitiveeventsapp.fragment.event

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPostDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentPostDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardPostDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.PunishmentService
import es.jocasolo.competitiveeventsapp.service.RewardService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListPunishmentLiteAdapter
import es.jocasolo.competitiveeventsapp.ui.adapters.ListRewardLiteAdapter
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerEventType
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerScoreType
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventCreationFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)
    private val rewardService = ServiceBuilder.buildService(RewardService::class.java)
    private val punishmentService = ServiceBuilder.buildService(PunishmentService::class.java)

    private var sdf : SimpleDateFormat? = null

    private var txtTitle : TextView? = null
    private var txtSubtitle : TextView? = null
    private var txtDescription : TextView? = null
    private var progressBar : ProgressBar? = null
    private var cmbEventType : Spinner? = null
    private var cmbScoreType : Spinner? = null
    private var imgEventImage : ImageView? = null
    private var txtInitDate : TextView? = null
    private var txtEndDate : TextView? = null
    private var swtInscription : Switch? = null
    private var swtVisibility : Switch? = null
    private var swtApproval : Switch? = null
    private var swtSort: Switch? = null
    private var txtMaxPlaces : TextView? = null

    private var rewardsRecyclerView: RecyclerView? = null
    private var punishmentsRecyclerView: RecyclerView? = null
    private var rewardAdapter : ListRewardLiteAdapter? = null
    private var punishmentAdapter : ListPunishmentLiteAdapter? = null

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

        sdf = SimpleDateFormat(getString(R.string.sdf_date))

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_title)
        txtSubtitle = view.findViewById(R.id.txt_event_subtitle)
        txtDescription = view.findViewById(R.id.txt_event_description)
        progressBar = view.findViewById(R.id.spn_event_creation)
        cmbEventType = view.findViewById(R.id.combo_events_type)
        cmbScoreType = view.findViewById(R.id.combo_score_type)
        txtInitDate = view.findViewById(R.id.txt_event_init_date)
        txtInitDate?.setOnClickListener { showDatePicker(txtInitDate) }
        txtEndDate = view.findViewById(R.id.txt_event_end_date)
        txtEndDate?.setOnClickListener { showDatePicker(txtEndDate) }
        swtApproval = view.findViewById(R.id.switch_event_approval)
        swtInscription = view.findViewById(R.id.switch_event_inscription)
        swtSort = view.findViewById(R.id.switch_event_sort_type)
        swtVisibility = view.findViewById(R.id.switch_event_visibility)
        txtMaxPlaces = view.findViewById(R.id.txt_event_max_places)

        // Image upload button
        view.findViewById<Button>(R.id.btn_event_upload_image).setOnClickListener { imageChooser() }
        imgEventImage = view.findViewById(R.id.img_event_upload_image)
        imgEventImage?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary_dark), PorterDuff.Mode.SRC_IN)
        imgEventImage?.setOnClickListener { imageChooser() }

        // Switchs
        swtInscription?.setOnCheckedChangeListener { _, isChecked -> if(isChecked) swtVisibility?.isChecked = false }
        swtVisibility?.setOnCheckedChangeListener { _, isChecked -> if(isChecked) swtInscription?.isChecked = false }

        // EventTypes
        initEventTypesComboBox()

        // ScoreTypes
        initScoreTypesComboBox()

        // Create event button
        view.findViewById<Button>(R.id.btn_event_creation).setOnClickListener { create(view) }

        // Rewards list and add button
        initRewards()

        // Punishments list and add button
        initPunishments()

    }

    /**
     * Initializes the list of rewards and the add button actions. Register an observer to get a
     * new award added in another fragment when comes back to this fragment.
     */
    private fun initRewards(){

        // Reward list (RecyclerView)
        if(rewardAdapter == null){
            rewardAdapter = ListRewardLiteAdapter(requireContext(), null)
        }
        rewardsRecyclerView = requireView().findViewById(R.id.recycler_event_creation_reward_list)
        rewardsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        rewardsRecyclerView?.adapter = rewardAdapter

        // Rewards add button and observer
        requireView().findViewById<TextView>(R.id.btn_event_creation_add_reward).setOnClickListener {
            findNavController().navigate(R.id.action_event_creation_to_reward_creation)
        }
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<BackStackEntryDTO>("reward")
            ?.observe(viewLifecycleOwner, {
                addReward(it)
                findNavController().currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<BackStackEntryDTO>("reward")
            })
    }

    /**
     * Adds a reward to the rewards adapter and recycler view. This award comes from another event from which it has been returned.
     */
    private fun addReward(it: BackStackEntryDTO?) {
        rewardAdapter?.addReward(it)
        rewardAdapter?.notifyDataSetChanged()
    }

    /**
     * Initializes the list of punishments and the add button actions. Register an observer to get a
     * new punishment added in another fragment when comes back to this fragment.
     */
    private fun initPunishments() {

        // Punishment list (RecyclerView)
        if(punishmentAdapter == null){
            punishmentAdapter = ListPunishmentLiteAdapter(requireContext(), null)
        }
        punishmentsRecyclerView = requireView().findViewById(R.id.recycler_event_creation_punishment_list)
        punishmentsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        punishmentsRecyclerView?.adapter = punishmentAdapter

        // Punishments add button and observer
        requireView().findViewById<TextView>(R.id.btn_event_creation_add_punishment).setOnClickListener {
            findNavController().navigate(R.id.action_event_creation_to_punishment_creation)
        }
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<BackStackEntryDTO>("punishment")
            ?.observe(viewLifecycleOwner, {
                addPunishment(it)
                findNavController().currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<BackStackEntryDTO>("punishment")
            })
    }

    /**
     * Adds a reward to the rewards adapter and recycler view. This punishment comes from another event from which it has been returned.
     */
    private fun addPunishment(it: BackStackEntryDTO?) {
        punishmentAdapter?.addPunishment(it)
        punishmentAdapter?.notifyDataSetChanged()
    }

    /**
     * Initilize the event types combo box
     */
    private fun initEventTypesComboBox() {
        // Combo box event types
        val eventTypes: MutableList<SpinnerEventType> = ArrayList()
        eventTypes.add(SpinnerEventType(EventType.SPORTS, getString(R.string.events_sports)))
        eventTypes.add(SpinnerEventType(EventType.OTHER, getString(R.string.events_other)))
        eventTypes.add(SpinnerEventType(EventType.ACADEMIC, getString(R.string.events_academic)))
        eventTypes.add(SpinnerEventType(EventType.FAMILY, getString(R.string.events_family)))
        eventTypes.add(SpinnerEventType(EventType.VIDEOGAMES, getString(R.string.events_videogames)))

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            eventTypes
        ).also {  adapter ->
            cmbEventType?.adapter = adapter
        }
        cmbEventType?.setSelection(0)
    }

    /**
     * Initilize the score types combo box
     */
    private fun initScoreTypesComboBox() {
        // Combo box score types
        val scoreTypes: MutableList<SpinnerScoreType> = ArrayList()
        scoreTypes.add(SpinnerScoreType(ScoreValueType.NUMERIC, getString(R.string.score_numeric)))
        scoreTypes.add(SpinnerScoreType(ScoreValueType.DECIMAL, getString(R.string.score_decimal)))
        scoreTypes.add(SpinnerScoreType(ScoreValueType.TIME, getString(R.string.score_time)))

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            scoreTypes
        ).also {  adapter ->
            cmbScoreType?.adapter = adapter
        }
        cmbScoreType?.setSelection(0)
    }

    /**
     * Validates the fields and call to the commit function to create the event
     */
    private fun create(view: View) {
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validate()) {
            commit()
        }
    }

    /**
     * Validate all the fields of the form.
     * @return True if all fields are correct and false if not
     */
    private fun validate() : Boolean {
        val validTitle = validateTitle()
        val validDates = validateDates()
        return validTitle && validDates
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
            val scrollView : ScrollView? = view?.findViewById(R.id.scrollview_events_creation)
            scrollView?.scrollTo(0, 0)
        }

        return result
    }

    private fun validateDates(): Boolean {

        var endDate : Date? = null
        if(txtEndDate?.text?.isNotEmpty() == true)
            endDate = sdf?.parse(txtEndDate?.text.toString())

        var initDate : Date? = null
        if(txtInitDate?.text?.isNotEmpty() == true)
            initDate = sdf?.parse(txtInitDate?.text.toString())

        if(initDate == null && endDate == null)
            return true

        if(initDate != null && endDate != null){
            return if(initDate.before(endDate))
                true
            else {
                txtInitDate?.error = getString(R.string.error_date_before)
                false
            }
        }

        return true
    }

    /**
     * Build the EventDTO to call the service of events creation
     */
    private fun commit() {

        // Generate de DTO for event creation
        val eventPostDTO : EventPostDTO = generateRequestDTO()

        progressBar?.visibility = View.VISIBLE

        // Call to the event creation service
        eventService.create(eventPostDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    val newEvent = response.body()
                    if (newEvent != null) {
                        uploadEventImage(newEvent.id)
                        commitRewards(newEvent.id)
                        commitPunishments(newEvent.id)
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

    /**
     * Generates the DTO required to call the event creation service
     */
    private fun generateRequestDTO(): EventPostDTO {

        val sdfApi : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_api_date))

        // Build request
        val eventDTO = EventPostDTO(txtTitle?.text.toString())
        eventDTO.subtitle = txtSubtitle?.text.toString()
        eventDTO.description = txtDescription?.text.toString()

        // End date
        var endDate : Date? = null
        if(txtEndDate?.text?.isNotEmpty() == true)
            endDate = sdf?.parse(txtEndDate?.text.toString())
        endDate?.let { eventDTO.endDate = sdfApi.format(endDate) }

        // Init date
        var initDate : Date? = null
        if(txtInitDate?.text?.isNotEmpty() == true)
            initDate = sdf?.parse(txtInitDate?.text.toString())
        initDate?.let { eventDTO.initDate = sdfApi.format(initDate) }

        // Max places
        if(txtMaxPlaces?.text != null && txtMaxPlaces?.text!!.isNotEmpty()) {
            eventDTO.maxPlaces = Integer.valueOf(txtMaxPlaces?.text.toString())
        }

        // Event type
        val eventType = cmbEventType?.selectedItem as SpinnerEventType
        eventDTO.type = eventType.key
        val scoreType = cmbScoreType?.selectedItem as SpinnerScoreType
        eventDTO.scoreType = scoreType.key

        // Approval needed
        eventDTO.approvalNeeded = swtApproval?.isChecked

        // Inscription type
        if(swtInscription?.isChecked == false){
            eventDTO.inscription = EventInscriptionType.PUBLIC
        } else {
            eventDTO.inscription = EventInscriptionType.PRIVATE
        }

        // Sort type
        if(swtSort?.isChecked == true){
            eventDTO.sortScore = ScoreSortType.ASC
        } else {
            eventDTO.sortScore = ScoreSortType.DESC
        }

        // Visibility type
        if(swtVisibility?.isChecked == true){
            eventDTO.visibility = EventVisibilityType.PUBLIC
        } else {
            eventDTO.visibility = EventVisibilityType.PRIVATE
        }

        return eventDTO
    }

    private fun uploadEventImage(id : String) {
        filePart?.let {
            eventService.updateImage(it, id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
                override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                }
                override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                }
            })
        }
    }

    private fun uploadRewardImage(id : Int, imagePart : MultipartBody.Part?) {
        imagePart?.let {
            rewardService.updateImage(it, id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<RewardDTO> {
                override fun onResponse(call: Call<RewardDTO>, response: Response<RewardDTO>) {
                }
                override fun onFailure(call: Call<RewardDTO>, t: Throwable) {
                }
            })
        }
    }

    private fun uploadPunishmentImage(id : Int, imagePart : MultipartBody.Part?) {
        imagePart?.let {
            punishmentService.updateImage(it, id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<PunishmentDTO> {
                override fun onResponse(call: Call<PunishmentDTO>, response: Response<PunishmentDTO>) {
                }
                override fun onFailure(call: Call<PunishmentDTO>, t: Throwable) {
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

    private fun commitRewards(id: String) {
        rewardAdapter?.rewards?.forEach { r ->
            val rewardPostDTO = r as RewardPostDTO
            rewardService.create(rewardPostDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<RewardDTO> {
                override fun onResponse(call: Call<RewardDTO>, response: Response<RewardDTO>) {
                    val newReward = response.body()
                    if(newReward != null) {
                        uploadRewardImage(newReward.id, r.imagePart)
                    }
                }
                override fun onFailure(call: Call<RewardDTO>, t: Throwable) {
                }
            })
        }
    }

    private fun commitPunishments(id: String) {
        punishmentAdapter?.punishments?.forEach { p ->
            val punishmentPostDTO = p as PunishmentPostDTO
            punishmentService.create(punishmentPostDTO, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<PunishmentDTO> {
                override fun onResponse(call: Call<PunishmentDTO>, response: Response<PunishmentDTO>) {
                    val newPunishment = response.body()
                    if(newPunishment != null) {
                        uploadPunishmentImage(newPunishment.id, p.imagePart)
                    }
                }
                override fun onFailure(call: Call<PunishmentDTO>, t: Throwable) {
                }
            })
        }
    }

    private fun showDatePicker(txt: TextView?){

        val calendar = Calendar.getInstance()
        if (txt?.text != null && txt.text!!.isNotEmpty()){
            calendar.time = sdf?.parse(txt.text.toString())
        } else {
            calendar.time = Date()
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> updateDate(
                txt,
                year,
                month,
                dayOfMonth
        ) }
        val dialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun updateDate(txt: TextView?, year: Int, month: Int, dayOfMonth: Int) {
        txt?.text = "$dayOfMonth-$month-$year"
    }

    private fun showSuccessDialog() {
        MyDialog.confirmNavigate(this, getString(R.string.event_created_title), getString(R.string.event_created),
            R.id.action_event_creation_to_home
        )
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}