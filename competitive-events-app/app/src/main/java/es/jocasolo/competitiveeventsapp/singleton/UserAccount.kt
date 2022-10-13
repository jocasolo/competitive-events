package es.jocasolo.competitiveeventsapp.singleton

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import es.jocasolo.competitiveeventsapp.constants.Constants
import org.apache.commons.lang3.StringUtils

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

    fun setAccount(newAccount : Account){
        account = newAccount
    }

    fun getToken() : String {
        val token = accManager.getUserData(account, Constants.ACCESS_TOKEN)
        token?.let { return "Bearer $token" }
        return StringUtils.EMPTY
    }

    fun getName() : String {
        return account.name
    }


    companion object : SingletonHolder<UserAccount, Context>(::UserAccount)

}