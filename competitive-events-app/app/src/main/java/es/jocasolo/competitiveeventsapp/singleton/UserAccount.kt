package es.jocasolo.competitiveeventsapp.singleton

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import es.jocasolo.competitiveeventsapp.constants.Constants

class UserAccount private constructor(context: Context) {

    private var accManager : AccountManager = AccountManager.get(context)
    private lateinit var account : Account

    init {
        for(a in accManager.accounts){
            if(a.type.equals(Constants.ACCOUNT_TYPE)){
                account = a
                break
            }
        }
    }

    fun getAccount() : Account {
        return account
    }

    fun getToken() : String {
        return "Bearer " + accManager.getUserData(account, Constants.ACCESS_TOKEN)
    }

    fun getName() : String {
        return account.name
    }

    companion object : SingletonHolder<UserAccount, Context>(::UserAccount)

}