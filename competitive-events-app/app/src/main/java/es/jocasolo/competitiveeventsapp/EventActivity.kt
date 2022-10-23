package es.jocasolo.competitiveeventsapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPutDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserLiteWithEventDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventStatusType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ViewPagerAdapter
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection


class EventActivity : AppCompatActivity() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    var eventId : String? = null
    var eventTitle : String? = null
    var eventDTO : EventDTO? = null
    var userEvent : UserLiteWithEventDTO? = null

    var wasPaused: Boolean = false
    var actionsMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // Actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Get event id to show detail
        val extras = intent.extras
        if(extras != null) {
            eventId = extras.getString("eventId")
            eventTitle = extras.getString("eventTitle")
            actionBar?.title = eventTitle
        }

        // Page adapter for tabs
        val tabLayout = findViewById<TabLayout>(R.id.tab_event_main)
        val viewPager = findViewById<ViewPager>(R.id.viewpager_event_main)

        // Tabs navigation
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, 3, eventId!!)
        viewPager?.adapter = pagerAdapter
        viewPager?.clearOnPageChangeListeners()
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout?.clearOnTabSelectedListeners()
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.event_nav_menu, menu)

        actionsMenu = menu

        // Load event and hide menu options
        loadEventUser(eventId!!)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.item_edit_event -> {
                // Open dialog edition
                val myIntent = Intent(this, EventEditionActivity::class.java)
                myIntent.putExtra("eventId", eventId)
                myIntent.putExtra("eventTitle", eventTitle)
                startActivity(myIntent)
                true
            }
            R.id.item_abandon_event -> {
                openAbandonConfirmDialog()
                true
            }
            R.id.item_start_event -> {
                openInitEventConfirmDialog()
                true
            }
            R.id.item_finish_event -> {
                openFinishEventConfirmDialog()
                true
            }
            R.id.item_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAbandonConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Abandonar evento")
            .setMessage("¿Estas seguro de querer abandonar este evento?")
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm)) { _: DialogInterface, i: Int ->
                abandonEvent()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create().show()
    }

    private fun abandonEvent() {
        val request = EventUserPutDTO(UserAccount.getInstance(this).getName())
        request.status = EventUserStatusType.DELETED
        eventService.updateUser(eventId!!, request, UserAccount.getInstance(this).getToken()).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        finish()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                }
            })
    }

    private fun openInitEventConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Iniciar evento")
            .setMessage("¿Estas seguro de querer iniciar este evento?")
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm)) { _: DialogInterface, i: Int ->
                initEvent()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create().show()
    }

    private fun initEvent() {
        eventService.init(eventId!!, UserAccount.getInstance(this).getToken()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    showSuccessDialog("Evento iniciado con éxito")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

    private fun openFinishEventConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Finalizar evento")
            .setMessage("¿Estas seguro de querer finalizar este evento?")
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm)) { _: DialogInterface, i: Int ->
                finishEvent()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create().show()
    }

    private fun finishEvent() {
        eventService.finish(eventId!!, UserAccount.getInstance(this).getToken()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    showSuccessDialog("Evento finalizado con éxito")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

    private fun loadEventUser(eventId: String) {
        eventService.findOne(eventId, UserAccount.getInstance(this).getToken()).enqueue(object :
            Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    eventDTO = response.body()
                    if(eventDTO != null){
                        userEvent = findUser(eventDTO!!)
                        hideMenuOptions()
                    }
                }
            }

            override fun onFailure(call: Call<EventDTO>, t: Throwable) {
            }
        })
    }

    private fun hideMenuOptions() {
        // Hide options considering user privilege and event status
        if(userEvent?.privilege != EventUserPrivilegeType.OWNER){
            actionsMenu?.findItem(R.id.item_delete_event)?.isVisible = false
            actionsMenu?.findItem(R.id.item_edit_event)?.isVisible = false
            actionsMenu?.findItem(R.id.item_finish_event)?.isVisible = false
            actionsMenu?.findItem(R.id.item_start_event)?.isVisible = false

        } else {
            actionsMenu?.findItem(R.id.item_abandon_event)?.isVisible = false
            when(eventDTO?.status) {
                EventStatusType.DELETED,
                EventStatusType.FINISHED  ->{
                    actionsMenu?.findItem(R.id.item_edit_event)?.isVisible = false
                    actionsMenu?.findItem(R.id.item_finish_event)?.isVisible = false
                    actionsMenu?.findItem(R.id.item_start_event)?.isVisible = false
                }
                EventStatusType.NOT_ACTIVE -> {
                    actionsMenu?.findItem(R.id.item_finish_event)?.isVisible = false
                }
                EventStatusType.ACTIVE -> {
                    actionsMenu?.findItem(R.id.item_start_event)?.isVisible = false
                }
            }
        }
    }

    private fun findUser(eventDTO: EventDTO): UserLiteWithEventDTO? {
        eventDTO.users?.forEach {
            if(it.id.equals(UserAccount.getInstance(this).getName())){
                return it
            }
        }
        return null
    }

    override fun onPause() {
        super.onPause()
        wasPaused = true
    }

    override fun onBackPressed() {
        finish()
    }

    fun changeActionBarTitle(newTitle: String){
        supportActionBar?.title = newTitle
    }

    private fun showSuccessDialog(message : String) {
        MyDialog.message(this, getString(R.string.events), message)
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}