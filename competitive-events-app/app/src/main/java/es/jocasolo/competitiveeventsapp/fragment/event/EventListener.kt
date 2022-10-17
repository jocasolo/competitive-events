package es.jocasolo.competitiveeventsapp.fragment.event

import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO

interface EventListener {
    fun backStackAction(data : BackStackEntryDTO)
}