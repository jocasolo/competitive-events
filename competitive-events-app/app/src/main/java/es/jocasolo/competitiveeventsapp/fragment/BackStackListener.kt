package es.jocasolo.competitiveeventsapp.fragment

import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO

interface BackStackListener {
    fun backStackAction(data : BackStackEntryDTO)
}