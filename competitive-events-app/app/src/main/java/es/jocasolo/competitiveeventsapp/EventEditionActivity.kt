package es.jocasolo.competitiveeventsapp

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPutDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentPostDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardPostDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.fragment.BackStackListener
import es.jocasolo.competitiveeventsapp.fragment.punishment.PunishmentCreationDialogFragment
import es.jocasolo.competitiveeventsapp.fragment.reward.RewardCreationDialogFragment
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

class EventEditionActivity : AppCompatActivity(), BackStackListener {

    var eventId : String? = null

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

    private var rewardsToCreate : MutableList<RewardPostDTO> = mutableListOf()
    private var punishmentsToDelete : MutableList<String> = mutableListOf()
    private var punishmentsToCreate : MutableList<PunishmentPostDTO> = mutableListOf()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edition)

        // Actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Get event id to show detail
        val extras = intent.extras
        if(extras != null) {
            eventId = extras.getString("eventId")
            actionBar?.title = extras.getString("eventTitle")
        }

        // Init fields
        txtTitle = findViewById(R.id.txt_event_title_edit)
        txtSubtitle = findViewById(R.id.txt_event_subtitle_edit)
        txtDescription = findViewById(R.id.txt_event_description_edit)
        progressBar = findViewById(R.id.spn_event_update_edit)
        cmbEventType = findViewById(R.id.combo_events_type_edit)
        cmbScoreType = findViewById(R.id.combo_score_type_edit)
        txtInitDate = findViewById(R.id.txt_event_init_date_edit)
        txtInitDate?.setOnClickListener { showDatePicker(txtInitDate) }
        txtEndDate = findViewById(R.id.txt_event_end_date_edit)
        txtEndDate?.setOnClickListener { showDatePicker(txtEndDate) }
        swtApproval = findViewById(R.id.switch_event_approval_edit)
        swtInscription = findViewById(R.id.switch_event_inscription_edit)
        swtSort = findViewById(R.id.switch_event_sort_type_edit)
        swtVisibility = findViewById(R.id.switch_event_visibility_edit)
        txtMaxPlaces = findViewById(R.id.txt_event_max_places_edit)

        // Image upload button
        findViewById<Button>(R.id.btn_event_upload_image_edit).setOnClickListener { imageChooser() }
        imgEventImage = findViewById(R.id.img_event_upload_image_edit)
        imgEventImage?.setColorFilter(
            ContextCompat.getColor(this, R.color.primary_dark),
            PorterDuff.Mode.SRC_IN
        )
        imgEventImage?.setOnClickListener { imageChooser() }

        // Switchs
        swtInscription?.setOnCheckedChangeListener { _, isChecked -> if(isChecked) swtVisibility?.isChecked = false }
        swtVisibility?.setOnCheckedChangeListener { _, isChecked -> if(isChecked) swtInscription?.isChecked = false }

        // EventTypes
        initEventTypesComboBox()

        // ScoreTypes
        initScoreTypesComboBox()

        // Create event button
        findViewById<Button>(R.id.btn_event_update_edit).setOnClickListener { update() }

        // Load the event and init fields
        if(eventId != null) {
            loadEvent(eventId!!)
        }

    }

    private fun loadEvent(id: String) {
        eventService.findOne(id, UserAccount.getInstance(this).getToken()).enqueue(object :
            Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val event = response.body()
                    if (event != null) {
                        fillFields(event)
                    }
                } else {
                    try {
                        val errorDto = Gson().fromJson(
                            response.errorBody()?.string(),
                            ErrorDTO::class.java
                        ) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e: Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
            }

            override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })
    }

    private fun fillFields(event: EventDTO) {
        val calendar = Calendar.getInstance()

        txtTitle?.text = event.title
        txtSubtitle?.text = event.subtitle
        txtDescription?.text = event.description
        txtMaxPlaces?.text = event.maxPlaces?.toString()
        swtApproval?.isChecked = event.approvalNeeded == true
        swtInscription?.isChecked = event.inscription == EventInscriptionType.PRIVATE
        swtVisibility?.isChecked = event.visibility == EventVisibilityType.PUBLIC
        swtSort?.isChecked = event.sortScore == ScoreSortType.ASC
        if(event.image != null) {
            Picasso.get().load(event.image!!.link()).into(imgEventImage)
            imgEventImage?.clearColorFilter()
            imgEventImage?.setPadding(0, 0, 0, 5)
        }
        if(event.initDate != null) {
            calendar.time = event.initDate!!
            updateDate(
                txtInitDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(
                    Calendar.DATE
                )
            )
        }
        if(event.endDate != null) {
            calendar.time = event.endDate!!
            updateDate(
                txtEndDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(
                    Calendar.DATE
                )
            )
        }
        if(event.type != null){
            val selected = when(event.type){
                EventType.SPORTS -> 0
                EventType.OTHER -> 1
                EventType.ACADEMIC -> 2
                EventType.FAMILY -> 3
                EventType.VIDEOGAMES -> 4
                else -> 0
            }
            cmbEventType?.setSelection(selected)
        }
        if(event.scoreType != null) {
            val selected = when(event.scoreType) {
                ScoreValueType.NUMERIC -> 0
                ScoreValueType.DECIMAL -> 1
                ScoreValueType.TIME -> 2
                else -> 0
            }
            cmbScoreType?.setSelection(selected)
        }

        // Rewards list and add button
        initRewards(event)

        // Punishments list and add button
        initPunishments(event)

    }

    /**
     * Validates the fields and call to the commit function to create the event
     */
    private fun update() {
        if(validate()) {
            commit()
        }
    }

    /**
     * Build the EventDTO to call the service of events update
     */
    private fun commit() {

        // Generate de DTO for event creation
        val eventPutDTO : EventPutDTO = generateRequestDTO()

        progressBar?.visibility = View.VISIBLE

        // Call to the event creation service
        eventService.update(eventId!!, eventPutDTO, UserAccount.getInstance(this).getToken()).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        uploadEventImage(eventId!!)
                        commitRewards()
                        commitPunishments()
                        showSuccessDialog(getString(R.string.event_edit_success))
                    } else {
                        try {
                            val errorDto = Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorDTO::class.java
                            ) as ErrorDTO
                            showErrorDialog(getString(Message.forCode(errorDto.message)))
                        } catch (e: Exception) {
                            showErrorDialog(getString(R.string.error_api_undefined))
                        }
                    }
                    progressBar?.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                    progressBar?.visibility = View.INVISIBLE
                }
            })

    }

    /**
     * Generates the DTO required to call the event creation service
     */
    private fun generateRequestDTO(): EventPutDTO {

        val sdfApi : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_api_date))

        // Build request
        val eventDTO = EventPutDTO(eventId!!)
        eventDTO.title = txtTitle?.text.toString()
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

    private fun uploadEventImage(id: String) {
        filePart?.let {
            eventService.updateImage(it, id, UserAccount.getInstance(this).getToken()).enqueue(
                object : Callback<EventDTO> {
                    override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                    }

                    override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                    }
                })
        }
    }

    private fun uploadRewardImage(id: Int, imagePart: MultipartBody.Part?) {
        imagePart?.let {
            rewardService.updateImage(it, id, UserAccount.getInstance(this).getToken()).enqueue(
                object : Callback<RewardDTO> {
                    override fun onResponse(call: Call<RewardDTO>, response: Response<RewardDTO>) {
                    }

                    override fun onFailure(call: Call<RewardDTO>, t: Throwable) {
                    }
                })
        }
    }

    private fun uploadPunishmentImage(id: Int, imagePart: MultipartBody.Part?) {
        imagePart?.let {
            punishmentService.updateImage(it, id, UserAccount.getInstance(this).getToken()).enqueue(
                object : Callback<PunishmentDTO> {
                    override fun onResponse(
                        call: Call<PunishmentDTO>,
                        response: Response<PunishmentDTO>
                    ) {
                    }

                    override fun onFailure(call: Call<PunishmentDTO>, t: Throwable) {
                    }
                })
        }
    }

    private fun commitRewards() {
        // Create new rewards
        rewardsToCreate.forEach { r ->
            r.eventId = eventId
            rewardService.create(r, UserAccount.getInstance(this).getToken()).enqueue(object :
                Callback<RewardDTO> {
                override fun onResponse(call: Call<RewardDTO>, response: Response<RewardDTO>) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        val newReward = response.body()
                        if (newReward != null) {
                            uploadRewardImage(newReward.id, r.imagePart)
                        }
                    }
                }

                override fun onFailure(call: Call<RewardDTO>, t: Throwable) {
                }
            })
        }
        // Delete rewards
        rewardAdapter?.rewardsToDelete?.forEach {
            rewardService.delete(it, UserAccount.getInstance(this).getToken()).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                }
            })
        }
    }

    private fun commitPunishments() {
        // Create new punishments
        punishmentsToCreate.forEach { p ->
            p.eventId = eventId
            punishmentService.create(p, UserAccount.getInstance(this).getToken()).enqueue(object :
                Callback<PunishmentDTO> {
                override fun onResponse(
                    call: Call<PunishmentDTO>,
                    response: Response<PunishmentDTO>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        val newPunishment = response.body()
                        if (newPunishment != null) {
                            uploadPunishmentImage(newPunishment.id, p.imagePart)
                        }
                    }
                }

                override fun onFailure(call: Call<PunishmentDTO>, t: Throwable) {
                }
            })
        }
        // Delete punishments
        punishmentAdapter?.punishmentsToDelete?.forEach {
            punishmentService.delete(it, UserAccount.getInstance(this).getToken()).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                }
            })
        }
    }

    override fun onBackPressed() {
        finish()
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
        eventTypes.add(
            SpinnerEventType(
                EventType.VIDEOGAMES,
                getString(R.string.events_videogames)
            )
        )

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            eventTypes
        ).also { adapter ->
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
            this,
            android.R.layout.simple_spinner_dropdown_item,
            scoreTypes
        ).also { adapter ->
            cmbScoreType?.adapter = adapter
        }
        cmbScoreType?.setSelection(0)
    }

    /**
     * Initializes the list of rewards and the add button actions.
     */
    private fun initRewards(event: EventDTO){

        // Reward list (RecyclerView)
        if(rewardAdapter == null){
            rewardAdapter = ListRewardLiteAdapter(this, null, true)
        }
        rewardsRecyclerView = findViewById(R.id.recycler_event_creation_reward_list_edit)
        rewardsRecyclerView?.layoutManager = LinearLayoutManager(this)
        rewardsRecyclerView?.adapter = rewardAdapter

        // Rewards add button and observer
        findViewById<TextView>(R.id.btn_event_creation_add_reward_edit).setOnClickListener {
            openRewardCreationDialog()
        }

        if(event.rewards != null) {
            event.rewards!!.forEach {
                val reward = RewardPostDTO(it.title)
                reward.id = it.id
                reward.description = it.description
                reward.sortScore = it.sortScore
                reward.requiredPosition = it.requiredPosition
                addReward(reward)
            }
        }

    }

    private fun openRewardCreationDialog() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("rewardEditDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment = RewardCreationDialogFragment(this)
        newDialogFragment.show(ft, "rewardEditDialog")
    }

    /**
     * Initializes the list of punishments and the add button actions.
     */
    private fun initPunishments(event: EventDTO){

        // Reward list (RecyclerView)
        if(punishmentAdapter == null){
            punishmentAdapter = ListPunishmentLiteAdapter(this, null, true)
        }
        punishmentsRecyclerView = findViewById(R.id.recycler_event_creation_punishment_list_edit)
        punishmentsRecyclerView?.layoutManager = LinearLayoutManager(this)
        punishmentsRecyclerView?.adapter = punishmentAdapter

        // Rewards add button and observer
        findViewById<TextView>(R.id.btn_event_creation_add_punishment_edit).setOnClickListener {
            openPunishmentCreationDialog()
        }

        if(event.punishments != null) {
            event.punishments!!.forEach {
                val punishment = PunishmentPostDTO(it.title)
                punishment.id = it.id
                punishment.description = it.description
                punishment.sortScore = it.sortScore
                punishment.requiredPosition = it.requiredPosition
                addPunishment(punishment)
            }
        }

    }

    private fun openPunishmentCreationDialog() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("punishmentEditDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment = PunishmentCreationDialogFragment(this)
        newDialogFragment.show(ft, "punishmentEditDialog")
    }

    /**
     * Adds a reward to the rewards adapter and recycler view. This award comes from another event from which it has been returned.
     */
    private fun addReward(it: BackStackEntryDTO?) {
        rewardAdapter?.addReward(it)
        rewardAdapter?.notifyDataSetChanged()
    }

    /**
     * Adds a reward to the rewards adapter and recycler view. This award comes from another event from which it has been returned.
     */
    private fun addPunishment(it: BackStackEntryDTO?) {
        punishmentAdapter?.addPunishment(it)
        punishmentAdapter?.notifyDataSetChanged()
    }

    private fun showDatePicker(txt: TextView?){

        sdf = SimpleDateFormat(getString(R.string.sdf_date))

        val calendar = Calendar.getInstance()
        if (StringUtils.isNotEmpty(txt?.text)){
            calendar.time = sdf?.parse(txt?.text.toString())
        } else {
            calendar.time = Date()
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> updateDate(
            txt,
            year,
            month,
            dayOfMonth
        ) }
        val dialog = DatePickerDialog(
            this, listener, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH
            ), calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    private fun updateDate(txt: TextView?, year: Int, month: Int, dayOfMonth: Int) {
        var monthAdd = month + 1
        txt?.text = "$dayOfMonth-$monthAdd-$year"
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
                    imgEventImage?.setPadding(0, 0, 0, 5)

                    // Upload image to storage service
                    val filePath: String = getImagePath(selectedImageUri)
                    val file = File(filePath)
                    filePart = MultipartBody.Part.createFormData(
                        "file", file.name, RequestBody.create(
                            MediaType.parse("image/*"), file
                        )
                    )

                }
            }
        }
    }

    private fun getImagePath(selectedImageUri: Uri): String {
        val filePath: String
        if ("content" == selectedImageUri.scheme) {
            val cursor = this.contentResolver.query(
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
            val scrollView : ScrollView? = findViewById(R.id.scrollview_events_edition)
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
     * Action executed after returning from a dialog
     */
    override fun backStackAction(data: BackStackEntryDTO) {
        if(data is RewardPostDTO){
            rewardsToCreate.add(data)
            addReward(data)
        } else if(data is PunishmentPostDTO) {
            punishmentsToCreate.add(data)
            addPunishment(data)
        }
    }

    private fun showSuccessDialog(message: String) {
        MyDialog.message(this, getString(R.string.events), message)
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}